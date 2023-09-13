package com.betterreads.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.betterreads.assemblers.PublishersAssembler;
import com.betterreads.exceptions.ItemNotFoundException;
import com.betterreads.repositories.PublishersRepository;
import com.betterreads.models.Publisher;
import com.betterreads.services.IService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Service for Publishers CRUD operations
 * </p>
 */
@Service
@Slf4j
public class PublishersService implements IService {

    @Autowired
    private PublishersRepository repository;

    @Autowired
    private PublishersAssembler assembler;

    /**
     * <p>
     * Gets all publishers
     * </p>
     * 
     * @return The publishers
     */
    @Override
    public List<EntityModel<?>> getAll() {
        return repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
    }

    /**
     * <p>
     * Gets publisher by id
     * </p>
     * 
     * @param id the publishers's id in the database
     * @return The publisher
     */
    @Override
    public EntityModel<?> getById(String id) {
        Publisher publisher = repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));

        return assembler.toModel(publisher);
    }

    /**
     * <p>
     * Searches for a publisher with the given parameters
     * <p>
     * 
     * @param request The search request
     * @return List of publishers
     */
    @Override
    public List<EntityModel<?>> search(Object request) {
        Publisher publisher = (Publisher) request;
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreCase();
        Example<Publisher> example = Example.of(publisher, matcher);

        return repository.findAll(example).stream().map(assembler::toModel).collect(Collectors.toList());
    }

    /**
     * <p>
     * Creates a publisher
     * </p>
     * 
     * @param entity the publisher to create
     * @return The created publisher
     */
    @Override
    public EntityModel<?> add(Object entity) {
        Publisher saved = repository.save((Publisher) entity);

        log.info("Saved publisher with id " + saved.getId());

        return assembler.toModel(saved);
    }

    /**
     * <p>
     * Updates a publisher
     * </p>
     * 
     * @param id     the id of the publisher to update
     * @param entity the updated publisher
     * @return the updated publisher
     */
    @Override
    public EntityModel<?> update(String id, Object entity) {
        Publisher update = (Publisher) entity;
        Publisher updated = repository.findById(id).map(publisher -> {
            publisher.setName(update.getName());
            publisher.setBooks(update.getBooks());
            return repository.save(publisher);
        }).orElseThrow(() -> new ItemNotFoundException(id));

        log.info("Updated publisher with id " + id);

        return assembler.toModel(updated);
    }

    /**
     * <p>
     * Deletes a publisher by id
     * </p>
     * 
     * @param id the id of the publisher to delete
     */
    @Override
    public void delete(String id) {
        log.info("Deleted publisher with id " + id);

        repository.deleteById(id);
    }

    /**
     * <p>
     * Deletes all publishers
     * </p>
     */
    @Override
    public void deleteAll() {
        log.info("Deleted all publishers");

        repository.deleteAll();
    }

}
