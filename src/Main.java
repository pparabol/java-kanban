import api.HttpTaskServer;
import api.KVServer;
import manager.HttpTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new KVServer().start();

        HttpTaskManager httpManager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
        new HttpTaskServer(httpManager).start();

        httpManager.createTask(new Task("Кино", "Купить билеты", httpManager.getId()));
        httpManager.createTask(new Task("Проект", "Написать рабочий код", httpManager.getId()));

        Epic epic1 = new Epic("Важный эпик", "Много дел", httpManager.getId());
        httpManager.createTask(epic1);
        httpManager.createTask(new Subtask("Кот", "Покормить кота", httpManager.getId(), epic1));
        httpManager.createTask(new Subtask("Пёс", "Погладить собаку", httpManager.getId(), epic1));
        httpManager.createTask(new Subtask("Суши", "Заказать доставку", httpManager.getId(), epic1));

        Epic epic2 = new Epic("Очень важный эпик", "Куча дел", httpManager.getId());
        httpManager.createTask(epic2);

        httpManager.getTaskById(5);
        httpManager.getTaskById(3);
        httpManager.getTaskById(4);

        httpManager = (HttpTaskManager) Managers.getDefault("http://localhost:8078");
        httpManager.loadFromServer();

        System.out.println(httpManager.getEpics());
        System.out.println(httpManager.getHistory());
        System.out.println(httpManager.getPrioritizedTasks());
    }
}
