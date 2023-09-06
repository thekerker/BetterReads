package com.betterreads.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * A document that represents a Book
 * </p>
 */
@Document(collection = "books")
@Data
@AllArgsConstructor
@Builder
public class Book {
    @Id
    private String id;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    public String title;

    @DBRef(db = "authors")
    @NotEmpty(message = "At least 1 Author is required")
    private List<Author> authors;

    private Date publishedDate;

    private List<String> genres;

    private int pages;

    @DBRef(db = "publishers")
    private Publisher publisher;

    private String language;

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String pubDate = this.publishedDate != null ? df.format(this.publishedDate) : StringUtils.EMPTY;

        String formattedAuthors = authors.stream()
                .map(a -> a.getName().toString())
                .collect(Collectors.joining("; "));

        return "Book: [Id:" + this.id + ", ISBN: " + this.isbn + ", Title: " + this.title +
                ", Author(s): " + formattedAuthors + ", Date Published: " + pubDate +
                ", Number of Pages: " + this.pages + ", Genres: " + String.join(",", genres) +
                ", Publisher: " + this.publisher.getName() + ", Language: " + language + "]";
    }
}
