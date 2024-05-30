package com.example.Nhi_Do;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BookingDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "booking.db";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_BOOKING_TABLE = "bookings";
    private static final String COLUMN_ID ="id";
    public static final String COLUMN_CHECK_IN = "check_in";
    public static final String COLUMN_CHECK_OUT = "check_out";
    private static final String COLUMN_GUESTS = "guests_Rating";
    private static final String COLUMN_TRAVELERS_COUNT = "travelers_count";

    public BookingDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the booking table
        String createTable = "CREATE TABLE " + SQL_CREATE_BOOKING_TABLE +"("+
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CHECK_IN + " TEXT, " +
            COLUMN_CHECK_OUT + " TEXT, " +
            COLUMN_GUESTS + " INTEGER, " +
                COLUMN_TRAVELERS_COUNT + " INTEGER)";
            db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_BOOKING_TABLE);
        onCreate(db);

    }

    public long addBookingDates(String checkIn, String checkOut, int guests, String roomType, String roomNumber, String location){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHECK_IN, checkIn);
        values.put(COLUMN_CHECK_OUT, checkOut);
        values.put(COLUMN_GUESTS, guests);
        values.put(COLUMN_TRAVELERS_COUNT, 1);
        long newRowId = db.insert(SQL_CREATE_BOOKING_TABLE, null, values);
        db.close();
        return newRowId;

    }
    public Cursor getAllBookings(){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_ID, COLUMN_CHECK_IN, COLUMN_CHECK_OUT, COLUMN_GUESTS,  COLUMN_TRAVELERS_COUNT};
        return db.query(SQL_CREATE_BOOKING_TABLE, projection, null, null, null, null, null);

    }




}

