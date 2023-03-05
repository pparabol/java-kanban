package manager;

import manager.history.HistoryManager;
import model.Subtask;
import model.Task;
import model.Epic;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Task> epics;
    protected final Map<Integer, Task> subtasks;
    public final HistoryManager historyManager;
    private final Set<Task> prioritizedTasks;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.historyManager = historyManager;
        prioritizedTasks = new TreeSet<>((t1, t2) -> {
            if (t1.getStartTime() != null && t2.getStartTime() != null) {
                return t1.getStartTime().compareTo(t2.getStartTime());
            } else if (t1.getStartTime() == null && t2.getStartTime() != null) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    @Override
    public int getId() {
        return ++id;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
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
            prioritizedTasks.removeAll(tasks.values());
            tasks.clear();
        }
    }

    @Override
    public void removeAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            prioritizedTasks.removeAll(epics.values());
            epics.clear();
        }
    }

    @Override
    public void removeAllSubtasks() {
        if (subtasks.isEmpty()) {
            System.out.println("В этой категории нет задач");
        } else {
            prioritizedTasks.removeAll(subtasks.values());
            subtasks.clear();
        }
    }

   @Override
   public Task getTaskById(int id) throws NoSuchElementException {
        Task task;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            historyManager.add(task);
        } else if (epics.containsKey(id)) {
            task = epics.get(id);
            historyManager.add(task);
        } else if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
            historyManager.add(task);
        } else {
            throw new NoSuchElementException("Задачи под номером " + id + " нет в списке задач");
        }
        return task;
    }

    @Override
    public void createTask(Task task) throws NullPointerException, DateTimeException {
        if (task == null) {
            throw new NullPointerException("Задача не найдена");
        }

        if (task.getClass() == Task.class) {
            tasks.put(task.getId(), task);
        } else if(task.getClass() == Epic.class) {
            epics.put(task.getId(), task);
        } else if(task.getClass() == Subtask.class) {
            subtasks.put(task.getId(), task);
        }

        if (!prioritizedTasks.isEmpty() && task.getStartTime() != null) {
            if (isOverlapping(task)) {
                throw new DateTimeException("Задача пересекается по времени выполнения с одной из созданных");
            }
        }
        prioritizedTasks.add(task);
    }

    @Override
    public void updateTask(Task task) throws NullPointerException {
        if (task == null) {
            throw new NullPointerException("Задача не найдена");
        }
        prioritizedTasks.remove(task);
        int id = task.getId();
        task.changeStatus();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        } else if (epics.containsKey(id)) {
            epics.put(id, task);
        } else if (subtasks.containsKey(id)) {
            subtasks.put(id, task);
        }
        prioritizedTasks.add(task);
    }

    @Override
    public void removeTask(int id) throws NoSuchElementException {
        Task task;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
            prioritizedTasks.remove(task);
            tasks.remove(id);
            historyManager.remove(id);
        } else if (epics.containsKey(id)) {
            task = epics.get(id);
            prioritizedTasks.remove(task);
            epics.remove(id);
            historyManager.remove(id);
        } else if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
            prioritizedTasks.remove(task);
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new NoSuchElementException("Задачи под номером " + id + " нет в списке задач");
        }
    }

    @Override
    public Collection<Task> getSubtasksOfEpic(Task task) throws NoSuchElementException {
        Epic epic = (Epic) task;
        List<Task> subtasksOfEpic;
        if (epics.containsValue(epic)) {
            subtasksOfEpic = epic.getSubtasks();
        } else {
             throw new NoSuchElementException("Такого эпика нет в списке задач");
        }
        return subtasksOfEpic;
    }

    private boolean isOverlapping(Task task) {
        boolean isOverlapping = false;
        LocalDateTime startOfTask = task.getStartTime();
        LocalDateTime endOfTask = task.getEndTime();
        for (Task t : prioritizedTasks) {
            if (t.getStartTime() == null) continue;
            LocalDateTime startOfT = t.getStartTime();
            LocalDateTime endOfT = t.getEndTime();
            boolean isCovering = startOfT.isBefore(startOfTask) && endOfT.isAfter(endOfTask);
            boolean isOverlappingByEnd = startOfT.isBefore(startOfTask) && endOfT.isAfter(startOfTask);
            boolean isOverlappingByStart = startOfT.isBefore(endOfTask) && endOfT.isAfter(endOfTask);
            boolean isWithin = startOfT.isAfter(startOfTask) && endOfT.isBefore(endOfTask);
            isOverlapping = isCovering || isOverlappingByEnd || isOverlappingByStart || isWithin;
        }
        return isOverlapping;
    }
}
