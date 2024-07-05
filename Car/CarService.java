package com.example.lab_rest.remote;

import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.Car;
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

public interface CarService {
    @GET("car")
    Call<List<Car>> getAllCars(@Header("api-key") String api_key);

   @GET("car/{carID}")
    Call<Car> getCar(@Header("api-key") String api_key, @Path("carID") int carID);

    @FormUrlEncoded
    @POST("car")
    Call<Car> addCar(@Header ("api-key") String apiKey, @Field("model") String model,
                     @Field("availabilityStatus") String availiabiltyStatus,
                     @Field("remarks") String remarks);
    @DELETE("car/{carID}")
    Call<DeleteResponse> deleteCar(@Header ("api-key") String apiKey, @Path("carID") int carID);

    @FormUrlEncoded
    @POST("car/{carID}")
    Call<Car> updateCar(@Header ("api-key") String apiKey, @Path("carID") int carID,
                          @Field("model") String model, @Field("availabilityStatus") String availiabiltyStatus,
                          @Field("remarks") String remarks
                          );

}