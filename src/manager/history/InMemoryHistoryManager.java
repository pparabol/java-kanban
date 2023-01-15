package manager.history;

import model.Task;

import java.util.Collection;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history;
    private final static int MAX_HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (history.size() < MAX_HISTORY_SIZE) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public Collection<Task> getHistory() {
        return history;
    }
}
