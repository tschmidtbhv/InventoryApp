package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * InventoryApp
 * API Contract for InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */
public class ProductContract {

    //Prevent from instantiate
    private ProductContract() {

    }

    /**
     * Inner Class to define DB Constants
     */
    public static final class ProductEntry implements BaseColumns {

        //Name for the DB Table
        public final static String TABLENAME = "products";

        /**
         * Column for the product ID
         * <p>
         * PRIMARY KEY
         * <p>
         * Type INTEGER
         * AUTOINCREMENT
         */
        public final static String COLUMN_ID = BaseColumns._ID;

        /**
         * Colum for the product name
         * <p>
         * Type TEXT
         * NOT NULL
         */
        public final static String COLUMN_PRODUCTNAME = "productname";

        /**
         * Column for the price in cents
         * <p>
         * Type INTEGER
         * NOT NULL
         */
        public final static String COLUMN_PRODUCTPRICE = "price";

        /**
         * Column for the product quantity
         * <p>
         * Type INTEGER
         * DEFAULT 0
         */
        public final static String COLUMN_PRODUCTQUANTITY = "quantity";

        /**
         * Column for product variant
         * Optional value if the product has variants
         * <p>
         * possible product variants are {@link #PRODUCTVARIANT_NO_VARIANT},
         * {@link #PRODUCTVARIANT_SIZE_S}, {@link #PRODUCTVARIANT_SIZE_M}
         * {@link #PRODUCTVARIANT_SIZE_L} or {@link #PRODUCTVARIANT_SIZE_XL}
         * <p>
         * Type INTEGER
         * DEFAULT 0
         */
        public final static String COLUMN_PRODUCTVARIANT = "variant";

        /**
         * Column for supplier name
         * <p>
         * Type TEXT
         * NOT NULL
         */

        public final static String COLUMN_SUPPLIERNAME = "suppliername";

        /**
         * Column for supplier phone nr
         * <p>
         * Type INTEGER
         */

        public final static String COLUMN_SUPPLIER_PHONENR = "supplierphonenr";


        /**
         * Possible product variants
         */
        public static final int PRODUCTVARIANT_NO_VARIANT = 0;
        public static final int PRODUCTVARIANT_SIZE_S = 1;
        public static final int PRODUCTVARIANT_SIZE_M = 2;
        public static final int PRODUCTVARIANT_SIZE_L = 3;
        public static final int PRODUCTVARIANT_SIZE_XL = 4;

    }
}
