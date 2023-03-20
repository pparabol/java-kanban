package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String serverUrl;
    private final String apiToken;

    public KVTaskClient(String serverUrl) throws IOException, InterruptedException {
        this.serverUrl = serverUrl;
        apiToken = getApiToken();
    }

    private String getApiToken() throws IOException, InterruptedException {
        URI url = URI.create(serverUrl + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(serverUrl + "/save/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            } else {
                System.out.println("Данные успешно сохранены");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        URI uri = URI.create(serverUrl + "/load/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return "Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode();
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            return "Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.";
        }
    }
}
