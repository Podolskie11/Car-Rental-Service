package com.example.lab_rest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;


import com.bumptech.glide.Glide;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.FileInfo;
import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;

import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class UpdateCarActivity extends AppCompatActivity {

    private EditText txtModel;
    private EditText txtRemarks;
    private EditText txtAStatus;
    private EditText txtBrand;

    private EditText txtPlateNumber;


    private static TextView tvCreatedAt; // static because need to be accessed by DatePickerFragment
    private static Date createdAt; // static because need to be accessed by DatePickerFragment
    private Spinner spCategory;
    private ArrayAdapter<Maintenance> adapter;
    private Car car;  // current car to be updated
    private Car updatedCar;

    // file upload feature
    private ImageView imgBookCover;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private Uri uri;


    /**
     * Date picker fragment class
     * Reference: https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            c.setTime(createdAt); // Use the current book created date as the default date in the picker
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tvCreatedAt.setText( sdf.format(createdAt) );
        }
    }

    /**
     * Called when pick date button is clicked. Display a date picker dialog
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new UpdateCarActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

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
        createdAt = new Date();

        // get references to the form fields in layout
        txtModel = findViewById(R.id.txtModel);
        txtAStatus = findViewById(R.id.txtAStatus);
        txtRemarks = findViewById(R.id.txtRemarks);
        txtBrand = findViewById(R.id.txtBrand);
        txtPlateNumber= findViewById(R.id.txtPlateNumber);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        spCategory = findViewById(R.id.spCategory);
        imgBookCover = findViewById(R.id.imgBookCover);

        // retrieve car info from database using the car id
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // fetch all book categories and set to spinner
        fetchAllMainCategories();

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
                    txtBrand.setText(car.getBrand());
                    txtPlateNumber.setText(car.getPlateNumber());
                    tvCreatedAt.setText(car.getCreatedAt());

                    // parse created_at date to date object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        // parse created date string to date object
                        createdAt = sdf.parse(car.getCreatedAt());
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }

                    // set spinner to select default category based on category of this book
                    for (int i=0; i<adapter.getCount(); i++)
                        if (adapter.getItem(i).getMaintenanceID() == car.getMaintenance_ID())
                            spCategory.setSelection(i);


                    // Use Glide to load the image into the ImageView
                    Glide.with(getApplicationContext())
                            .load("http://178.128.220.20/fkexample/api/" + car.getImage()) // Make sure your Book object has a method to get the image URL
                            .placeholder(R.drawable.default_cover) // Placeholder image if the URL is empty
                            .error(R.drawable.default_cover) // Error image if there is a problem loading the image
                            .into(imgBookCover);



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

    private void fetchAllMainCategories() {
        MaintenanceService catService = ApiUtils.getMaintenanceService();
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        Call<List<Maintenance>> call = catService.getAllMaintenances(user.getToken());
        call.enqueue(new Callback<List<Maintenance>>() {
            @Override
            public void onResponse(Call<List<Maintenance>> call, Response<List<Maintenance>> response) {
                if (response.code() == 200) {
                    // successfully fetch. get category list from response
                    List<Maintenance> maintenance = response.body();
                    // prepare and set adapter to spinner
                    adapter = new ArrayAdapter<Maintenance>(getApplicationContext(),
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
     * Update Book button action handler
     * Update book info in database when the user click Update Book button
     * @param view
     */
    public void updateCar(View view) {

        if (uri != null && !uri.getPath().isEmpty()) {
            // new image selected
            // Upload file first. if successful, call updateBookRecord in onResponse()
            uploadFile(uri);
        }
        else {
            // no new image selected
            // no need to upload file, update all other fields
            updateCarRecord();
        }

    }


    public void updateCarRecord() {
        // get values in form
        String model = txtModel.getText().toString();
        String availabilityStatus = txtAStatus.getText().toString();
        String remarks = txtRemarks.getText().toString();
        String brand = txtBrand.getText().toString();
        String plateNumber = txtPlateNumber.getText().toString();

        // convert createdAt date to format required by Database - yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String created_at = sdf.format(createdAt);

        // set updated_at to current date and time


        Log.d("MyApp:", "Old Car info: " + car.toString());

        // update the car object retrieved in when populating the form with the new data.
        // update all fields excluding the id
        car.setModel(model);
        car.setAvailabilityStatus(availabilityStatus);
        car.setRemarks(remarks);
        car.setBrand(brand);
        car.setPlateNumber(plateNumber);
        car.setCreatedAt(created_at);

        if (spCategory.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a maintenance category", Toast.LENGTH_SHORT).show();
            return;
        }

        int maintenance_ID = ((Maintenance) spCategory.getSelectedItem()).getMaintenanceID();
        car.setMaintenance_ID(maintenance_ID);


        Log.d("MyApp:", "New Car info: " + car.toString());

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // send request to update the car record to the REST API
        CarService carService = ApiUtils.getCarService();
        Call<Car> call = carService.updateCar(user.getToken(), car.getCarID(), car.getModel(),
                car.getAvailabilityStatus(), car.getRemarks(), car.getBrand(), car.getPlateNumber(),
                car.getCreatedAt(), car.getMaintenance_ID(), car.getImage());

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

    private void uploadFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            byte[] fileBytes = getBytesFromInputStream(inputStream);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), fileBytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", getFileName(uri), requestFile);

            // get token user info from shared preference in order to get token value
            SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
            User user = spm.getUser();

            CarService carService = ApiUtils.getCarService();
            Call<FileInfo> call = carService.uploadFile(user.getToken(), body);

            call.enqueue(new Callback<FileInfo>() {
                @Override
                public void onResponse(Call<FileInfo> call, Response<FileInfo> response) {
                    if (response.isSuccessful()) {
                        // file uploaded successfully
                        // Now add the book record with the uploaded file name
                        FileInfo fi = response.body();
                        String fileName = fi.getFile();

                        // update book object to newly uploaded image
                        car.setImage(fileName);

                        updateCarRecord();    // update book record with the new information
                    } else {
                        Toast.makeText(getApplicationContext(), "File upload failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<FileInfo> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error uploading file", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error preparing file for upload", Toast.LENGTH_LONG).show();
        }
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
        builder.setMessage(updatedCar.getModel() + " update successfully");
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

    /**
     * Get image bytes from local disk. Used to upload the image bytes to Rest API
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * Get image file name from Uri
     * @param uri
     * @return
     */
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * Button browse action handler
     * @param view
     */
    public void choosePhoto(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+, require READ_MEDIA_IMAGES permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_REQUEST_STORAGE);
            } else {
                openGallery();
            }
        } else {
            // For Android 12 and below, require READ_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_STORAGE);
            } else {
                openGallery();
            }
        }
    }

    /**
     * User clicked deny or allow in permission request dialog
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Open Image Picker Activity
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    /**
     * Callback for Activity, in this case will handle the result of Image Picker Activity
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            // image picker activity return a value
            if(data != null) {
                // get file uri
                uri = data.getData();
                // set image to imageview
                imgBookCover.setImageURI(uri);
            }
        }
    }

}
