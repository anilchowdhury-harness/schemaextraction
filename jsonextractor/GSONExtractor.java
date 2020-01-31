package jsonextractor;

import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Anil Chowdhury
 *         Created on 11/14/2018.
 */

public class GSONExtractor {

    public static void main(String[] args) {

        GSONExtractor driver = new GSONExtractor();
        /*
        driver.flatten("C:\\files\\json\\flattening\\8.json");
        driver.flatten("C:\\files\\json\\flattening\\batterSimple.json");
        driver.flatten("C:\\files\\json\\flattening\\books.json");
        driver.flatten("C:\\files\\json\\flattening\\emp.json");
        driver.flatten("C:\\files\\json\\flattening\\emp1.json");
        driver.flatten("C:\\files\\json\\flattening\\items.json");
        driver.flatten("C:\\files\\json\\flattening\\medication.json");
        driver.flatten("C:\\files\\json\\flattening\\menu.json");
        driver.flatten("C:\\files\\json\\flattening\\NestedObjects.json");
        driver.flatten("C:\\files\\json\\flattening\\sampleJsonPerson.json");
        driver.flatten("C:\\files\\json\\flattening\\swiggyPayments.json");
        driver.flatten("C:\\files\\json\\flattening\\swiggyAllOrders.json");
        */
        driver.flatten("C:\\files\\json\\flattening\\Original Credit Transaction_OCT.json");
        driver.flatten("C:\\files\\json\\flattening\\employeeAddress.json");
//      driver.flatten("C:\\Users\\anchowdh\\Desktop\\swiggyPayments.json");
//      driver.flatten("C:\\Users\\anchowdh\\Desktop\\swiggyAllOrders.json");
//      driver.flatten("C:\\files\\avro\\allTypesMix.json");
//      driver.flatten("C:\\files\\avro\\sony.json");
    }

    private void flatten(String filePath) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonParser parser = new JsonParser();
            JsonElement parse = parser.parse(jsonString);
            Set<String> stringList = processJsonElement(parse);
            System.out.println("--------------------------------" + new File(filePath).getName() + "--------------------------------");
            stringList.forEach(System.out :: println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Set<String> processJsonElement(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            return processJsonArray(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonObject()) {
            return processJsonObject(jsonElement.getAsJsonObject());
        }
        return Collections.emptySet();
    }

    private Set<String> processJsonArray(JsonArray a) {
        Set<String> children = new TreeSet<>();
        for (JsonElement e : a) {
            children.addAll(processJsonElement(e));
        }
        return children;
    }

    private Set<String> processJsonObject(JsonObject o) {
        Set<String> children = new TreeSet<>();
        Set<Map.Entry<String, JsonElement>> members= o.entrySet();
        for (Map.Entry<String, JsonElement> e : members) {
            String nodeName = e.getKey();
            JsonElement value = e.getValue();
            if(isLeafElement(value)) {
                children.add("/" + nodeName);
            } else {
                children.addAll(processJsonElement(value).stream().map(p -> "/" + nodeName + p).
                        collect(Collectors.toList()));
            }
        }
        return children;
    }

    private boolean isLeafElement(JsonElement value) {
        return value.isJsonPrimitive() ||
               value.isJsonNull() ||
               (value.isJsonArray() && (value.getAsJsonArray().size() == 0 ||
                       value.getAsJsonArray().get(0).isJsonPrimitive()));
    }
}
