package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;
import com.example.android.inventoryapp.helper.QueryHelper;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int PRODUCTLOADERID = 1;

    private boolean isEditMode = false;
    private boolean isModified = false;
    private int productId;

    EditText productName;
    EditText productPrice;
    EditText productQuantity;
    EditText productSupplierName;
    EditText productSupplierPhone;
    Spinner productVariant;
    Button callButton;
    Button increaseButton;
    Button deacreaseButton;

    View.OnClickListener callOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String phoneNr = productSupplierPhone.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNr));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    };

    View.OnClickListener quantityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String quantityString = productQuantity.getText().toString().trim();

            if (!TextUtils.isEmpty(quantityString)) {

                int quantity = Integer.parseInt(quantityString);

                switch (v.getId()) {

                    case R.id.increaseButton:
                        productQuantity.setText(String.valueOf(quantity + 1));
                        break;

                    case R.id.deacreaseButton:
                        int result = quantity - 1;
                        if (result >= 0) {
                            productQuantity.setText(String.valueOf(result));
                        } else {
                            Toast.makeText(EditActivity.this, getString(R.string.quantity_count), Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            } else {
                productQuantity.setText(getString(R.string.initial_quantity));
            }

        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isModified = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setupActionBar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEditMode = true;
            getSupportActionBar().setTitle(R.string.edit_product);
        } else {
            getSupportActionBar().setTitle(R.string.add_product);
        }

        setReferences();
        setValuesIfNeeded(extras);
    }


    /**
     * Setup for ActionBar
     */
    private void setupActionBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
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
        if (isEditMode) {
            callButton = findViewById(R.id.callsupplierButton);
            callButton.setVisibility(View.VISIBLE);
            callButton.setOnClickListener(callOnClickListener);
        }

        increaseButton = findViewById(R.id.increaseButton);
        deacreaseButton = findViewById(R.id.deacreaseButton);

        increaseButton.setOnClickListener(quantityListener);
        deacreaseButton.setOnClickListener(quantityListener);

        setOnTouchListener();
    }

    /**
     * Set the onTouchListener for Views
     */
    private void setOnTouchListener() {
        productName.setOnTouchListener(onTouchListener);
        productPrice.setOnTouchListener(onTouchListener);
        productQuantity.setOnTouchListener(onTouchListener);
        productSupplierName.setOnTouchListener(onTouchListener);
        productSupplierPhone.setOnTouchListener(onTouchListener);
    }

    /**
     * Checks the bundle extras
     * Has extras it will load the given Product ID
     */
    private void setValuesIfNeeded(Bundle extras) {

        if (isEditMode) {

            isEditMode = true;
            productId = extras.getInt(ProductEntry.COLUMN_ID);

            getLoaderManager().restartLoader(PRODUCTLOADERID, null, this);
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
                saveProduct();
                break;
            case R.id.deleteproduct:
                showDeleteDialog();
                break;

            default:
                if (checkModified()) {
                    showModifiedDialog();
                } else {
                    backHome();
                }
                break;
        }


        return true;
    }


    /**
     * Save the product to DB
     * Check before the input
     */
    private void saveProduct() {
        if (editComplete()) {
            boolean saved = QueryHelper.saveDataIntoDataBase(this, getValuesFromViews(), productId);

            if (saved) {
                backHome();
            } else {
                Toast.makeText(this, getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Check the fields for input
     *
     * @return
     */
    private boolean editComplete() {

        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(productName);
        editTexts.add(productPrice);
        editTexts.add(productQuantity);
        editTexts.add(productSupplierName);
        editTexts.add(productSupplierPhone);

        boolean canPass = true;
        for (EditText editText : editTexts) {
            if (!checkView(editText)) {
                canPass = false;
            }
        }

        return canPass;
    }

    private boolean checkView(EditText editText) {

        String input = editText.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            editText.setError(getString(R.string.input_required));
            return false;
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

    @Override
    public void onBackPressed() {

        if (checkModified()) {
            showModifiedDialog();
            return;
        }

        super.onBackPressed();
    }

    private boolean checkModified() {
        if (isEditMode) {
            return isModified;
        }
        return false;
    }

    /**
     * Show the modified dialog
     */
    private void showModifiedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.you_modified).setTitle(R.string.modified);

        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backHome();
            }
        });

        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    /**
     * Show the delete Dialog
     */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_product).setTitle(R.string.delete_product);

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = QueryHelper.deleteItemWithId(EditActivity.this, productId);
                if (id != -1) backHome();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
        return new CursorLoader(this, uri, QueryHelper.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (loader.getId() == PRODUCTLOADERID) {
            int[] columns = QueryHelper.createColumnIndexArray(data);
            data.moveToFirst();
            Product product = QueryHelper.createProduct(data, columns);
            if (product != null) setValuesForReferences(product);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
