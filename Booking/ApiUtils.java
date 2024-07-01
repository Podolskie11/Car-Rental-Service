package com.example.lab_rest.remote;

public class ApiUtils {
    // REST API server URL
    public static final String BASE_URL = "http://178.128.220.20/2023189557/api/";

    //
    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    // return BookService instance
    public static BookService getBookService() {
        return RetrofitClient.getClient(BASE_URL).create(BookService.class);
    }
    public static BookingService getBookingService() {
        return RetrofitClient.getClient(BASE_URL).create(BookingService.class);
    }
}

