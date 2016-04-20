package com.multivonex.keehoo.thetotallynewinsurancereminder;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBCreator extends SQLiteOpenHelper {

    /**
     * This class is responsible for creating the database. THe onUpgrade() method will simply
     * delet all existing data and re-create the table. It also defines several constrants
     * for the table name and the table columns.
     */

    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAR = "car";

    private static final String DATABASE_NAME = "cars.db";
    private static final int DATABASE_VERSION = 1;

    // DataBase creation sql statement

    private static final String DATABASE_CREATE = "create table "
            + TABLE_CARS
            + "("
            + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_CAR
            + " text not null);";


    public DBCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        Log.d("DBCreator.java", " data base creation command executed in onCreate method");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {   // upgrade bazy danych db z wersji OldVersion do wercji newVersion
        Log.d(DBCreator.class.getName(),
                "Upgrading database from version " +
                        oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CARS);
        onCreate(db);
    }
}
