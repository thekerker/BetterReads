package com.betterreads.unit.assemblers;

import com.betterreads.assemblers.PublishersAssembler;
import com.betterreads.models.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PublishersAssemblerTest {

    private PublishersAssembler assembler;

    @BeforeEach
    public void setupTests() {
        assembler = new PublishersAssembler();
    }

    @SuppressWarnings("null")
    @Test
    public void whenToModel_thenCorrectResponse() {
        Publisher publisher = Publisher
                .builder()
                .id("1")
                .name("Prison Publishing")
                .build();

        EntityModel<Publisher> entity = assembler.toModel(publisher);

        assertEquals("1", Objects.requireNonNull(entity.getContent()).getId());
        assertEquals("Prison Publishing", entity.getContent().getName());

        assertEquals(2, entity.getLinks().toList().size());
        assertEquals("/v1/publishers/1", Objects.requireNonNull(entity.getLinks().getLink(IanaLinkRelations.SELF).orElse(null)).getHref());
        assertEquals("/v1/publishers", Objects.requireNonNull(entity.getLinks().getLink("v1/publishers").orElse(null)).getHref());
    }
}
