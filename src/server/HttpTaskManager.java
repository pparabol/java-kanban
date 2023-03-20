package server;

import adapter.LocalDateTimeAdapter;
import com.google.gson.*;
import exception.ManagerSaveException;
import manager.FileBackedTasksManager;
import manager.history.HistoryManager;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient taskClient;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskManager(HistoryManager historyManager, String url) throws IOException, InterruptedException {
        super(historyManager, url);
        taskClient = new KVTaskClient(url);
        loadTasks("task");
        loadTasks("epic");
        loadTasks("subtask");
        loadHistory();
    }

    @Override
    public void save() throws ManagerSaveException {
        taskClient.put("task", gson.toJson(tasks.values()));
        taskClient.put("epic", gson.toJson(epics.values()));
        taskClient.put("subtask", gson.toJson(subtasks.values()));
        taskClient.put("tasks", gson.toJson(getPrioritizedTasks()));

        List<Integer> historyIds = historyManager.getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        taskClient.put("history", gson.toJson(historyIds));
    }

    private void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load(key));
        JsonArray jsonTasksArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonTasksArray) {


            Task task = gson.fromJson(element, Task.class);


           createTask(task);
        }
    }

    private void loadHistory() {
        JsonElement jsonElement = JsonParser.parseString(taskClient.load("history"));
        JsonArray jsonHistoryArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonHistoryArray) {
            int id = element.getAsInt();
            getTaskById(id);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void removeTask(int id) {
        save();
        super.removeTask(id);
    }
}
