import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedExceptionAction;

/**
 * Created by Anil Chowdhury on 1/8/2018.
 *
 */
public class MapRFSReader {

    public  static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "C:\\winutils");
//        readFromMapRFS("hdfs://10.75.142.48:8020", "/Informatica/xml/Department_HDFS.csv");
//        readFromMapRFS("maprfs://192.168.56.101:7222", "/user/mapr/anil/Department_HDFS.csv");
//        readFromMapRFS("maprfs://10.80.112.120:7222", "/user/mapr/anil/Department_HDFS.csv");
//        readFromMapRFS("maprfs://demo.mapr.com:7222", "/user/mapr/anil/Department_HDFS.csv");
//        readFromMapRFS("maprfs://192.168.56.101:7222", "/user/mapr/MapRFSReader.java");
//        readFromMapRFS("/user/mapr/MapRFSReader.java");
//        System.out.println(System.getProperty("mapr.home.dir"));
//        System.out.println(System.getenv("MAPR_HOME"));

//        readFromMapRFS("maprfs://10.65.147.35:7222", "/user/mapr/anil/boolean_table.csv");
//        readFromMapRFS("maprfs://10.65.147.35:7222", "/user/mapr/anil/date.txt");

        readFromMapRFS("maprfs://10.65.147.41:7222", "/user/mapr/anil/date.txt");
        readFromMapRFS("maprfs://10.65.147.41:7222", "/user/mapr/anil/date.txt");
    }

    private static void readFromMapRFS(String clusterURI, String file) {
        FSDataInputStream inputStream = null;
        try {
            final Configuration configuration = new Configuration();
//            configuration.set("fs.default.name", "maprfs:///");
//          configuration.set("fs.mapr.trace","debug");configuration.set("fs.maprfs.impl.disable.cache", "true");
//            FileSystem fs1 = FileSystem.get(configuration);
//            FileSystem fs = FileSystem.get(URI.create(clusterURI), configuration);

            UserGroupInformation mapr = UserGroupInformation.createRemoteUser("mapr");
            FileSystem fs = mapr.doAs(new PrivilegedExceptionAction<FileSystem>() {
                public FileSystem run() throws Exception {
                    return FileSystem.newInstance(configuration);
                }
            });

//            FileSystem fs = FileSystem.get(configuration);
            String absoluteFilePath = clusterURI + file;
            Path filePath = new Path(absoluteFilePath);
            inputStream = fs.open(filePath);
            System.out.println();
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
            inputStream.close();
            System.out.println();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inputStream);
        }
    }

    private static void readFromMapRFS(String file) {
        FSDataInputStream inputStream = null;
        try {
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(configuration);
            Path filePath = new Path(file);
            inputStream = fs.open(filePath);
            System.out.println();
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inputStream);
        }
    }
}
