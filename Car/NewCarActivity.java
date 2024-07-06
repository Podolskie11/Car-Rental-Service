package com.example.lab_rest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookService;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCarActivity extends AppCompatActivity {

    private EditText txtModel;
    private EditText txtRemarks;
    private EditText txtAStatus;
    private EditText txtPlateNumber;
    private EditText txtBrand;

    private static TextView tvCreatedAt; // static because need to be accessed by DatePickerFragment
    private static Date createdAt; // static because need to be accessed by DatePickerFragment
    private Spinner spCategory;

    /**
     * Date picker fragment class
     * Reference: https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            // create a date object from selected year, month and day
            createdAt = new GregorianCalendar(year, month, day).getTime();

            // display in the label beside the button with specific date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            tvCreatedAt.setText(sdf.format(createdAt));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get view objects references
        txtModel = findViewById(R.id.txtModel);
        txtAStatus = findViewById(R.id.txtAStatus);
        txtRemarks = findViewById(R.id.txtRemarks);
        txtBrand = findViewById(R.id.txtBrand);
        txtPlateNumber = findViewById(R.id.txtPlateNumber);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        spCategory = findViewById(R.id.spCategory);

        // set default createdAt value to current date
        createdAt = new Date();
        // display in the label beside the button with specific date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        tvCreatedAt.setText( sdf.format(createdAt) );

        // fetch all book categories and set to spinner
        fetchAllMainCategories();
    }

    /**
     * Called when pick date button is clicked. Display a date picker dialog
     *
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new NewCarActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void fetchAllMainCategories() {
        MaintenanceService maintenanceService = ApiUtils.getMaintenanceService();
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        Call<List<Maintenance>> call = maintenanceService.getAllMaintenances(user.getToken());
        call.enqueue(new Callback<List<Maintenance>>() {
            @Override
            public void onResponse(Call<List<Maintenance>> call, Response<List<Maintenance>> response) {
                if (response.code() == 200) {
                    // successfully fetch. get category list from response
                    List<Maintenance> maintenance = response.body();
                    // prepare and set adapter to spinner
                    ArrayAdapter<Maintenance> adapter = new ArrayAdapter<Maintenance>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, maintenance);
                    spCategory.setAdapter(adapter);
                }
                else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Maintenance>> call, Throwable throwable) {
                Log.e("MyApp:", throwable.getMessage());
            }
        });
    }

    /**
     * Called when pick date button is clicked. Display a date picker dialog
     * @param v
     */


    /**
     * Called when Add Book button is clicked
     * @param v
     */
    public void addNewCar(View v) {
        // get values in form
        String model = txtModel.getText().toString();
        String availabilityStatus = txtAStatus.getText().toString();
        String remarks = txtRemarks.getText().toString();
        String brand = txtBrand.getText().toString();
        String plateNumber = txtPlateNumber.getText().toString();
        int maintenance_ID = ((Maintenance)spCategory.getSelectedItem()).getMaintenanceID();

        // convert createdAt date to format in DB
        // reference: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String created_at = sdf.format(createdAt);

        // set updated_at with the same value as created_at
        //String updated_at = created_at;

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // send request to add new book to the REST API
        CarService carService = ApiUtils.getCarService();
        Call<Car> call = carService.addCar(user.getToken(), model, availabilityStatus, remarks,brand,plateNumber, created_at,maintenance_ID);

        // execute
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 201) {
                    // book added successfully
                    Car addedCar = response.body();
                    // display message
                    Toast.makeText(getApplicationContext(),
                            addedCar.getModel() + " added successfully.",
                            Toast.LENGTH_LONG).show();

                    // end this activity and forward user to BookListActivity
                    Intent intent = new Intent(getApplicationContext(), CarListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error [" + t.getMessage() + "]",
                        Toast.LENGTH_LONG).show();
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }


    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
