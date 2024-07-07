package com.example.lab_rest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookingService;
import com.example.lab_rest.remote.CarService;
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

public class NewBookingActivity extends AppCompatActivity {

    private Spinner spCar;
    private TextView tvBookingDate;
    private EditText txtRemarks;
    private EditText txtStatus;
    private TextView tvCreatedAt;
    private Button btnAddBooking;

    private static Date createdAt;
    private User user;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        spCar = findViewById(R.id.spCar);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        txtRemarks = findViewById(R.id.txtRemarks);
        txtStatus = findViewById(R.id.txtStatus);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        btnAddBooking = findViewById(R.id.btnAddBooking);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        user = spm.getUser(); // Retrieve user instance from shared preferences
        if (user != null) {
            userId = user.getId(); // Ensure this method returns the correct user ID
            Log.d("NewBookingActivity", "User retrieved: " + user + ", UserId: " + userId);
        } else {
            Log.e("NewBookingActivity", "User not found in SharedPrefManager");
        }

        createdAt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        tvCreatedAt.setText(sdf.format(createdAt));

        fetchAllCarModel();

        tvBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvBookingDate);
            }
        });

        tvCreatedAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvCreatedAt);
            }
        });

        btnAddBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBooking(v);
            }
        });
    }

    private int getSelectedCarId() {
        return (int) spCar.getSelectedItemId(); // Adjust this based on your implementation
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment(tvBookingDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addNewBooking(View view) {
        int carId = getSelectedCarId();
        String bookingDate = tvBookingDate.getText().toString();
        String status = txtStatus.getText().toString();
        String remarks = txtRemarks.getText().toString();
        String createdAtStr = tvCreatedAt.getText().toString();

        Log.d("NewBookingActivity", "Data - UserId: " + userId + ", CarId: " + carId + ", BookingDate: " + bookingDate + ", Status: " + status + ", Remarks: " + remarks + ", CreatedAt: " + createdAtStr);

        if (user == null || user.getToken() == null) {
            Log.e("NewBookingActivity", "User or user token is null");
            return;
        }

        BookingService bookingService = ApiUtils.getBookingService();
        Call<Booking> call = bookingService.addBooking(
                user.getToken(),
                userId,
                carId,
                bookingDate,
                status,
                remarks,
                createdAtStr
        );

        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    Booking booking = response.body();
                    Log.d("NewBookingActivity", "Booking created: " + booking);
                } else {
                    Log.e("NewBookingActivity", "Request failed. Response code: " + response.code() + ", Response message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Log.e("NewBookingActivity", "Request failed. Error: " + t.getMessage());
            }
        });
    }

    private void fetchAllCarModel() {
        CarService carService = ApiUtils.getCarService();
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        Call<List<Car>> call = carService.getAllCars(user.getToken());
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    List<Car> carList = response.body();
                    ArrayAdapter<Car> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, carList);
                    spCar.setAdapter(adapter);
                } else if (response.code() == 401) {
                    Toast.makeText(NewBookingActivity.this, "Unauthorized access. Please login again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(NewBookingActivity.this, "Error fetching car models: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private TextView dateTextView;

        public DatePickerFragment(TextView dateTextView) {
            this.dateTextView = dateTextView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            createdAt = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            dateTextView.setText(sdf.format(createdAt));
        }
    }
}
