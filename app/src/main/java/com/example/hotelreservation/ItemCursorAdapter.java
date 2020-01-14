package com.example.hotelreservation;

import android.content.Context;
import android.database.Cursor;
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
        TextView no_of_rooms = view.findViewById(R.id.textRooms);
        TextView no_of_guests = view.findViewById(R.id.textGuests);

        no_of_rooms.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_ROOMS)));
        no_of_guests.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_GUESTS)));
        final String id = cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.ID));


        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView roomsTaken = view.findViewById(R.id.tvRooms);
                TextView currentGuests = view.findViewById(R.id.tvGuests);
                Cursor currentBooking = db.getBooking(Integer.parseInt(id));
                int roomsTakenInt = Integer.parseInt(roomsTaken.getText().toString());
                int currentGuestsInt = Integer.parseInt(currentGuests.getText().toString());

                int bookingRooms = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_ROOMS)));
                int bookingGuests = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_GUESTS)));

                deleteShape(id);

                roomsTaken.setText(roomsTakenInt - bookingRooms);
                currentGuests.setText(currentGuestsInt - bookingGuests);

            }
        });
    }

    public void deleteShape(String id) {
        db.deleteBooking(Integer.parseInt(id));
        changeCursor(db.getAllBookings());
    }
}
