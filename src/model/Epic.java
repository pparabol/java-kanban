package model;

import util.Status;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Task> subtasks;
    private final LocalDateTime endTime;

    public Epic(String title, String description, int id) {
        super(title, description, id);
        subtasks = new ArrayList<>();
        type = TaskType.EPIC;
        startTime = getStartTime();
        endTime = getEndTime();
    }

    @Override
    public Duration getDuration() {
        this.duration = Duration.ofMinutes(0);
        if (!subtasks.isEmpty()) {
            subtasks.forEach(subtask -> this.duration = duration.plus(subtask.getDuration()));
        }
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtasks.isEmpty()) return null;

        int counter = (int) subtasks.stream().filter(subtask -> subtask.getStartTime() == null).count();
        if (counter >= 1) return null;

        LocalDateTime start = subtasks.get(0).getStartTime();
        for (int i = 1; i < subtasks.size(); i++) {
            LocalDateTime checker = subtasks.get(i).getStartTime();
            if (checker.isBefore(start)) {
                start = checker;
            }
        }
        return start;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.isEmpty()) {
            return null;
        }

        LocalDateTime end = subtasks.get(0).getEndTime();
        for (int i = 1; i < subtasks.size(); i++) {
            LocalDateTime checker = subtasks.get(i).getEndTime();
            if (checker.isAfter(end)) {
                end = checker;
            }
        }
        return end;
    }

    public void setSubtasks(Task task) {
        subtasks.add(task);
    }

    public List<Task> getSubtasks() {
        return subtasks;
    }

    @Override
    public void changeStatus() {
        if (subtasks == null || subtasks.isEmpty()) {
            status = Status.NEW;
            return;
        }

        int tasks = 0;
        Status checker = null;

        for(int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).status == subtasks.get(subtasks.size() - 1).status) {
                tasks++;
                checker = subtasks.get(i).status;
            }
        }
        if (tasks == subtasks.size() && checker == Status.NEW) {
            status = Status.NEW;
        } else if (tasks == subtasks.size() && checker == Status.DONE) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks.size()=" + subtasks.size() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", startTime=" + (startTime == null ? "null" : startTime.format(DATE_TIME_FORMATTER)) +
                ", duration=" + duration.toMinutes() +
                '}';
    }
}
