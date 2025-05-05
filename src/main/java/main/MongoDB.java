package main;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class MongoDB {

  public static MongoCollection<Document> collection;
  public static MongoClient client;

  public static void initializeClient() {
    Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
    String uri = "mongodb://localhost:27017";

    try {
      MongoClient mongoClient = MongoClients.create(uri);
      client = mongoClient;
      collection = client.getDatabase("DB").getCollection("COLL");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
