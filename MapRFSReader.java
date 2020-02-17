package com.infa.products.ldm.maprPOC;

import com.mapr.fs.MapRFileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Anil Chowdhury on 1/3/2018.
 *
 */
public class MapRFSReader {

    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "C:\\winutils");
//        readFromHDFS("hdfs://10.75.142.48:8020", "/Informatica/xml/Department_HDFS.csv");
//        readFromHDFS("maprfs:///192.168.56.101:7222", "/user/mapr/anil/Department_HDFS.csv");
        readFromMapRFS("maprfs:///192.168.56.101:7222", "/user/mapr/anil/Department_HDFS.csv");
//        printClassPath();
    }

    private static void printClassPath() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) cl).getURLs();
        for (URL url : urls) {
            System.out.println(url.getFile());
        }
    }

    private static void readFromHDFS(String clusterURI, String file) {
        FSDataInputStream inputStream = null;
        try {
            Configuration configuration = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(clusterURI), configuration);
            String absoluteFilePath = clusterURI + file;
            Path filePath = new Path(absoluteFilePath);
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

    private static void readFromMapRFS(String clusterURI, String file) {
        String MAPR_CLASS_IMPL = "com.mapr.fs.MapRFileSystem";
        try {
            Class.forName(MAPR_CLASS_IMPL);
            Configuration configuration = new Configuration();
            configuration.set("fs.default.name", clusterURI);
            FileSystem fs = FileSystem.get(configuration);
            System.out.println(fs.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
