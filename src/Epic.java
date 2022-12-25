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
        if (subtasks == null) {
            status = "NEW";
            return;
        }
        int newTasks = 0;
        int doneTasks = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.status.equals("NEW")) {
                newTasks ++;
            } else if (subtask.status.equals("DONE")) {
                doneTasks ++;
            }
        }
        if (newTasks == subtasks.size()) {
           status = "NEW";
        } else if (doneTasks == subtasks.size()) {
            status = "DONE";
        } else {
            status = "IN_PROGRESS";
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
