package util;

import java.time.LocalDate;
import java.util.*;

import com.example.messmanagementsystem.HomePageController;
import com.example.messmanagementsystem.User;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.set;
//import static jdk.internal.org.jline.utils.AttributedStringBuilder.append;


public class DatabaseController {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private static MongoCollection<Document> collection;

    public void database(String collectionName) {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.database = mongoClient.getDatabase("messManagementSystem");
        this.collection = database.getCollection(collectionName);
    }



    public void database(){
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
    }

    // insert document in database collection
    public void insertUSer(Document document) {
        this.collection.insertOne(document);
    }

    // find all documents
    public List<Document> findAllUser(String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        if (messName != null && !messName.isEmpty()) {
            return collection.find(Filters.eq("messName", messName)).into(new ArrayList<>());
        } else {
            return null;
        }
    }


    // getting user's email
    public boolean getEmail(String email) {
        database("users");
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return true;
        }
        return false;
    }

    // getting user's name is exist or not
    public boolean getFullName(String name) {
        database("users");
        BasicDBObject query = new BasicDBObject();
        query.put("displayName", name);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return true;
        }
        return false;
    }

    // getting user's mess name is exist or not for manager or admin
    public boolean getMessNameForManager(String messName) {
        database("users");
        BasicDBObject query = new BasicDBObject();
        query.put("messName", messName);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return true;
        }
        return false;
    }

    // getting user's mess name is exist or not for user
    public boolean getMessNameForUser(String messName) {
        database("users");
        BasicDBObject query = new BasicDBObject();
        query.put("messName", messName);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return false;
        }
        return true;
    }

    // getting user's nid is exists or not
    public boolean getNidNo(String nid) {
        database("users");
        BasicDBObject query = new BasicDBObject();
        query.put("nidNo", nid);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return true;
        }
        return false;
    }

    // getting user's nid no
    public String getNidNoThroughEmail(String email) {
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        Document user = collection.find(query).first();
        if (user != null) {
            return user.getString("nidNo");
        }
        return null;
    }

    // getting user's phone no is exists or not
    public boolean getphoneNo(String phoneNum) {
        database("users");
        BasicDBObject query = new BasicDBObject();
        query.put("phoneNo", phoneNum);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return true;
        }
        return false;
    }

    // getting user's password
    public String getPassword(String email) {
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return user.getString("password");
        }
        return null;
    }

    // getting email of all users
    public List<String> getUsersEmail(String messName) {
        List<String> names = new ArrayList<>();
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");

        for (Document doc : collection.find(eq("messName", messName))) {
            names.add(doc.getString("email"));
        }
        return names;
    }
    // getting name of all users
    public List<String> getUsersName(String messName) {
        List<String> names = new ArrayList<>();
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");

        for (Document doc : collection.find(eq("messName", messName))) {
            names.add(doc.getString("displayName"));
        }
        return names;
    }


    // get display name through email
    public String getUserName(String email) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("messManagementSystem");
        MongoCollection<Document> collection = database.getCollection("users");

        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return user.getString("displayName");
        }
        return null;
    }

    // get user's email through name
    public String getUserEmail(String name) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("messManagementSystem");
        MongoCollection<Document> collection = database.getCollection("users");

        BasicDBObject query = new BasicDBObject();
        query.put("displayName", name);
        Document user = collection.find(query).first();
        if (user != null) {
            return user.getString("email");
        }
        return null;
    }

    // get mess name through email
    public String getMessName(String email) {
        database("users");
//        MongoCollection<Document> collection = database.getCollection("users");

        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return user.getString("messName");
        }
        return null;
    }

    // get user phone number through email
    public String getUserPhoneNo(String email) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        Document user = DatabaseController.collection.find(query).first();
        if (user != null) {
            return user.getString("phoneNo");
        }
        return null;
    }

    // get meals number by email for current month and year
    public int getMealsByEmail(String email,String messName) {
        BasicDBObject query = new BasicDBObject();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        query.put("email", email);
        query.put("date.month", month);
        query.put("date.year", year);
        query.put("messName", messName);
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("meals");
        List<Document> meals = collection.find(query).into(new ArrayList<>());
        int sum = 0;
        for (Document meal : meals) {
            sum += meal.getInteger("mealNumber");
        }
        return sum;
    }

    // update user's password
    public void updateUsersPassword(String email, String newPassword){
        BasicDBObject newPasswordData = new BasicDBObject();
        newPasswordData.append("$set", new BasicDBObject().append("password", newPassword));

        BasicDBObject searchQuery = new BasicDBObject().append("email", email);

        collection.updateOne(searchQuery, newPasswordData);
    }

    // get total meals number for current month and year
