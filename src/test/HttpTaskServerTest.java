package test;

import api.HttpTaskServer;
import api.KVServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static api.HttpTaskServer.gson;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private TaskManager manager;
    private KVServer kvServer;
    private HttpTaskServer taskServer;

    @BeforeEach
    void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault("http://localhost:8078");
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @Test
    void shouldPostTask() {
        Task newTask = new Task("Task", "shouldPost", 1);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
            assertEquals(newTask, manager.getTaskById(newTask.getId()));
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldGetPrioritized() {
        Task task = new Task("Task", "getPrioritized", manager.getId());
        task.setStartTime(LocalDateTime.of(2023, 3, 5, 12,0));
        Task task1 = new Task("Task1", "getPrioritized", manager.getId());
        task1.setStartTime(LocalDateTime.of(2023, 3, 5, 10,0));
        Task task2 = new Task("Task2", "getPrioritized", manager.getId());
        manager.createTask(task);
        manager.createTask(task1);
        manager.createTask(task2);

        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonArray(), "Получен неверный JSON");
            assertEquals(3, jsonElement.getAsJsonArray().size(),
                    "Получено неверное количество задач");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldGetTasks() {
        manager.createTask(new Task("Task", "getTasks", manager.getId()));
        manager.createTask(new Task("Task1", "getTasks", manager.getId()));

        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonArray(), "Получен неверный JSON");
            assertEquals(2, jsonElement.getAsJsonArray().size(),
                    "Получено неверное количество задач");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldGetEpics() {
        manager.createTask(new Epic("Epic", "getEpics", manager.getId()));
        manager.createTask(new Epic("Epic1", "getEpics", manager.getId()));

        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonArray(), "Получен неверный JSON");
            assertEquals(2, jsonElement.getAsJsonArray().size(),
                    "Получено неверное количество эпиков");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }

    }

    @Test
    void shouldGetSubtasks() {
        Epic epic = new Epic("Epic", "getSubtasks", manager.getId());
        manager.createTask(new Subtask("Subtask", "getSubtasks", manager.getId(), epic));
        manager.createTask(new Subtask("Subtask1", "getSubtasks", manager.getId(), epic));

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonArray(), "Получен неверный JSON");
            assertEquals(2, jsonElement.getAsJsonArray().size(),
                    "Получено неверное количество подзадач");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldGetTaskById() {
        Task task = new Task("Task", "getById", manager.getId());
        int id = task.getId();
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonObject(), "Получен неверный JSON");

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            int jsonId = jsonObject.get("id").getAsInt();
            assertEquals(id, jsonId, "Идентификаторы задач не совпадают");

            String jsonDescription = jsonObject.get("description").getAsString();
            assertEquals(task.getDescription(), jsonDescription, "Задачи не совпадают");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldGetSubtasksOfEpicById() {
        Epic epic = new Epic("Epic", "getSubtasksById", manager.getId());
        manager.createTask(epic);
        manager.createTask(new Subtask("Subtask", "getSubtasksById", manager.getId(), epic));
        manager.createTask(new Subtask("Subtask1", "getSubtasksById", manager.getId(), epic));
        int id = epic.getId();

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonArray(), "Получен неверный JSON");
            assertEquals(2, jsonElement.getAsJsonArray().size(),
                    "Получено неверное количество подзадач");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldDeleteTasks() {
        manager.createTask(new Task("Task", "deleteTasks", manager.getId()));
        manager.createTask(new Task("Task1", "deleteTasks", manager.getId()));

        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertAll(
                    () -> assertEquals(200, response.statusCode()),
                    () -> assertEquals("Все задачи успешно удалены", response.body(),
                            "Задачи не удалились")
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldDeleteEpics() {
        manager.createTask(new Epic("Epic", "deleteEpics", manager.getId()));
        manager.createTask(new Epic("Epic1", "deleteEpics", manager.getId()));

        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertAll(
                    () -> assertEquals(200, response.statusCode()),
                    () -> assertEquals("Все эпики успешно удалены", response.body(),
                            "Эпики не удалились")
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldDeleteSubtasks() {
        Epic epic = new Epic("Epic", "deleteSubtasks", manager.getId());
        manager.createTask(new Subtask("Subtask", "deleteSubtasks", manager.getId(), epic));
        manager.createTask(new Subtask("Subtask1", "deleteSubtasks", manager.getId(), epic));

        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertAll(
                    () -> assertEquals(200, response.statusCode()),
                    () -> assertEquals("Все подзадачи успешно удалены", response.body(),
                            "Подзадачи не удалились")
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldDeleteTaskById() {
        Task task = new Task("Task", "getById", manager.getId());
        int id = task.getId();
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertAll(
                    () -> assertEquals(200, response.statusCode()),
                    () -> assertEquals("Задача успешно удалена", response.body(),
                            "Задачи не удалились")
            );
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @Test
    void shouldGetHistory() {
        manager.createTask(new Task("Task1", "getHistory", 1));
        manager.createTask(new Task("Task2", "getHistory", 2));

         manager.getTaskById(1);
         manager.getTaskById(2);

        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            JsonElement jsonElement = JsonParser.parseString(response.body());
            assertTrue(jsonElement.isJsonArray(), "Получен неверный JSON");

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            assertEquals(2, jsonArray.size(), "Размер истории не совпадает");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.");
        }
    }

    @AfterEach
    void stopServers() {
        kvServer.stop();
        taskServer.stop();
    }
}