package com.betterreads.unit.assemblers;

import com.betterreads.assemblers.BooksAssembler;
import com.betterreads.models.Author;
import com.betterreads.models.Book;
import com.betterreads.models.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BooksAssemblerTest {

    private BooksAssembler assembler;

    @BeforeEach
    public void setupTests() {
        assembler = new BooksAssembler();
    }

    @SuppressWarnings("null")
    @Test
    public void whenToModel_thenCorrectResponse() {
        Author author = Author.builder()
                .id("1")
                .firstName("George")
                .lastName("Bluth")
                .suffix("Sr")
                .build();

        Book book = Book.builder()
                .id("1")
                .isbn("000-98766612")
                .title("Caged Wisdom")
                .authors(Collections.singletonList(author))
                .publisher(Publisher.builder().id("1").name("Prison Publishing").build())
                .build();

        EntityModel<Book> entity = assembler.toModel(book);

        assertEquals("1", Objects.requireNonNull(entity.getContent()).getId());
        assertEquals("000-98766612", entity.getContent().getIsbn());
        assertEquals("Caged Wisdom", entity.getContent().getTitle());
        assertEquals(1, entity.getContent().getAuthors().size());
        assertEquals("1", entity.getContent().getAuthors().getFirst().getId());
        assertEquals("George", entity.getContent().getAuthors().getFirst().getFirstName());
        assertEquals("Bluth", entity.getContent().getAuthors().getFirst().getLastName());
        assertEquals("Sr", entity.getContent().getAuthors().getFirst().getSuffix());
        assertEquals("1", entity.getContent().getPublisher().getId());
        assertEquals("Prison Publishing", entity.getContent().getPublisher().getName());

        assertEquals(2, entity.getLinks().toList().size());
        assertEquals("/v1/books/1", Objects.requireNonNull(entity.getLinks().getLink(IanaLinkRelations.SELF).orElse(null)).getHref());
        assertEquals("/v1/books", Objects.requireNonNull(entity.getLinks().getLink("v1/books").orElse(null)).getHref());
    }
}
