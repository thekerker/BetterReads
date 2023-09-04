package com.betterreads.unit.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import static org.hamcrest.Matchers.*;

import java.util.Calendar;
import java.util.Date;
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

import com.betterreads.controllers.BooksController;
import com.betterreads.models.Author;
import com.betterreads.models.Author.Name;
import com.betterreads.models.Book;
import com.betterreads.models.Publisher;
import com.betterreads.models.Search;
import com.betterreads.services.IService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@WebMvcTest
@AutoConfigureMockMvc
public class BooksControllerTest {

    private static final String MEDIA_TYPE_APPLICATION_HAL_JSON = "application/hal+json";

    private static final String BASE_URL = "/v1/books";

    @MockBean
    private IService booksService;

    @Autowired
    BooksController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenGetAllBooks_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        EntityModel<Book> entity = getMockEntityModel(book);
        List<EntityModel<Book>> books = new ArrayList<>();
        books.add(entity);

        doReturn(books).when(booksService).getAll();

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.books[0].title", Is.is(book.getTitle())));
    }

    @Test
    public void whenGetBookById_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        EntityModel<Book> entity = getMockEntityModel(book);

        doReturn(entity).when(booksService).getById("1");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(book.getId())));
    }

    @Test
    public void whenGetBookById_notFound_thenCorrectResponse() throws Exception {
        doReturn(null).when(booksService).getById("1");

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenSearchBooksWithValidRequest_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        EntityModel<Book> entity = getMockEntityModel(getMockBook());
        List<EntityModel<Book>> books = new ArrayList<>();
        books.add(entity);

        Search search = Search.builder().searchTerm("awesome").build();

        doReturn(books).when(booksService).search(search);

        String searchJson = new ObjectMapper().writeValueAsString(search);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/search")
                .content(searchJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", aMapWithSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.books[0].title", Is.is(book.getTitle())));
    }

    @Test
    public void whenSearchBooksWithInvalidRequest_thenCorrectResponse() throws Exception {
        Search search = Search.builder().searchTerm(StringUtils.EMPTY).build();

        String searchJson = new ObjectMapper().writeValueAsString(search);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/search")
                .content(searchJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.searchTerm", Is.is("Must supply a search term")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenAddBookWithValidRequest_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        String bookJson = new ObjectMapper().writeValueAsString(book);

        EntityModel<Book> expected = getMockEntityModel(book);

        doReturn(expected).when(booksService).add(book);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(bookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MEDIA_TYPE_APPLICATION_HAL_JSON));
    }

    @Test
    public void whenAddBookWithInvalidRequest_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        book.setIsbn(StringUtils.EMPTY);
        String bookJson = new ObjectMapper().writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .content(bookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn", Is.is("ISBN is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenUpdateBookWithValidRequest_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        String bookJson = new ObjectMapper().writeValueAsString(book);

        EntityModel<Book> expected = getMockEntityModel(book);

        doReturn(expected).when(booksService).update("1", book);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .content(bookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MEDIA_TYPE_APPLICATION_HAL_JSON));
    }

    @Test
    public void whenUpdateBookWithInvalidRequest_thenCorrectResponse() throws Exception {
        Book book = getMockBook();
        book.setIsbn(StringUtils.EMPTY);
        String bookJson = new ObjectMapper().writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .content(bookJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn", Is.is("ISBN is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void whenDeleteBookWithId_thenCorrectResponse() throws Exception {
        doNothing().when(booksService).delete("1");

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void whenDeleteAllBooks_thenCorrectResponse() throws Exception {
        doNothing().when(booksService).deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private Book getMockBook() {
        Author author = Author.builder()
                .id("1")
                .name(Name.builder().firstName("George").middleName("Michael").lastName("Bluth").build())
                .dateOfBirth(new GregorianCalendar(1987, Calendar.JULY, 13).getTime())
                .city("Modesto")
                .state("CA")
                .build();

        Author[] authors = { author };

        return Book.builder()
                .id("1")
                .isbn("000-5555523")
                .title("My Awesome Book")
                .authors(authors)
                .pages(351)
                .genres(new String[] { "non-fiction", "autobiography" })
                .publishedDate(new Date())
                .publisher(Publisher.builder().id("1").name("McGraw").build())
                .build();
    }

    public EntityModel<Book> getMockEntityModel(Book entity) {
        return EntityModel.of(entity, linkTo(methodOn(BooksController.class).getById(entity.getId())).withSelfRel(),
                linkTo(methodOn(BooksController.class).getAll()).withRel("v1/books"));
    }
}
