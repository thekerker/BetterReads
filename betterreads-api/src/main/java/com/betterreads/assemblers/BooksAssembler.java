package com.betterreads.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.betterreads.controllers.BooksController;
import com.betterreads.models.Book;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * <p>
 * Assembles an EntityModel from a Book
 * </p>
 */
@Component
public class BooksAssembler implements RepresentationModelAssembler<Book, EntityModel<Book>> {

    /**
     * <p>
     * Maps a Book to an EntityModel
     * </p>
     * 
     * @param entity the book
     * @return the mapped EntityModel
     */
    @Override
    public EntityModel<Book> toModel(Book entity) {
        return EntityModel.of(entity, linkTo(methodOn(BooksController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(BooksController.class).getAll()).withRel("v1/books"));
    }

}
