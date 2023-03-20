package util;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import server.HttpTaskManager;

import java.io.IOException;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String url) throws IOException, InterruptedException {
        return new HttpTaskManager(getDefaultHistory(), url);
    }
}
