package com.borisgd.files;

import com.borisgd.domain.Review;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PipeDelimitedFileWriter {

    public static void write(List<Review> reviews, String filePath, String urlPrefix) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            for(Review r: reviews) {
                writer.write(urlPrefix);
                writer.write(r.getUrl());
                writer.write("|");
                writer.write(r.getTopicName());
                writer.write("|");
                writer.write(r.getTitle());
                writer.write("|");
                writer.write(r.getAuthor());
                writer.write("|");
                writer.write(r.getDate());
                writer.newLine();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
    }
}
