package com.example.android.inventoryapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.Product;

import java.util.List;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView idTextView;
        TextView productNameTextView;
        TextView productPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id);
            productNameTextView = itemView.findViewById(R.id.name);
            productPriceTextView = itemView.findViewById(R.id.price);
        }
    }

    private Context mContext;
    private List<Product> mProductList;

    public ProductAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        mProductList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = mProductList.get(position);

        holder.idTextView.setText(String.valueOf(product.get_id()));
        holder.productNameTextView.setText(product.getProductname());
        holder.productPriceTextView.setText(String.valueOf(product.getFormattedPrice()));
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

}
