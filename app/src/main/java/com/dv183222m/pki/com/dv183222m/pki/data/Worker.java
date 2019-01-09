package com.dv183222m.pki.com.dv183222m.pki.data;

import com.dv183222m.pki.R;

import java.util.ArrayList;
import java.util.List;

public class Worker {
    private User user;
    private float rating;
    private int experience;
    private List<WorkerType> types;

    public Worker(User user) {
        this.user = user;
        this.rating = 0f;
        this.experience = 0;
        types = new ArrayList<>();
    }

    public User getUser() { return user; }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<WorkerType> getTypes() {
        return types;
    }

    public void setTypes(List<WorkerType> types) {
        this.types = types;
    }
}
