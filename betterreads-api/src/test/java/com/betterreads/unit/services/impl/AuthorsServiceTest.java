package com.betterreads.unit.services.impl;

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

import com.betterreads.assemblers.AuthorsAssembler;
import com.betterreads.exceptions.ItemNotFoundException;
import com.betterreads.models.Author;
import com.betterreads.repositories.AuthorsRepository;
import com.betterreads.services.impl.AuthorsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorsServiceTest {

    @Mock
    private AuthorsRepository repository;

    @Mock
    private AuthorsAssembler assembler;

    @InjectMocks
    private AuthorsService service;

    @BeforeEach
    public void setupTests() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    public void whenGetAll_thenCorrectResponse() {
        Author author = getMockAuthor();

        when(repository.findAll()).thenReturn(Collections.singletonList(author));
        when(assembler.toModel(author)).thenReturn(EntityModel.of(author));

        List<EntityModel<?>> actual = service.getAll();

        Author actualAuthor = (Author) actual.get(0).getContent();

        assertEquals(author.getId(), actualAuthor.getId());
        assertEquals(author.getFirstName(), actualAuthor.getFirstName());
        assertEquals(author.getMiddleName(), actualAuthor.getMiddleName());
        assertEquals(author.getLastName(), actualAuthor.getLastName());
        assertEquals(author.getSuffix(), actualAuthor.getSuffix());
        assertEquals(author.getCity(), actualAuthor.getCity());
        assertEquals(author.getState(), actualAuthor.getState());

        verify(repository).findAll();
        verify(assembler).toModel(author);
    }

    @SuppressWarnings("null")
    @Test
    public void whenGetById_thenCorrectResponse() {
        Author author = getMockAuthor();

        when(repository.findById("1")).thenReturn(Optional.of(author));
        when(assembler.toModel(author)).thenReturn(EntityModel.of(author));

        EntityModel<?> actual = service.getById("1");
        Author actualAuthor = (Author) actual.getContent();

        assertEquals(author.getId(), actualAuthor.getId());
        assertEquals(author.getFirstName(), actualAuthor.getFirstName());
        assertEquals(author.getMiddleName(), actualAuthor.getMiddleName());
        assertEquals(author.getLastName(), actualAuthor.getLastName());
        assertEquals(author.getSuffix(), actualAuthor.getSuffix());
        assertEquals(author.getCity(), actualAuthor.getCity());
        assertEquals(author.getState(), actualAuthor.getState());

        verify(repository).findById("1");
        verify(assembler).toModel(author);
    }

    @Test
    public void whenGetById_notFound_thenCorrectResponse() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ItemNotFoundException.class, () -> {
            service.getById("1");
        });

        assertEquals("Could not find item with id 1", ex.getMessage());

        verify(repository).findById("1");
        verify(assembler, never()).toModel(any(Author.class));
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    public void whenSearch_thenCorrectResponse() {
        Author author = getMockAuthor();

        when(repository.findAll(isA(Example.class))).thenReturn(Collections.singletonList(author));
        when(assembler.toModel(author)).thenReturn(EntityModel.of(author));

        Author request = Author.builder().city("modesto").build();

        List<EntityModel<?>> actual = service.search(request);

        Author actualAuthor = (Author) actual.get(0).getContent();

        assertEquals(author.getId(), actualAuthor.getId());
        assertEquals(author.getFirstName(), actualAuthor.getFirstName());
        assertEquals(author.getMiddleName(), actualAuthor.getMiddleName());
        assertEquals(author.getLastName(), actualAuthor.getLastName());
        assertEquals(author.getSuffix(), actualAuthor.getSuffix());
        assertEquals(author.getCity(), actualAuthor.getCity());
        assertEquals(author.getState(), actualAuthor.getState());

        verify(repository).findAll(isA(Example.class));
        verify(assembler).toModel(author);
    }

    @SuppressWarnings("null")
    @Test
    public void whenAdd_thenCorrectResponse() {
        Author author = getMockAuthor();

        when(repository.save(author)).thenReturn(author);
        when(assembler.toModel(author)).thenReturn(EntityModel.of(author));

        EntityModel<?> actual = service.add(author);
        Author actualAuthor = (Author) actual.getContent();

        assertEquals(author.getId(), actualAuthor.getId());
        assertEquals(author.getFirstName(), actualAuthor.getFirstName());
        assertEquals(author.getMiddleName(), actualAuthor.getMiddleName());
        assertEquals(author.getLastName(), actualAuthor.getLastName());
        assertEquals(author.getSuffix(), actualAuthor.getSuffix());
        assertEquals(author.getCity(), actualAuthor.getCity());
        assertEquals(author.getState(), actualAuthor.getState());

        verify(repository).save(author);
        verify(assembler).toModel(author);
    }

    @SuppressWarnings("null")
    @Test
    public void whenUpdate_thenCorrectResponse() {
        Author author = getMockAuthor();

        Author authorUpdate = author;
        authorUpdate.setCity("Los Angeles");

        when(repository.findById("1")).thenReturn(Optional.of(author));
        when(repository.save(author)).thenReturn(author);
        when(assembler.toModel(author)).thenReturn(EntityModel.of(author));

        EntityModel<?> actual = service.update("1", authorUpdate);
        Author actualAuthor = (Author) actual.getContent();

        assertEquals(author.getId(), actualAuthor.getId());
        assertEquals(author.getId(), actualAuthor.getId());
        assertEquals(author.getFirstName(), actualAuthor.getFirstName());
        assertEquals(author.getMiddleName(), actualAuthor.getMiddleName());
        assertEquals(author.getLastName(), actualAuthor.getLastName());
        assertEquals(author.getSuffix(), actualAuthor.getSuffix());
        assertEquals("Los Angeles", actualAuthor.getCity());
        assertEquals(author.getState(), actualAuthor.getState());

        verify(repository).findById("1");
        verify(repository).save(author);
        verify(assembler).toModel(author);
    }

    @Test
    public void whenUpdate_notFound_thenCorrectResponse() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ItemNotFoundException.class, () -> {
            service.update("1", getMockAuthor());
        });

        assertEquals("Could not find item with id 1", ex.getMessage());

        verify(repository).findById("1");
        verify(repository, never()).save(any(Author.class));
        verify(assembler, never()).toModel(any(Author.class));
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

    private Author getMockAuthor() {
        return Author.builder()
                .id("1")
                .firstName("George")
                .middleName("Michael")
                .lastName("Bluth")
                .suffix("III")
                .city("Modesto")
                .state("CA")
                .dateOfBirth(new Date())
                .gender("Male")
                .build();
    }
}
