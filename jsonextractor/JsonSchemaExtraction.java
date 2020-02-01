package jsonextractor;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Anil Chowdhury
 *         Created on 11/13/2018.
 */
public class JsonSchemaExtraction {

    public static void main(String[] args) {
        JsonSchemaExtraction driver = new JsonSchemaExtraction();
//        driver.flattenElements("C:\\files\\json\\flattening\\books.json");
        driver.flattenElements("C:\\files\\json\\flattening\\medication.json");
    }

    private void flattenElements(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            processJsonString(content);
//          System.out.println(content);
//          usingwnamelessFlattener(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processJsonString(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        processArrayNode(mapper.readTree(jsonString));
    }

    private void processArrayNode(JsonNode node) {
        for (JsonNode jsonNode : node) {
            processObjectNode(jsonNode);
        }
    }

    private void processObjectNode(JsonNode jsonNode) {
        Map<String, String> result = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.getFields();
        iterator.forEachRemaining(node -> mapAppender(result, node, new ArrayList<>()));
        System.out.println(result);
    }

    private void mapAppender(Map<String, String> result, Map.Entry<String, JsonNode> node, List<String> names) {
        names.add(node.getKey());
        if (node.getValue().isTextual()) {
            String name = names.stream().collect(Collectors.joining("."));
            result.put(name, node.getValue().asText());
        } else if (node.getValue().isArray()) {
            processArrayNode((ArrayNode) node.getValue());
        } else if (node.getValue().isNull()) {
            String name = names.stream().collect(Collectors.joining("."));
            result.put(name, null);
        } else {
            node.getValue().getFields()
                    .forEachRemaining(nested -> mapAppender(result, nested, new ArrayList<>(names)));
        }
    }

    /*private void usingwnamelessFlattener(String jsonString) {
        JSONObject jsonObject = new JSONObject();
        String flattenedJson = JsonFlattener.flatten(jsonString);
        Map<String, Object> flattenedJsonMap = JsonFlattener.flattenAsMap(jsonString);
        System.out.println(flattenedJsonMap);
    }*/
}
