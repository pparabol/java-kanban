package model;

import util.Status;
import util.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Task> subtasks;

    public Epic(String title, String description, int id) {
        super(title, description, id);
        subtasks = new ArrayList<>();
        type = TaskType.EPIC;
    }

    public void setSubtasks(Task subtask) {
        subtasks.add(subtask);
    }

    public List<Task> getSubtasks() {
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
        return "Epic{" +
                "subtasks.size()=" + subtasks.size() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
