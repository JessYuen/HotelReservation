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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        Log.d(TAG, "onCreate: views declared");

        Button btnBook = findViewById(R.id.btnBook);

        updateViews(roomsTaken, currentGuests, db);

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

                if ((roomsTakenInt + roomsInputInt) <= MAX_CAPACITY) {

                    String bookTstamp = new SimpleDateFormat("dd/mm/yyyy @ hh:mm:ss", Locale.UK).format(new Date());

                    // store booking details
                    contentValues.put(TaskScheme.NO_OF_ROOMS, roomsInputInt);
                    contentValues.put(TaskScheme.NO_OF_GUESTS, guestsInputInt);
                    contentValues.put(TaskScheme.TIMESTAMP, bookTstamp);


                    // insert booking into database
                    db.addBooking(contentValues);
                    loadBookings();
                    updateViews(roomsTaken, currentGuests, db);
                    roomsInput.setText("");
                    guestsInput.setText("");

                } else if (roomsInputInt == 0) {
                    Toast.makeText(MainActivity.this, "You need to book at least 1 room.", Toast.LENGTH_SHORT).show();
                } else if ((roomsTakenInt + roomsInputInt) > MAX_CAPACITY) {
                    Toast.makeText(MainActivity.this, "Number of rooms taken cannot exceed 100.", Toast.LENGTH_SHORT).show();
                    roomsInput.setText("");
                }
            }
        });

        loadBookings();

    }

    private void loadBookings() {
        //Cursor is a temporary buffer area which stores results from a SQLiteDataBase query.
        Cursor cursor = db.getAllBookings();
        itemAdaptor.changeCursor(cursor);
        Log.d(TAG, "loadBookings: bookings loaded " + cursor.getCount());
    }

    public static void updateViews(TextView tv1, TextView tv2, TasksDBHelper db) {
        String roomsTotal, guestsTotal;

        Cursor totals = db.getTotals();

        totals.moveToFirst();
        roomsTotal = totals.getString(totals.getColumnIndexOrThrow("RoomsTotal"));
        guestsTotal = totals.getString(totals.getColumnIndexOrThrow("GuestsTotal"));
        Log.d(TAG, "updateViews: " + roomsTotal + " " + guestsTotal);

        tv1.setText(roomsTotal);
        tv2.setText(guestsTotal);



    }


}
