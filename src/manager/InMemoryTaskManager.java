package manager;

import manager.history.HistoryManager;
import model.Subtask;
import model.Task;
import model.Epic;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Task> epics;
    private final Map<Integer, Task> subtasks;
    public final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.historyManager = historyManager;
    }

    @Override
    public int getId() {
        return ++id;
    }

   @Override
    public Collection<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            taskList.addAll(tasks.values());
        }
        return taskList;
    }

    @Override
    public Collection<Task> getAllEpics() {
        ArrayList<Task> taskList = new ArrayList<>();
        if (epics.isEmpty()) {
            System.out.println("Список эпиков пуст");
        } else {
            taskList.addAll(epics.values());
        }
        return taskList;
    }

    @Override
    public Collection<Task> getAllSubtasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        if (subtasks.isEmpty()) {
            System.out.println("Список подзадач пуст");
        } else {
            taskList.addAll(subtasks.values());
        }
        return taskList;
    }

    @Override
    public void removeAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            tasks.clear();
            System.out.println("Все задачи удалены");
        }
    }

    @Override
    public void removeAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            epics.clear();
            System.out.println("Все эпики удалены");
        }
    }

    @Override
    public void removeAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            subtasks.clear();
            System.out.println("Все подзадачи удалены");
        }
    }

   @Override
   public Task getTask(int id) {
        Task task = null;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task);
        } else {
            System.out.printf("Задачи под номером %d нет в списке задач\n", id);
        }
        return task;
    }

    @Override
    public Task getEpic(int id) {
        Task task = null;
        if (epics.containsKey(id)) {
            task = epics.get(id);
            historyManager.add(task);
        } else {
            System.out.printf("Эпика под номером %d нет в списке задач\n", id);
        }
        return task;
    }

    @Override
    public Task getSubtask(int id) {
        Task task = null;
        if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
            historyManager.add(task);
        } else {
            System.out.printf("Подзадачи под номером %d нет в списке задач\n", id);
        }
        return task;
    }

    @Override
    public void createTask(Task task) {
        if (task.getClass() == Task.class) {
            tasks.put(task.getId(), task);
        } else if(task.getClass() == Epic.class) {
            epics.put(task.getId(), task);
        } else if(task.getClass() == Subtask.class) {
            subtasks.put(task.getId(), task);
        } else {
            System.out.println("Невозможно создать задачу. Такая категория не поддерживается");
        }
    }

    @Override
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

    @Override
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
            historyManager.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.printf("Задачи под номером %d нет в списке задач\n", id);
        }
    }

    @Override
    public Collection<Task> getSubtasksOfEpic(Task task) {
        Epic epic = (Epic) task;
        List<Task> subtasksOfEpic = new ArrayList<>();
        if (epics.containsValue(epic)) {
            subtasksOfEpic = epic.getSubtasks();
        } else {
            System.out.println("Такого эпика нет в списке задач");
        }
        return subtasksOfEpic;
    }

}
