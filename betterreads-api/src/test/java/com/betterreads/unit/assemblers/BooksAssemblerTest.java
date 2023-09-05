package com.betterreads.unit.assemblers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import com.betterreads.assemblers.BooksAssembler;
import com.betterreads.models.Author;
import com.betterreads.models.Book;
import com.betterreads.models.Publisher;
import com.betterreads.models.Author.Name;

@SpringBootTest
public class BooksAssemblerTest {

    @Autowired
    private BooksAssembler assembler;

    @Test
    public void whenToModel_thenCorrectResponse() {
        Author author = Author.builder()
                .id("1")
                .name(Name.builder().firstName("George").lastName("Bluth").suffix("Sr").build())
                .build();

        Book book = Book.builder()
                .id("1")
                .isbn("000-98766612")
                .title("Caged Wisdom")
                .authors(Collections.singletonList(author))
                .publisher(Publisher.builder().id("1").name("Prison Publishing").build())
                .build();

        EntityModel<Book> entity = assembler.toModel(book);

        assertEquals("1", entity.getContent().getId());
        assertEquals("000-98766612", entity.getContent().getIsbn());
        assertEquals("Caged Wisdom", entity.getContent().getTitle());
        assertEquals(1, entity.getContent().getAuthors().size());
        assertEquals("1", entity.getContent().getAuthors().get(0).getId());
        assertEquals("George", entity.getContent().getAuthors().get(0).getName().getFirstName());
        assertEquals("Bluth", entity.getContent().getAuthors().get(0).getName().getLastName());
        assertEquals("Sr", entity.getContent().getAuthors().get(0).getName().getSuffix());
        assertEquals("1", entity.getContent().getPublisher().getId());
        assertEquals("Prison Publishing", entity.getContent().getPublisher().getName());

        assertEquals(2, entity.getLinks().toList().size());
        assertEquals("http://localhost/v1/books/1", entity.getLinks().getLink(IanaLinkRelations.SELF).get().getHref());
        assertEquals("http://localhost/v1/books", entity.getLinks().getLink("v1/books").get().getHref());
    }
}
