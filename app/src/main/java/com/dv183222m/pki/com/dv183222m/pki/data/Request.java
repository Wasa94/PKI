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
    private String details;
    private int rating;
    private String review;

    public Request(User client, User worker, Date from, Date to, WorkerType type, int price, String details) {
        this.client = client;
        this.worker = worker;
        this.from = from;
        this.to = to;
        this.type = type;
        this.price = price;
        this.details = details;

        this.status = RequestStatus.New;
        this.rating = 0;
        this.review = "";
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
