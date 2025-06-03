package com.borisgd.domain;

import java.util.ArrayList;
import java.util.List;

public class ReviewResponse {

    private List<Review> reviews;
    private String nextUrl;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
