package api.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Epic;
import model.Task;
import util.Status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static model.Task.DATE_TIME_FORMATTER;

public class EpicAdapter extends TypeAdapter<Epic> {
    private static final Map<Integer, Task> subtasks = new HashMap<>();
    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("title");
        jsonWriter.value(epic.getTitle());
        jsonWriter.name("description");
        jsonWriter.value(epic.getDescription());
       jsonWriter.name("subtasksIds");
        StringJoiner joiner = new StringJoiner(",");
        for (Task task : epic.getSubtasks()) {
            Integer id = task.getId();
            subtasks.put(id, task);
            joiner.add(String.valueOf(id));
        }
        jsonWriter.value(joiner.toString());
        jsonWriter.name("status");
        jsonWriter.value(epic.getStatus().toString());
        jsonWriter.name("id");
        jsonWriter.value(epic.getId());
        jsonWriter.name("duration");
        jsonWriter.value(epic.getDuration().toMinutes());
        jsonWriter.name("startTime");
        jsonWriter.value(epic.getStartTime() == null ?
                "Not stated" : epic.getStartTime().format(DATE_TIME_FORMATTER)
        );
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        String title = "";
        String description = "";
        String[] subtasksIds = new String[]{};
        int id = 0;
        Status status = null;
        long duration = 0;
        LocalDateTime startTime = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "title":
                    title = jsonReader.nextString();
                    break;
                case "description":
                    description = jsonReader.nextString();
                    break;
                case "subtasksIds":
                    subtasksIds = jsonReader.nextString().split(",");
                    break;
                case "status":
                    status = Status.valueOf(jsonReader.nextString());
                    break;
                case "id":
                    id = jsonReader.nextInt();
                    break;
                case "duration":
                    duration = jsonReader.nextLong();
                    break;
                case "startTime":
                    if (!jsonReader.nextString().equals("Not stated")) {
                        startTime = LocalDateTime.parse(jsonReader.nextString(), DATE_TIME_FORMATTER);
                    }
                    break;
            }
        }
        jsonReader.endObject();

        Epic epic = new Epic(title, description, id);
        epic.setStatus(status);
        epic.setDuration(duration);
        if (startTime != null) {
            epic.setStartTime(startTime);
        }
        if (subtasksIds.length >= 1 && !subtasksIds[0].isEmpty()) {
            for (String stringId : subtasksIds) {
                int subtaskId = Integer.parseInt(stringId);
                epic.setSubtasks(subtasks.get(subtaskId));
            }
        }
        return epic;
    }
}
