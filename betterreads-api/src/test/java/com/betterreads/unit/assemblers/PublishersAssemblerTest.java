package com.betterreads.unit.assemblers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import com.betterreads.assemblers.PublishersAssembler;
import com.betterreads.models.Publisher;

public class PublishersAssemblerTest {

    private PublishersAssembler assembler;

    @BeforeEach
    public void setupTests() {
        assembler = new PublishersAssembler();
    }

    @Test
    public void whenToModel_thenCorrectResponse() {
        Publisher publisher = Publisher
                .builder()
                .id("1")
                .name("Prison Publishing")
                .build();

        EntityModel<Publisher> entity = assembler.toModel(publisher);

        assertEquals("1", entity.getContent().getId());
        assertEquals("Prison Publishing", entity.getContent().getName());

        assertEquals(2, entity.getLinks().toList().size());
        assertEquals("/v1/publishers/1", entity.getLinks().getLink(IanaLinkRelations.SELF).get().getHref());
        assertEquals("/v1/publishers", entity.getLinks().getLink("v1/publishers").get().getHref());
    }
}
