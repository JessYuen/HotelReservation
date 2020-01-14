package com.example.hotelreservation.Provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.widget.TextView;

import com.example.hotelreservation.MainActivity;

public class TasksDBHelper extends SQLiteOpenHelper {
    Context myContext;
    public final static String DB_NAME = "TasksDB.db";
    public final static int DB_VERSION = 1;

    private final static String TASKS_TABLE_CREATE =
        "CREATE TABLE " +
                TaskScheme.TABLE_NAME + " (" +
                TaskScheme.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TaskScheme.NO_OF_ROOMS + " INT, " +
                TaskScheme.NO_OF_GUESTS + " INT);";

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
        Cursor reservations = db.rawQuery("select * from " + TaskScheme.TABLE_NAME, null);
        return reservations;

    }

    public Cursor getTotals() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor totals = db.rawQuery("SELECT Sum (" + (TaskScheme.NO_OF_ROOMS) + ") as RoomsTotal, " +
                "Sum (" + (TaskScheme.NO_OF_GUESTS) + ") as GuestsTotal FROM " + TaskScheme.TABLE_NAME, null);
        return totals;
    }
}
