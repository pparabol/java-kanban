import manager.FileBackedTasksManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

public class Main {

    public static void main(String[] args) {
        TaskManager fileManager = Managers.getDefault("src/file/data.csv");

        fileManager.createTask(new Task("Кино", "Купить билеты", fileManager.getId()));
        fileManager.createTask(new Task("Проект", "Написать рабочий код", fileManager.getId()));

        Epic epic1 = new Epic("Важный эпик", "Много дел", fileManager.getId());
        fileManager.createTask(epic1);
        fileManager.createTask(new Subtask("Кот", "Покормить кота", fileManager.getId(), epic1));
        fileManager.createTask(new Subtask("Пёс", "Погладить собаку", fileManager.getId(), epic1));
        fileManager.createTask(new Subtask("Суши", "Заказать доставку", fileManager.getId(), epic1));

        Epic epic2 = new Epic("Очень важный эпик", "Куча дел", fileManager.getId());
        fileManager.createTask(epic2);

        fileManager.getTaskById(5);
        fileManager.getTaskById(3);
        fileManager.getTaskById(4);

        FileBackedTasksManager fM = (FileBackedTasksManager) Managers.loadFromFile("src/file/data.csv");

        System.out.println(fM.getTaskById(1));

        System.out.println(fM.historyManager.getHistory());
    }
}
