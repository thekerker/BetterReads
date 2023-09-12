package com.betterreads.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.betterreads.assemblers.AuthorsAssembler;
import com.betterreads.exceptions.ItemNotFoundException;
import com.betterreads.models.Author;
import com.betterreads.repositories.AuthorsRepository;
import com.betterreads.services.IService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Service for Authors CRUD operations
 * </p>
 */
@Service
@Slf4j
public class AuthorsService implements IService {

    @Autowired
    private AuthorsRepository repository;

    @Autowired
    private AuthorsAssembler assembler;

    /**
     * <p>
     * Gets all authors
     * </p>
     * 
     * @return The authors
     */
    @Override
    public List<EntityModel<?>> getAll() {
        return repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
    }

    /**
     * <p>
     * Gets author by id
     * </p>
     * 
     * @param id the author's id in the database
     * @return The author
     */
    @Override
    public EntityModel<?> getById(String id) {
        Author author = repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));

        return assembler.toModel(author);
    }

    /**
     * <p>
     * Searches for an author with the given parameters
     * <p>
     * 
     * @param request The search request
     * @return List of authors
     */
    @Override
    public List<EntityModel<?>> search(Object request) {
        Author author = (Author) request;
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreCase();
        Example<Author> example = Example.of(author, matcher);

        return repository.findAll(example).stream().map(assembler::toModel).collect(Collectors.toList());
    }

    /**
     * <p>
     * Creates an author
     * </p>
     * 
     * @param entity the author to create
     * @return The created author
     */
    @Override
    public EntityModel<?> add(Object entity) {
        Author saved = repository.save((Author) entity);

        log.info("Saved author with id " + saved.getId());

        return assembler.toModel(saved);
    }

    /**
     * <p>
     * Updates an author
     * </p>
     * 
     * @param id     the id of the author to update
     * @param entity the updated author
     * @return the updated author
     */
    @Override
    public EntityModel<?> update(String id, Object entity) {
        Author update = (Author) entity;
        Author updated = repository.findById(id).map(author -> {
            author.setFirstName(update.getFirstName());
            author.setMiddleName(update.getMiddleName());
            author.setLastName(update.getLastName());
            author.setSuffix(update.getSuffix());
            author.setGender(update.getGender());
            author.setDateOfBirth(update.getDateOfBirth());
            author.setCity(update.getCity());
            author.setState(update.getState());
            return repository.save(author);
        }).orElseThrow(() -> new ItemNotFoundException(id));

        log.info("Updated author with id " + id);

        return assembler.toModel(updated);
    }

    /**
     * <p>
     * Deletes an author by id
     * </p>
     * 
     * @param id the id of the author to delete
     */
    @Override
    public void delete(String id) {
        log.info("Deleted author with id " + id);

        repository.deleteById(id);
    }

    /**
     * <p>
     * Deletes all authors
     * </p>
     */
    @Override
    public void deleteAll() {
        log.info("Deleted all authors");

        repository.deleteAll();
    }

}
