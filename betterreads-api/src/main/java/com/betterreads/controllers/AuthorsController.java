package com.betterreads.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.betterreads.models.Author;
import com.betterreads.models.Author.Name;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthorsController {
    @GetMapping(path = "/authors")
    public CollectionModel<EntityModel<Author>> getAll() {
        EntityModel<Author> entity = EntityModel.of(getMockAuthor());

        List<EntityModel<Author>> authors = new ArrayList<EntityModel<Author>>();
        authors.add(entity);

        return CollectionModel.of(authors, linkTo(methodOn(AuthorsController.class).getAll()).withSelfRel());
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        EntityModel<Author> entity = EntityModel.of(getMockAuthor());

        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "/authors/search")
    public CollectionModel<EntityModel<Author>> search(@RequestBody Author request) {
        EntityModel<Author> entity = EntityModel.of(getMockAuthor());

        List<EntityModel<Author>> authors = new ArrayList<EntityModel<Author>>();
        authors.add(entity);

        return CollectionModel.of(authors, linkTo(methodOn(AuthorsController.class).search(request)).withSelfRel());
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<?> add(@RequestBody Author book) {
        EntityModel<Author> entity = EntityModel.of(book);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Author book) {
        EntityModel<Author> entity = EntityModel.of(book);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/authors")
    public ResponseEntity<?> deleteAll() {
        return ResponseEntity.noContent().build();
    }

    private Author getMockAuthor() {
        return Author.builder()
                .id("1")
                .name(Name.builder().firstName("George").middleName("Michael").lastName("Bluth").build())
                .dateOfBirth(new GregorianCalendar(1987, Calendar.JULY, 13).getTime())
                .city("Modesto")
                .state("CA")
                .build();
    }
}
