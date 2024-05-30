
package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PaymentBookingDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "paymentBooking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKINGS = "paymentBooking";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HOTEL_NAME = "hotelName";
    public static final String COLUMN_HOTEL_LOCATION = "hotelLocation";
    public static final String COLUMN_HOTEL_PRICE = "hotelPrice";
    public static final String COLUMN_USER_NAME = "userName";
    public static final String COLUMN_USER_EMAIL = "userEmail";
    public static final String COLUMN_USER_PHONE = "userPhone";
    public static final String COLUMN_CARD_NUMBER = "cardNumber";
    public static final String COLUMN_CARD_EXPIRY = "cardExpiry";
    public static final String COLUMN_CARD_CVV = "cardCVV";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOTEL_NAME + " TEXT, " +
                    COLUMN_HOTEL_LOCATION + " TEXT, " +
                    COLUMN_HOTEL_PRICE + " TEXT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_USER_PHONE + " TEXT, " +
                    COLUMN_CARD_NUMBER + " TEXT, " +
                    COLUMN_CARD_EXPIRY + " TEXT, " +
                    COLUMN_CARD_CVV + " TEXT" +
                    ");";

    public void updateBooking(int id, String hotelName, String hotelLocation, String hotelPrice,
                              String userName, String userEmail, String userPhone,
                              String cardNumber, String cardExpiry, String cardCVV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOTEL_NAME, hotelName);
        values.put(COLUMN_HOTEL_LOCATION, hotelLocation);
        values.put(COLUMN_HOTEL_PRICE, hotelPrice);
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_USER_PHONE, userPhone);
        values.put(COLUMN_CARD_NUMBER, cardNumber);
        values.put(COLUMN_CARD_EXPIRY, cardExpiry);
        values.put(COLUMN_CARD_CVV, cardCVV);
        db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteBooking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }



    public PaymentBookingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }
}
