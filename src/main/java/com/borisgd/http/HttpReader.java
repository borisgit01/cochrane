package com.borisgd.http;

import com.borisgd.domain.Review;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HttpReader {

    private static final String topUrl = "https://www.cochranelibrary.com/cdsr/reviews/topics";
    private static final String topicMark = "li class=\"browse-by-list-item\"";
    private static final String reviewMark = "a target=\"_blank\" href=\"";
    private static final String topicNameMark = "<button class=\"btn-link browse-by-list-item-link\">";

    public void parseReviews(String allReviewLine) {
        int count = 0;
        int reviewMarkIndex = allReviewLine.indexOf(reviewMark);
        while (reviewMarkIndex >= 0) {
            System.out.println(reviewMarkIndex);
            int indexOfQuote = allReviewLine.indexOf("\"", reviewMarkIndex + reviewMark.length());
            String url = allReviewLine.substring(reviewMarkIndex + reviewMark.length(), indexOfQuote);
            System.out.println(url);
            allReviewLine = allReviewLine.substring(reviewMarkIndex + 1);
            reviewMarkIndex = allReviewLine.indexOf(reviewMark);
            count++;
        }
        System.out.println("Counted " + count + " reviews");
    }

    public void readReviews(String topicUrl) {
        System.out.println("Getting reviews for " + topicUrl);
        RequestConfig requestConfig = RequestConfig.custom()
                .setCircularRedirectsAllowed(true)
                .build();
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build()) {
            final HttpGet httpGet = new HttpGet(topicUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                Scanner scanner = new Scanner(response.getEntity().getContent());
                while (scanner.hasNext()) {
                    String s = scanner.nextLine();
                    if(s.contains(reviewMark)) {
                        parseReviews(s);
                        break;
                    }
                }
                //String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                //System.out.println("Response body: " + responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readReviews1(String topicUrl) {
        System.out.println("Getting reviews for " + topicUrl);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(topicUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                Scanner scanner = new Scanner(response.getEntity().getContent());
                while (scanner.hasNext()) {
                    System.out.println(scanner.nextLine());
                }
                //String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                //System.out.println("Response body: " + responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getContent(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(topUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                return response.getEntity().getContent();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Review> readTopUrl() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(topUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                //System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                Scanner scanner = new Scanner(response.getEntity().getContent());
                int i = 0;
                boolean topicFound = false;
                int firstQuoteIndex;
                int secondQuoteIndex;
                List<Review> revies = new ArrayList<>();
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if(line.contains(topicMark)) {
                        topicFound = true;
                        //System.out.println(s);
                        i++;
                    }
                    if(topicFound) {
                        Review review = new Review();
                        scanner.nextLine(); //skipping empty line
                        line = scanner.nextLine();
                        System.out.println(line);
                        firstQuoteIndex = line.indexOf("\"");
                        secondQuoteIndex = line.indexOf("\"", firstQuoteIndex + 1);
                        //System.out.println("first quote: " + firstQuoteIndex + ", second quote: " + secondQuoteIndex);
                        String theUrl = line.substring(firstQuoteIndex + 1, secondQuoteIndex);
                        //System.out.println(theUrl);
                        review.setUrl(theUrl);
                        int indexOfTopicName = line.indexOf(topicNameMark);
                        int indexOfClosingButtonTag = line.indexOf("<", indexOfTopicName + 1);
                        String topicName = line.substring(indexOfTopicName + topicNameMark.length(), indexOfClosingButtonTag);
                        System.out.println("topic name " + topicName);
                        revies.add(review);
                        topicFound = false;
                    }
                }
                //System.out.println("Counted " + i + " topics");
                return revies;
                //String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                //System.out.println("Response body: " + responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
