package com.betterreads.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.betterreads.models.Publisher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PublishersController {
    @GetMapping(path = "/publishers")
    public CollectionModel<EntityModel<Publisher>> getAll() {
        EntityModel<Publisher> entity = EntityModel.of(getMockPublisher());

        List<EntityModel<Publisher>> publishers = new ArrayList<EntityModel<Publisher>>();
        publishers.add(entity);

        return CollectionModel.of(publishers, linkTo(methodOn(PublishersController.class).getAll()).withSelfRel());
    }

    @GetMapping(path = "/publishers/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        EntityModel<Publisher> entity = EntityModel.of(getMockPublisher());

        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "/publishers/search")
    public CollectionModel<EntityModel<Publisher>> search(@RequestBody Publisher request) {
        EntityModel<Publisher> entity = EntityModel.of(getMockPublisher());

        List<EntityModel<Publisher>> publishers = new ArrayList<EntityModel<Publisher>>();
        publishers.add(entity);

        return CollectionModel.of(publishers,
                linkTo(methodOn(PublishersController.class).search(request)).withSelfRel());
    }

    @PostMapping(path = "/publishers")
    public ResponseEntity<?> add(@RequestBody Publisher Publisher) {
        EntityModel<Publisher> entity = EntityModel.of(Publisher);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    @PutMapping(path = "/publishers/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Publisher Publisher) {
        EntityModel<Publisher> entity = EntityModel.of(Publisher);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    @DeleteMapping(path = "/publishers/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/publishers")
    public ResponseEntity<?> deleteAll() {
        return ResponseEntity.noContent().build();
    }

    private Publisher getMockPublisher() {
        return Publisher.builder()
                .id("1")
                .name("McGraw")
                .build();
    }
}
