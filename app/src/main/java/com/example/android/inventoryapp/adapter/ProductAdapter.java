package com.example.android.inventoryapp.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.EditActivity;
import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.Product;
import com.example.android.inventoryapp.data.ProductContract;
import com.example.android.inventoryapp.helper.Config;

import java.util.List;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface ReloadInterface {
        void reload();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productQuantityTextView;

        Button cartButton;


        public ViewHolder(final View itemView) {
            super(itemView);


            productNameTextView = itemView.findViewById(R.id.name);
            productPriceTextView = itemView.findViewById(R.id.price);
            productQuantityTextView = itemView.findViewById(R.id.quantity);

            cartButton = itemView.findViewById(R.id.cartButton);

            cartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = mProductList.get(getAdapterPosition());

                    int changedQuantity = product.getQuantity() - 1;
                    if (changedQuantity >= Config.MINIMUMQUANTITY) {

                        Uri uri = Uri.withAppendedPath(ProductContract.ProductEntry.PRODUCTS_CONTENT_URI, String.valueOf(product.get_id()));
                        ContentResolver resolver = mContext.getContentResolver();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCTQUANTITY, changedQuantity);

                        resolver.update(uri,
                                contentValues,
                                null,
                                null);

                        ReloadInterface reloadInterface = (ReloadInterface) mContext;
                        reloadInterface.reload();
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.quantity_count), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    Product product = mProductList.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, EditActivity.class);
                    intent.putExtra(ProductContract.ProductEntry.COLUMN_ID, product.get_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private Context mContext;
    private List<Product> mProductList;
    private Cursor mCursor;

    public ProductAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        mProductList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = mProductList.get(position);

        holder.productNameTextView.setText(product.getProductname());
        holder.productPriceTextView.setText(String.valueOf(product.getPrice()));
        holder.productQuantityTextView.setText(String.valueOf(product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    /**
     * Update the RecylerView Data
     *
     * @param data Products
     */
    public void updateData(List<Product> data) {

        if (data != null) {
            Log.v(ProductAdapter.class.getSimpleName(), "++++updateDATA++++ " + data.toString());
            mProductList.clear();
            mProductList.addAll(data);
            notifyDataSetChanged();
        }
    }
}
