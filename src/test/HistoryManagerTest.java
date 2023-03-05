package test;

import manager.history.HistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setHistory() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        Task task = new Task("Task", "TaskAdd", 1);
        historyManager.add(task);
        final List<Task> singletonHistory = historyManager.getHistory();

        assertAll(
                () -> assertFalse(singletonHistory.isEmpty(), "История пустая"),
                () -> assertEquals(1, singletonHistory.size(), "Неверный размер истории"),
                () -> assertTrue(singletonHistory.contains(task), "Задача не добавлена в историю")
        );

        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size(), "Задача добавлена в историю дважды");

        Task otherTask = new Task("Task1", "TaskAdd", 2);
        historyManager.add(otherTask);
        final List<Task> history = historyManager.getHistory();

        assertAll(
                () -> assertEquals(2, history.size(), "Неверный размер истории"),
                () -> assertEquals(otherTask, history.get(1), "Задача не на своём месте в истории")
        );
    }

    @Test
    void remove() {
        List<Task> tasks = List.of(new Task("Task1", "TaskGetHistory", 1),
                new Task("Task2", "TaskGetHistory", 2),
                new Task("Task3", "TaskGetHistory", 3),
                new Task("Task4", "TaskGetHistory", 4)
        );
        tasks.forEach(task -> historyManager.add(task));

        historyManager.remove(2);
        assertAll(
                () -> assertEquals(3, historyManager.getHistory().size(), "Задача не удалилась"),
                () -> assertEquals(tasks.get(0), historyManager.getHistory().get(0),
                        "Ошибка удаления из середины списка"),
                () -> assertEquals(tasks.get(3), historyManager.getHistory().get(2),
                        "Ошибка удаления из середины списка")
        );

        historyManager.remove(1);
        assertAll(
                () -> assertEquals(2, historyManager.getHistory().size(), "Задача не удалилась"),
                () -> assertEquals(tasks.get(2), historyManager.getHistory().get(0),
                        "Ошибка удаления из начала списка")
        );

        historyManager.remove(4);
        assertAll(
                () -> assertEquals(1, historyManager.getHistory().size(), "Задача не удалилась"),
                () -> assertEquals(tasks.get(2), historyManager.getHistory().get(0),
                "Ошибка удаления из конца списка")
        );
    }

    @Test
    void getHistory() {
        assertTrue(historyManager.getHistory().isEmpty());

        List<Task> tasks = List.of(new Task("Task1", "TaskGetHistory", 1),
                new Task("Task2", "TaskGetHistory", 2),
                new Task("Task3", "TaskGetHistory", 3)
        );
        tasks.forEach(task -> historyManager.add(task));
        final List<Task> history = historyManager.getHistory();

        assertAll(
                () -> assertFalse(history.isEmpty(), "История пустая"),
                () -> assertEquals(3, history.size(), "Неверный размер истории")
        );

        for (int i = 0; i < history.size(); i++) {
            assertEquals(tasks.get(i), history.get(i), "Неверный порядок задач");
        }
    }
}