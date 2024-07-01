package com.example.lab_rest;

import static com.example.lab_rest.R.id.txtRemarks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;

import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.sharedpref.SharedPrefManager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class UpdateCarActivity extends AppCompatActivity {

    private EditText txtModel;
    private EditText txtRemarks;
    private EditText txtAStatus;

    private Car car;  // current car to be updated
    private Car updatedCar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // retrieve car id from intent
        // get car id sent by BookListActivity, -1 if not found
        Intent intent = getIntent();
        int carId = intent.getIntExtra("car_id", -1);

        // initialize createdAt to today's date
        //createdAt = new Date();

        // get references to the form fields in layout
        txtModel = findViewById(R.id.txtModel);
        txtAStatus = findViewById(R.id.txtAStatus);
        txtRemarks = findViewById(R.id.txtRemarks);

        // retrieve car info from database using the car id
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // get car service instance
        CarService carService = ApiUtils.getCarService();

        // execute the API query. send the token and car id
        carService.getCar(user.getToken(), carId).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                // for debug purpose
                Log.d("MyApp:", "Update Form Populate Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success
                    // get car object from response
                    car = response.body();

                    // set values into forms
                    txtModel.setText(car.getModel());
                    txtAStatus.setText(car.getAvailabilityStatus());
                    txtRemarks.setText(car.getRemarks());


                    // parse created_at date to date object

                }
                else if (response.code() == 401) {
                    // unauthorized error. invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    // server return other error
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Update car info in database when the user click Update Book button
     * @param view
     */
    public void updateCar(View view) {
        // get values in form
        String model = txtModel.getText().toString();
        String availabilityStatus = txtAStatus.getText().toString();
        String remarks = txtRemarks.getText().toString();

        // convert createdAt date to format required by Database - yyyy-MM-dd HH:mm:ss


        // set updated_at to current date and time


        Log.d("MyApp:", "Old Book info: " + car.toString());

        // update the car object retrieved in when populating the form with the new data.
        // update all fields excluding the id
        car.setModel(model);
        car.setAvailabilityStatus(availabilityStatus);
        car.setRemarks(remarks);


        Log.d("MyApp:", "New Book info: " + car.toString());

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // send request to update the car record to the REST API
        CarService carService = ApiUtils.getCarService();
        Call<Car> call = carService.updateCar(user.getToken(), car.getCarID(), car.getModel(),
                car.getAvailabilityStatus(), car.getRemarks());

        // execute
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {

                // for debug purpose
                Log.d("MyApp:", "Update Request Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success code for update request
                    // get updated car object from response
                    updatedCar = response.body();
                    // display message
                    showAlertDialog();
                   /* Toast.makeText(getApplicationContext(),
                            updatedBook.getName() + " updated successfully.",
                            Toast.LENGTH_LONG).show();*/

                    // end this activity and forward user to BookListActivity
                    /*Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                    startActivity(intent);
                    finish();*/
                }
                else if (response.code() == 401) {
                    // unauthorized error. invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    // server return other error
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }

            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
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

    /**
     * Displaying an alert dialog with a single button
     //* @param message - message to be displayed
     */

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(updatedCar.getModel() + "update successfully");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //do things
                Intent intent = new Intent(getApplicationContext(), CarListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}