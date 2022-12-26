import manager.Manager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Кино", "Купить билеты");
        Task task2 = new Task("Проект", "Написать рабочий код");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Важный эпик", "Много дел");
        Subtask subtask1 = new Subtask("Кот", "Покормить кота");
        Subtask subtask2 = new Subtask("Пёс", "Погладить собаку");
        epic1.setSubtasks(subtask1);
        epic1.setSubtasks(subtask2);
        manager.createTask(epic1);
        manager.createTask(subtask1);
        manager.createTask(subtask2);

        Epic epic2 = new Epic("Очень важный эпик", "Куча дел");
        Subtask subtask3 = new Subtask("Суши", "Заказать доставку");
        epic2.setSubtasks(subtask3);
        manager.createTask(epic2);
        manager.createTask(subtask3);

        System.out.println(manager.getAllTasks(manager.tasks));
        System.out.println(manager.getAllTasks(manager.epics));
        System.out.println(manager.getAllTasks(manager.subtasks));

        manager.updateTask(task1);
        manager.updateTask(subtask1);
        manager.updateTask(epic1);
        manager.updateTask(subtask3);
        manager.updateTask(epic2);

        System.out.println(manager.getAllTasks(manager.tasks));
        System.out.println(manager.getAllTasks(manager.epics));
        System.out.println(manager.getAllTasks(manager.subtasks));

        manager.getTaskById(5);
        manager.getTaskById(2);

        manager.removeTaskById(1);
        manager.removeTaskById(3);

        manager.removeAllTasks(manager.subtasks);
        System.out.println(manager.getAllTasks(manager.subtasks));

    }
}
