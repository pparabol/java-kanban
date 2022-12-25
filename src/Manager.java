import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id;
    HashMap<Integer, Object> tasks;
    HashMap<Integer, Object> epics;
    HashMap<Integer, Object> subtasks;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public int getId() {
        return ++id;
    }

   ArrayList<Object> getAllTasks(HashMap<Integer, Object> tasks) {
        ArrayList<Object> taskList = new ArrayList<>();
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("В этой категории нет ни одной задачи");
        } else {
            taskList.addAll(tasks.values());
        }
        return taskList;
    }

    void removeAllTasks(HashMap<Integer, Object> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            tasks.clear();
            System.out.println("Все задачи категории удалены");
        }
    }

   Object getTaskById(int id) {
        Object task = null;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else if (epics.containsKey(id)) {
            task = epics.get(id);
        } else if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
        } else {
            System.out.println("Задачи под номером " + id + " нет в списке задач");
        }
        return task;
    }

    void createTask(Object task) {
        if (task.getClass() == Task.class) {
            tasks.put(getId(), task);
        } else if(task.getClass() == Epic.class) {
            epics.put(getId(), task);
        } else if(task.getClass() == Subtask.class) {
            subtasks.put(getId(), task);
        } else {
            System.out.println("Невозможно создать задачу. Такая категория не поддерживается");
        }
    }

    void updateTask(Object task) {
        if (task.getClass() == Task.class) {
            for (Integer id : tasks.keySet()) {
                if (tasks.get(id) == task) {
                    ((Task) task).changeStatus();
                    tasks.put(id, task);
                }
            }
        } else if (task.getClass() == Epic.class) {
            for (Integer id : epics.keySet()) {
                if (epics.get(id) == task) {
                    ((Epic) task).changeStatus();
                    epics.put(id, task);
                }
            }
        } else if (task.getClass() == Subtask.class) {
            for (Integer id : subtasks.keySet()) {
                if (subtasks.get(id) == task) {
                    ((Subtask) task).changeStatus();
                    subtasks.put(id, task);
                }
            }
        } else {
            System.out.println("Не удалось обновить задачу. " +
                    "Вероятно, указанная задача не относится ни к одной из категорий");
        }
    }

    void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        } else {
            System.out.println("Задачи под номером " + id + " нет в списке задач");
        }
    }

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        if (epics.containsValue(epic)) {
            subtasksOfEpic = epic.getSubtasks();
        } else {
            System.out.println("Такого эпика нет в списке задач");
        }
        return subtasksOfEpic;
    }

}