//    public int getMealsByEmail() {
//        String currentUser = getCurrentUserEmail();
//        String messName = getMessName(currentUser);
//
//        BasicDBObject query = new BasicDBObject();
//        Calendar cal = Calendar.getInstance();
//        int month = cal.get(Calendar.MONTH) + 1;
//        int year = cal.get(Calendar.YEAR);
//        query.put("date.month", month);
//        query.put("date.year", year);
//        query.put("messName",messName);
//        collection = mongoClient.getDatabase("messManagementSystem").getCollection("meals");
//        List<Document> meals = collection.find(query).into(new ArrayList<>());
//        int sum = 0;
//        for (Document meal : meals) {
//            sum += meal.getInteger("mealNumber");
//        }
//        return sum;
//    }

    // updated version
    public int getMealsByEmail() {
        String currentUser = getCurrentUserEmail();
        String messName = getMessName(currentUser);

        // Query to find users with a matching mess name
        BasicDBObject userQuery = new BasicDBObject();
        //userQuery.put("messName", messName);
        Collection<Document> users = mongoClient.getDatabase("messManagementSystem").getCollection("users").find(userQuery).into(new ArrayList<>());

        // Query to find meals for the current month and year
        BasicDBObject mealQuery = new BasicDBObject();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        mealQuery.put("date.month", month);
        mealQuery.put("date.year", year);
        mealQuery.put("messName", messName);

        collection = mongoClient.getDatabase("messManagementSystem").getCollection("meals");

        int totalMeals = 0;

        for (Document user : users) {
            // Add the email of the current user to the meal query
            mealQuery.put("email", user.getString("email"));

            // Find all meals for the current user and current month and year
            List<Document> meals = collection.find(mealQuery).into(new ArrayList<>());

            int sum = 0;
            for (Document meal : meals) {
                sum += meal.getInteger("mealNumber");
            }

            totalMeals += sum;
        }

        return totalMeals;
    }


    // add meal info for current month and year to database
    public void addMeal(String email, int mealNumber, LocalDate date,String messName) {
//        LocalDate mealDate = date;
//        Document meal = new Document()
//                .append("email", email)
//                .append("mealNumber", mealNumber)
//                .append("messName", messName)
//                .append("date", new Document("year", mealDate.getYear()).append("month", mealDate.getMonthValue()).append("day",mealDate.getDayOfYear()));
//        collection = mongoClient.getDatabase("messManagementSystem").getCollection("meals");
//        collection.insertOne(meal);

        LocalDate mealDate = date;
        Bson filter = Filters.and(
                Filters.eq("email", email),
                Filters.eq("messName", messName),
                Filters.eq("date.year", mealDate.getYear()),
                Filters.eq("date.month", mealDate.getMonthValue()),
                Filters.eq("date.day", mealDate.getDayOfYear())
        );
        Bson update = new Document("$set", new Document("email", email)
                .append("mealNumber", mealNumber)
                .append("messName", messName)
                .append("date", new Document("year", mealDate.getYear())
                        .append("month", mealDate.getMonthValue())
                        .append("day", mealDate.getDayOfYear())));
        UpdateOptions options = new UpdateOptions().upsert(true);
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("meals");
        collection.updateOne(filter, update, options);
    }

    // find meal number based on date, email and mess name
    public int findMealNumber(String email, String messName, LocalDate date) {
        LocalDate mealDate = date;
        Document query = new Document("email", email)
                .append("messName", messName)
                .append("date", new Document("year", mealDate.getYear())
                        .append("month", mealDate.getMonthValue())
                        .append("day", mealDate.getDayOfYear()));

        collection = mongoClient.getDatabase("messManagementSystem").getCollection("meals");
        FindIterable<Document> results = collection.find(query);

        if (results.iterator().hasNext()) {
            Document meal = results.first();
            return meal.getInteger("mealNumber");
        } else {
            return 0;
        }
    }




    // add Meal market Cost to database for current month and year
    public void addMealMarketCostByCurrentMonth(String marketerName, double cost,String messName) {
        System.out.println("mess Name from add meal market cost " + messName);
        LocalDate now = LocalDate.now();
        Document mealMarketCost = new Document()
                .append("marketerName", marketerName)
                .append("messName",messName)
                .append("cost", cost)
                .append("date", new Document("year", now.getYear()).append("month", now.getMonthValue()));
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("mealMarketCost");
        collection.insertOne(mealMarketCost);
    }

    // get total meals cost for current month and year
