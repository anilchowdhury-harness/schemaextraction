package com.infa.products.ldm.scanners.hdfsscanner.config;

import org.apache.hadoop.conf.Configuration;

import java.util.Map;

/**
 * Created by Anil Chowdhury on 1/11/2018.
 *
 */
public class MapRFSManualConfigBuilder extends ManualConfigBuilder {

    public MapRFSManualConfigBuilder(Map<String, String> configMap) {
        super(configMap);
    }

    public Configuration buildConfiguration() {
        Configuration conf = super.buildConfiguration(false);
        conf.set("fs.AbstractFileSystem.maprfs.impl", "com.mapr.fs.MFS");
        conf.set("fs.defaultFS", nameNodeUris.get(0));
        conf.set("fs.maprfs.impl", "com.mapr.fs.MapRFileSystem");
        return conf;
    }

    public String getDistributionJarsLocation() {
        return System.getProperty("HdfsScanner_Mapr_SystemDriverLocation");
    }
}
