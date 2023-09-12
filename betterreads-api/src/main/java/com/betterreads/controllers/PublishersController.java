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

import com.betterreads.models.Publisher;
import com.betterreads.services.IService;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API for handling publishers
 */
@RestController
@RequestMapping("/v1")
public class PublishersController extends BaseController {

    @Autowired
    private IService publishersService;

    /**
     * <p>
     * Gets publishers from the data store
     * </p>
     * 
     * @return all publishers
     */
    @GetMapping(path = "/publishers")
    public CollectionModel<EntityModel<?>> getAll() {

        List<EntityModel<?>> publishers = publishersService.getAll();

        return CollectionModel.of(publishers, linkTo(methodOn(PublishersController.class).getAll()).withSelfRel());
    }

    /**
     * <p>
     * Gets publishers by id
     * <p>
     * 
     * @param id id of the publisher to retrieve
     * @return the publisher
     */
    @GetMapping(path = "/publishers/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        EntityModel<?> entity = publishersService.getById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entity);
    }

    /**
     * <p>
     * Searches for all publishers based on the provided criteria
     * </p>
     * 
     * @param request the search request parameters
     * @return all publishers that match the search criteria
     */
    @PostMapping(path = "/publishers/search")
    public CollectionModel<EntityModel<?>> search(@RequestBody Publisher request) {
        List<EntityModel<?>> publishers = publishersService.search(request);

        return CollectionModel.of(publishers,
                linkTo(methodOn(PublishersController.class).search(request)).withSelfRel());
    }

    /**
     * <p>
     * Saves a publisher in the repository
     * <p>
     * 
     * @param publisher the publisher to save
     * @return the publisher that was saved
     */
    @PostMapping(path = "/publishers")
    public ResponseEntity<?> add(@Valid @RequestBody Publisher publisher) {
        EntityModel<?> entity = publishersService.add(publisher);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    /**
     * <p>
     * Updates a publisher in the repository by the provided id
     * </p>
     * 
     * @param id        the id of the publisher
     * @param publisher the publisher to update
     * @return the updated publisher
     */
    @PutMapping(path = "/publishers/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody Publisher publisher) {
        EntityModel<?> entity = publishersService.update(id, publisher);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    /**
     * <p>
     * Deletes the publisher associated with the provided id
     * </p>
     * 
     * @param id the id of the publisher to delete
     */
    @DeleteMapping(path = "/publishers/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        publishersService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * <p>
     * Deletes all publishers in the repository
     * </p>
     */
    @DeleteMapping(path = "/publishers")
    public ResponseEntity<?> deleteAll() {
        publishersService.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
