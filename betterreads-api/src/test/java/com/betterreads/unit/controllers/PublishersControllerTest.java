package com.betterreads.unit.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.betterreads.controllers.PublishersController;
import com.betterreads.models.Publisher;
import com.betterreads.services.IService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@WebMvcTest
@AutoConfigureMockMvc
public class PublishersControllerTest {

    private static final String MEDIA_TYPE_APPLICATION_HAL_JSON = "application/hal+json";

    private static final String BASE_URL = "/v1/publishers";

    @MockBean
    private IService publishersService;

    @Autowired
    PublishersController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAllPublishers_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        EntityModel<Publisher> entity = getMockEntityModel(publisher);
        List<EntityModel<Publisher>> publishers = new ArrayList<>();
        publishers.add(entity);

        doReturn(publishers).when(publishersService).getAll();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(2)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$._embedded.publishers[0].name", Is.is(publisher.getName())));
    }

    @Test
    public void whenGetPublisherById_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        EntityModel<Publisher> entity = getMockEntityModel(publisher);

        doReturn(entity).when(publishersService).getById("1");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(publisher.getId())));
    }

    @Test
    public void whenGetPublisherById_notFound_thenCorrectResponse() throws Exception {
        doReturn(null).when(publishersService).getById("1");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenSearchPublishers_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        EntityModel<Publisher> entity = getMockEntityModel(getMockPublisher());
        List<EntityModel<Publisher>> publishers = new ArrayList<>();
        publishers.add(entity);

        Publisher search = Publisher.builder().name("pendant").build();

        doReturn(publishers).when(publishersService).search(search);

        String searchJson = new ObjectMapper().writeValueAsString(search);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/search")
                .content(searchJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(2)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$._embedded.publishers[0].name", Is.is(publisher.getName())));
    }

    @Test
    public void whenAddPublisherWithValidRequest_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        String publisherJson = new ObjectMapper().writeValueAsString(publisher);

        EntityModel<Publisher> expected = getMockEntityModel(publisher);

        doReturn(expected).when(publishersService).add(publisher);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(publisherJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MEDIA_TYPE_APPLICATION_HAL_JSON));
    }

    @Test
    public void whenAddPublisherWithInvalidRequest_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        publisher.setName(StringUtils.EMPTY);
        String publisherJson = new ObjectMapper().writeValueAsString(publisher);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(publisherJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenUpdatePublisherWithValidRequest_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        String publisherJson = new ObjectMapper().writeValueAsString(publisher);

        EntityModel<Publisher> expected = getMockEntityModel(publisher);

        doReturn(expected).when(publishersService).update("1", publisher);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .content(publisherJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MEDIA_TYPE_APPLICATION_HAL_JSON));
    }

    @Test
    public void whenUpdatePublisherWithInvalidRequest_thenCorrectResponse() throws Exception {
        Publisher publisher = getMockPublisher();
        publisher.setName(StringUtils.EMPTY);
        String publisherJson = new ObjectMapper().writeValueAsString(publisher);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .content(publisherJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenDeletePublisherWithId_thenCorrectResponse() throws Exception {
        doNothing().when(publishersService).delete("1");

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void whenDeleteAllPublishers_thenCorrectResponse() throws Exception {
        doNothing().when(publishersService).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private Publisher getMockPublisher() {

        return Publisher.builder()
                .id("1")
                .name("Pendant Publishing")
                .build();
    }

    public EntityModel<Publisher> getMockEntityModel(Publisher entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PublishersController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(PublishersController.class).getAll()).withRel("v1/publishers"));
    }
}
