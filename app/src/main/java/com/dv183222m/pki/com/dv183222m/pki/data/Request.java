package com.dv183222m.pki.com.dv183222m.pki.data;

import java.util.Date;

public class Request {

    private static int nextId = 0;

    private int id = ++nextId;
    private User client;
    private User worker;
    private Date from;
    private Date to;
    private RequestStatus status;
    private WorkerType type;
    private int price;
    private boolean creditCard;
    private String details;
    private String review;
    private String municipality;
    private String address;
    private float rating;

    public Request(User client, User worker, String municipality, String address, Date from, Date to, WorkerType type, boolean creditCard, int price, String details) {
        this.client = client;
        this.worker = worker;
        this.from = from;
        this.to = to;
        this.type = type;
        this.price = price;
        this.details = details;
        this.creditCard = creditCard;
        this.municipality = municipality;
        this.address = address;

        this.status = RequestStatus.New;
        this.rating = 0f;
        this.review = "";
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public int getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getType() {
        return type.getWorkType();
    }

    public void setType(WorkerType type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
