package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class EditActivity extends AppCompatActivity {

    EditText productName;
    EditText productPrice;
    EditText productQuantity;
    EditText productSupplierName;
    EditText productSupplierPhone;
    Spinner productVariant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setupActionBar();
        setReferences();
    }

    private void setReferences() {
        productName = findViewById(R.id.prod_name);
        productPrice = findViewById(R.id.prod_price);
        productQuantity = findViewById(R.id.prod_quantity);
        productSupplierName = findViewById(R.id.prod_supplier_name);
        productSupplierPhone = findViewById(R.id.prod_supplier_phone);
        productVariant = findViewById(R.id.prod_variant_spinner);
    }

    /**
     * Setup for ActionBar
     */
    private void setupActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.saveproduct) {
            boolean saved = saveDataIntoDataBase();

            if (saved) {
                backHome();
            } else {
                Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            backHome();
        }

        return true;
    }

    private void backHome() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Save Date into Database
     *
     * @return successfull saved or not
     */
    private boolean saveDataIntoDataBase() {
        Log.v(EditActivity.class.getSimpleName(), "saved");

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCTNAME, productName.getText().toString());
        contentValues.put(ProductEntry.COLUMN_PRODUCTPRICE, productPrice.getText().toString());
        contentValues.put(ProductEntry.COLUMN_PRODUCTQUANTITY, productQuantity.getText().toString());
        contentValues.put(ProductEntry.COLUMN_SUPPLIERNAME, productSupplierName.getText().toString());
        contentValues.put(ProductEntry.COLUMN_SUPPLIER_PHONENR, productSupplierPhone.getText().toString());
        contentValues.put(ProductEntry.COLUMN_PRODUCTVARIANT, productVariant.getSelectedItemPosition());

        ContentResolver resolver = getContentResolver();
        resolver.insert(ProductEntry.PRODUCTS_CONTENT_URI, contentValues);
        return true;
    }
}
