package com.betterreads.models;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Search {
    @NotBlank(message = "Must supply a search term")
    private String searchTerm;

    private Map<Object, Object> filters;
}
