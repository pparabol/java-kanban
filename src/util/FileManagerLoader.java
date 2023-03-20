package util;

import exception.ManagerLoadException;
import manager.FileBackedTasksManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileManagerLoader {

    private FileManagerLoader() {

    }

    public static FileBackedTasksManager loadFromFile(String path) throws ManagerLoadException {
        FileBackedTasksManager manager = new FileBackedTasksManager(Managers.getDefaultHistory(), path);
        try {
            String[] data = Files.readAllLines(Path.of(path)).toArray(new String[0]);
            int i = 1;
            while (!data[i].isBlank()) {
                manager.createTask(manager.fromString(data[i]));
                i++;
            }

            List<Integer> historyIds = FileBackedTasksManager.historyFromString(data[data.length - 1]);
            for (int id : historyIds) {
                manager.getTaskById(id);
            }

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка считывания из файла");
        }
        return manager;
    }
}