//    public double getTotalMealMarketCostByCurrentMonth() {
//        LocalDate now = LocalDate.now();
//        int month = now.getMonthValue();
//        int year = now.getYear();
//
//        String currentUser = getCurrentUserEmail();
//        String messName = getMessName(currentUser);
//        System.out.println(messName);
//
//        MongoCollection<Document> usersCollection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
//        BasicDBObject query = new BasicDBObject();
//        query.put("messName", messName);
//        List<Document> users = usersCollection.find(query).into(new ArrayList<>());
//
//        List<String> emails = new ArrayList<>();
//        for (Document user : users) {
//            emails.add(user.getString("email"));
//        }
//
//        collection = mongoClient.getDatabase("messManagementSystem").getCollection("mealMarketCost");
//        List<Document> pipeline = Arrays.asList(
//                new Document("$match",
//                        new Document("$and",
//                                Arrays.asList(
//                                        new Document("date.month", month),
//                                        new Document("date.year", year),
//                                        new Document("email", new Document("$in", emails))
//                                )
//                        )
//                ),
//                new Document("$group",
//                        new Document("_id", "null")
//                                .append("totalCost", new Document("$sum", "$cost"))
//                )
//        );
//        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
//        if(cursor.hasNext()){
//            Document result = cursor.next();
//            return result.getDouble("totalCost");
//        }else{
//            return 0;
//        }
//    }






    public double getTotalMealMarketCostByCurrentMonth() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        String user=getCurrentUserEmail();
        String messName=getMessName(user);

        collection = mongoClient.getDatabase("messManagementSystem").getCollection("mealMarketCost");
        List<Document> pipeline = Arrays.asList(
                new Document("$match",
                        new Document("$and",
                                Arrays.asList(
                                        new Document("date.month", month),
                                        new Document("date.year", year),
                                        new Document("messName", messName)
                                )
                        )
                ),
                new Document("$group",
                        new Document("_id", "null")
                                .append("totalCost", new Document("$sum", "$cost"))
                )
        );
        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
        if(cursor.hasNext()){
            Document result = cursor.next();
            return result.getDouble("totalCost");
        }else{
            return 0;
        }
    }

    // deposit money for meal market cost by email address for current date
    public void addDepositMoneyForMealMarket(String email, double amount,String messName) {
        LocalDate date = LocalDate.now();
        Document deposit = new Document()
                .append("email", email)
                .append("messName",messName)
                .append("amount", amount)
                .append("date", new Document("year", date.getYear()).append("month", date.getMonthValue()).append("day",date.getDayOfMonth()));
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("depositMoneyForMealMarket");
        collection.insertOne(deposit);
    }

    // get deposit money for specific user by email for current month and year
    public double getTotalDepositByEmail(String email,String messName) {
        BasicDBObject query = new BasicDBObject();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        query.put("email", email);
        query.put("date.month", month);
        query.put("date.year", year);
        query.put("messName", messName);
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("depositMoneyForMealMarket");
        List<Document> deposits = collection.find(query).into(new ArrayList<>());
        double sum = 0;
        for (Document deposit : deposits) {
            sum += deposit.getDouble("amount");
        }
        return sum;
    }

    // get deposit money for all users for current month and year
    public double getTotalDepositByEmail(String messName) {
        BasicDBObject query = new BasicDBObject();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        query.put("date.month", month);
        query.put("date.year", year);
        query.put("messName",messName);
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("depositMoneyForMealMarket");
        List<Document> deposits = collection.find(query).into(new ArrayList<>());
        double sum = 0;
        for (Document deposit : deposits) {
            sum += deposit.getDouble("amount");
        }
        return sum;
    }



    // add house rent for current month and year
