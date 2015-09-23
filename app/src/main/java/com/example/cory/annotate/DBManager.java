package com.example.cory.annotate;

import android.content.*;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cory on 9/21/15.
 */
public class DBManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContextDatabase";
    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_NETWORKS = "networks";
    private static final String TABLE_TAGS = "tags";

    // Note Table Columns
    private static final String NOTES_KEY_ID = "_id";
    private static final String NOTES_KEY_BODY = "body";
    private static final String NOTES_KEY_TITLE = "title";
    private static final String NOTES_KEY_NETwORK_ID = "network";
    private static final String NOTES_KEY_DATE_CREATED = "date_created";

    // Network Table Columns
    private static final String NETWORK_KEY_ID = "_id";
    private static final String NETWORK_KEY_SSID = "ssid";
    private static final String NETWORK_KEY_BSSID = "bssid";
    private static final String NETWORK_KEY_CUSTOM_SSID = "custom_ssid";

    // Tags Table Columns
    private static final String TAG_KEY_ID = "_id";
    private static final String TAG_KEY_NAME = "name";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NETWORKS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TAGS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NETWORKS_TABLE = "CREATE TABLE " + TABLE_NETWORKS + "(" +
                NETWORK_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NETWORK_KEY_CUSTOM_SSID + " TEXT," +
                NETWORK_KEY_BSSID + " TEXT," +
                NETWORK_KEY_SSID + " TEXT)";
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "(" +
                NOTES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOTES_KEY_TITLE + " TEXT," +
                NOTES_KEY_BODY + " TEXT," +
                NOTES_KEY_DATE_CREATED + " TEXT, " +
                NOTES_KEY_NETwORK_ID + " INTEGER, " +
                "FOREIGN KEY(" + NOTES_KEY_NETwORK_ID + ") REFERENCES " + TABLE_NETWORKS + "(" + NETWORK_KEY_ID + "))";
        String CREATE_TAGS_TABLE =  "CREATE TABLE " + TABLE_TAGS + "(" +
                TAG_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TAG_KEY_NAME + " TEXT)";
        db.execSQL(CREATE_NETWORKS_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_TAGS_TABLE);
    }

    public long saveNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTES_KEY_TITLE, note.getNoteTitle());
        values.put(NOTES_KEY_BODY, note.getBody());
        values.put(NOTES_KEY_DATE_CREATED, note.getDateCreated());
        values.put(NOTES_KEY_NETwORK_ID, note.getNetworkId());
        long newRow = db.insert(TABLE_NOTES, null, values);
        db.close();
        return newRow;
    }

    public Note getNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, NOTES_KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor.moveToFirst()){
            int noteId = cursor.getColumnIndex(NOTES_KEY_ID);
            int noteTitle = cursor.getColumnIndex(NOTES_KEY_TITLE);
            int noteBody = cursor.getColumnIndex(NOTES_KEY_BODY);
            int noteDateCreated = cursor.getColumnIndex(NOTES_KEY_DATE_CREATED);
            Note note = new Note(cursor.getString(noteTitle), cursor.getString(noteBody), null,
                    cursor.getString(noteDateCreated));
            note.setId(noteId);
            note.setNetworkId(cursor.getLong(cursor.getColumnIndex(NOTES_KEY_NETwORK_ID)));
            cursor.close();
            db.close();
            return note;
        } else {
            return null;
        }
    }

    public Note[] getNotesByNetworkId(long networkId){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NOTES, null, NOTES_KEY_NETwORK_ID + "=?", new String[]{ String.valueOf(networkId) }, null, null, null);
        if(cursor.moveToFirst()){
            int titleId = cursor.getColumnIndex(NOTES_KEY_TITLE);
            int bodyId = cursor.getColumnIndex(NOTES_KEY_BODY);
            int noteId = cursor.getColumnIndex(NOTES_KEY_ID);
            do{
                Note note = new Note();
                note.setTitle(cursor.getString(titleId));
                note.setBody(cursor.getString(bodyId));
                note.setId(noteId);
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList.toArray(new Note[noteList.size()]);
    }

    public WifiNetwork getNetworkByBSSID(String bssid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NETWORKS, null, NETWORK_KEY_BSSID + "=?", new String[]{bssid}, null, null, null);
        if(cursor.moveToFirst()){
            int ssidId = cursor.getColumnIndex(NETWORK_KEY_SSID);
            int customId = cursor.getColumnIndex(NETWORK_KEY_CUSTOM_SSID);
            int idId = cursor.getColumnIndex(NETWORK_KEY_ID);
            WifiNetwork network = new WifiNetwork();
            network.setBSSID(bssid);
            network.setSSID(cursor.getString(ssidId));
            network.setId(cursor.getInt(idId));
            network.setCustomSSID(cursor.getString(customId));
            db.close();
            cursor.close();
            return network;
        } else {
            return null;
        }


    }

    public List<String> getSavedNetworkNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + NETWORK_KEY_BSSID + " FROM " + TABLE_NETWORKS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> savedNetworks = new ArrayList<>();
        if(cursor.moveToFirst()){
            String bssid = cursor.getString(cursor.getColumnIndex(NETWORK_KEY_BSSID));
            savedNetworks.add(bssid);
        }
        return savedNetworks;
    }

    public long saveNetwork(WifiNetwork network) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NETWORK_KEY_BSSID, network.getBSSID());
        values.put(NETWORK_KEY_SSID, network.getSSID());
        values.put(NETWORK_KEY_CUSTOM_SSID, network.getCustomSSID());
        long newRow = db.insert(TABLE_NETWORKS, null, values);
        db.close();
        return newRow;
    }

    public void deleteNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, NOTES_KEY_ID + "=?", new String[]{ String.valueOf(id)});
        db.close();
    }
}
