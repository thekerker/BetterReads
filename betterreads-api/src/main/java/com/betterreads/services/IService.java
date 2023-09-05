package com.betterreads.services;

import java.util.List;

import org.springframework.hateoas.EntityModel;

/**
 * <p>
 * Interface for services that provide basic CRUD operations
 * </p>
 */
public interface IService {

    /**
     * <p>
     * Gets all documenents
     * </p>
     * 
     * @return The documents
     */
    List<EntityModel<?>> getAll();

    /**
     * <p>
     * Gets document by id
     * </p>
     * 
     * @param id the document's id in the database
     * @return List of documents
     */
    EntityModel<?> getById(String id);

    /**
     * <p>
     * Searches for an document with the given parameters
     * <p>
     * 
     * @param request The search request
     * @return List of documents
     */
    List<EntityModel<?>> search(Object request);

    /**
     * <p>
     * Creates a document
     * </p>
     * 
     * @param entity the document to create
     * @return The created document
     */
    EntityModel<?> add(Object entity);

    /**
     * <p>
     * Updates a document
     * </p>
     * 
     * @param id     the id of the document to update
     * @param entity the updated document
     * @return the updated document
     */
    EntityModel<?> update(String id, Object entity);

    /**
     * <p>
     * Deletes a document by id
     * </p>
     * 
     * @param id the id of the document to delete
     */
    void delete(String id);

    /**
     * <p>
     * Deletes all documents
     * </p>
     */
    void deleteAll();
}
