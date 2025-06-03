package com.borisgd.http;

import com.borisgd.domain.Review;
import com.borisgd.domain.ReviewResponse;
import com.borisgd.domain.Topic;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;

import java.io.IOException;
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
    private static final String lessThenMark = "<";
    private static final String greaterThenMark = ">";
    private static final String authorsMark = "authors\"> <div>";
    private static final String closingDivMark = "</div>";
    private static final String dateMark = "date\"> <div>";

    public void parseNextLink(String allReviewLine, ReviewResponse reviewResponse) {
        int indexOfNextMark = allReviewLine.indexOf(nextMark);
        int indexOfEndOfLink = allReviewLine.indexOf(endOfNextLinkMark);
        String nextUrl = allReviewLine.substring(indexOfNextMark + nextMark.length(), indexOfEndOfLink);
        reviewResponse.setNextUrl(nextUrl);
        //System.out.println("next url: " + nextUrl);
    }

    private void parseTheRest(String s, Review review) {
        int indexOfGreaterThen = s.indexOf(greaterThenMark);
        int indexOfLessThen = s.indexOf(lessThenMark);
        review.setTitle(s.substring(indexOfGreaterThen + 1, indexOfLessThen));
        int indexOfAuthors = s.indexOf(authorsMark);
        String work = s.substring(indexOfAuthors + authorsMark.length());
        int closingDivIndex = work.indexOf(closingDivMark);
        review.setAuthor(work.substring(0, closingDivIndex).replaceAll("<[^>]*>", ""));
        int indexOfDate = work.indexOf(dateMark);
        closingDivIndex = work.indexOf(closingDivMark, indexOfDate + dateMark.length() - 1);
        review.setDate(work.substring(indexOfDate + dateMark.length(), closingDivIndex).trim());
    }

    public void parseReviews(String allReviewLine, String topicName, ReviewResponse reviewResponse) {
        //System.out.println("line with reviews [" + allReviewLine + "]");
        int count = 0;
        int reviewMarkIndex = allReviewLine.indexOf(reviewMark);
        int tmp = 0;
        while (reviewMarkIndex >= 0) {
            Review review = new Review();
            review.setTopicName(topicName);
            int indexOfQuote = allReviewLine.indexOf("\"", reviewMarkIndex + reviewMark.length());
            String url = allReviewLine.substring(reviewMarkIndex + reviewMark.length(), indexOfQuote);
            review.setUrl(url);
            allReviewLine = allReviewLine.substring(reviewMarkIndex + 1);
            parseTheRest(allReviewLine, review);
            reviewMarkIndex = allReviewLine.indexOf(reviewMark);
            count++;
            reviewResponse.getReviews().add(review);
        }
        //System.out.println("Counted " + count + " reviews");
    }

    public ReviewResponse readNext(Topic topic, ReviewResponse reviewResponse, CloseableHttpClient httpClient) {
        String topicUrl = topic.getUrl();
        //System.out.println("Getting reviews for " + topicUrl);
        try {
            final HttpGet httpGet = new HttpGet(topicUrl);
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";
            httpGet.addHeader("User-Agent", userAgent);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                Scanner scanner = new Scanner(response.getEntity().getContent());
                reviewResponse.setNextUrl(null);
                while (scanner.hasNext()) {
                    String s = scanner.nextLine();
                    if(s.contains(reviewMark)) {
                        parseReviews(s, topic.getName(), reviewResponse);
                    }
                    if(s.contains(nextMark)) {
                        parseNextLink(s, reviewResponse);
                    }
                }
                return reviewResponse;
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
            Scanner scanner = new Scanner(response.getEntity().getContent());
            int i = 0;
            boolean topicFound = false;
            int firstQuoteIndex;
            int secondQuoteIndex;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.contains(topicMark)) {
                    topicFound = true;
                    i++;
                }
                if (topicFound) {
                    Topic topic = new Topic();
                    scanner.nextLine(); //skipping empty line
                    line = scanner.nextLine();
                    firstQuoteIndex = line.indexOf("\"");
                    secondQuoteIndex = line.indexOf("\"", firstQuoteIndex + 1);
                    String theUrl = line.substring(firstQuoteIndex + 1, secondQuoteIndex);
                    topic.setUrl(theUrl);
                    int indexOfTopicName = line.indexOf(topicNameMark);
                    int indexOfClosingButtonTag = line.indexOf("<", indexOfTopicName + 1);
                    String topicName = line.substring(indexOfTopicName + topicNameMark.length(), indexOfClosingButtonTag);
                    topic.setName(topicName);
                    topics.add(topic);
                    topicFound = false;
                }
            }
            return topics;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topics;
    }
}
