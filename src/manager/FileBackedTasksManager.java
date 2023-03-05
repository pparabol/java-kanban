package manager;

import exception.ManagerSaveException;
import manager.history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Status;
import util.TaskType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static model.Task.DATE_TIME_FORMATTER;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(HistoryManager historyManager, String path) {
        super(historyManager);
        this.file = new File(path);
    }

    private void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,start,duration,epic");

           List<Task> createdTasks = new ArrayList<>();
           createdTasks.addAll(tasks.values());
           createdTasks.addAll(epics.values());
           createdTasks.addAll(subtasks.values());

           for (Task task : createdTasks) {
               fileWriter.write(toString(task));
           }

           fileWriter.write("\n\n");

           fileWriter.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    private String toString(Task task) {
        int id = task.getId();
        String type = task.getType().toString();
        String name = task.getTitle();
        String status = task.getStatus().toString();
        String description = task.getDescription();
        String start = task.getStartTime() == null ? "Not stated" : task.getStartTime().format(DATE_TIME_FORMATTER);
        long duration = task.getDuration().toMinutes();

        String result = String.format("\n%d,%s,%s,%s,%s,%s,%d", id, type, name, status, description, start, duration);

        if (task instanceof Subtask) {
            int epicId = ((Subtask) task).getEpicId();
            result += "," + epicId;
        }

        return result;
    }

    public Task fromString(String value) {
        String[] data = value.split(",");
        int id = Integer.parseInt(data[0]);
        TaskType type = TaskType.valueOf(data[1]);
        String title = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];
        long duration = Long.parseLong(data[6]);

        Task task = null;

        switch (type) {
            case TASK:
                task = new Task(title, description, id);
                break;
            case EPIC:
                task = new Epic(title, description, id);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(data[7]);
                Task epic = epics.get(epicId);
                task = new Subtask(title, description, id, (Epic) epic);
                break;
        }
        task.setStatus(status);
        task.setDuration(duration);

        if (!data[5].equals("Not stated")) {
            LocalDateTime start = LocalDateTime.parse(data[5], DATE_TIME_FORMATTER);
            task.setStartTime(start);
        }

        return task;
    }

    private static String historyToString(HistoryManager manager) {
        List<String> ids = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join(",", ids);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();
        for (String s : value.split(",")) {
            result.add(Integer.parseInt(s));
        }
        return result;
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
