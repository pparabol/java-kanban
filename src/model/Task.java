package model;

import util.Status;
import util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected final String title;
    protected final String description;
    protected Status status;
    protected final int id;
    protected TaskType type;
    protected Duration duration;
    protected LocalDateTime startTime;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm");


    public Task(String title, String description, int id) {
        status = Status.NEW;
        type = TaskType.TASK;
        this.title = title;
        this.description = description;
        this.id = id;
        duration = Duration.ofMinutes(0);
    }

    public void setDuration(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return type;
    }

    public void changeStatus() {
        switch (status) {
            case NEW:
                status = Status.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                status = Status.DONE;
                break;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", startTime=" + (startTime == null ? "null" : startTime.format(DATE_TIME_FORMATTER)) +
                ", duration=" + duration.toMinutes() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() &&
                getTitle().equals(task.getTitle()) &&
                getDescription().equals(task.getDescription()) &&
                getStatus() == task.getStatus() &&
                getType() == task.getType() &&
                Objects.equals(getDuration(), task.getDuration()) &&
                Objects.equals(getStartTime(), task.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
