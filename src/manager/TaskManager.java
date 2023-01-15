package manager;

import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    int getId();

    ArrayList<Task> getAllTasks();

    ArrayList<Task> getAllEpics();

    ArrayList<Task> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTask(int id);

    Task getEpic(int id);

    Task getSubtask(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void removeTask(int id);

    ArrayList<Task> getSubtasksOfEpic(Task task);
}
