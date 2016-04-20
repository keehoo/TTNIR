package com.multivonex.keehoo.thetotallynewinsurancereminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by keehoo on 20.04.2016.
 */
public class DAO {


    /** DAO class, maintains connection with the db.*/

    private SQLiteDatabase db;
    private DBCreator dbCreator;  //helper class SQLirwOpenHelper
    private String[] allCars = {DBCreator.COLUMN_ID, DBCreator.COLUMN_CAR};

    public DAO(Context context) {

        dbCreator = new DBCreator(context);
    }

    public void open() {
        db = dbCreator.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public Car createCar() {


    }

}
