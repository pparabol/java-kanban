package manager.history;

import model.Task;

import java.util.Collection;

public interface HistoryManager {

    void add(Task task);
    void remove(int id);

    Collection<Task> getHistory();
}
