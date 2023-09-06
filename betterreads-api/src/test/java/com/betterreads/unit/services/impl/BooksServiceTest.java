package com.betterreads.unit.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.EntityModel;

import com.betterreads.assemblers.BooksAssembler;
import com.betterreads.exceptions.ItemNotFoundException;
import com.betterreads.models.Author;
import com.betterreads.models.Author.Name;
import com.betterreads.models.Book;
import com.betterreads.models.Publisher;
import com.betterreads.repositories.BooksRepository;
import com.betterreads.services.impl.BooksService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BooksServiceTest {

    @Mock
    private BooksRepository repository;

    @Mock
    private BooksAssembler assembler;

    @InjectMocks
    private BooksService service;

    @BeforeEach
    public void setupTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetAll_thenCorrectResponse() {
        Book book = getMockBook();

        when(repository.findAll()).thenReturn(Collections.singletonList(book));
        when(assembler.toModel(book)).thenReturn(EntityModel.of(book));

        List<EntityModel<?>> actual = service.getAll();

        Book actualBook = (Book) actual.get(0).getContent();

        assertEquals(book.getId(), actualBook.getId());
        assertEquals(book.getIsbn(), actualBook.getIsbn());
        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getAuthors().get(0).getId(), actualBook.getAuthors().get(0).getId());
        assertEquals(book.getPages(), actualBook.getPages());
        assertEquals(book.getPublisher().getId(), actualBook.getPublisher().getId());

        verify(repository).findAll();
        verify(assembler).toModel(book);
    }

    @Test
    public void whenGetById_thenCorrectResponse() {
        Book book = getMockBook();

        when(repository.findById("1")).thenReturn(Optional.of(book));
        when(assembler.toModel(book)).thenReturn(EntityModel.of(book));

        EntityModel<?> actual = service.getById("1");
        Book actualBook = (Book) actual.getContent();

        assertEquals(book.getId(), actualBook.getId());
        assertEquals(book.getIsbn(), actualBook.getIsbn());
        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getAuthors().get(0).getId(), actualBook.getAuthors().get(0).getId());
        assertEquals(book.getPages(), actualBook.getPages());
        assertEquals(book.getPublisher().getId(), actualBook.getPublisher().getId());

        verify(repository).findById("1");
        verify(assembler).toModel(book);
    }

    @Test
    public void whenGetById_notFound_thenCorrectResponse() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ItemNotFoundException.class, () -> {
            service.getById("1");
        });

        assertEquals("Could not find item with id 1", ex.getMessage());

        verify(repository).findById("1");
        verify(assembler, never()).toModel(any(Book.class));
    }

    @Test
    public void whenSearch_thenCorrectResponse() {
        Book book = getMockBook();

        when(repository.findAll(isA(Example.class))).thenReturn(Collections.singletonList(book));
        when(assembler.toModel(book)).thenReturn(EntityModel.of(book));

        Book request = Book.builder().title("caged").build();

        List<EntityModel<?>> actual = service.search(request);

        Book actualBook = (Book) actual.get(0).getContent();

        assertEquals(book.getId(), actualBook.getId());
        assertEquals(book.getIsbn(), actualBook.getIsbn());
        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getAuthors().get(0).getId(), actualBook.getAuthors().get(0).getId());
        assertEquals(book.getPages(), actualBook.getPages());
        assertEquals(book.getPublisher().getId(), actualBook.getPublisher().getId());

        verify(repository).findAll(isA(Example.class));
        verify(assembler).toModel(book);
    }

    @Test
    public void whenAdd_thenCorrectResponse() {
        Book book = getMockBook();

        when(repository.save(book)).thenReturn(book);
        when(assembler.toModel(book)).thenReturn(EntityModel.of(book));

        EntityModel<?> actual = service.add(book);
        Book actualBook = (Book) actual.getContent();

        assertEquals(book.getId(), actualBook.getId());
        assertEquals(book.getIsbn(), actualBook.getIsbn());
        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getAuthors().get(0).getId(), actualBook.getAuthors().get(0).getId());
        assertEquals(book.getPages(), actualBook.getPages());
        assertEquals(book.getPublisher().getId(), actualBook.getPublisher().getId());

        verify(repository).save(book);
        verify(assembler).toModel(book);
    }

    @Test
    public void whenUpdate_thenCorrectResponse() {
        Book book = getMockBook();

        Book bookUpdate = book;
        bookUpdate.setGenres(Collections.singletonList("Fiction"));

        when(repository.findById("1")).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(book);
        when(assembler.toModel(book)).thenReturn(EntityModel.of(book));

        EntityModel<?> actual = service.update("1", bookUpdate);
        Book actualBook = (Book) actual.getContent();

        assertEquals(book.getId(), actualBook.getId());
        assertEquals(book.getIsbn(), actualBook.getIsbn());
        assertEquals(book.getTitle(), actualBook.getTitle());
        assertEquals(book.getAuthors().get(0).getId(), actualBook.getAuthors().get(0).getId());
        assertEquals(book.getPages(), actualBook.getPages());
        assertEquals(book.getPublisher().getId(), actualBook.getPublisher().getId());
        assertTrue(actualBook.getGenres().contains("Fiction"));

        verify(repository).findById("1");
        verify(repository).save(book);
        verify(assembler).toModel(book);
    }

    @Test
    public void whenUpdate_notFound_thenCorrectResponse() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ItemNotFoundException.class, () -> {
            service.update("1", getMockBook());
        });

        assertEquals("Could not find item with id 1", ex.getMessage());

        verify(repository).findById("1");
        verify(repository, never()).save(any(Book.class));
        verify(assembler, never()).toModel(any(Book.class));
    }

    @Test
    public void whenDeleteById_thenCorrectResponse() {
        doNothing().when(repository).deleteById("1");

        service.delete("1");

        verify(repository).deleteById("1");
    }

    @Test
    public void whenDeleteAll_thenCorrectResponse() {
        doNothing().when(repository).deleteAll();

        service.deleteAll();

        verify(repository).deleteAll();
    }

    private Book getMockBook() {
        Author author = Author.builder()
                .id("1")
                .name(Name.builder().firstName("George").middleName("Michael").lastName("Bluth").build())
                .build();

        return Book.builder()
                .id("1")
                .isbn("000-5555523")
                .title("My Awesome Book")
                .authors(Collections.singletonList(author))
                .pages(351)
                .genres(Arrays.asList("non-fiction", "autobiography"))
                .publishedDate(new Date())
                .publisher(Publisher.builder().id("1").name("McGraw").build())
                .build();
    }
}
