package model;

public class Subtask extends Task {

    public Subtask(String title, String description) {
        super(title, description);
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
