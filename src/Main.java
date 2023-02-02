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

        Task task1 = new Task("Кино", "Купить билеты", inMemoryTaskManager.getId());
        Task task2 = new Task("Проект", "Написать рабочий код", inMemoryTaskManager.getId());
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        Epic epic1 = new Epic("Важный эпик", "Много дел", inMemoryTaskManager.getId());
        Subtask subtask1 = new Subtask("Кот", "Покормить кота", inMemoryTaskManager.getId(), epic1);
        Subtask subtask2 = new Subtask("Пёс", "Погладить собаку", inMemoryTaskManager.getId(), epic1);
        Subtask subtask3 = new Subtask("Суши", "Заказать доставку", inMemoryTaskManager.getId(), epic1);
        epic1.setSubtasks(subtask1);
        epic1.setSubtasks(subtask2);
        epic1.setSubtasks(subtask3);
        inMemoryTaskManager.createTask(epic1);
        inMemoryTaskManager.createTask(subtask1);
        inMemoryTaskManager.createTask(subtask2);
        inMemoryTaskManager.createTask(subtask3);

        Epic epic2 = new Epic("Очень важный эпик", "Куча дел", inMemoryTaskManager.getId());
        inMemoryTaskManager.createTask(epic2);

        System.out.println(inMemoryTaskManager.getSubtask(5));
        System.out.println(inMemoryTaskManager.getTask(2));
        System.out.println(inMemoryTaskManager.getEpic(3));
        System.out.println(inMemoryTaskManager.getSubtask(5));
        System.out.println(inMemoryTaskManager.getTask(2));

        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubtask(4);

        System.out.println(inMemoryTaskManager.historyManager.getHistory());

        inMemoryTaskManager.removeTask(5);
        inMemoryTaskManager.removeTask(4);

        System.out.println(inMemoryTaskManager.historyManager.getHistory());

    }
}
