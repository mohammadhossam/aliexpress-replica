package com.msa.mongo;

import com.google.gson.Gson;
import com.mongodb.client.*;
import com.msa.properties.Property;
import com.msa.properties.ServiceProperties;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;


public class MongoDB {


    private MongoDatabase db;

    public MongoDB()  {
        String connectionString = "mongodb+srv://manga:manga@cluster1.rhopy9t.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        db =  mongoClient.getDatabase("Service-Configs");
    }

    public void insert(ServiceProperties properties) {
        Gson gson = new Gson();
        String json = gson.toJson(properties);
        Document doc = Document.parse(json);
        db.getCollection("configs").insertOne(doc);
    }

    public ArrayList<ServiceProperties> getAllConfigs() {
        MongoCollection<Document> collection = db.getCollection("configs");
        FindIterable<Document> iterDoc = collection.find();
        ArrayList<ServiceProperties> configs = new ArrayList<>();
        Gson gson = new Gson();
        for (Document document : iterDoc) {
            configs.add(gson.fromJson(document.toJson(), ServiceProperties.class));
        }
        System.out.println(configs);
        return configs;
    }

    public static void main(String[] args) {
        new MongoDB();
    }

}
