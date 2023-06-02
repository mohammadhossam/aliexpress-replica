package com.msa.configManager.manager;

import com.msa.configManager.mongo.MongoDB;
import com.msa.configManager.properties.ApplicationPropertiesParser;
import com.msa.configManager.properties.Property;
import com.msa.configManager.properties.ServiceProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class ConfigurationManager {

    private ApplicationPropertiesParser applicationPropertiesParser;
    private final File[] propertiesFiles;
    private MongoDB mongoDB;
    public ConfigurationManager(String dir) {
        File f = new File("./");
        File folder = new File(dir);
        propertiesFiles = folder.listFiles();
        mongoDB = new MongoDB();
    }

    public String getFileContent(File file) throws IOException {
        // open the file
        FileReader fileReader = new FileReader(file);
        BufferedReader bf = new BufferedReader(fileReader);

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bf.readLine() )!= null) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void addConfigsToDB() throws ParseException {
        applicationPropertiesParser = new ApplicationPropertiesParser();
        for (File file : propertiesFiles) {
            try {
                String fileContent = getFileContent(file);
                applicationPropertiesParser.setProperties(fileContent);
                ArrayList<Property> properties = applicationPropertiesParser.parse();
                mongoDB.insert(new ServiceProperties(file.getName(), properties));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addSingleConfig(String serviceName, String configs) throws ParseException {
        applicationPropertiesParser = new ApplicationPropertiesParser();
        applicationPropertiesParser.setProperties(configs);
        ArrayList<Property> properties = applicationPropertiesParser.parse();
        mongoDB.insert(new ServiceProperties(serviceName, properties));
    }

    public ArrayList<ServiceProperties> getProperties() {
        return mongoDB.getAllConfigs();
    }

    public static void main(String[] args) throws ParseException {
        MongoDB mongoDB = new MongoDB();
        mongoDB.getAllConfigs();
    }

}