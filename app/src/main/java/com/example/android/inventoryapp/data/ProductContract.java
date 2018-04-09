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
         * Type TEXT
         */

        public final static String COLUMN_SUPPLIER_PHONENR = "supplierphonenr";


        /**
         * Possible product variants
         * Note to reviewer: Will be used for next Version
         * Currently needed for link Comment
         */
        public static final int PRODUCTVARIANT_NO_VARIANT = 0;

    }
}
