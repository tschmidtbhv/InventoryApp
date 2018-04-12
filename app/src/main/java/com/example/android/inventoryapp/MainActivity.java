package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.adapter.ProductAdapter;
import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.helper.QueryHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ProductAdapter.ReloadInterface {

    private static final int LOADERID = 1;

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecycler();
    }

    @Override
    protected void onStart() {

        //Using restartLoader instead of initLoader otherwise can`t close save the Cursor
        getLoaderManager().restartLoader(LOADERID, null, this);
        super.onStart();
    }

    /**
     * Initial Setup for RecyclerView
     */
    private void setUpRecycler() {
        recyclerView = findViewById(R.id.products_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);

        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, ProductContract.ProductEntry.PRODUCTS_CONTENT_URI, QueryHelper.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (loader.getId() == LOADERID) {
            if (data != null) {
                List<Product> products = QueryHelper.createProductListFromCursor(data);
                if (products.size() == 0) {
                    TextView infoTextView = findViewById(R.id.infotext);
                    infoTextView.setText(getString(R.string.no_content));
                    infoTextView.setVisibility(View.VISIBLE);
                }
                productAdapter.updateData(products);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void reload() {
        getLoaderManager().restartLoader(LOADERID, null, this);
    }
}
