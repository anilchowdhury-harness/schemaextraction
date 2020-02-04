package jsonextractor;

import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * @author Anil Chowdhury
 *         Created on 11/14/2018.
 */
public class PrintJsonElements {

    public static void main(String[] args) {
        PrintJsonElements driver = new PrintJsonElements();
//      driver.flatten("C:\\files\\json\\flattening\\books.json");
//      driver.flatten("C:\\files\\json\\flattening\\emp1.json");
//      driver.flatten("C:\\files\\json\\flattening\\medication.json");
//      driver.flatten("C:\\files\\json\\flattening\\NestedObjects.json");
        driver.flatten("C:\\files\\json\\flattening\\batterSimple.json");
    }

    private void flatten(String filePath) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonParser parser = new JsonParser();
            JsonElement parse = parser.parse(jsonString);
            processJsonElement(parse);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void processJsonElement(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            processJsonArray(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonNull()) {
            processJsonNull(jsonElement.getAsJsonNull());
        } else if (jsonElement.isJsonObject()) {
            processJsonObject(jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonPrimitive()) {
            processJsonPrimitive(jsonElement.getAsJsonPrimitive());
        }
    }

    private void processJsonArray(JsonArray a) {
        for (JsonElement e : a) {
            processJsonElement(e);
        }
    }

    private void processJsonNull(JsonNull n) {
        System.out.println("null || : " + n);
    }

    private void processJsonObject(JsonObject o) {
        Set<Map.Entry<String, JsonElement>> members= o.entrySet();
        for (Map.Entry<String, JsonElement> e : members) {
            System.out.println("Processing object member: " + e.getKey());
            processJsonElement(e.getValue());
        }
    }

    private void processJsonPrimitive(JsonPrimitive p) {
        System.out.println("Primitive || :" + p);
    }
}
