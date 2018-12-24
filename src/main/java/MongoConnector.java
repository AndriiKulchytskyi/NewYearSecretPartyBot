import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Sorts.descending;

public class MongoConnector {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoConnector() {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("NewYearParty");
        collection = database.getCollection("Groups");
    }

    public void saveMessage(Integer id, String who, int number) {
        Document document = new Document();
        document.append("_id", Integer.parseInt(getCurrentId()) + 1);
        document.append("exId", id);
        document.append("name", who);
        document.append("number", number);
        document.append("date", LocalDateTime.now().toString());
        collection.insertOne(document);
    }

    private String getCurrentId() {
        Document myDoc = (Document) collection.find().sort(descending("_id")).first();
        try {
            return myDoc.get("_id").toString();
        } catch (Exception e) {
            return "0";
        }
    }


    public void readAll() {
        collection.find().forEach(printBlock);
    }

    public ArrayList<String> returnAllIds() {
        ArrayList<String> ids = new ArrayList<String>();
        collection.find().forEach((Consumer<? super Document>) x -> ids.add(x.get("exId").toString()));
        return ids;
    }

    public ArrayList<String> returnAllNames() {
        ArrayList<String> names = new ArrayList<String>();
        collection.find().forEach((Consumer<? super Document>) x -> names.add(x.get("name").toString()));
        return names;
    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    public ArrayList<String> returnNamesInGroup(int groupNumber){
        Bson filter = Filters.eq("number", groupNumber);
        ArrayList<String> names=new ArrayList<String>();
        collection.find(filter).forEach((Consumer<? super Document>) x->names.add(x.get("name").toString()));
        return names;
    }

    public int returnGroupSize(int groupNumber) {
        Bson filter = Filters.eq("number", groupNumber);
        ArrayList<String> groups=new ArrayList<String>();
        collection.find(filter).forEach((Consumer<? super Document>) x->groups.add(x.get("number").toString()));
        return groups.size();
    }

    public int returnGroupNumberByName(String name) {
        Bson filter = Filters.eq("name", name);
        ArrayList<String> groups=new ArrayList<String>();
        return (int) collection.find(filter).first().get("number");

    }
}
