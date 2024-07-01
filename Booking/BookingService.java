package com.example.lab_rest.remote;

import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookingService {

    @GET("Bookings")
    Call<List<Booking>> getAllBookings(@Header("api-key") String apiKey);

    @GET("Bookings/{BookingID}")
    Call<Booking> getBooking(@Header("api-key") String apiKey, @Path("BookingID") int BookingId);

    @FormUrlEncoded
    @POST("Bookings")
    Call<Booking> addBooking(@Header("api-key") String apiKey,
                             @Field("UserId") int UserId,
                             @Field("CarId") int CarId,
                             @Field("BookingDate") String BookingDate,
                             @Field("Status") String Status,
                             @Field("Remarks") String Remarks,
                             @Field("AdminMsg") String AdminMsg,
                             @Field("CreatedAt") String CreatedAt);

    @DELETE("Bookings/{BookingID}")
    Call<DeleteResponse> deleteBooking(@Header("api-key") String apiKey, @Path("BookingID") int id);

    @FormUrlEncoded
    @POST("Bookings/{BookingID}")
    Call<Booking> updateBooking(@Header("api-key") String apiKey, @Path("BookingID") int id,
                                @Field("UserId") int UserId,
                                @Field("CarId") int CarId,
                                @Field("BookingDate") String BookingDate,
                                @Field("Status") String Status,
                                @Field("Remarks") String Remarks,
                                @Field("AdminMsg") String AdminMsg,
                                @Field("CreatedAt") String CreatedAt);
}
