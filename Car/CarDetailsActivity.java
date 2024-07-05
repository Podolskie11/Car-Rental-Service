package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarDetailsActivity extends AppCompatActivity {

    private CarService carService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // retrieve car details based on selected id

        // get car id sent by CarListActivity, -1 if not found
        Intent intent = getIntent();
        int carId = intent.getIntExtra("car_id", -1);

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        // get car service instance
        carService = ApiUtils.getCarService();

        // execute the API query. send the token and car id
        carService.getCar(token, carId).enqueue(new Callback<Car>() {

            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success

                    // get car object from response
                    Car car = response.body();

                    // get references to the view elements
                    TextView tvModel = findViewById(R.id.tvModel);
                    TextView tvAStatus = findViewById(R.id.tvAStatus);
                    TextView tvRemark = findViewById(R.id.tvRemark);
                    TextView tvBrand = findViewById(R.id.tvBrand);
                    TextView tvPlateNumber = findViewById(R.id.tvPlateNumber);

                    // set values
                    tvModel.setText(car.getModel());
                    tvAStatus.setText(car.getAvailabilityStatus());
                    tvRemark.setText(car.getRemarks());
                    tvBrand.setText(car.getBrand());
                    tvPlateNumber.setText(car.getPlateNumber());

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

    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this activity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
