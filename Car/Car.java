package com.example.lab_rest.model;

public class Car {
    private int carID;
    private int maintenance_ID;

    private Maintenance maintenance;

    private String model;

    private String brand;

    private String plateNumber;

    private String availabilityStatus;
    private String remarks;
    private String createdAt;
    private String image;

    public Car() {
    }

    public Car(int carID, int maintenance_ID, Maintenance maintenance, String model, String brand, String plateNumber, String availabilityStatus, String remarks, String createdAt, String image) {
        this.carID = carID;
        this.maintenance_ID = maintenance_ID;
        this.maintenance = maintenance;
        this.model = model;
        this.brand = brand;
        this.plateNumber = plateNumber;
        this.availabilityStatus = availabilityStatus;
        this.remarks = remarks;
        this.createdAt = createdAt;
        this.image = image;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getMaintenance_ID() {
        return maintenance_ID;
    }

    public void setMaintenance_ID(int maintenance_ID) {
        this.maintenance_ID = maintenance_ID;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carID=" + carID +
                ", maintenance_ID=" + maintenance_ID +
                ", maintenance=" + maintenance +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
