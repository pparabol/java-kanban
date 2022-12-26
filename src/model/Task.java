package model;

public class Task {
    protected String title;
    protected String description;
    protected String status;

    public Task(String title, String description) {
        status = "NEW";
        this.title = title;
        this.description = description;
    }

    public void changeStatus() {
        if (status.equals("NEW")) {
            status = "IN_PROGRESS";
        } else {
            status = "DONE";
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
