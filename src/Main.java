import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) taskManager;

        Task task1 = new Task("Кино", "Купить билеты");
        Task task2 = new Task("Проект", "Написать рабочий код");
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        Epic epic1 = new Epic("Важный эпик", "Много дел");
        Subtask subtask1 = new Subtask("Кот", "Покормить кота");
        Subtask subtask2 = new Subtask("Пёс", "Погладить собаку");
        epic1.setSubtasks(subtask1);
        epic1.setSubtasks(subtask2);
        inMemoryTaskManager.createTask(epic1);
        inMemoryTaskManager.createTask(subtask1);
        inMemoryTaskManager.createTask(subtask2);

        Epic epic2 = new Epic("Очень важный эпик", "Куча дел");
        Subtask subtask3 = new Subtask("Суши", "Заказать доставку");
        epic2.setSubtasks(subtask3);
        inMemoryTaskManager.createTask(epic2);
        inMemoryTaskManager.createTask(subtask3);

        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());

        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(subtask1);
        inMemoryTaskManager.updateTask(epic1);
        inMemoryTaskManager.updateTask(subtask3);
        inMemoryTaskManager.updateTask(epic2);

        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubtasks());

        System.out.println(inMemoryTaskManager.getSubtask(5));
        System.out.println(inMemoryTaskManager.getTask(2));
        System.out.println(inMemoryTaskManager.getEpic(3));
        inMemoryTaskManager.getTask(10);

        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.getSubtask(5);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getEpic(3);

        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.removeTask(1);
        inMemoryTaskManager.removeTask(3);

        inMemoryTaskManager.removeAllTasks();
        System.out.println(inMemoryTaskManager.getAllTasks());

    }
}