//    public void insertHouseRent(double houseRent) {
//        LocalDate now = LocalDate.now();
//        String month = now.getMonth().toString();
//        String year = Integer.toString(now.getYear());
//        collection = mongoClient.getDatabase("messManagementSystem").getCollection("houseRent");
//        Document houseRentDoc = new Document()
//                .append("month", month)
//                .append("year", year)
//                .append("houseRent", houseRent);
//        collection.insertOne(houseRentDoc);
//    }
    // set house rent for current month
    public void upsertHouseRent(String currentUserMessName, double newHouseRent) {
        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString();
        String year = Integer.toString(now.getYear());
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("houseRent");
        Bson filter = Filters.and(Filters.eq("month", month), Filters.eq("year", year), Filters.eq("messName", currentUserMessName));
        Bson update = Updates.set("houseRent", newHouseRent);
        collection.updateOne(filter, update, new UpdateOptions().upsert(true));
    }


    // get house rent for current month and year
    public double getHouseRentForCurrentMonth(String messName) {
        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString();
        String year = Integer.toString(now.getYear());
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("houseRent");
        Document query = new Document("month", month)
                .append("year", year)
                .append("messName", messName);
        Document houseRentDoc = collection.find(query).first();
        if (houseRentDoc != null) {
            return houseRentDoc.getDouble("houseRent");
        }
        return 0.0;
    }


    // set current meal marketer
    public void upsertCurrentMealMarketer(String email,String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("currentMealMarketer");

        collection.deleteOne(new Document());
        Document currentMarketer = new Document("email", email)
                .append("messName", messName);
        collection.insertOne(currentMarketer);
    }

    // get current meal marketer
    public String getCurrentMealMarketer(String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("currentMealMarketer");
        Document query = new Document("messName", messName);
        Document currentUser = collection.find(query).first();

        if(currentUser!=null){
            return currentUser.getString("email");
        }
        return null;
    }

    // set next meal marketer
    public void upsertNextMealMarketer(String email,String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("nextMealMarketer");

        collection.deleteOne(new Document());
        Document nextMarketer = new Document("email", email)
                .append("messName", messName);
        collection.insertOne(nextMarketer);
    }
    // get current meal marketer
    public String getNextMealMarketer(String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("nextMealMarketer");
        Document query = new Document("messName", messName);
        Document currentUser = collection.find(query).first();
        if(currentUser!=null){
            return currentUser.getString("email");
        }
        return null;
    }

    // add new utility bill for current month and year in the collection
    public void addUtilitiesMarketCostByCurrentDate(String utilityName, double cost,String messName){
        LocalDate now = LocalDate.now();
        Document mealMarketCost = new Document()
                .append("utilityName", utilityName)
                .append("cost", cost)
                .append("messName",messName)
                .append("date", new Document("year", now.getYear()).append("month", now.getMonthValue()));
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("utilities");
        collection.insertOne(mealMarketCost);
    }

    // get total utility bill for current month and year
    public double getTotalUtilityCostByCurrentMonth(String messName) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("utilities");
        List<Document> pipeline = Arrays.asList(
                new Document("$match",
                        new Document("$and",
                                Arrays.asList(
                                        new Document("date.month", month),
                                        new Document("date.year", year),
                                        new Document("messName", messName)
                                )
                        )
                ),
                new Document("$group",
                        new Document("_id", "null")
                                .append("totalCost", new Document("$sum", "$cost"))
                )
        );
        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
        if(cursor.hasNext()){
            Document result = cursor.next();
            return result.getDouble("totalCost");
        }else{
            return 0;
        }
    }

    // get all utilities name
    public List<String> getUtilityNamesByCurrentMonth(String messName) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        MongoCollection<Document> collection = mongoClient.getDatabase("messManagementSystem").getCollection("utilities");
        List<Document> pipeline = Arrays.asList(
                new Document("$match",
                        new Document("$and",
                                Arrays.asList(
                                        new Document("date.month", month),
                                        new Document("date.year", year),
                                        new Document("messName", messName)
                                )
                        )
                ),
                new Document("$group",
                        new Document("_id", "$utilityName")
                                .append("count", new Document("$sum", 1))
                )
        );
        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
        List<String> utilityNames = new ArrayList<>();
        while (cursor.hasNext()) {
            Document result = cursor.next();
            String utilityName = result.getString("_id");
            utilityNames.add(utilityName);
        }
        return utilityNames;
    }

    // get utility price by name
    public double getUtilityPriceByCurrentMonthAndMessName(String utilityName, String messName) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        MongoCollection<Document> collection = mongoClient.getDatabase("messManagementSystem").getCollection("utilities");
        Document query = new Document("$and", Arrays.asList(
                new Document("date.month", month),
                new Document("date.year", year),
                new Document("messName", messName),
                new Document("utilityName", utilityName)
        ));
        Document result = collection.find(query).first();
        if (result != null) {
            return result.getDouble("cost");
        } else {
            return 0.0;
        }
    }

    // update utility cost
    public void updateUtilityPriceByCurrentMonthAndMessName(String utilityName, String messName, double newPrice) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        MongoCollection<Document> collection = mongoClient.getDatabase("messManagementSystem").getCollection("utilities");
        BasicDBObject query = new BasicDBObject("date.month", month)
                .append("date.year", year)
                .append("messName", messName)
                .append("utilityName", utilityName);
        BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("cost", newPrice));
        collection.updateOne(query, update);
    }

    // delete utility item
    public void deleteUtility(String utilityName, String messName) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        MongoCollection<Document> collection = mongoClient.getDatabase("messManagementSystem").getCollection("utilities");
        BasicDBObject query = new BasicDBObject("utilityName", utilityName)
                .append("date.month", month)
                .append("date.year", year)
                .append("messName", messName);
        collection.deleteOne(query);
    }







    // is user, admin or not
    public boolean isAdmin(String email) {
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        Document user = collection.find(query).first();
        if (user != null && user.containsKey("role") && user.getString("role").equals("admin")) {
            return true;
        }
        return false;
    }

    // get current user's email


    public void insertCurrentUser(String email) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("currentUser");
        Document newUser = new Document("email", email);
        collection.insertOne(newUser);
    }

    public void deleteCurrentUser() {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("currentUser");
        collection.deleteOne(new Document());
    }

    public String getCurrentUserEmail() {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("currentUser");
        Document currentUser = collection.find().first();
        return currentUser.getString("email");
    }

    // set an admin
    public void addAdmin(String email){
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        Document update = new Document("$set", new Document("role", "admin"));
        collection.updateOne(new Document("email", email), update);
    }

    // remove an admin
    public void removeAdmin(String email){
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        Document update = new Document("$set", new Document("role", "admin"));
        collection.updateOne(new Document("email", email), new Document("$unset", new Document("role", "")));
    }

    // get admin's email lists
    public List<String> getAdminsEmail(String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        BasicDBObject query = new BasicDBObject();
        query.put("role", "admin");
        query.put("messName",messName);
        List<String> names = new ArrayList<>();
        for (Document doc : collection.find(query)) {
            names.add(doc.getString("email"));
        }
        return names;
    }
    // get regular users email lists
    public List<String> getRegularUsers(String messName) {
        collection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        Document query = new Document("role", new Document("$exists", false));
        query.put("messName",messName);
        List<String> names = new ArrayList<>();
        for (Document doc : collection.find(query)) {
            names.add(doc.getString("email"));
        }
        return names;
    }

    // delete inactive members from "users" collection and insert them into "inactiveMemberes" collection
    public void deleteUserAndMoveToInactive(String email, String displayName, String messName, LocalDate localDate) {
        MongoCollection<Document> usersCollection = mongoClient.getDatabase("messManagementSystem").getCollection("users");
        MongoCollection<Document> inactiveMembersCollection = mongoClient.getDatabase("messManagementSystem").getCollection("inactiveMembers");

        // Find the document to delete in the users collection
        Document userDocument = usersCollection.findOneAndDelete(eq("email", email));

        // Create a new document with the required fields
        Document inactiveMemberDocument = new Document()
                .append("displayName", displayName)
                .append("email", email)
                .append("messName", messName)
                .append("localDate", localDate);

        // Insert the new inactive member document into the inactiveMembers collection
        if (userDocument != null) {
            inactiveMembersCollection.insertOne(inactiveMemberDocument);
        }
    }



    // close the database after operation is done
    public void close() {
        mongoClient.close();
    }
}