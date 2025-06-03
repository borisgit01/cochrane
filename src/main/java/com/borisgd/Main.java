package com.borisgd;

import com.borisgd.domain.Review;
import com.borisgd.domain.ReviewResponse;
import com.borisgd.domain.Topic;
import com.borisgd.files.PipeDelimitedFileWriter;
import com.borisgd.http.HttpReader;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        HttpReader httpReader = new HttpReader();
        CloseableHttpClient httpClient = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setCircularRedirectsAllowed(true)
                    .build();
            //httpClient = HttpClients.createDefault();

            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();

            List<Topic> topics = httpReader.readTopUrl(httpClient);
            System.out.println("main: have " + topics.size() + " topics");
            System.out.println("first topic url " + topics.get(0).getUrl());
            System.out.println("first topic name " + topics.get(0).getName());
            ReviewResponse response = new ReviewResponse();
            response.setReviews(new ArrayList<>());
            response.setNextUrl(null);
            response = httpReader.readNext(topics.get(0), response, httpClient);
            while(response.getNextUrl() != null) {
                topics.get(0).setUrl(response.getNextUrl());
                response = httpReader.readNext(topics.get(0), response, httpClient);
            }
            System.out.println("main: have " + response.getReviews().size() + " reviews");
            PipeDelimitedFileWriter.write(response.getReviews(), "cochrane_reviews.txt", "https://www.cochranelibrary.com");
            //for(Review r : response.getReviews()) {
            //    System.out.println(r);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(httpClient != null) {
                try {
                    httpClient.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
