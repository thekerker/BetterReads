package com.betterreads.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.betterreads.controllers.PublishersController;
import com.betterreads.models.Publisher;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * <p>
 * Assembles an EntityModel from a Publisher
 * </p>
 */
@Component
public class PublishersAssembler implements RepresentationModelAssembler<Publisher, EntityModel<Publisher>> {

    /**
     * <p>
     * Maps a Publisher to an EntityModel
     * </p>
     * 
     * @param entity the publisher
     * @return the mapped EntityModel
     */
    @Override
    public EntityModel<Publisher> toModel(Publisher entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PublishersController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(PublishersController.class).getAll()).withRel("v1/publishers"));
    }

}
