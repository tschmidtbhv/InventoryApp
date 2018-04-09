package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.helper.QueryHelper;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */
public class ProductDbHelper extends SQLiteOpenHelper {

    //Database name
    private static final String DATABASE = "products.db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Constructor calls super with given params
    public ProductDbHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QueryHelper.SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
