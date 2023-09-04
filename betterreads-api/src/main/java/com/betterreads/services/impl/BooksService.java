package com.betterreads.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.betterreads.models.Author;
import com.betterreads.models.Author.Name;
import com.betterreads.models.Book;
import com.betterreads.models.Publisher;
import com.betterreads.models.Search;
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

    /**
     * <p>
     * Gets all books
     * </p>
     * 
     * @return The books
     */
    @Override
    public List<EntityModel<?>> getAll() {
        EntityModel<Book> entity = EntityModel.of(getMockBook());

        List<EntityModel<?>> books = new ArrayList<EntityModel<?>>();
        books.add(entity);

        return books;
    }

    /**
     * <p>
     * Gets book by id
     * </p>
     * 
     * @param id the books's id in the database
     * @return List of books
     */
    @Override
    public EntityModel<?> getById(String id) {
        EntityModel<?> entity = EntityModel.of(getMockBook());

        return entity;
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
    public List<EntityModel<?>> search(Search request) {
        EntityModel<Book> bookEntity = EntityModel.of(getMockBook());

        List<EntityModel<?>> books = new ArrayList<EntityModel<?>>();
        books.add(bookEntity);

        return books;
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
        EntityModel<Book> bookEntity = EntityModel.of((Book) entity);

        return bookEntity;
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
        EntityModel<Book> bookEntity = EntityModel.of((Book) entity);

        return bookEntity;
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
    }

    /**
     * <p>
     * Deletes all books
     * </p>
     */
    @Override
    public void deleteAll() {
        log.info("Deleted all books");
    }

    private Book getMockBook() {
        Author author = Author.builder()
                .id("1")
                .name(Name.builder().firstName("George").middleName("Michael").lastName("Bluth").build())
                .dateOfBirth(new GregorianCalendar(1987, Calendar.JULY, 13).getTime())
                .city("Modesto")
                .state("CA")
                .build();

        Author[] authors = { author };

        return Book.builder()
                .id("1")
                .isbn("000-5555523")
                .title("My Awesome Book")
                .authors(authors)
                .pages(351)
                .genres(new String[] { "non-fiction", "autobiography" })
                .publishedDate(new Date())
                .publisher(Publisher.builder().id("1").name("McGraw").build())
                .build();
    }

}
