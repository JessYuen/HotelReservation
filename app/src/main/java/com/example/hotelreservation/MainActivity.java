package com.example.hotelreservation;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelreservation.Provider.TaskScheme;
import com.example.hotelreservation.Provider.TasksDBHelper;

public class MainActivity extends AppCompatActivity {
    TasksDBHelper db;
    ListView listView;
    ItemCursorAdapter itemAdaptor;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new TasksDBHelper(this, TasksDBHelper.DB_NAME, null, TasksDBHelper.DB_VERSION);
        itemAdaptor = new ItemCursorAdapter(this, null, 2, db);

        listView = findViewById(R.id.ListView);
        listView.setAdapter(itemAdaptor);

        final EditText roomsInput = findViewById(R.id.etRoomNo);
        final EditText guestsInput = findViewById(R.id.etGuestNo);
        Button btnBook = findViewById(R.id.btnBook);

        updateViews();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create content values reading to insert into db
                ContentValues contentValues = new ContentValues();

                // collect user inputs
                String roomsInputStr = roomsInput.getText().toString();
                String guestsInputStr = guestsInput.getText().toString();

                // store booking details
                contentValues.put(TaskScheme.NO_OF_ROOMS, Integer.valueOf(roomsInputStr));
                contentValues.put(TaskScheme.NO_OF_GUESTS, Integer.valueOf(guestsInputStr));

                // insert booking into database
                db.addBooking(contentValues);
                loadBookings();
                updateViews();

            }
        });

        loadBookings();

    }

    private void loadBookings() {
        //Cursor is a temporary buffer area which stores results from a SQLiteDataBase query.
        Cursor cursor = db.getAllBookings();
        itemAdaptor.changeCursor(cursor);
    }

    private void updateViews() {
        TextView roomsTaken = findViewById(R.id.textRooms);
        TextView currentGuests = findViewById(R.id.textGuests);

        String roomsTotal = "";
        String guestsTotal = "";

        Cursor totals = db.getTotals();
        if(totals.moveToFirst()) {
            roomsTotal = totals.getString(totals.getColumnIndexOrThrow("RoomsTotal"));
            guestsTotal = totals.getString(totals.getColumnIndexOrThrow("GuestsTotal"));
        }
        roomsTaken.setText(roomsTotal);
        currentGuests.setText(guestsTotal);

    }


}
