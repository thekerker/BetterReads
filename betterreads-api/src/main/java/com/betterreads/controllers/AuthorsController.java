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

import com.betterreads.models.Author;
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
 * API for handling authors
 */
@RestController
@RequestMapping("/v1")
public class AuthorsController extends BaseController {

    @Autowired
    private IService authorsService;

    /**
     * <p>
     * Gets authors from the data store
     * </p>
     * 
     * @return all authors
     */
    @Operation(summary = "Gets all authors from the data store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found authors", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class)) })
    })
    @GetMapping(path = "/authors")
    public CollectionModel<EntityModel<?>> getAll() {
        List<EntityModel<?>> authors = authorsService.getAll();

        return CollectionModel.of(authors, linkTo(methodOn(AuthorsController.class).getAll()).withSelfRel());
    }

    /**
     * <p>
     * Gets authors by id
     * <p>
     * 
     * @param id id of the author to retrieve
     * @return the author
     */
    @Operation(summary = "Gets an author from the data store by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the author", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class)) }),
            @ApiResponse(responseCode = "404", description = "No author found for the id", content = {
                    @Content
            })
    })
    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        EntityModel<?> entity = authorsService.getById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entity);
    }

    /**
     * <p>
     * Searches for all authors based on the provided criteria
     * </p>
     * 
     * @param request the search request parameters
     * @return all authors that match the search criteria
     */
    @Operation(summary = "Searches for authors based on a set of criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found results", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class)) })
    })
    @PostMapping(path = "/authors/search")
    public CollectionModel<EntityModel<?>> search(@RequestBody Author request) {
        List<EntityModel<?>> authors = authorsService.search(request);

        return CollectionModel.of(authors, linkTo(methodOn(AuthorsController.class).search(request)).withSelfRel());
    }

    /**
     * <p>
     * Saves an author in the repository
     * <p>
     * 
     * @param author the author to save
     * @return the author that was saved
     */
    @Operation(summary = "Adds a new author to the data store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the author", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class)) })
    })
    @PostMapping(path = "/authors")
    public ResponseEntity<?> add(@Valid @RequestBody Author author) {
        EntityModel<?> entity = authorsService.add(author);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    /**
     * <p>
     * Updates an author in the repository by the provided id
     * </p>
     * 
     * @param id     the id of the author
     * @param author the author to update
     * @return the updated author
     */
    @Operation(summary = "Updates an author in the data store by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Updated the author", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Author.class)) })
    })
    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody Author author) {
        EntityModel<?> entity = authorsService.update(id, author);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    /**
     * <p>
     * Deletes the author associated with the provided id
     * </p>
     * 
     * @param id the id of the author to delete
     */
    @Operation(summary = "Deletes an author from the data store by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the author", content = {
                    @Content })
    })
    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        authorsService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * <p>
     * Deletes all authors in the repository
     * </p>
     */
    @Operation(summary = "Deletes all authors from the data store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted all books", content = {
                    @Content })
    })
    @DeleteMapping(path = "/authors")
    public ResponseEntity<?> deleteAll() {
        authorsService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
