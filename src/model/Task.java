package model;

import util.Status;

public class Task {
    protected String title;
    protected String description;
    protected Status status;

    public Task(String title, String description) {
        status = Status.NEW;
        this.title = title;
        this.description = description;
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
        return "model.Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
