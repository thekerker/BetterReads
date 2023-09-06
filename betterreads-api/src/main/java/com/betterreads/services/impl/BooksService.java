package com.betterreads.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.betterreads.assemblers.BooksAssembler;
import com.betterreads.exceptions.ItemNotFoundException;
import com.betterreads.repositories.BooksRepository;
import com.betterreads.models.Book;
import com.betterreads.services.IService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Service for Books CRUD operations
 * </p>
 */
@Service
@Slf4j
public class BooksService implements IService {

    @Autowired
    private BooksRepository repository;

    @Autowired
    private BooksAssembler assembler;

    /**
     * <p>
     * Gets all books
     * </p>
     * 
     * @return The books
     */
    @Override
    public List<EntityModel<?>> getAll() {
        return repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
    }

    /**
     * <p>
     * Gets book by id
     * </p>
     * 
     * @param id the books's id in the database
     * @return The book
     */
    @Override
    public EntityModel<?> getById(String id) {
        Book book = repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));

        return assembler.toModel(book);
    }

    /**
     * <p>
     * Searches for a book with the given parameters
     * <p>
     * 
     * @param request The search request
     * @return List of books
     */
    @Override
    public List<EntityModel<?>> search(Object request) {
        Book book = (Book) request;
        ExampleMatcher matcher = ExampleMatcher.matchingAny().withIgnoreCase();
        Example<Book> example = Example.of(book, matcher);

        return repository.findAll(example).stream().map(assembler::toModel).collect(Collectors.toList());
    }

    /**
     * <p>
     * Creates a book
     * </p>
     * 
     * @param entity the book to create
     * @return The created book
     */
    @Override
    public EntityModel<?> add(Object entity) {
        Book saved = repository.save((Book) entity);

        log.info("Saved book with id " + saved.getId());

        return assembler.toModel(saved);
    }

    /**
     * <p>
     * Updates a book
     * </p>
     * 
     * @param id     the id of the book to update
     * @param entity the updated book
     * @return the updated book
     */
    @Override
    public EntityModel<?> update(String id, Object entity) {
        Book update = (Book) entity;
        Book updated = repository.findById(id).map(book -> {
            book.setAuthors(update.getAuthors());
            book.setTitle(update.getTitle());
            book.setIsbn(update.getIsbn());
            book.setGenres(update.getGenres());
            book.setPages(update.getPages());
            book.setPublishedDate(update.getPublishedDate());
            book.setPublisher(update.getPublisher());
            book.setLanguage(update.getLanguage());
            return repository.save(book);
        }).orElseThrow(() -> new ItemNotFoundException(id));

        log.info("Updated book with id " + id);

        return assembler.toModel(updated);
    }

    /**
     * <p>
     * Deletes a book by id
     * </p>
     * 
     * @param id the id of the book to delete
     */
    @Override
    public void delete(String id) {
        log.info("Deleted book with id " + id);

        repository.deleteById(id);
    }

    /**
     * <p>
     * Deletes all books
     * </p>
     */
    @Override
    public void deleteAll() {
        log.info("Deleted all books");

        repository.deleteAll();
    }

}
