//package com.msa.configManager.manager;
//
//import com.msa.configManager.storage.ConfigurationStorage;
//import com.msa.configManager.properties.ApplicationPropertiesParser;
//import com.msa.configManager.properties.Property;
//import com.msa.configManager.properties.ServiceProperties;
//import org.apache.commons.text.StringSubstitutor;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ConfigurationManager {
//
//    private ApplicationPropertiesParser applicationPropertiesParser;
//    private final File[] propertiesFiles;
//    private ConfigurationStorage configurationStorage;
//    public ConfigurationManager(String dir) {
//        File f = new File("./");
//        File folder = new File(dir);
//        propertiesFiles = folder.listFiles();
//        configurationStorage = new ConfigurationStorage();
//    }
//
//    public String getFileContent(File file) throws IOException {
//        // open the file
//        FileReader fileReader = new FileReader(file);
//        BufferedReader bf = new BufferedReader(fileReader);
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//
//        while ((line = bf.readLine() )!= null) {
//            sb.append(line);
//            sb.append("\n");
//        }
//        return sb.toString();
//    }
//
//    public void addConfigsToDB() throws ParseException {
//        applicationPropertiesParser = new ApplicationPropertiesParser();
//        for (File file : propertiesFiles) {
//            try {
//                String fileContent = getFileContent(file);
//                applicationPropertiesParser.setProperties(fileContent);
//                ArrayList<Property> properties = applicationPropertiesParser.parse();
//                configurationStorage.insert(new ServiceProperties(file.getName(), properties));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void addSingleConfig(String serviceName, String configs) throws ParseException {
//        applicationPropertiesParser = new ApplicationPropertiesParser();
//        applicationPropertiesParser.setProperties(configs);
//        ArrayList<Property> properties = applicationPropertiesParser.parse();
//        configurationStorage.insert(new ServiceProperties(serviceName, properties));
//    }
//
//    public ArrayList<ServiceProperties> getProperties() {
//        ArrayList<ServiceProperties> properties = configurationStorage.getAllConfigs();
//        ArrayList<ServiceProperties> newProps = new ArrayList<>();
//
//        for (ServiceProperties prop: properties) {
//            ArrayList<Property> newProperties = new ArrayList<>();
//            for (Property p: prop.getProperties()) {
//                String val = p.getVal();
//                Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
//                Matcher matcher = pattern.matcher(val);
//                ArrayList<String> placeHoldersToBeQueried = new ArrayList<>();
//                while (matcher.find()) {
//                    String match = matcher.group(1);
//                    placeHoldersToBeQueried.add(match);
//                }
//                HashMap<String, String> newValMap = getResourceVal(placeHoldersToBeQueried);
//                String newVal = StringSubstitutor.replace(p.getVal(), newValMap);
//                newProperties.add(new Property(p.getPropertyName(), newVal));
//            }
//            newProps.add(new ServiceProperties(prop.getServiceName(), newProperties));
//        }
//        return newProps;
//    }
//
//    public HashMap<String, String> getResourceVal(ArrayList<String> placeHoldersToBeQueried) {
//        return new HashMap<>();
//    }
//}
