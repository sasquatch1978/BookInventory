package com.example.android.bookinventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.bookinventory.data.BookContract.BookEntry;
import com.example.android.bookinventory.data.BookDbHelper;
import com.example.android.bookinventory.databinding.ActivityCatalogBinding;

public class CatalogActivity extends AppCompatActivity {

    private TextView tvBook;
    private BookDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCatalogBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_catalog);

        // Bind the views.
        tvBook = binding.tvBook;
        FloatingActionButton fab = binding.fab;

        // Start the EditorActivity.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBook = new Intent(CatalogActivity.this,
                        EditorActivity.class);
                startActivity(addBook);
            }
        });

        // To access the database, instantiate the subclass of SQLiteOpenHelper.
        dbHelper = new BookDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the projection that specifies which columns from the database
        // will be used in the query.
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_PRODUCT_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER,
                BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER
        };

        // Perform a query on the books table, using 'try' with resources
        // which automatically closes the cursor.
        try (Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null)) {
            // Create a header in the TextView.
            int cursorCount = cursor.getCount();
            tvBook.setText(getString(R.string.book_count, cursorCount));
            tvBook.append(BookEntry._ID + " - "
                    + BookEntry.COLUMN_BOOK_PRODUCT_NAME + " - "
                    + BookEntry.COLUMN_BOOK_PRICE + " - "
                    + BookEntry.COLUMN_BOOK_QUANTITY + " - "
                    + BookEntry.COLUMN_BOOK_SUPPLIER + " - "
                    + BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER + "\n");

            // Figure out the index of each column.
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex
                    (BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentSupplier = cursor.getInt(supplierColumnIndex);
                String currentSupplierPhoneNumber = cursor.getString(supplierPhoneNumberColumnIndex);

                // Display the values from each column of the current row in the cursor
                // in the TextView.
                tvBook.append("\n" + currentID + " - "
                        + currentProductName + " - "
                        + currentPrice + " - "
                        + currentQuantity + " - "
                        + currentSupplier + " - "
                        + currentSupplierPhoneNumber);
            }
        }
    }
}
