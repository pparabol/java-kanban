package model;

import util.Status;

public class Task {
    protected final String title;
    protected final String description;
    protected Status status;
    protected int id;

    public Task(String title, String description, int id) {
        status = Status.NEW;
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
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
