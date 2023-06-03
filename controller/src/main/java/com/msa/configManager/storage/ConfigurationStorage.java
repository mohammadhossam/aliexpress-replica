//package com.msa.configManager.storage;
//
//import com.google.gson.Gson;
//import com.mongodb.client.*;
//import com.msa.configManager.properties.ServiceProperties;
//import org.bson.Document;
//
//import java.util.ArrayList;
//
//
//public class ConfigurationStorage {
//
//    private MongoDatabase db;
//
//    public ConfigurationStorage()  {
//        String connectionString = "";
//        MongoClient mongoClient = MongoClients.create(connectionString);
//        db =  mongoClient.getDatabase("Service-Configs");
//    }
//
//    public void insert(ServiceProperties properties) {
//        Gson gson = new Gson();
//        String json = gson.toJson(properties);
//        Document doc = Document.parse(json);
//        db.getCollection("configs").insertOne(doc);
//    }
//
//    public ArrayList<ServiceProperties> getAllConfigs() {
//        MongoCollection<Document> collection = db.getCollection("configs");
//        FindIterable<Document> iterDoc = collection.find();
//        ArrayList<ServiceProperties> configs = new ArrayList<>();
//        Gson gson = new Gson();
//        for (Document document : iterDoc) {
//            configs.add(gson.fromJson(document.toJson(), ServiceProperties.class));
//        }
//        return configs;
//    }
//}
