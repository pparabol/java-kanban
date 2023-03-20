import com.google.gson.Gson;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVServer;
import util.Managers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        Gson gson = new Gson();

        TaskManager httpManager = Managers.getDefault("http://localhost:8078");

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



        System.out.println(gson.toJson(httpManager.getTasks()));
        System.out.println(gson.toJson(httpManager.getSubtasks()));



        /* String str = "tasks/task/?id=3";
        String[] arr = str.split("/");
        System.out.println(arr[arr.length-1]);
        if (arr[arr.length - 1].startsWith("?id")) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

        int id = Integer.parseInt(arr[arr.length - 1].split("=")[1]);
        System.out.println(id);*/

        /*HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());*/

        //new KVServer().start();

    }
}
