package com.example.lab_rest.model;

public class Car {
    private int carID;

    private String model;

    private String availabilityStatus;
    private String remarks;

    public Car() {
    }

    public Car(int carID, String model, String availabilityStatus, String remarks) {
        this.carID = carID;
        this.model = model;
        this.availabilityStatus = availabilityStatus;
        this.remarks = remarks;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carID=" + carID +
                ", model='" + model + '\'' +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
