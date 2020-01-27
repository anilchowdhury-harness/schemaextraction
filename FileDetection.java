import org.apache.tika.Tika;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Anil Chowdhury
 *         Created on 11/28/2018.
 */
public class FileDetection {
    private final Tika tika = new Tika();

    public static void main(String[] args) {
        FileDetection driver = new FileDetection();

        /*System.out.println(driver.probeContent("C:\\files\\avro\\sony.avro"));
        System.out.println(driver.probeContent("C:\\files\\parquet\\Input1.parq"));
        System.out.println(driver.probeContent("C:\\files\\csv\\customer.csv"));
        System.out.println(driver.probeContent("C:\\files\\json\\emp.json"));
        System.out.println(driver.probeContent("C:\\files\\xml\\employee.xml"));
        System.out.println(driver.probeContent("C:\\files\\pdf\\Final_Bank Account Change Letter_MASKED.pdf"));
        System.out.println(driver.probeContent("C:\\files\\docx\\Top_10_Health_Voilations_City_of_SanFrancisco.docx"));
        System.out.println(driver.probeContent("C:\\files\\ppt\\presen-eng.ppt"));*/


       /* System.out.println(driver.urlContent("C:\\files\\avro\\sony.avro"));
        System.out.println(driver.urlContent("C:\\files\\parquet\\Input1.parq"));
        System.out.println(driver.urlContent("C:\\files\\csv\\customer.csv"));
        System.out.println(driver.urlContent("C:\\files\\json\\emp.json"));
        System.out.println(driver.urlContent("C:\\files\\xml\\employee.xml"));
        System.out.println(driver.urlContent("C:\\files\\pdf\\Final_Bank Account Change Letter_MASKED.pdf"));
        System.out.println(driver.urlContent("C:\\files\\docx\\Top_10_Health_Voilations_City_of_SanFrancisco.docx"));
        System.out.println(driver.urlContent("C:\\files\\ppt\\presen-eng.ppt"));*/

        /*System.out.println(driver.fileMap("C:\\files\\avro\\sony.avro"));
        System.out.println(driver.fileMap("C:\\files\\parquet\\Input1.parq"));
        System.out.println(driver.fileMap("C:\\files\\csv\\customer.csv"));
        System.out.println(driver.fileMap("C:\\files\\json\\emp.json"));
        System.out.println(driver.fileMap("C:\\files\\xml\\employee.xml"));
        System.out.println(driver.fileMap("C:\\files\\pdf\\Final_Bank Account Change Letter_MASKED.pdf"));
        System.out.println(driver.fileMap("C:\\files\\docx\\Top_10_Health_Voilations_City_of_SanFrancisco.docx"));
        System.out.println(driver.fileMap("C:\\files\\ppt\\presen-eng.ppt"));*/

        System.out.println(driver.usingTika("C:\\files\\avro\\sony.avro"));
        System.out.println(driver.usingTika("C:\\files\\parquet\\Input1.parq"));
        System.out.println(driver.usingTika("C:\\files\\csv\\customer.csv"));
        System.out.println(driver.usingTika("C:\\files\\json\\emp.json"));
        System.out.println(driver.usingTika("C:\\files\\xml\\employee.xml"));
        System.out.println(driver.usingTika("C:\\files\\pdf\\Final_Bank Account Change Letter_MASKED.pdf"));
        System.out.println(driver.usingTika("C:\\files\\docx\\Top_10_Health_Voilations_City_of_SanFrancisco.docx"));
        System.out.println(driver.usingTika("C:\\files\\ppt\\presen-eng.ppt"));
    }

    private String probeContent(String filepath) {
        String mimeType = "";
        try {
            mimeType = Files.probeContentType(Paths.get(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimeType;
    }

    private String urlContent(String filePath) {
        String mimeType = "";
        try {
            URL url = new URL(filePath);
            URLConnection conn = url.openConnection();
            mimeType = conn.getContentType();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimeType;
    }

    private String fileMap(String filePath) {
        String mimeType;
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        mimeType = fileNameMap.getContentTypeFor(filePath);
        return mimeType;
    }

    private String usingTika(String filePath) {
        String mimeType = "";
        try {
            mimeType = tika.detect(Paths.get(filePath).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimeType;
    }
}
