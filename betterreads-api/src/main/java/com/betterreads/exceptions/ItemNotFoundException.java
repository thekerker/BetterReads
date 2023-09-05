package com.betterreads.exceptions;

/**
 * <p>
 * Custom exception for when an item is not found in the repository.
 * </p>
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String id) {
        super("Could not find item with id " + id);
    }
}
