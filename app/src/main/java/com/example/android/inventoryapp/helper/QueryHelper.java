package com.example.android.inventoryapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */
public final class QueryHelper {

    // Initial sql creation for products table
    public static String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE "
            + ProductEntry.TABLENAME + " ("
            + ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductEntry.COLUMN_PRODUCTNAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_PRODUCTPRICE + " INTEGER NOT NULL, "
            + ProductEntry.COLUMN_PRODUCTQUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            + ProductEntry.COLUMN_PRODUCTVARIANT + " INTEGER DEFAULT 0, "
            + ProductEntry.COLUMN_SUPPLIERNAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_SUPPLIER_PHONENR + " TEXT); ";

    private static String[] PROJECTION = {
            ProductEntry.COLUMN_ID,
            ProductEntry.COLUMN_PRODUCTNAME,
            ProductEntry.COLUMN_PRODUCTPRICE,
            ProductEntry.COLUMN_PRODUCTQUANTITY,
            ProductEntry.COLUMN_PRODUCTVARIANT,
            ProductEntry.COLUMN_SUPPLIERNAME,
            ProductEntry.COLUMN_SUPPLIER_PHONENR
    };


    /**
     * Load * product data from Database
     */
    public static List<Product> loadContentFromDb(Context context) {

        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                ProductContract.ProductEntry.TABLENAME,
                PROJECTION,
                null,
                null,
                null,
                null,
                null);


        return createProductListFromCursor(cursor);
    }

    /**
     * Create the Product List from given cursor
     *
     * @param cursor cursor db
     * @return List of Products from DB
     */
    private static List<Product> createProductListFromCursor(Cursor cursor) {
        List<Product> products = new ArrayList<>();
        try {
            if (cursor != null) {

                int idColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_ID);
                int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTNAME);
                int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTPRICE);
                int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTQUANTITY);
                int variantColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTVARIANT);
                int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIERNAME);
                int suppliernrColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONENR);

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(idColumnIndex);
                    String name = cursor.getString(nameColumnIndex);
                    int price = cursor.getInt(priceColumnIndex);
                    int quantity = cursor.getInt(quantityColumnIndex);
                    int variant = cursor.getInt(variantColumnIndex);
                    String supplier = cursor.getString(supplierColumnIndex);
                    String supplierNr = cursor.getString(suppliernrColumnIndex);

                    Product product = new Product(
                            id,
                            name,
                            price,
                            quantity,
                            variant,
                            supplier,
                            supplierNr);

                    products.add(product);
                }
            }
        } finally {
            cursor.close();
        }

        return products;
    }

    /**
     * Insert Dummy data into DB
     * @param context Act Context
     * @param count how many dummy data will create
     */
    public static void createDummyDataInDataBase(Context context, int count){

        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        for(int i = 0; i < count; i++){

            int tempProductCounter = i +1;
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCTNAME, context.getString(R.string.productname, tempProductCounter));
            values.put(ProductEntry.COLUMN_PRODUCTPRICE, Config.DUMMYPRICE);
            values.put(ProductEntry.COLUMN_PRODUCTQUANTITY, Config.DUMMYQUANTITY);
            values.put(ProductEntry.COLUMN_PRODUCTVARIANT,ProductEntry.PRODUCTVARIANT_NO_VARIANT);
            values.put(ProductEntry.COLUMN_SUPPLIERNAME, context.getString(R.string.suppliername, tempProductCounter));
            values.put(ProductEntry.COLUMN_SUPPLIER_PHONENR,Config.DUMMYPHONENUMBER);

            long rowId = database.insert(ProductEntry.TABLENAME, null ,values);
            if(rowId == -1 ){
                Toast.makeText(context, context.getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }
}
