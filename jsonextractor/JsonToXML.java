package jsonextractor;

import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Anil Chowdhury
 *         Created on 11/14/2018.
 *
 *         https://gist.github.com/zuch/6375829
 *         https://stackoverflow.com/questions/19977979/converting-json-to-xml-in-java
 */
public class JsonToXML {

    public static void main(String[] args) {
        JsonToXML driver = new JsonToXML();
        driver.convert("C:\\files\\json\\flattening\\emp1.json");
    }

    private void convert(String filePath) {
        try {
            JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(filePath))));
            String xml = XML.toString(json);
            System.out.println(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
