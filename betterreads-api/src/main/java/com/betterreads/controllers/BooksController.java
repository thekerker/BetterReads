package com.betterreads.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betterreads.models.Book;
import com.betterreads.services.IService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API for handling books
 */
@RestController
@RequestMapping("/v1")
public class BooksController extends BaseController {

    @Autowired
    private IService booksService;

    /**
     * <p>
     * Gets books from the data store
     * </p>
     * 
     * @return all books
     */
    @Operation(summary = "Gets all books from the data store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found books", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) })
    })
    @GetMapping(path = "/books")
    public CollectionModel<EntityModel<?>> getAll() {

        List<EntityModel<?>> books = booksService.getAll();

        return CollectionModel.of(books, linkTo(methodOn(BooksController.class).getAll()).withSelfRel());
    }

    /**
     * <p>
     * Gets books by id
     * <p>
     * 
     * @param id id of the book to retrieve
     * @return the book
     */
    @Operation(summary = "Gets a book from the data store by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) }),
            @ApiResponse(responseCode = "404", description = "No book found for the id", content = {
                    @Content
            })
    })
    @GetMapping(path = "/books/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        EntityModel<?> entity = booksService.getById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entity);
    }

    /**
     * <p>
     * Searches for all books based on the provided criteria
     * </p>
     * 
     * @param request the search request parameters
     * @return all books that match the search criteria
     */
    @Operation(summary = "Searches for books based on a set of criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found results", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) })
    })
    @PostMapping(path = "/books/search")
    public CollectionModel<EntityModel<?>> search(@RequestBody Book request) {
        List<EntityModel<?>> books = booksService.search(request);

        return CollectionModel.of(books, linkTo(methodOn(BooksController.class).search(request)).withSelfRel());
    }

    /**
     * <p>
     * Saves a book in the repository
     * <p>
     * 
     * @param book the book to save
     * @return the book that was saved
     */
    @Operation(summary = "Adds a new book to the data store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the book", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) })
    })
    @PostMapping(path = "/books")
    public ResponseEntity<?> add(@Valid @RequestBody Book book) {
        EntityModel<?> entity = booksService.add(book);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    /**
     * <p>
     * Updates a book in the repository by the provided id
     * </p>
     * 
     * @param id   the id of the book
     * @param book the book to update
     * @return the updated book
     */
    @Operation(summary = "Updates a book in the data store by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Updated the book", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class)) })
    })
    @PutMapping(path = "/books/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody Book book) {
        EntityModel<?> entity = booksService.update(id, book);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    /**
     * <p>
     * Deletes the book associated with the provided id
     * </p>
     * 
     * @param id the id of the book to delete
     */
    @Operation(summary = "Deletes a book from the data store by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the book", content = {
                    @Content })
    })
    @DeleteMapping(path = "/books/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        booksService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * <p>
     * Deletes all books in the repository
     * </p>
     */
    @Operation(summary = "Deletes all books from the data store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted all books", content = {
                    @Content })
    })
    @DeleteMapping(path = "/books")
    public ResponseEntity<?> deleteAll() {
        booksService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
