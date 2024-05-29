package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BookingDAO {
    private SQLiteDatabase database;
    private PaymentBookingDbHelper dbHelper;
    private String[] allColumns = {
            PaymentBookingDbHelper.COLUMN_ID,
            PaymentBookingDbHelper.COLUMN_HOTEL_NAME,
            PaymentBookingDbHelper.COLUMN_HOTEL_LOCATION,
            PaymentBookingDbHelper.COLUMN_HOTEL_PRICE,
            PaymentBookingDbHelper.COLUMN_USER_NAME,
            PaymentBookingDbHelper.COLUMN_USER_EMAIL,
            PaymentBookingDbHelper.COLUMN_USER_PHONE,
            PaymentBookingDbHelper.COLUMN_CARD_NUMBER,
            PaymentBookingDbHelper.COLUMN_CARD_EXPIRY,
            PaymentBookingDbHelper.COLUMN_CARD_CVV
    };

    public BookingDAO(Context context) {
        dbHelper = new PaymentBookingDbHelper(context);
    }
    // Opens Db for writing
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    //Closes Db connection
    public void close() {
        dbHelper.close();
    }
    // insert booking into database
    public long insertBooking(String hotelName, String hotelLocation, String hotelPrice,
                              String userName, String userEmail, String userPhone,
                              String cardNumber, String cardExpiry, String cardCVV) {
        ContentValues values = new ContentValues();
        values.put(PaymentBookingDbHelper.COLUMN_HOTEL_NAME, hotelName);
        values.put(PaymentBookingDbHelper.COLUMN_HOTEL_LOCATION, hotelLocation);
        values.put(PaymentBookingDbHelper.COLUMN_HOTEL_PRICE, hotelPrice);
        values.put(PaymentBookingDbHelper.COLUMN_USER_NAME, userName);
        values.put(PaymentBookingDbHelper.COLUMN_USER_EMAIL, userEmail);
        values.put(PaymentBookingDbHelper.COLUMN_USER_PHONE, userPhone);
        values.put(PaymentBookingDbHelper.COLUMN_CARD_NUMBER, cardNumber);
        values.put(PaymentBookingDbHelper.COLUMN_CARD_EXPIRY, cardExpiry);
        values.put(PaymentBookingDbHelper.COLUMN_CARD_CVV, cardCVV);

        open(); // Open the database connection
        long insertId = database.insert(PaymentBookingDbHelper.TABLE_BOOKINGS, null, values);
        close(); // Close the database connection

        return insertId;
    }
    //Retrieve all bookings
    public Cursor fetchAllBookings() {
        return database.query(PaymentBookingDbHelper.TABLE_BOOKINGS,
                allColumns, null, null, null, null, null);
    }
    // Delete booking
    public void deleteBooking(long bookingId) {
        database.delete(PaymentBookingDbHelper.TABLE_BOOKINGS,
                PaymentBookingDbHelper.COLUMN_ID + " = " + bookingId, null);
    }
}
