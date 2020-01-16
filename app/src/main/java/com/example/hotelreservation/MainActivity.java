package com.example.hotelreservation;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelreservation.Provider.TaskScheme;
import com.example.hotelreservation.Provider.TasksDBHelper;

public class MainActivity extends AppCompatActivity {
    TasksDBHelper db;
    ListView listView;
    ItemCursorAdapter itemAdaptor;
    public static final String TAG = "MainActivity";
    EditText roomsInput, guestsInput;
    public static final int MAX_CAPACITY = 100;
    TextView roomsTaken, currentGuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new TasksDBHelper(this, TasksDBHelper.DB_NAME, null, TasksDBHelper.DB_VERSION);
        itemAdaptor = new ItemCursorAdapter(this, null, 2, db);

        listView = findViewById(R.id.ListView);
        listView.setAdapter(itemAdaptor);

        roomsInput = findViewById(R.id.etRoomNo);
        guestsInput = findViewById(R.id.etGuestNo);
        roomsTaken = findViewById(R.id.textRooms);
        currentGuests = findViewById(R.id.textGuests);

        Button btnBook = findViewById(R.id.btnBook);

        updateViews();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create content values reading to insert into db
                ContentValues contentValues = new ContentValues();

                // collect user inputs and rooms taken string
                String roomsInputStr = roomsInput.getText().toString();
                String guestsInputStr = guestsInput.getText().toString();
                Log.d(TAG, "onClick: got inputs");

                // convert to int
                Integer roomsInputInt = Integer.valueOf(roomsInputStr);
                Integer guestsInputInt = Integer.valueOf(guestsInputStr);
                int roomsTakenInt = Integer.valueOf(roomsTaken.getText().toString());

                //String bookDate = new SimpleDateFormat("dd.MM.yyyy @ hh:mm:ss", Locale.UK).format(new Date());
                String bookDate = "test";


                if ((roomsTakenInt + roomsInputInt) <= MAX_CAPACITY) {

                    // store booking details
                    contentValues.put(TaskScheme.NO_OF_ROOMS, roomsInputInt);
                    contentValues.put(TaskScheme.NO_OF_GUESTS, guestsInputInt);
                    contentValues.put(TaskScheme.TIMESTAMP, bookDate);

                    Log.d(TAG, "onClick: stored into contentvalues" + bookDate);


                    // insert booking into database
                    db.addBooking(contentValues);
                    Log.d(TAG, "onClick: booking added");
                    loadBookings();
                    updateViews();
                    roomsInput.setText("");
                    guestsInput.setText("");

                    Log.d(TAG, "onClick: updated");
                } else if (roomsInputInt == 0) {
                    Toast.makeText(MainActivity.this, "You need to book at least 1 room.", Toast.LENGTH_SHORT).show();
                } else if ((roomsTakenInt + roomsInputInt) > MAX_CAPACITY) {
                    Toast.makeText(MainActivity.this, "Number of rooms taken cannot exceed 100.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadBookings();

    }

    private void loadBookings() {
        //Cursor is a temporary buffer area which stores results from a SQLiteDataBase query.
        Cursor cursor = db.getAllBookings();
        Log.d(TAG, String.valueOf(cursor));
        itemAdaptor.changeCursor(cursor);
        itemAdaptor.notifyDataSetChanged();
        //Log.d(TAG, "loadBookings: bookings loaded");
    }

    private void updateViews() {
        String roomsTotal, guestsTotal;
        Cursor allRecords = db.getAllBookings();

        Log.d(TAG, "updateViews: number of rows = " + allRecords.getCount());

        if (!(allRecords.getCount() == 0)) {
            Log.d(TAG, "updateViews: not null");
            roomsTotal = String.valueOf(db.getRoomTotal());
            guestsTotal = String.valueOf(db.getGuestTotal());
            Log.d(TAG, "updateViews: got strings");


        } else {
            roomsTotal = "0";
            guestsTotal = "0";
        }
        roomsTaken.setText(roomsTotal);
        currentGuests.setText(guestsTotal);



    }


}
