package com.betterreads.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * A document that represents a Book
 */
@Document(collection = "books")
@Data
@AllArgsConstructor
@Builder
public class Book {
    @Id
    public String id;

    public String isbn;

    public String title;

    public Author[] authors;

    public Date publishedDate;

    public String[] genres;

    public int pages;

    public Publisher publisher;

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String pubDate = this.publishedDate != null ? df.format(this.publishedDate) : StringUtils.EMPTY;

        String formattedAuthors = Arrays.stream(authors)
                .map(a -> a.getName().toString())
                .collect(Collectors.joining("; "));

        return "Book: [Id:" + this.id + ", ISBN: " + this.isbn + ", Title: " + this.title +
                ", Author(s): " + formattedAuthors + ", Date Published: " + pubDate +
                ", Number of Pages: " + this.pages + ", Genres: " + String.join(",", genres) +
                ", Publisher: " + this.publisher.getName() + "]";
    }
}
