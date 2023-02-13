package manager;

import exception.ManagerLoadException;
import exception.ManagerSaveException;
import manager.history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;
import util.Status;
import util.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(HistoryManager historyManager, String path) {
        super(historyManager);
        this.file = new File(path);
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(Managers.getDefaultHistory(),
                "src/file/data.csv");
        Task task1 = new Task("Кино", "Купить билеты", fileManager.getId());
        Task task2 = new Task("Проект", "Написать рабочий код", fileManager.getId());
        fileManager.createTask(task1);
        fileManager.createTask(task2);

        Epic epic1 = new Epic("Важный эпик", "Много дел", fileManager.getId());
        Subtask subtask1 = new Subtask("Кот", "Покормить кота", fileManager.getId(), epic1);
        Subtask subtask2 = new Subtask("Пёс", "Погладить собаку", fileManager.getId(), epic1);
        Subtask subtask3 = new Subtask("Суши", "Заказать доставку", fileManager.getId(), epic1);
        fileManager.createTask(epic1);
        fileManager.createTask(subtask1);
        fileManager.createTask(subtask2);
        fileManager.createTask(subtask3);

        Epic epic2 = new Epic("Очень важный эпик", "Куча дел", fileManager.getId());
        fileManager.createTask(epic2);

        fileManager.getTaskById(5);
        fileManager.getTaskById(3);
        fileManager.getTaskById(4);

        FileBackedTasksManager fM = loadFromFile(new File("src/file/data.csv"));

        for (Task task : fM.tasks.values()) {
            System.out.println(task);
        }

        System.out.println(fM.historyManager.getHistory());
    }

    private void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic");

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

        String result = String.format("\n%d,%s,%s,%s,%s", id, type, name, status, description);

        if (task instanceof Subtask) {
            int epicId = ((Subtask) task).getEpicId();
            result += "," + epicId;
        }

        return result;
    }

    private Task fromString(String value) {
        String[] data = value.split(",");
        int id = Integer.parseInt(data[0]);
        TaskType type = TaskType.valueOf(data[1]);
        String title = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];

        Task task = null;

        switch (type) {
            case TASK:
                task = new Task(title, description, id);
                break;
            case EPIC:
                task = new Epic(title, description, id);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(data[5]);
                Task epic = epics.get(epicId);
                task = new Subtask(title, description, id, (Epic) epic);
                break;
        }
        task.setStatus(status);
        return task;
    }

    public static String historyToString(HistoryManager manager) {
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

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerLoadException {
        FileBackedTasksManager manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file.getPath());
        try {
            String[] data = Files.readAllLines(file.toPath()).toArray(new String[0]);
            int i = 1;
            while (!data[i].isBlank()) {
                Task task = manager.fromString(data[i]);
                switch (task.getType()) {
                    case TASK:
                        manager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        manager.epics.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        manager.subtasks.put(task.getId(), task);
                        break;
                }
                i++;
            }

            List<Integer> historyIds = historyFromString(data[data.length - 1]);
            for (int id : historyIds) {
                Task task = manager.getTaskById(id);
            }
            
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка считывания из файла");
        }
        return manager;
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
