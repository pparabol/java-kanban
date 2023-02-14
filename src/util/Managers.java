package util;

import exception.ManagerLoadException;
import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String path) {
        return new FileBackedTasksManager(getDefaultHistory(), path);
    }

    public static TaskManager loadFromFile(String path) throws ManagerLoadException {
        FileBackedTasksManager manager = (FileBackedTasksManager) getDefault(path);
        try {
            String[] data = Files.readAllLines(Path.of(path)).toArray(new String[0]);
            int i = 1;
            while (!data[i].isBlank()) {
                manager.createTask(manager.fromString(data[i]));
                i++;
            }

            List<Integer> historyIds = FileBackedTasksManager.historyFromString(data[data.length - 1]);
            for (int id : historyIds) {
                Task task = manager.getTaskById(id);
            }

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка считывания из файла");
        }
        return manager;
    }
}
