package com.betterreads.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.betterreads.controllers.AuthorsController;
import com.betterreads.models.Author;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * <p>
 * Assembles an EntityModel from an Author
 * </p>
 */
@Component
public class AuthorsAssembler implements RepresentationModelAssembler<Author, EntityModel<Author>> {

    /**
     * <p>
     * Maps an Author to an EntityModel
     * </p>
     * 
     * @param entity the author
     * @return the mapped EntityModel
     */
    @SuppressWarnings("null")
    @Override
    public EntityModel<Author> toModel(Author entity) {
        return EntityModel.of(entity, linkTo(methodOn(AuthorsController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(AuthorsController.class).getAll()).withRel("v1/authors"));
    }

}
