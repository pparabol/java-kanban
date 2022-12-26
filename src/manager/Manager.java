package manager;

import model.Subtask;
import model.Task;
import model.Epic;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id;
    public HashMap<Integer, Task> tasks;
    public HashMap<Integer, Task> epics;
    public HashMap<Integer, Task> subtasks;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public int getId() {
        return ++id;
    }

   public ArrayList<Task> getAllTasks(HashMap<Integer, Task> tasks) {
        ArrayList<Task> taskList = new ArrayList<>();
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("В этой категории нет ни одной задачи");
        } else {
            taskList.addAll(tasks.values());
        }
        return taskList;
    }

    public void removeAllTasks(HashMap<Integer, Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            tasks.clear();
            System.out.println("Все задачи категории удалены");
        }
    }

   public Task getTaskById(int id) {
        Task task = null;
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

    public void createTask(Task task) {
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

    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Не удалось обновить задачу. Вероятно, задача ещё не создана");
            return;
        }
        for (Integer id : tasks.keySet()) {
            if (tasks.get(id) == task) {
                task.changeStatus();
                tasks.put(id, task);
            }
        }
        for (Integer id : epics.keySet()) {
            if (epics.get(id) == task) {
                task.changeStatus();
                epics.put(id, task);
            }
        }
        for (Integer id : subtasks.keySet()) {
            if (subtasks.get(id) == task) {
                task.changeStatus();
                subtasks.put(id, task);
            }
        }
    }

    public void removeTaskById(int id) {
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

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        if (epics.containsValue(epic)) {
            subtasksOfEpic = epic.getSubtasks();
        } else {
            System.out.println("Такого эпика нет в списке задач");
        }
        return subtasksOfEpic;
    }

}
