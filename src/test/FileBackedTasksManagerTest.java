package test;

import exception.ManagerSaveException;
import manager.FileBackedTasksManager;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.FileManagerLoader;
import util.Managers;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    void setManager() {
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), "src/file/data.csv");
    }

    @Test
    void shouldThrowManagerSaveExceptionWhenInvalidPath() {
        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), " ");
        Task task = new Task("Task", "checkSaveException", manager.getId());

        final ManagerSaveException exception = assertThrows(ManagerSaveException.class,
                () -> manager.createTask(task)
        );
        assertEquals("Ошибка записи в файл", exception.getMessage());
    }

    @Test
    void shouldLoadFromFile() {
        Epic epic = new Epic("Epic", "checkLoading", manager.getId());
        int id = epic.getId();

        manager.createTask(epic);
        manager.getTaskById(id);

        FileBackedTasksManager fM = FileManagerLoader.loadFromFile("src/file/data.csv");
        final Task loadedEpic = fM.getTaskById(id);
        final Collection<Task> history = fM.historyManager.getHistory();

        assertAll(
                () -> assertNotNull(loadedEpic, "Задача не найдена"),
                () -> assertEquals(epic, loadedEpic, "Задачи не совпадают"),
                () -> assertTrue(epic.getSubtasks().isEmpty(), "Загружаются лишние подазадачи")
        );

        assertAll(
                () -> assertFalse(history.isEmpty(), "История не загружается"),
                () -> assertEquals(1, history.size(), "Неверный размер истории")
        );
    }
}