package manager.history.test;

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
        List<Task> history = historyManager.getHistory();

        assertFalse(history.isEmpty(), "История пустая");
        assertEquals(1,history.size(), "Неверный размер истории");
        assertTrue(history.contains(task), "Задача не добавлена в историю");

        historyManager.add(task);
        history = historyManager.getHistory();

        assertEquals(1, history.size(), "Задача добавлена в историю дважды");

        Task otherTask = new Task("Task1", "TaskAdd", 2);
        historyManager.add(otherTask);
        history = historyManager.getHistory();

        assertEquals(2, history.size(), "Неверный размер истории");
        assertEquals(otherTask, history.get(1), "Задача не на своём месте в истории");
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
        assertEquals(3, historyManager.getHistory().size(), "Задача не удалилась");
        assertEquals(tasks.get(0), historyManager.getHistory().get(0), "Ошибка удаления из середины списка");
        assertEquals(tasks.get(3), historyManager.getHistory().get(2), "Ошибка удаления из середины списка");

        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Задача не удалилась");
        assertEquals(tasks.get(2), historyManager.getHistory().get(0), "Ошибка удаления из начала списка");

        historyManager.remove(4);
        assertEquals(1, historyManager.getHistory().size(), "Задача не удалилась");
        assertEquals(tasks.get(2), historyManager.getHistory().get(0), "Ошибка удаления из начала списка");
    }

    @Test
    void getHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());

        List<Task> tasks = List.of(new Task("Task1", "TaskGetHistory", 1),
                new Task("Task2", "TaskGetHistory", 2),
                new Task("Task3", "TaskGetHistory", 3)
        );
        tasks.forEach(task -> historyManager.add(task));
        history = historyManager.getHistory();

        assertFalse(history.isEmpty(), "История пустая");
        assertEquals(3, history.size(), "Неверный размер истории");

        for (int i = 0; i < history.size(); i++) {
            assertEquals(tasks.get(i), history.get(i), "Неверный порядок задач");
        }
    }
}