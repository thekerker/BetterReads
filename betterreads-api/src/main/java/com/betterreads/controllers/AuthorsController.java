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
    @DeleteMapping(path = "/authors")
    public ResponseEntity<?> deleteAll() {
        authorsService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
