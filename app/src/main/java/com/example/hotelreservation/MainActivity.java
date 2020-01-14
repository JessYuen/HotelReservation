package com.example.hotelreservation;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelreservation.Provider.TaskScheme;
import com.example.hotelreservation.Provider.TasksDBHelper;

public class MainActivity extends AppCompatActivity {
    TasksDBHelper db;
    ListView listView;
    ItemCursorAdapter itemAdaptor;


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

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                String roomsInputStr = roomsInput.getText().toString();
                String guestsInputStr = guestsInput.getText().toString();

                contentValues.put(TaskScheme.NO_OF_ROOMS, Integer.valueOf(roomsInputStr));
                contentValues.put(TaskScheme.NO_OF_GUESTS, Integer.valueOf(guestsInputStr));

                db.addBooking(contentValues);
                loadBookings();



            }
        });

        loadBookings();

    }

    private void loadBookings() {
        //Cursor is a temporary buffer area which stores results from a SQLiteDataBase query.
        Cursor cursor = db.getAllBookings();
        itemAdaptor.changeCursor(cursor);
    }

}
