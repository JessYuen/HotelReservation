package com.example.hotelreservation;

import android.app.Activity;
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
    Context myContext;

    public ItemCursorAdapter(Context context, Cursor c, int flags, TasksDBHelper db) {
        super(context, c, flags);
        myContext = context;
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
        TextView timestamp = view.findViewById(R.id.list_item_tstamp);

        no_of_rooms.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_ROOMS)));
        no_of_guests.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.NO_OF_GUESTS)));
        timestamp.setText(cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.TIMESTAMP)));
        final String id = cursor.getString(cursor.getColumnIndexOrThrow(TaskScheme.ID));

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteShape(id);

                TextView roomsTaken = ((Activity)myContext).findViewById(R.id.textRooms);
                TextView currentGuests = ((Activity)myContext).findViewById(R.id.textGuests);
                MainActivity.updateViews(roomsTaken, currentGuests, db);
            }
        });
    }

    public void deleteShape(String id) {
        db.deleteBooking(Integer.parseInt(id));
        changeCursor(db.getAllBookings());
    }


}
