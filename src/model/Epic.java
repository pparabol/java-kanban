package model;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description);
        subtasks = new ArrayList<>();
    }

    public void setSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void changeStatus() {
        if (subtasks == null || subtasks.isEmpty()) {
            status = "NEW";
            return;
        }

        int tasks = 0;
        String checker = "";

        for(int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).status.equals(subtasks.get(subtasks.size() - 1).status)) {
                tasks++;
                checker = subtasks.get(i).status;
            }
        }
        if (tasks == subtasks.size() && checker.equals("NEW")) {
            status = "NEW";
        } else if (tasks == subtasks.size() && checker.equals("DONE")) {
            status = "DONE";
        } else {
            status = "IN_PROGRESS";
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
