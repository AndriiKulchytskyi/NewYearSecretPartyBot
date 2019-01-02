import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Sorts.descending;

public class MongoConnector {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collectionGroups;
    private MongoCollection<Document> collectionUsers;

    public MongoConnector() {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("NewYearParty");
        collectionGroups = database.getCollection("Groups");
        collectionUsers = database.getCollection("Users");
    }

    private String getCurrentId() {
        Document myDoc = (Document) collectionGroups.find().sort(descending("_id")).first();
        try {
            return myDoc.get("_id").toString();
        } catch (Exception e) {
            return "0";
        }
    }


    public void readAll() {
        collectionGroups.find().forEach(printBlock);
    }

    public ArrayList<String> returnAllIds() {
        ArrayList<String> ids = new ArrayList<String>();
        collectionGroups.find().forEach((Consumer<? super Document>) x -> ids.add(x.get("exId").toString()));
        return ids;
    }

    public ArrayList<String> returnAllNames() {
        ArrayList<String> names = new ArrayList<String>();
        collectionGroups.find().forEach((Consumer<? super Document>) x -> names.add(x.get("name").toString()));
        return names;
    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    public ArrayList<String> returnNamesInGroup(int groupNumber) {
        Bson filter = Filters.eq("number", groupNumber);
        ArrayList<String> names = new ArrayList<String>();
        collectionGroups.find(filter).forEach((Consumer<? super Document>) x -> names.add(x.get("name").toString()));
        return names;
    }

    public int returnGroupSize(int groupNumber) {
        Bson filter = Filters.eq("number", groupNumber);
        ArrayList<String> groups = new ArrayList<String>();
        collectionGroups.find(filter).forEach((Consumer<? super Document>) x -> groups.add(x.get("number").toString()));
        return groups.size();
    }

    public int returnGroupNumberByName(String name) {
        Bson filter = Filters.eq("name", name);
        ArrayList<String> groups = new ArrayList<String>();
        return (int) collectionGroups.find(filter).first().get("number");

    }


    public boolean saveUser(User from) {
        if (!isAlreadyPresent(collectionUsers, from)) {
            Document document = new Document();
            document.append("_id", Integer.parseInt(getCurrentId()) + 1);
            document.append("exId", from.getId());
            document.append("firstLastName", from.getFirstName() + " " + from.getLastName());
            document.append("date", LocalDateTime.now().toString());
            collectionUsers.insertOne(document);
            return checkInserted(collectionUsers, document);
        } else
            return false;
    }

    private boolean isAlreadyPresent(MongoCollection<Document> collection, User from) {
        if (collection.find(Filters.eq("exId", from.getId())).first() != null) {
            System.out.println("Record already presented in db.");
            return true;
        }
        else
            return false;

    }

    private boolean checkInserted(MongoCollection<Document> collection, Document document) {
        if (collection.find().sort(descending("_id")).first().equals(document))
            return true;
        else
            return false;
    }


    public boolean saveMessage(Integer id, String who, int number) {
        Document document = new Document();
        document.append("_id", Integer.parseInt(getCurrentId()) + 1);
        document.append("exId", id);
        document.append("name", who);
        document.append("number", number);
        document.append("date", LocalDateTime.now().toString());
        collectionGroups.insertOne(document);
        return checkInserted(collectionGroups, document);
    }
}
