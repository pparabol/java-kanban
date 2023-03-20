package model;

import util.TaskType;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private final Task epic;

    public Subtask(String title, String description, int id, Epic epic) {
        super(title, description, id);
        this.epic = epic;
        epic.setSubtasks(this);
        type = TaskType.SUBTASK;
    }

    @Override
    public void setDuration(long minutes) {
        super.setDuration(minutes);
        epic.setDuration(epic.getDuration().toMinutes());
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
        epic.setStartTime(epic.getStartTime());
    }

    public int getEpicId() {
        return epic.getId();
    }

    @Override
    public void changeStatus() {
        super.changeStatus();
        epic.changeStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic.id=" + getEpicId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", startTime=" + (startTime == null ? "null" : startTime.format(DATE_TIME_FORMATTER)) +
                ", duration=" + duration.toMinutes() +
                '}';
    }
}
