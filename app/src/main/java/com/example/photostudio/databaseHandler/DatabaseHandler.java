//package com.example.photostudio.databaseHandler;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.example.photostudio.models.Client;
//import com.example.photostudio.models.Room;
//
//import java.util.ArrayList;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//    private static final String TAG = "DatabaseHandler";
//
//    private static final String DATABASE_NAME = "photostudio.db";
//    private static final int DATABASE_VERSION = 2;
//
//    private static final String BOOKINGS_TABLE = "bookings";
//    private static final String CLIENT_TABLE = "client";
//    private static final String ROOM_TABLE = "room";
//
//    public DatabaseHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE " + BOOKINGS_TABLE + " (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
//                "client_id INTEGER," +
//                "room_id INTEGER," +
//                "date DATE," +
//                "time TEXT)");
//
//        db.execSQL("CREATE TABLE " + CLIENT_TABLE + " (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
//                "name TEXT," +
//                "email TEXT," +
//                "phone TEXT," +
//                "password TEXT)");
//
//        db.execSQL("CREATE TABLE " + ROOM_TABLE + " (" +
//                "room_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
//
//                "name TEXT," +
//                "description TEXT," +
//                "price INTEGER)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + BOOKINGS_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + ROOM_TABLE);
//
//        onCreate(db);
//    }
//
//    public void write(String tableName, ContentValues values) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // Run an insert statement using the provided values
//        db.insert(tableName, null, values);
//
//        // Close db connection
//        db.close();
//    }
//
//    public ArrayList<Client> readAllClients() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Client> clients = new ArrayList<>();
//
//        Cursor cursor = db.query("client",
//                null, // columns - null will give all
//                null, // selection
//                null, // selection arguments
//                null, // groupBy
//                null, // having
//                null); // no need or order by for now;
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Client client = readClient(cursor);
//                clients.add(client);
//            }
//        }
//        return clients;
//    }
//
//    private Client readClient(Cursor cursor) {
//        Client client = new Client();
//
//        String clientID = cursor.getString(cursor.getColumnIndexOrThrow("id"));
//        client.setId(clientID);
//
//        String clientName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
//        client.setName(clientName);
//
//        String clientEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
//        client.setEmail(clientEmail);
//
//        String clientPhone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
//        client.setPhone(clientPhone);
//
//        String clientPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
//        client.setPassword(clientPassword);
//
//        return client;
//    }
//
//    private Room readRoom(Cursor cursor){
//        Room room = new Room();
//        int room_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("room_id")));
//        room.setRoom_id(String.valueOf(room_id));
//
//        String name=cursor.getString(cursor.getColumnIndexOrThrow("name"));
//        room.setName(name);
//
//        String description=cursor.getString(cursor.getColumnIndexOrThrow("description"));
//        room.setDescription(description);
//
//        Integer price=cursor.getInt(cursor.getColumnIndexOrThrow("price"));
//        room.setPrice(price);
//
//return room;
//    }
//}
//
