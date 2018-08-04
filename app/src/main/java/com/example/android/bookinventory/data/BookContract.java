package com.example.android.bookinventory.data;

import android.provider.BaseColumns;

public final class BookContract {

    // Empty constructor to prevent the contract class from being instantiated.
    private BookContract() {
    }

    // Inner class that defines the table contents of the books table.
    public static final class BookEntry implements BaseColumns {

        // Table name for the books database.
        public static final String TABLE_NAME = "books";

        // Column names for the books table.
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_PRODUCT_NAME = "product_name";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_SUPPLIER = "supplier";
        public static final String COLUMN_BOOK_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        // Possible values for the supplier of the books.
        public static final int SUPPLIER_ONE = 0;
        public static final int SUPPLIER_TWO = 1;
        public static final int SUPPLIER_THREE = 2;
        public static final int SUPPLIER_FOUR = 3;
    }
}
