package test;

import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;
import util.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic;
    private TaskManager manager;

    @BeforeEach
    void setEpicAndManager() {
        manager = Managers.getDefault();
        epic = new Epic("Epic", "epic description", manager.getId());
    }

    @Test
    void shouldBeNewWhenSubtasksListIsEmpty() {
        final List<Task> subtasks = epic.getSubtasks();
        final Status expectedStatus = Status.NEW;

        assertAll(
                () -> assertTrue(subtasks.isEmpty(), "Список не пуст"),
                () -> assertEquals(expectedStatus, epic.getStatus(), "Неверный статус эпика")
        );
    }

    @Test
    void shouldBeNewWhenSubtasksAreNew() {
        List<Subtask> subs = List.of(new Subtask("Subtask1", "description", manager.getId(), epic),
                new Subtask("Subtask2", "description", manager.getId(), epic),
                new Subtask("Subtask3", "description", manager.getId(), epic)
        );
        Status expectedStatus = Status.NEW;
        List<Task> subtasks = epic.getSubtasks();

        assertEquals(3, subtasks.size(), "Неверное количество подзадач");
        subtasks.forEach(subtask -> assertEquals(expectedStatus, subtask.getStatus(),
                "Неверный статус подзачадачи")
        );
        assertEquals(expectedStatus, epic.getStatus(), "Неверный статус эпика");
    }

    @Test
    void shouldBeDoneWhenSubtasksAreDone() {
        List<Subtask> subs = List.of(new Subtask("Subtask1", "description", manager.getId(), epic),
                new Subtask("Subtask2", "description", manager.getId(), epic),
                new Subtask("Subtask3", "description", manager.getId(), epic)
        );
        Status expectedStatus = Status.DONE;

        subs.forEach(subtask -> {
            subtask.changeStatus();
            subtask.changeStatus();
            assertEquals(expectedStatus, subtask.getStatus(), "Неверный статус подзачадачи");
        });

        assertEquals(expectedStatus, epic.getStatus(), "Неверный статус эпика");
    }

    @Test
    void shouldBeInProgressWhenSubtasksAreNewAndDone() {
        Subtask sub1 = new Subtask("Subtask1", "description", manager.getId(), epic);
        Subtask sub2 = new Subtask("Subtask1", "description", manager.getId(), epic);
        final Status expectedStatus = Status.IN_PROGRESS;

        sub1.changeStatus();
        sub1.changeStatus();

        assertAll(
                () -> assertEquals(Status.DONE, sub1.getStatus(), "Неверный статус подзачадачи"),
                () -> assertEquals(Status.NEW, sub2.getStatus(), "Неверный статус подзачадачи"),
                () -> assertEquals(expectedStatus, epic.getStatus(), "Неверный статус эпика")
        );
    }

    @Test
    void shouldBeInProgressWhenSubtasksAreInProgress() {
        List<Subtask> subs = List.of(new Subtask("Subtask1", "description", manager.getId(), epic),
                new Subtask("Subtask2", "description", manager.getId(), epic),
                new Subtask("Subtask3", "description", manager.getId(), epic));
        Status expectedStatus = Status.IN_PROGRESS;

        subs.forEach(subtask -> {
            subtask.changeStatus();
            assertEquals(expectedStatus, subtask.getStatus(), "Неверный статус подзачадачи");
        });

        assertEquals(expectedStatus, epic.getStatus(), "Неверный статус эпика");
    }

    @Test
    void shouldCalculateStartTimeAndEndTimeAndDuration() {
        Subtask sub1 = new Subtask("Subtask1", "description", manager.getId(), epic);
        Subtask sub2 = new Subtask("Subtask1", "description", manager.getId(), epic);
        sub1.setStartTime(LocalDateTime.now());
        sub1.setDuration(60);
        sub2.setStartTime(LocalDateTime.of(2024, 5, 25, 5, 45));
        sub2.setDuration(60);

        assertAll(
                () -> assertEquals(sub1.getStartTime(), epic.getStartTime(), "Начало рассчитано неверно"),
                () -> assertEquals(sub2.getEndTime(), epic.getEndTime(), "Завершение рассчитано неверно"),
                () -> assertEquals(120, epic.getDuration().toMinutes(),
                        "Продолжительность рассчитана неверно")
        );
    }
}