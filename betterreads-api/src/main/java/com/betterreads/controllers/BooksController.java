package com.betterreads.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.betterreads.models.Book;
import com.betterreads.models.Publisher;
import com.betterreads.models.Author.Name;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BooksController {

    @GetMapping(path = "/books")
    public CollectionModel<EntityModel<Book>> getAll() {
        EntityModel<Book> entity = EntityModel.of(getMockBook());

        List<EntityModel<Book>> books = new ArrayList<EntityModel<Book>>();
        books.add(entity);

        return CollectionModel.of(books, linkTo(methodOn(BooksController.class).getAll()).withSelfRel());
    }

    @GetMapping(path = "/books/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        EntityModel<Book> entity = EntityModel.of(getMockBook());

        return ResponseEntity.ok(entity);
    }

    @PostMapping(path = "/books/search")
    public CollectionModel<EntityModel<Book>> search(@RequestBody Book request) {
        EntityModel<Book> entity = EntityModel.of(getMockBook());

        List<EntityModel<Book>> books = new ArrayList<EntityModel<Book>>();
        books.add(entity);

        return CollectionModel.of(books, linkTo(methodOn(BooksController.class).search(request)).withSelfRel());
    }

    @PostMapping(path = "/books")
    public ResponseEntity<?> add(@RequestBody Book book) {
        EntityModel<Book> entity = EntityModel.of(book);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    @PutMapping(path = "/books/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Book book) {
        EntityModel<Book> entity = EntityModel.of(book);

        return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entity);
    }

    @DeleteMapping(path = "/books/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/books")
    public ResponseEntity<?> deleteAll() {
        return ResponseEntity.noContent().build();
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
