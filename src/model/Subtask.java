package model;

import util.TaskType;

public class Subtask extends Task {

    private final Task epic;

    public Subtask(String title, String description, int id, Epic epic) {
        super(title, description, id);
        this.epic = epic;
        epic.setSubtasks(this);
        type = TaskType.SUBTASK;
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
                "epic.id=" + epic.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
