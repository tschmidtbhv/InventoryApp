package com.example.android.inventoryapp.helper;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */
public interface QueryHelper {

    // Initial sql creation for products table
    String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE "
            + ProductEntry.TABLENAME + " ("
            + ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductEntry.COLUMN_PRODUCTNAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_PRODUCTPRICE + " INTEGER NOT NULL, "
            + ProductEntry.COLUMN_PRODUCTQUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            + ProductEntry.COLUMN_PRODUCTVARIANT + " INTEGER DEFAULT 0, "
            + ProductEntry.COLUMN_SUPPLIERNAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_SUPPLIER_PHONENR + " INTEGER); ";

}
