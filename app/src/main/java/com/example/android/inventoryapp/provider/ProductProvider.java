package com.example.android.inventoryapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

import java.util.Arrays;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 10.04.2018.
 */
public class ProductProvider extends ContentProvider {

    private final static int PRODUCTS = 1;
    private final static int PRODUCT_ID = 2;

    private final static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //URI for * Products
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCTS_PATH, PRODUCTS);

        //URI for ID products table
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCTS_PATH + "/#", PRODUCT_ID);
    }

    private ProductDbHelper dbHelper;

    @Override
    public boolean onCreate() {

        dbHelper = new ProductDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Log.v(ProductProvider.class.getSimpleName(), "query -----------------------------------");

        int matchID = sUriMatcher.match(uri);

        Cursor cursor = null;
        switch (matchID) {

            case PRODUCTS:
                Log.v(ProductProvider.class.getSimpleName(), "query PRODUCTS");
                cursor = loadProducts(uri, projection, true);
                break;

            case PRODUCT_ID:
                Log.v(ProductProvider.class.getSimpleName(), "query PRODUCTS ID");
                cursor = loadProducts(uri, projection, false);
                break;
            default:
                throw new IllegalArgumentException("INSERT NOT ALLOWED FOR " + uri);
        }

        return cursor;
    }

    private Cursor loadProducts(Uri uri, String[] projection, boolean loadAll) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String selection = null;
        String[] selectionArgs = null;

        if(!loadAll) {
            selection = ProductEntry.COLUMN_ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            Log.v(ProductProvider.class.getSimpleName(), "----- SELECTION " + Arrays.toString(selectionArgs));
        }

        Cursor cursor = database.query(
                ProductEntry.TABLENAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Log.v(ProductProvider.class.getSimpleName(), "INSERT ");
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCTNAME);
        if (name == null)
            throw new IllegalArgumentException(getContext().getString(R.string.name_required) + uri);

        Integer productVariant = values.getAsInteger(ProductEntry.COLUMN_PRODUCTVARIANT);
        if (productVariant == null || !ProductEntry.isValidVariant(productVariant))
            throw new IllegalArgumentException(getContext().getString(R.string.variant_required));

        Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCTPRICE);
        if (price == null || price < 0)
            throw new IllegalArgumentException(getContext().getString(R.string.price_required));

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(ProductEntry.TABLENAME,
                null, values);

        if (id == -1) return null;

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int matchId = sUriMatcher.match(uri);

        SQLiteDatabase database = dbHelper.getWritableDatabase();


        switch (matchId) {

            case PRODUCT_ID:
                selection = ProductEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(ProductEntry.TABLENAME, selection, selectionArgs);

            default:

                throw new IllegalArgumentException(getContext().getString(R.string.delete_error));

        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int matchId = sUriMatcher.match(uri);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        switch (matchId) {

            case PRODUCTS:
                return 0;

            case PRODUCT_ID:
                selection = ProductEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.update(ProductEntry.TABLENAME, values, selection, selectionArgs);
            default:

                throw new IllegalArgumentException(getContext().getString(R.string.update_error));
        }
    }
}
