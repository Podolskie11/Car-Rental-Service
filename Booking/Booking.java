package com.example.lab_rest.model;

import java.sql.Timestamp;

public class Booking {
    private int BookingID;
    private int UserId;
    private int CarId;
    private String BookingDate;
    private String Status;
    private String Remarks;
    private String AdminMsg;
    private String CreatedAt;

    public Booking() {
    }

    public Booking(int BookingID, int UserId, int CarId, String BookingDate, String Status, String Remarks, String AdminMsg, String CreatedAt) {
        this.BookingID = BookingID;
        this.UserId = UserId;
        this.CarId = CarId;
        this.BookingDate = BookingDate;
        this.Status = Status;
        this.Remarks = Remarks;
        this.AdminMsg = AdminMsg;
        this.CreatedAt = CreatedAt;
    }

    public int getBookingId() {
        return BookingID;
    }

    public void setBookingId(int BookingID) {
        this.BookingID = BookingID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getCarId() {
        return CarId;
    }

    public void setCarId(int CarId) {
        this.CarId = CarId;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String BookingDate) {
        this.BookingDate = BookingDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getAdminMsg() {
        return AdminMsg;
    }

    public void setAdminMsg(String AdminMsg) {
        this.AdminMsg = AdminMsg;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + BookingID +
                ", userId=" + UserId +
                ", carId=" + CarId +
                ", bookingDate='" + BookingDate + '\'' +
                ", status='" + Status + '\'' +
                ", remarks='" + Remarks + '\'' +
                ", adminMsg='" + AdminMsg + '\'' +
                ", createdAt='" + CreatedAt + '\'' +
                '}';
    }
}
