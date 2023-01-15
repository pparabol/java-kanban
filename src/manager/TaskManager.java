package manager;

import model.Task;

import java.util.Collection;

public interface TaskManager {
    int getId();

    Collection<Task> getAllTasks();

    Collection<Task> getAllEpics();

    Collection<Task> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTask(int id);

    Task getEpic(int id);

    Task getSubtask(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void removeTask(int id);

    Collection<Task> getSubtasksOfEpic(Task task);
}
