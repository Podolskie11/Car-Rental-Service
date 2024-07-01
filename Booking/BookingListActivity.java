package com.example.lab_rest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.BookingAdapter;
import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookingService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListActivity extends AppCompatActivity {

    private BookingService bookingService;
    private RecyclerView rvBookingList;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        // Initialize RecyclerView
        rvBookingList = findViewById(R.id.rvBookingList);
        registerForContextMenu(rvBookingList);

        // Fetch and update booking list
        updateBookingList();
    }

    private void updateBookingList() {
        // Get user info from SharedPreferences to retrieve token value
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        // Get booking service instance
        bookingService = ApiUtils.getBookingService();

        // Execute API call to fetch bookings
        bookingService.getAllBookings(token).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful()) {
                    List<Booking> bookings = response.body();

                    // Initialize adapter with retrieved bookings
                    adapter = new BookingAdapter(getApplicationContext(), bookings);

                    // Set adapter to RecyclerView
                    rvBookingList.setAdapter(adapter);

                    // Set layout manager to RecyclerView
                    rvBookingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // Add divider between items in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvBookingList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvBookingList.addItemDecoration(dividerItemDecoration);
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                showErrorToast("Error connecting to the server");
                Log.e("BookingListActivity", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void handleErrorResponse(Response<List<Booking>> response) {
        if (response.code() == 401) {
            showErrorToast("Invalid session. Please login again");
            clearSessionAndRedirect();
        } else {
            showErrorToast("Error: " + response.message());
            Log.e("BookingListActivity", "Error: " + response.message());
        }
    }

    private void clearSessionAndRedirect() {
        // Clear SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // Finish current activity
        finish();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.booking_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Booking selectedBooking = adapter.getSelectedItem();
        Log.d("MyApp", "selected " + selectedBooking.toString());    // debug purpose

        if (item.getItemId() == R.id.menuDetails) {    // user clicked details contextual menu
            doViewDetails(selectedBooking);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void doViewDetails(Booking selectedBooking) {
        Log.d("MyApp:", "viewing details: " + selectedBooking.toString());
        // Forward user to BookingDetailsActivity, passing the selected booking id
        Intent intent = new Intent(this, BookingDetailsActivity.class);
        intent.putExtra("BookingId", selectedBooking.getBookingId());
        startActivity(intent);
    }

    private void showErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
