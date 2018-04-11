package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.helper.QueryHelper;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private final static int PRODUCTLOADERID = 1;

    private boolean isEditMode = false;
    private int productId;

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
        setValuesIfNeeded();
    }


    /**
     * Setup for ActionBar
     */
    private void setupActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Set initial references to views
     */
    private void setReferences() {
        productName = findViewById(R.id.prod_name);
        productPrice = findViewById(R.id.prod_price);
        productQuantity = findViewById(R.id.prod_quantity);
        productSupplierName = findViewById(R.id.prod_supplier_name);
        productSupplierPhone = findViewById(R.id.prod_supplier_phone);
        productVariant = findViewById(R.id.prod_variant_spinner);
    }

    /**
     * Checks the bundle extras
     * Has extras it will load the given Product ID
     */
    private void setValuesIfNeeded() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            isEditMode = true;
            productId = extras.getInt(ProductEntry.COLUMN_ID);

            Log.v(EditActivity.class.getSimpleName(), "ID -------- " + productId);
            getLoaderManager().restartLoader(PRODUCTLOADERID, null,this);
        }
    }

    /**
     * Displays Values for given Product
     *
     * @param product contains informations
     */
    private void setValuesForReferences(Product product) {

        productName.setText(product.getProductname());
        productPrice.setText(String.valueOf(product.getPrice()));
        productQuantity.setText(String.valueOf(product.getQuantity()));
        productSupplierName.setText(product.getSuppliername());
        productSupplierPhone.setText(product.getSupplierphonenr());
        productVariant.setSelection(product.getVariant());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.deleteproduct);
        if (!isEditMode) menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.saveproduct:
                boolean saved = QueryHelper.saveDataIntoDataBase(this, getValuesFromViews(), productId);

                if (saved) {
                    backHome();
                } else {
                    Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.deleteproduct:
                int id = QueryHelper.deleteItemWithId(this,productId);
                if(id != -1)backHome();
                break;

            default:
                backHome();
                break;
        }


        return true;
    }

    /**
     * Get back to MainActivity
     */
    private void backHome() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    /**
     * Get the Values from the Views
     *
     * @return ContentValues
     */
    private ContentValues getValuesFromViews() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCTNAME, productName.getText().toString());
        contentValues.put(ProductEntry.COLUMN_PRODUCTPRICE, productPrice.getText().toString());
        contentValues.put(ProductEntry.COLUMN_PRODUCTQUANTITY, productQuantity.getText().toString());
        contentValues.put(ProductEntry.COLUMN_SUPPLIERNAME, productSupplierName.getText().toString());
        contentValues.put(ProductEntry.COLUMN_SUPPLIER_PHONENR, productSupplierPhone.getText().toString());
        contentValues.put(ProductEntry.COLUMN_PRODUCTVARIANT, productVariant.getSelectedItemPosition());

        return contentValues;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(ProductEntry.PRODUCTS_CONTENT_URI, String.valueOf(productId));
        return new CursorLoader(this,uri,QueryHelper.PROJECTION,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == PRODUCTLOADERID){
            int[] columns = QueryHelper.createColumnIndexArray(data);
            data.moveToFirst();
            Product product = QueryHelper.createProduct(data,columns);
            if (product != null) setValuesForReferences(product);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
