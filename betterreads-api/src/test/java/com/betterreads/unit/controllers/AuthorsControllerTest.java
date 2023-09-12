package com.betterreads.unit.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import static org.hamcrest.Matchers.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
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

import com.betterreads.controllers.AuthorsController;
import com.betterreads.models.Author;
import com.betterreads.services.IService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@WebMvcTest
@AutoConfigureMockMvc
public class AuthorsControllerTest {

    private static final String MEDIA_TYPE_APPLICATION_HAL_JSON = "application/hal+json";

    private static final String BASE_URL = "/v1/authors";

    @MockBean
    private IService authorsService;

    @Autowired
    AuthorsController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAllAuthors_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        EntityModel<Author> entity = getMockEntityModel(author);
        List<EntityModel<Author>> Authors = new ArrayList<>();
        Authors.add(entity);

        doReturn(Authors).when(authorsService).getAll();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.authors[0].firstName",
                        Is.is(author.getFirstName())));
    }

    @Test
    public void whenGetAuthorById_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        EntityModel<Author> entity = getMockEntityModel(author);

        doReturn(entity).when(authorsService).getById("1");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(author.getId())));
    }

    @Test
    public void whenGetAuthorById_notFound_thenCorrectResponse() throws Exception {
        doReturn(null).when(authorsService).getById("1");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenSearchAuthors_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        EntityModel<Author> entity = getMockEntityModel(getMockAuthor());
        List<EntityModel<Author>> Authors = new ArrayList<>();
        Authors.add(entity);

        Author search = Author.builder().city("modesto").build();

        doReturn(Authors).when(authorsService).search(search);

        String searchJson = new ObjectMapper().writeValueAsString(search);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/search")
                .content(searchJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.authors[0].city", Is.is(author.getCity())));
    }

    @Test
    public void whenAddAuthorWithValidRequest_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        String authorJson = new ObjectMapper().writeValueAsString(author);

        EntityModel<Author> expected = getMockEntityModel(author);

        doReturn(expected).when(authorsService).add(author);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(authorJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MEDIA_TYPE_APPLICATION_HAL_JSON));
    }

    @Test
    public void whenAddAuthorWithInvalidRequest_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        author.setLastName(StringUtils.EMPTY);
        String authorJson = new ObjectMapper().writeValueAsString(author);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(authorJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Is.is("Last Name is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenUpdateAuthorWithValidRequest_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        String authorJson = new ObjectMapper().writeValueAsString(author);

        EntityModel<Author> expected = getMockEntityModel(author);

        doReturn(expected).when(authorsService).update("1", author);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .content(authorJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MEDIA_TYPE_APPLICATION_HAL_JSON));
    }

    @Test
    public void whenUpdateAuthorWithInvalidRequest_thenCorrectResponse() throws Exception {
        Author author = getMockAuthor();
        author.setLastName(StringUtils.EMPTY);
        String authorJson = new ObjectMapper().writeValueAsString(author);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .content(authorJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Is.is("Last Name is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenDeleteAuthorWithId_thenCorrectResponse() throws Exception {
        doNothing().when(authorsService).delete("1");

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void whenDeleteAllAuthors_thenCorrectResponse() throws Exception {
        doNothing().when(authorsService).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private Author getMockAuthor() {
        return Author.builder()
                .id("1")
                .firstName("George")
                .middleName("Michael")
                .lastName("Bluth")
                .dateOfBirth(new GregorianCalendar(1987, Calendar.JULY, 13).getTime())
                .city("Modesto")
                .state("CA")
                .build();
    }

    public EntityModel<Author> getMockEntityModel(Author entity) {
        return EntityModel.of(entity, linkTo(methodOn(AuthorsController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(AuthorsController.class).getAll()).withRel("v1/Authors"));
    }
}
