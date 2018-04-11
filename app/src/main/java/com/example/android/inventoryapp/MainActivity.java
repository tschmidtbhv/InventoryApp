package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.inventoryapp.adapter.ProductAdapter;
import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.helper.Config;
import com.example.android.inventoryapp.helper.QueryHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //QueryHelper.createDummyDataInDataBase(this, Config.INSERTCOUNT);
        productList = QueryHelper.loadContentFromDb(this);
        setUpRecycler();
    }

    /**
     * Initial Setup for RecyclerView
     */
    private void setUpRecycler() {
        recyclerView = findViewById(R.id.products_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ProductAdapter(this, productList));
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


}
