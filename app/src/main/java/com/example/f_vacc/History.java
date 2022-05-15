package com.example.f_vacc;

public class History {

    String establishmentName;
    String timeIn;
    String timeOut;
    String id;

    public History(String establishmentName, String timeIn, String timeOut, String id) {
        this.establishmentName = establishmentName;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.id = id;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public String getId() {
        return id;
    }
}
