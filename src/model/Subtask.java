package model;

public class Subtask extends Task {

    private final Task epic;

    public Subtask(String title, String description, int id, Task epic) {
        super(title, description, id);
        this.epic = epic;
    }

    @Override
    public void changeStatus() {
        super.changeStatus();
        epic.changeStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic.id=" + epic.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
