package util;

import api.KVTaskClient;
import manager.HttpTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String url) {
        return new HttpTaskManager(getDefaultHistory(), url, new KVTaskClient(url));
    }
}
