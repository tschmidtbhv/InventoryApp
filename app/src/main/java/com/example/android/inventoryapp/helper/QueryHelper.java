package com.example.android.inventoryapp.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

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
            + ProductEntry.COLUMN_PRODUCTPRICE + " REAL NOT NULL, "
            + ProductEntry.COLUMN_PRODUCTQUANTITY + " INTEGER NOT NULL DEFAULT 0, "
            + ProductEntry.COLUMN_PRODUCTVARIANT + " INTEGER DEFAULT 0, "
            + ProductEntry.COLUMN_SUPPLIERNAME + " TEXT NOT NULL, "
            + ProductEntry.COLUMN_SUPPLIER_PHONENR + " TEXT); ";

    public static String[] PROJECTION = {
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


        Cursor cursor = context.getContentResolver().query(
                ProductEntry.PRODUCTS_CONTENT_URI,
                PROJECTION,
                null,
                null,
                null);


        return createProductListFromCursor(cursor);
    }


    public static Product loadProductWithId(Context context, int id) {

        Uri uri = Uri.withAppendedPath(ProductEntry.PRODUCTS_CONTENT_URI, String.valueOf(id));

        Cursor cursor = context.getContentResolver().query(uri,
                PROJECTION,
                null,
                null,
                null);

        if (cursor == null) return null;

        cursor.moveToFirst();

        int[] columns = createColumnIndexArray(cursor);
        Product product = createProduct(cursor, columns);

        return product;
    }

    /**
     * Create the Product List from given cursor
     *
     * @param cursor cursor db
     * @return List of Products from DB
     */
    public static List<Product> createProductListFromCursor(Cursor cursor) {
        List<Product> products = new ArrayList<>();
        try {
            if (cursor != null) {
                int[] columns = createColumnIndexArray(cursor);

                while (cursor.moveToNext()) {
                    Product product = createProduct(cursor, columns);
                    products.add(product);
                }
            }
        } finally {
            cursor.close();
        }

        return products;
    }

    public static Product createProduct(Cursor cursor, int[] columns) {

        int id = cursor.getInt(columns[0]);
        String name = cursor.getString(columns[1]);
        int price = cursor.getInt(columns[2]);
        int quantity = cursor.getInt(columns[3]);
        int variant = cursor.getInt(columns[4]);
        String supplier = cursor.getString(columns[5]);
        String supplierNr = cursor.getString(columns[6]);

        Product product = new Product(
                id,
                name,
                price,
                quantity,
                variant,
                supplier,
                supplierNr);

        return product;
    }

    public static int[] createColumnIndexArray(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTNAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTPRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTQUANTITY);
        int variantColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCTVARIANT);
        int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIERNAME);
        int suppliernrColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONENR);

        int[] columns = new int[]{idColumnIndex, nameColumnIndex, priceColumnIndex, quantityColumnIndex, variantColumnIndex, supplierColumnIndex, suppliernrColumnIndex};
        return columns;
    }

    /**
     * Insert Dummy data into DB
     *
     * @param context Act Context
     * @param count   how many dummy data will create
     */
    public static void createDummyDataInDataBase(Context context, int count) {

        ContentResolver resolver = context.getContentResolver();

        for (int i = 0; i < count; i++) {

            int tempProductCounter = i + 1;
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCTNAME, context.getString(R.string.productname, tempProductCounter));
            values.put(ProductEntry.COLUMN_PRODUCTPRICE, Config.DUMMYPRICE);
            values.put(ProductEntry.COLUMN_PRODUCTQUANTITY, Config.DUMMYQUANTITY);
            values.put(ProductEntry.COLUMN_PRODUCTVARIANT, ProductEntry.PRODUCTVARIANT_NO_VARIANT);
            values.put(ProductEntry.COLUMN_SUPPLIERNAME, context.getString(R.string.suppliername, tempProductCounter));
            values.put(ProductEntry.COLUMN_SUPPLIER_PHONENR, Config.DUMMYPHONENUMBER);

            Uri rowUri = resolver.insert(ProductEntry.PRODUCTS_CONTENT_URI, values);
            if (rowUri == null) {
                Toast.makeText(context, context.getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    /**
     * Delete Product with given id
     *
     * @param context current contest
     * @param id      product id
     * @return int
     */
    public static int deleteItemWithId(Context context, int id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ProductEntry.PRODUCTS_CONTENT_URI, String.valueOf(id));
        return resolver.delete(uri, null, null);
    }

    /**
     * Save Data into Database
     *
     * @return successfull saved or not
     */
    public static boolean saveDataIntoDataBase(Context context, ContentValues contentValues, int productId) {

        ContentResolver resolver = context.getContentResolver();
        if(productId > 0){
            Uri uri = Uri.withAppendedPath(ProductEntry.PRODUCTS_CONTENT_URI, String.valueOf(productId));
            resolver.update(uri,
                    contentValues,
                    null,
                    null);
        }else {
            resolver.insert(ProductEntry.PRODUCTS_CONTENT_URI, contentValues);
        }

        return true;
    }
}
