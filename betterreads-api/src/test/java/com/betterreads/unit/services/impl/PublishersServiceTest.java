package com.betterreads.unit.services.impl;

import com.betterreads.assemblers.PublishersAssembler;
import com.betterreads.exceptions.ItemNotFoundException;
import com.betterreads.models.Publisher;
import com.betterreads.repositories.PublishersRepository;
import com.betterreads.services.impl.PublishersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.EntityModel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class PublishersServiceTest {

    @Mock
    private PublishersRepository repository;

    @Mock
    private PublishersAssembler assembler;

    @InjectMocks
    private PublishersService service;

    @BeforeEach
    public void setupTests() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    public void whenGetAll_thenCorrectResponse() {
        Publisher publisher = getMockPublisher();

        when(repository.findAll()).thenReturn(Collections.singletonList(publisher));
        when(assembler.toModel(publisher)).thenReturn(EntityModel.of(publisher));

        List<EntityModel<?>> actual = service.getAll();

        Publisher actualPublisher = (Publisher) actual.getFirst().getContent();

        assert actualPublisher != null;
        assertEquals(publisher.getId(), actualPublisher.getId());
        assertEquals(publisher.getName(), actualPublisher.getName());

        verify(repository).findAll();
        verify(assembler).toModel(publisher);
    }

    @SuppressWarnings("null")
    @Test
    public void whenGetById_thenCorrectResponse() {
        Publisher publisher = getMockPublisher();

        when(repository.findById("1")).thenReturn(Optional.of(publisher));
        when(assembler.toModel(publisher)).thenReturn(EntityModel.of(publisher));

        EntityModel<?> actual = service.getById("1");
        Publisher actualPublisher = (Publisher) actual.getContent();

        assert actualPublisher != null;
        assertEquals(publisher.getId(), actualPublisher.getId());
        assertEquals(publisher.getName(), actualPublisher.getName());

        verify(repository).findById("1");
        verify(assembler).toModel(publisher);
    }

    @Test
    public void whenGetById_notFound_thenCorrectResponse() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ItemNotFoundException.class, () -> {
            service.getById("1");
        });

        assertEquals("Could not find item with id 1", ex.getMessage());

        verify(repository).findById("1");
        verify(assembler, never()).toModel(any(Publisher.class));
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Test
    public void whenSearch_thenCorrectResponse() {
        Publisher publisher = getMockPublisher();

        when(repository.findAll(isA(Example.class))).thenReturn(Collections.singletonList(publisher));
        when(assembler.toModel(publisher)).thenReturn(EntityModel.of(publisher));

        Publisher request = Publisher.builder().name("pendant").build();

        List<EntityModel<?>> actual = service.search(request);

        Publisher actualPublisher = (Publisher) actual.getFirst().getContent();

        assert actualPublisher != null;
        assertEquals(publisher.getId(), actualPublisher.getId());
        assertEquals(publisher.getName(), actualPublisher.getName());

        verify(repository).findAll(isA(Example.class));
        verify(assembler).toModel(publisher);
    }

    @SuppressWarnings("null")
    @Test
    public void whenAdd_thenCorrectResponse() {
        Publisher publisher = getMockPublisher();

        when(repository.save(publisher)).thenReturn(publisher);
        when(assembler.toModel(publisher)).thenReturn(EntityModel.of(publisher));

        EntityModel<?> actual = service.add(publisher);
        Publisher actualPublisher = (Publisher) actual.getContent();

        assert actualPublisher != null;
        assertEquals(publisher.getId(), actualPublisher.getId());
        assertEquals(publisher.getName(), actualPublisher.getName());

        verify(repository).save(publisher);
        verify(assembler).toModel(publisher);
    }

    @SuppressWarnings("null")
    @Test
    public void whenUpdate_thenCorrectResponse() {
        Publisher publisher = getMockPublisher();

        when(repository.findById("1")).thenReturn(Optional.of(publisher));
        when(repository.save(publisher)).thenReturn(publisher);
        when(assembler.toModel(publisher)).thenReturn(EntityModel.of(publisher));

        Publisher publisherUpdate = getMockPublisher();
        publisherUpdate.setName("Orbit");

        EntityModel<?> actual = service.update("1", publisherUpdate);
        Publisher actualPublisher = (Publisher) actual.getContent();

        assert actualPublisher != null;
        assertEquals(publisher.getId(), actualPublisher.getId());
        assertEquals(publisher.getName(), actualPublisher.getName());

        verify(repository).findById("1");
        verify(repository).save(publisher);
        verify(assembler).toModel(publisher);
    }

    @Test
    public void whenUpdate_notFound_thenCorrectResponse() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        Exception ex = assertThrows(ItemNotFoundException.class, () -> {
            service.update("1", getMockPublisher());
        });

        assertEquals("Could not find item with id 1", ex.getMessage());

        verify(repository).findById("1");
        verify(repository, never()).save(any(Publisher.class));
        verify(assembler, never()).toModel(any(Publisher.class));
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

    private Publisher getMockPublisher() {
        return Publisher.builder()
                .id("1")
                .name("Pendant Publishing")
                .build();
    }
}
