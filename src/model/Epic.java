package model;

import util.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Task> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        subtasks = new ArrayList<>();
    }

    public void setSubtasks(Task subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Task> getSubtasks() {
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
        return "model.Epic{" +
                "subtasks=" + subtasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
