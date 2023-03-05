package test;

import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import util.Status;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    @Test
    void getId() {
        int id = manager.getId();
        Task task = new Task("Task", "TaskGetId", id);

        assertEquals(id, task.getId(), "Задаче присвоен неверный id");
    }

    @Test
    void getAllTasks() {
        Task task = new Task("Task", "TaskGetAllTasks", manager.getId());
        task.setStartTime(LocalDateTime.of(2023, 7, 26, 17,25));
        task.setDuration(60);
        manager.createTask(task);
        final Collection<Task> tasks = manager.getAllTasks();

        assertAll(
                () -> assertNotNull(tasks, "Задачи не возвращаются"),
                () -> assertEquals(1, tasks.size()),
                () -> assertTrue(tasks.contains(task))
        );
    }

    @Test
    void getAllEpics() {
        Epic epic = new Epic("Epic", "EpicGetAllEpics", manager.getId());
        manager.createTask(epic);
        final Collection<Task> epics = manager.getAllEpics();

        assertAll(
                () -> assertNotNull(epics, "Задачи не возвращаются"),
                () -> assertEquals(1, epics.size()),
                () -> assertTrue(epics.contains(epic))
        );
    }

    @Test
    void getAllSubtasks() {
        Epic epic = new Epic("Epic", "EpicGetAllSubtasks", manager.getId());
        Subtask subtask = new Subtask("Subtask", "SubtaskGetAllSubtasks", manager.getId(), epic);
        subtask.setStartTime(LocalDateTime.of(2024, 4, 4, 14,4));
        subtask.setDuration(40);
        manager.createTask(subtask);

        assertEquals(epic.getId(), subtask.getEpicId());

        final Collection<Task> subtasks = manager.getAllSubtasks();

        assertAll(
                () -> assertNotNull(subtasks, "Задачи не возвращаются"),
                () -> assertEquals(1, subtasks.size()),
                () -> assertTrue(subtasks.contains(subtask))
        );
    }

    @Test
    void removeAllTasks() {
        manager.createTask(new Task("Task", "TaskRemoveAllTasks", manager.getId()));

        assertFalse(manager.getAllTasks().isEmpty());

        manager.removeAllTasks();

        assertTrue(manager.getAllTasks().isEmpty(), "Задачи не удаляются");
    }

    @Test
    void removeAllEpics() {
        manager.createTask(new Epic("Epic", "EpicRemoveAllEpics", manager.getId()));

        assertFalse(manager.getAllEpics().isEmpty());

        manager.removeAllEpics();

        assertTrue(manager.getAllEpics().isEmpty(), "Задачи не удаляются");
    }

    @Test
    void removeAllSubtasks() {
        Epic epic = new Epic("Epic", "EpicRemoveAllSubtasks", manager.getId());
        manager.createTask(new Subtask("Subtask", "SubtaskRemoveAllSubtasks", manager.getId(), epic));

        assertFalse(manager.getAllSubtasks().isEmpty());

        manager.removeAllSubtasks();

        assertTrue(manager.getAllSubtasks().isEmpty(), "Задачи не удаляются");
    }

    @Test
    void getTaskById() {
        Task uncreatedTask = new Task("Task", "TaskGetTaskById", manager.getId());
        int id = uncreatedTask.getId();

        final NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> manager.getTaskById(id)
        );
        assertEquals("Задачи под номером " + id + " нет в списке задач", exception.getMessage());

        manager.createTask(uncreatedTask);
        Task actual = manager.getTaskById(id);

        assertEquals(uncreatedTask, actual, "Задачи не совпадают");
    }

    @Test
    void createTask() {
        Epic epic = new Epic("Epic", "EpicCreateTask", manager.getId());
        Subtask subtask = new Subtask("Subtask", "SubtaskCreateTask", manager.getId(), epic);
        subtask.setStartTime(LocalDateTime.of(2023, 3, 3, 15, 15));
        subtask.setDuration(30);
        manager.createTask(epic);
        manager.createTask(subtask);

        assertAll("Время выполнения должно совпадать",
                () -> assertEquals(subtask.getStartTime(), epic.getStartTime()),
                () -> assertEquals(subtask.getDuration(), epic.getDuration())
        );

        final Collection<Task> epics = manager.getAllEpics();
        final Collection<Task> subtasks = manager.getAllSubtasks();

        assertAll("Менеджер должен вернуть созданные задачи",
                () -> assertTrue(epics.contains(epic)),
                () -> assertTrue(subtasks.contains(subtask))
        );

        Task task = new Task("Task", "TaskValidateOverlapping", manager.getId());
        task.setStartTime(LocalDateTime.of(2023, 3, 3, 15, 0));
        task.setDuration(60);

        final DateTimeException exception = assertThrows(DateTimeException.class,
                () -> manager.createTask(task)
        );
        assertEquals("Задача пересекается по времени выполнения с одной из созданных", exception.getMessage());
    }

    @Test
    void updateTask() {
        Task task = new Task("Task", "TaskUpdateTask", manager.getId());
        task.setStartTime(LocalDateTime.of(2027, 8, 9, 10, 11));
        task.setDuration(70);
        manager.createTask(task);

        assertEquals(Status.NEW, task.getStatus());

        manager.updateTask(task);

        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertTrue(manager.getAllTasks().contains(task));
    }

    @Test
    void removeTask() {
        Task uncreatedTask = new Task("Task", "TaskRemoveTask", manager.getId());
        int id = uncreatedTask.getId();

        final NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> manager.removeTask(id)
        );
        assertEquals("Задачи под номером " + id + " нет в списке задач", exception.getMessage());

        manager.createTask(uncreatedTask);
        assertTrue(manager.getAllTasks().contains(uncreatedTask));

        manager.removeTask(id);
        assertFalse(manager.getAllTasks().contains(uncreatedTask));
    }

    @Test
    void getSubtasksOfEpic() {
        Epic uncreatedEpic = new Epic("Epic", "EpicGetSubtasksOfEpic", manager.getId());
        manager.createTask(new Subtask("Subtask1", "getSubtasksOfEpic", manager.getId(), uncreatedEpic));
        manager.createTask(new Subtask("Subtask1", "getSubtasksOfEpic", manager.getId(), uncreatedEpic));
        Collection<Task> subtasksFromEpic = uncreatedEpic.getSubtasks();

        assertEquals(2, subtasksFromEpic.size());

        final NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> manager.getSubtasksOfEpic(uncreatedEpic)
        );
        assertEquals("Такого эпика нет в списке задач", exception.getMessage());

        manager.createTask(uncreatedEpic);
        Collection<Task> subtasksFromManager = manager.getSubtasksOfEpic(uncreatedEpic);

        assertEquals(subtasksFromEpic.size(), subtasksFromManager.size(), "Количество подзадач не совпадает");

        subtasksFromManager.forEach(subtask -> assertEquals("getSubtasksOfEpic", subtask.getDescription(),
                "Подзадачи не совпадают")
        );
    }

    @Test
    void getPrioritizedTasks() {
        Task task = new Task("Task", "TaskGetPrioritizedTasks", manager.getId());
        task.setStartTime(LocalDateTime.of(2023, 3, 5, 12,0));
        Task task1 = new Task("Task1", "TaskGetPrioritizedTasks", manager.getId());
        task1.setStartTime(LocalDateTime.of(2023, 3, 5, 10,0));
        Task task2 = new Task("Task2", "TaskGetPrioritizedTasks", manager.getId());

        manager.createTask(task);
        manager.createTask(task1);
        manager.createTask(task2);

        final TreeSet<Task> prioritizedTasks = (TreeSet<Task>) manager.getPrioritizedTasks();

        assertAll(
                () -> assertNotNull(prioritizedTasks, "Задачи не возвращаются"),
                () -> assertEquals(task1, prioritizedTasks.first(), "Неверный порядок задач"),
                () -> assertEquals(task2, prioritizedTasks.last(), "Неверный порядок задач")
        );
    }
}