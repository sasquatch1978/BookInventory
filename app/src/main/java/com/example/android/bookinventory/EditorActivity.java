package com.example.android.bookinventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookContract.BookEntry;
import com.example.android.bookinventory.data.BookDbHelper;
import com.example.android.bookinventory.databinding.ActivityEditorBinding;

public class EditorActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etPrice;
    private EditText etQuantity;
    private EditText etPhoneNumber;
    private Spinner spnSupplier;

    private int supplier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditorBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_editor);

        // Bind the views.
        etTitle = binding.etTitle;
        etPrice = binding.etPrice;
        etQuantity = binding.etQuantity;
        etPhoneNumber = binding.etPhoneNumber;
        spnSupplier = binding.spnSupplier;

        /* Format the phone number as the user types it.
           Reference: https://stackoverflow.com/a/15647444
           Date: 8/1/18
         */
        etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        // End referenced code.

        // Initialize the dropdown Spinner.
        setupSpinner();
    }

    // Setup the dropdown Spinner that allows the user to select the supplier of the books.
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout.
        ArrayAdapter supplierSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_dropdown_item);

        // Specify dropdown layout style - simple list view with 1 item per line.
        supplierSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner.
        spnSupplier.setAdapter(supplierSpinnerAdapter);

        // Set the integer selected to the constant values.
        spnSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_one))) {
                        supplier = BookEntry.SUPPLIER_ONE;
                    } else if (selection.equals(getString(R.string.supplier_two))) {
                        supplier = BookEntry.SUPPLIER_TWO;
                    } else if (selection.equals(getString(R.string.supplier_three))) {
                        supplier = BookEntry.SUPPLIER_THREE;
                    } else {
                        supplier = BookEntry.SUPPLIER_FOUR;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                supplier = 0;
            }
        });
    }

    // Get user input from the editor and save a new book into the database.
    private void insertBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productName = etTitle.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String quantity = etQuantity.getText().toString().trim();
        String supplierPhoneNumber = etPhoneNumber.getText().toString().trim();

        int bookPrice = 0;
        int bookQuantity = 0;
        // Make sure a price and quantity are entered before converting to integers.
        if (!price.equals("") && !quantity.equals("")) {
            // Convert the price to a double.
            double doublePrice = Double.parseDouble(price);

            // Convert the price and quantity to integers,
            // price will need converted back to double when displayed to user.
            bookPrice = (int) (doublePrice * 100);
            bookQuantity = Integer.parseInt(quantity);
        }

        // Create database helper.
        BookDbHelper dbHelper = new BookDbHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase inventoryDatabase = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys.
        ContentValues bookValues = new ContentValues();
        bookValues.put(BookEntry.COLUMN_BOOK_PRODUCT_NAME, productName);
        bookValues.put(BookEntry.COLUMN_BOOK_PRICE, bookPrice);
        bookValues.put(BookEntry.COLUMN_BOOK_QUANTITY, bookQuantity);
        bookValues.put(BookEntry.COLUMN_BOOK_SUPPLIER, supplier);
        bookValues.put(BookEntry.COLUMN_BOOK_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        // Make sure a title, price, and quantity are entered before
        // attempting to insert a new row into the table.
        if (productName.equals("") || price.equals("") || quantity.equals("")) {
            // If not entered display a toast message.
            Toast.makeText(this, R.string.enter_fields, Toast.LENGTH_SHORT).show();
        } else {
            // Insert the new row, returning the primary key value of the new row.
            long newRowId = inventoryDatabase.insert(BookEntry.TABLE_NAME,
                    null, bookValues);

            // Show a toast message depending on whether or not the insertion was successful
            if (newRowId == -1) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, R.string.save_error, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful, display a toast with the row ID.
                Toast.makeText(this, getString(R.string.save_successful)
                        + newRowId, Toast.LENGTH_SHORT).show();
            }

            // Exit the activity.
            // Called here so the activity doesn't finish if a title,
            // price, or quantity aren't entered.
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save a book to the database.
                insertBook();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
