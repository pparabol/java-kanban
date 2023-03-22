package test;

import api.KVServer;
import manager.HttpTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private static KVServer server;
    @BeforeAll
    static void startServer() throws IOException {
        server = new KVServer();
        server.start();
    }

    @BeforeEach
    void setManager() {
        manager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
    }

    @Test
    void shouldLoadFromServer() {
        Task task = new Task("Task", "shouldLoad", manager.getId());
        Epic epic = new Epic("Epic", "shouldLoad", manager.getId());
        Subtask subtask = new Subtask("Subtask", "shouldLoad", manager.getId(), epic);

        manager.createTask(task);
        manager.createTask(epic);
        manager.createTask(subtask);

        manager.getTaskById(task.getId());

        manager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
        manager.loadFromServer();

        assertAll(
                () -> assertEquals(3, manager.getPrioritizedTasks().size(),
                        "Количество задач не совпадает"),
                () -> assertEquals(1, manager.getHistory().size(),
                        "Размер истории не совпадает"),
                () -> assertEquals(task, manager.getTaskById(task.getId()),
                        "Задача не загрузилась"),
                () -> assertEquals(epic, manager.getTaskById(epic.getId()),
                        "Эпик не загрузился"),
                () -> assertEquals(subtask, manager.getTaskById(subtask.getId()),
                        "Подзадача не загрузилась")
        );
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
}