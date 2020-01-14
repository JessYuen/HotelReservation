package com.example.hotelreservation;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.hotelreservation.Provider.TaskScheme;
import com.example.hotelreservation.Provider.TasksDBHelper;

public class ItemCursorAdapter extends CursorAdapter {
    TasksDBHelper db;

    public ItemCursorAdapter(Context context, Cursor c, int flags, TasksDBHelper db) {
        super(context, c, flags);
        this.db = db;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        TextView no_of_rooms = view.findViewById(R.id.list_item_rooms);
        TextView no_of_guests = view.findViewById(R.id.list_item_guests);

        no_of_rooms.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_ROOMS)));
        no_of_guests.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_GUESTS)));
        final String id = cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.ID));


        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "onClick: delete btn clicked");
//                updateViews(v, id);
                Log.d(MainActivity.TAG, "onClick: updated delete views");
                deleteShape(id);
            }
        });
    }

    public void deleteShape(String id) {
        db.deleteBooking(Integer.parseInt(id));
        changeCursor(db.getAllBookings());
    }

//    private void updateViews(View v, String id) {
//        TextView roomsTaken = v.findViewById(R.id.textRooms);
//        TextView currentGuests = v.findViewById(R.id.textGuests);
//        Log.d(MainActivity.TAG, "updateViews: got views");
//
//        Cursor currentBooking = db.getBooking(Integer.parseInt(id));
//        Log.d(MainActivity.TAG, "updateViews: getting booking");
//
//        int roomsTakenInt = Integer.parseInt(roomsTaken.getText().toString());
//        int currentGuestsInt = Integer.parseInt(currentGuests.getText().toString());
//
//        int bookingRooms = Integer.parseInt(currentBooking.getString(currentBooking.getColumnIndexOrThrow(TaskScheme.NO_OF_ROOMS)));
//        int bookingGuests = Integer.parseInt(currentBooking.getString(currentBooking.getColumnIndexOrThrow(TaskScheme.NO_OF_GUESTS)));
//
//        roomsTaken.setText(roomsTakenInt - bookingRooms);
//        currentGuests.setText(currentGuestsInt - bookingGuests);
//
//    }
}
