package model;

import util.Status;
import util.TaskType;

public class Task {
    protected final String title;
    protected final String description;
    protected Status status;
    protected final int id;
    protected TaskType type;

    public Task(String title, String description, int id) {
        status = Status.NEW;
        type = TaskType.TASK;
        this.title = title;
        this.description = description;
        this.id = id;
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
                '}';
    }
}
