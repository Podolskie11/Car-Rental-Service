package com.example.lab_rest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import androidx.loader.content.CursorLoader;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.FileInfo;
import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    private ImageView imgBookCover;

    // file upload feature
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
        imgBookCover = findViewById(R.id.imgBookCover);

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
     * Called when Add Book button is clicked
     * @param v
     */
    public void addNewCar(View v) {

        if (uri != null && !uri.getPath().isEmpty()) {
            // Upload file first
            uploadFile(uri);
        } else {
            // No file to upload, proceed with adding book record with default image
            addCarRecord("3-default.png");
        }

    }

    /**
     * Upload selected image to server through REST API
     * @param fileUri   full path of the file
     */
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
                        String image = fi.getFile();
                        addCarRecord(image);
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


    /**
     * Called when Add Book button is clicked
     * @param image  image file name
     */
    public void addCarRecord(String image) {
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
        Call<Car> call = carService.addCar(user.getToken(), model, availabilityStatus, remarks,brand,plateNumber, created_at,
                maintenance_ID, image);

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





    /**
     * Get path from Uri object returned by gallery / image picker
     * @param contentUri
     * @return
     */
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
}
