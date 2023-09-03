package com.betterreads.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * A document that represents a Publisher
 */
@Document(collection = "publishers")
@Data
@AllArgsConstructor
@Builder
public class Publisher {

    @Id
    private String id;

    private String name;

    @Override
    public String toString() {
        return "Publisher: [Name: " + this.name + "]";
    }
}
