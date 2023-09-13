package com.betterreads.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * A document that represents a Publisher
 * </p>
 */
@Document(collection = "publishers")
@Data
@AllArgsConstructor
@Builder
public class Publisher {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @DocumentReference
    private List<Book> books;

    @Override
    public String toString() {
        return "Publisher: [Name: " + this.name + "]";
    }
}
