import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnection {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnection() {
        // Connect to MongoDB
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("HealthcareDB"); // Create/use HealthcareDB
    }

    public MongoCollection<Document> getPatientCollection() {
        return database.getCollection("patients"); // Create/use patients collection
    }

    public MongoCollection<Document> getAppointmentCollection() {
        return database.getCollection("appointments"); // Create/use appointments collection
    }

    public MongoCollection<Document> getLocationCollection(String location) {
        return database.getCollection(location); // Create/use the location-specific collection
    }

    public void close() {
        mongoClient.close();
    }
}
