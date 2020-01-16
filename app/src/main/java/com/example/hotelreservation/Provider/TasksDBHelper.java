package com.example.hotelreservation.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hotelreservation.MainActivity;

public class TasksDBHelper extends SQLiteOpenHelper {
    Context myContext;
    public final static String DB_NAME = "TasksDB.db";
    public final static int DB_VERSION = 1;

    private final static String TASKS_TABLE_CREATE =
        "CREATE TABLE " +
                TaskScheme.TABLE_NAME + " (" +
                TaskScheme.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TaskScheme.NO_OF_ROOMS + " INTEGER, " +
                TaskScheme.NO_OF_GUESTS + " INTEGER, " +
                TaskScheme.TIMESTAMP + " TEXT);";

    public TasksDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addBooking(ContentValues contentValues) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TaskScheme.TABLE_NAME, null, contentValues);
        Log.d(MainActivity.TAG, String.valueOf(contentValues));
        Log.d(MainActivity.TAG, "addBooking: booking added");
        db.close();
    }

    public boolean deleteBooking(int taskId) {
        boolean result;
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {Integer.toString(taskId)};
        result = db.delete(TaskScheme.TABLE_NAME, "_id=?", args) > 0;
        return result;
    }

    public Cursor getAllBookings() {
        SQLiteDatabase db = getReadableDatabase();
        Log.d(MainActivity.TAG, "getAllBookings: getting bookings");
        Cursor bookings = db.rawQuery("select * from " + TaskScheme.TABLE_NAME, null);
        Log.d(MainActivity.TAG, "getAllBookings: returning bookings");
        return bookings;

    }

    public Cursor getTotals() {
        SQLiteDatabase db = getReadableDatabase();
        Log.d(MainActivity.TAG, "getGuestTotal: database open");
        Cursor totals = db.rawQuery("SELECT TOTAL(" + TaskScheme.NO_OF_ROOMS + ") AS RoomsTotal, " +
                "TOTAL(" + TaskScheme.NO_OF_GUESTS + ") AS GuestsTotal FROM " + TaskScheme.TABLE_NAME,
                null);

        return totals;
    }




}
