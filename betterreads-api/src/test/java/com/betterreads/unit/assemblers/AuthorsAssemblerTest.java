package com.betterreads.unit.assemblers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import com.betterreads.assemblers.AuthorsAssembler;
import com.betterreads.models.Author;
import com.betterreads.models.Book;

public class AuthorsAssemblerTest {

    private AuthorsAssembler assembler;

    @BeforeEach
    public void setupTests() {
        assembler = new AuthorsAssembler();
    }

    @SuppressWarnings("null")
    @Test
    public void whenToModel_thenCorrectResponse() {
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().id("1").isbn("000-9766575").language("English").build());

        Author author = Author.builder()
                .id("1")
                .firstName("George")
                .lastName("Bluth")
                .suffix("Sr")
                .dateOfBirth(new Date())
                .gender("Male")
                .city("Los Angeles")
                .state("CA")
                .books(books)
                .build();

        EntityModel<Author> entity = assembler.toModel(author);

        assertEquals("1", Objects.requireNonNull(entity.getContent()).getId());
        assertEquals("George", entity.getContent().getFirstName());
        assertEquals("Bluth", entity.getContent().getLastName());
        assertEquals("Sr", entity.getContent().getSuffix());
        assertEquals("Male", entity.getContent().getGender());
        assertEquals("Los Angeles", entity.getContent().getCity());
        assertEquals("CA", entity.getContent().getState());
        assertNotNull(entity.getContent().getDateOfBirth());
        assertEquals("1", entity.getContent().getBooks().get(0).getId());

        assertEquals(2, entity.getLinks().toList().size());
        assertEquals("/v1/authors/1", entity.getLinks().getLink(IanaLinkRelations.SELF).get().getHref());
        assertEquals("/v1/authors", entity.getLinks().getLink("v1/authors").get().getHref());
    }
}
