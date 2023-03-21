package manager;

import api.KVTaskClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import manager.history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.stream.Collectors;

import static api.HttpTaskServer.gson;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient taskClient;

    public HttpTaskManager(HistoryManager historyManager, String url) {
        super(historyManager, url);
        taskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        taskClient.put("task", gson.toJson(tasks.values()));
        taskClient.put("subtask", gson.toJson(subtasks.values()));
        taskClient.put("epic", gson.toJson(epics.values()));
        taskClient.put("tasks", gson.toJson(getPrioritizedTasks()));

        List<Integer> historyIds = getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        taskClient.put("history", gson.toJson(historyIds));
    }

    public void loadFromServer() {
        loadTasks("task");
        loadTasks("subtask");
        loadTasks("epic");
        loadHistory();
    }

    private void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load(key));
        JsonArray jsonTasksArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonTasksArray) {
            Task task;
            switch (key) {
                case "task":
                    task = gson.fromJson(element.getAsJsonObject(), Task.class);
                    tasks.put(task.getId(), task);
                    break;
                case "subtask":
                    task = gson.fromJson(element.getAsJsonObject(), Subtask.class);
                    subtasks.put(task.getId(), task);
                    break;
                case "epic":
                    task = gson.fromJson(element.getAsJsonObject(), Epic.class);
                    epics.put(task.getId(), task);
                    break;
                default:
                    System.out.println("Ошибка загрузки задач");
                    return;
            }
            prioritizedTasks.add(task);
        }
    }

    private void loadHistory() {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load("history"));
        JsonArray jsonHistoryArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonHistoryArray) {
            int id = element.getAsInt();
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subtasks.containsKey(id)) {
                historyManager.add(subtasks.get(id));
            }
        }
    }

}
