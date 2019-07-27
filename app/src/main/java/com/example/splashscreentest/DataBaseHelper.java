package com.example.splashscreentest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    //    will need an idea, and then every other field
    private static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_ISMALE = "ismale";
    Cursor datacursor;

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_FIRSTNAME + " TEXT, " +
                COLUMN_LASTNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_NUMBER + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_BIRTHDAY + " TEXT, " +
                COLUMN_PICTURE + " TEXT, " +
                COLUMN_ISMALE + " TEXT " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    //    Add new row to the db
    public void addContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRSTNAME, contact.getFirstName());
        values.put(COLUMN_LASTNAME, contact.getLastName());
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_NUMBER, contact.getNumber());
        values.put(COLUMN_ADDRESS, contact.getAddress());
        values.put(COLUMN_BIRTHDAY, contact.getBirthday());
        values.put(COLUMN_PICTURE, contact.getPictureUrlString());
        values.put(COLUMN_ISMALE, contact.getIsMale());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    //    Delete a contact from the db
    public void deleteContact(String email) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_EMAIL + "=\"" + email + "\";");
    }

    public boolean hasContacts() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + "*" + " FROM " + TABLE_CONTACTS;

        datacursor = db.rawQuery(query, null);
        if (datacursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Contact[] databaseToArray() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + "*" + " FROM " + TABLE_CONTACTS;

        datacursor = db.rawQuery(query, null);
        Contact[] contacts = new Contact[datacursor.getCount()];
        int i = 0;
        while (datacursor.moveToNext()) {
            Contact currentContact = new Contact(
                    datacursor.getString(1),
                    datacursor.getString(2),
                    datacursor.getString(3),
                    datacursor.getString(4),
                    datacursor.getString(5),
                    datacursor.getString(6),
                    datacursor.getString(7),
                    datacursor.getString(8)
            );
            contacts[i] = currentContact;
            i++;
        }

        datacursor.close();
        db.close();
        return contacts;
    }

    public ArrayList<Contact> databaseToArrayList() {
        ArrayList<Contact> contactsList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + "*" + " FROM " + TABLE_CONTACTS;

        datacursor = db.rawQuery(query, null);
//        Contact[] contacts = new Contact[datacursor.getCount()];
        while (datacursor.moveToNext()) {
            Contact currentContact = new Contact(
                    datacursor.getString(1),
                    datacursor.getString(2),
                    datacursor.getString(3),
                    datacursor.getString(4),
                    datacursor.getString(5),
                    datacursor.getString(6),
                    datacursor.getString(7),
                    datacursor.getString(8)
            );
            contactsList.add(currentContact);
        }

        datacursor.close();
        db.close();

        return contactsList;
    }
}
