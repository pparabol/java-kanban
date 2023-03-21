package manager;

import model.Task;

import java.util.Collection;

public interface TaskManager {
    int getId();

    Collection<Task> getPrioritizedTasks();

    Collection<Task> getHistory();

    Collection<Task> getTasks();

    Collection<Task> getEpics();

    Collection<Task> getSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void removeTask(int id);

    Collection<Task> getSubtasksOfEpic(int id);
    boolean isTaskPresent(Task task);
}
