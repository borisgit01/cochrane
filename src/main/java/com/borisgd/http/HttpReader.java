package com.borisgd.http;

import com.borisgd.domain.Review;
import com.borisgd.domain.ReviewResponse;
import com.borisgd.domain.Topic;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
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
    private static final String nextMark = "div class=\"pagination-next-link\"> <a href=\"";
    private static final String endOfNextLinkMark = "\">Next";
    private String JSESSIONID = null;
    private static final String JSESSIONIDMark = "JSESSIONID=";


    public ReviewResponse readNext(Topic topic, ReviewResponse reviewResponse, CloseableHttpClient httpClient) {
        String topicUrl = topic.getUrl();
        System.out.println("Getting reviews for " + topicUrl);
        try {
            final HttpGet httpGet = new HttpGet(topicUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            //httpGet.addHeader("JSESSIONID", JSESSIONID);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                Scanner scanner = new Scanner(response.getEntity().getContent());
                while (scanner.hasNext()) {
                    String s = scanner.nextLine();
                    System.out.println(s);
                    //if(s.contains(reviewMark)) {
                    //    parseReviews(s);
                    //}
                    //if(s.contains(nextMark)) {
                    //    System.out.println("have next link");
                    //    parseNextLink(s, reviewResponse);
                    //}
                }
                reviewResponse.setNextUrl(null);
                return reviewResponse;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReviewResponse readNext(Topic topic, ReviewResponse reviewResponse) {
        String topicUrl = topic.getUrl();
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
            //httpGet.addHeader("JSESSIONID", JSESSIONID);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                Scanner scanner = new Scanner(response.getEntity().getContent());
                while (scanner.hasNext()) {
                    String s = scanner.nextLine();
                    System.out.println(s);
                    //if(s.contains(reviewMark)) {
                    //    parseReviews(s);
                    //}
                    //if(s.contains(nextMark)) {
                    //    System.out.println("have next link");
                    //    parseNextLink(s, reviewResponse);
                    //}
                }
                reviewResponse.setNextUrl(null);
                return reviewResponse;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseNextLink(String allReviewLine, ReviewResponse reviewResponse) {
        System.out.println("parseNextLink: allReviesLine length = " + allReviewLine.length());
        int indexOfNextMark = allReviewLine.indexOf(nextMark);
        int indexOfEndOfLink = allReviewLine.indexOf(endOfNextLinkMark);
        String nextUrl = allReviewLine.substring(indexOfNextMark + nextMark.length(), indexOfEndOfLink);
        reviewResponse.setNextUrl(nextUrl);
        System.out.println("next url: " + nextUrl);
    }

    public void parseReviews(String allReviewLine) {
        System.out.println("parseReviews: allReviesLine length = " + allReviewLine.length());
        int count = 0;
        int reviewMarkIndex = allReviewLine.indexOf(reviewMark);
        while (reviewMarkIndex >= 0) {
            //System.out.println(reviewMarkIndex);
            int indexOfQuote = allReviewLine.indexOf("\"", reviewMarkIndex + reviewMark.length());
            String url = allReviewLine.substring(reviewMarkIndex + reviewMark.length(), indexOfQuote);
            //System.out.println(url);
            allReviewLine = allReviewLine.substring(reviewMarkIndex + 1);
            reviewMarkIndex = allReviewLine.indexOf(reviewMark);
            count++;
        }
        System.out.println("Counted " + count + " reviews");
    }

    public ReviewResponse readReviews(Topic topic, ReviewResponse reviewResponse) {
        String topicUrl = topic.getUrl();
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
                    }
                    if(s.contains(nextMark)) {
                        System.out.println("have next link");
                        parseNextLink(s, reviewResponse);
                    }
                }
                return reviewResponse;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReviewResponse readReviews(Topic topic, ReviewResponse reviewResponse, CloseableHttpClient httpClient) {
        String topicUrl = topic.getUrl();
        System.out.println("Getting reviews for " + topicUrl);
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
                }
                if(s.contains(nextMark)) {
                    System.out.println("have next link");
                    parseNextLink(s, reviewResponse);
                }
            }
            return reviewResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Topic> readTopUrl() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet(topUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                Header[] allHeaders = response.getAllHeaders();
                for (Header h : allHeaders) {
                    //System.out.println("header name: " + h.getName() + " value: " + h.getValue());
                    if (h.getValue().startsWith(JSESSIONIDMark)) {
                        JSESSIONID = h.getValue().substring(JSESSIONIDMark.length());
                        System.out.println("session id is " + JSESSIONID);
                    }
                }
                //StatusLine statusLine = response.getStatusLine();
                //System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                Scanner scanner = new Scanner(response.getEntity().getContent());
                int i = 0;
                boolean topicFound = false;
                int firstQuoteIndex;
                int secondQuoteIndex;
                List<Topic> topics = new ArrayList<>();
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    if (line.contains(topicMark)) {
                        topicFound = true;
                        //System.out.println(s);
                        i++;
                    }
                    if (topicFound) {
                        Topic topic = new Topic();
                        scanner.nextLine(); //skipping empty line
                        line = scanner.nextLine();
                        //System.out.println(line);
                        firstQuoteIndex = line.indexOf("\"");
                        secondQuoteIndex = line.indexOf("\"", firstQuoteIndex + 1);
                        //System.out.println("first quote: " + firstQuoteIndex + ", second quote: " + secondQuoteIndex);
                        String theUrl = line.substring(firstQuoteIndex + 1, secondQuoteIndex);
                        //System.out.println(theUrl);
                        topic.setUrl(theUrl);
                        int indexOfTopicName = line.indexOf(topicNameMark);
                        int indexOfClosingButtonTag = line.indexOf("<", indexOfTopicName + 1);
                        String topicName = line.substring(indexOfTopicName + topicNameMark.length(), indexOfClosingButtonTag);
                        //System.out.println("topic name " + topicName);
                        topic.setName(topicName);
                        topics.add(topic);
                        topicFound = false;
                    }
                }
                //System.out.println("Counted " + i + " topics");
                return topics;
                //String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                //System.out.println("Response body: " + responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Topic> readTopUrl(CloseableHttpClient httpClient) {
        List<Topic> topics = new ArrayList<>();
        final HttpGet httpGet = new HttpGet(topUrl);
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
        httpGet.addHeader("User-Agent", userAgent);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            Header[] allHeaders = response.getAllHeaders();
            for (Header h : allHeaders) {
                //System.out.println("header name: " + h.getName() + " value: " + h.getValue());
                if (h.getValue().startsWith(JSESSIONIDMark)) {
                    JSESSIONID = h.getValue().substring(JSESSIONIDMark.length());
                    System.out.println("session id is " + JSESSIONID);
                }
            }
            //StatusLine statusLine = response.getStatusLine();
            //System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
            Scanner scanner = new Scanner(response.getEntity().getContent());
            int i = 0;
            boolean topicFound = false;
            int firstQuoteIndex;
            int secondQuoteIndex;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains(topicMark)) {
                    topicFound = true;
                    //System.out.println(s);
                    i++;
                }
                if (topicFound) {
                    Topic topic = new Topic();
                    scanner.nextLine(); //skipping empty line
                    line = scanner.nextLine();
                    //System.out.println(line);
                    firstQuoteIndex = line.indexOf("\"");
                    secondQuoteIndex = line.indexOf("\"", firstQuoteIndex + 1);
                    //System.out.println("first quote: " + firstQuoteIndex + ", second quote: " + secondQuoteIndex);
                    String theUrl = line.substring(firstQuoteIndex + 1, secondQuoteIndex);
                    //System.out.println(theUrl);
                    topic.setUrl(theUrl);
                    int indexOfTopicName = line.indexOf(topicNameMark);
                    int indexOfClosingButtonTag = line.indexOf("<", indexOfTopicName + 1);
                    String topicName = line.substring(indexOfTopicName + topicNameMark.length(), indexOfClosingButtonTag);
                    //System.out.println("topic name " + topicName);
                    topic.setName(topicName);
                    topics.add(topic);
                    topicFound = false;
                }
            }
            //System.out.println("Counted " + i + " topics");
            return topics;
            //String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            //System.out.println("Response body: " + responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topics;
    }
}
