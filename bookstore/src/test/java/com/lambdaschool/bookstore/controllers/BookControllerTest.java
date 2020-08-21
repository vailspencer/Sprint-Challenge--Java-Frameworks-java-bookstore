package com.lambdaschool.bookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.Author;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Section;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)

/*****
 * Due to security being in place, we have to switch out WebMvcTest for SpringBootTest
 * @WebMvcTest(value = BookController.class)
 */
@SpringBootTest(classes = BookstoreApplication.class)

/****
 * This is the user and roles we will use to test!
 */
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    /******
     * WebApplicationContext is needed due to security being in place.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> bookList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        /*****
         * The following is needed due to security being in place!
         */
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        /*****
         * Note that since we are only testing bookstore data, you only need to mock up bookstore data.
         * You do NOT need to mock up user data. You can. It is not wrong, just extra work.
         */

        bookList = new ArrayList<>();

        Author a1 = new Author("test_John", "Mitchell");
        Author a2 = new Author("test_Dan", "Brown");
        Author a3 = new Author("test_Jerry", "Poe");
        Author a4 = new Author("test_Wells", "Teague");
        Author a5 = new Author("test_George", "Gallinger");
        Author a6 = new Author("test_Ian", "Stewart");

        a1.setAuthorid(10);
        a2.setAuthorid(20);
        a3.setAuthorid(30);
        a4.setAuthorid(40);
        a5.setAuthorid(50);
        a6.setAuthorid(60);


        Section s1 = new Section("Fiction");
        Section s2 = new Section("Technology");
        Section s3 = new Section("Travel");
        Section s4 = new Section("Business");
        Section s5 = new Section("Religion");

        s1.setSectionid(70);
        s3.setSectionid(90);
        s4.setSectionid(100);

        Book b1 = new Book("test_Flatterland", "9780738206752", 2001, s1);
        b1.getWrotes()
            .add(new Wrote(a6, new Book()));
        b1.setBookid(120);

        Book b2 = new Book("test_Digital Fortess", "9788489367012", 2007, s1);
        b2.getWrotes()
            .add(new Wrote(a2, new Book()));
        b2.setBookid(130);

        Book b3 = new Book("test_The Da Vinci Code", "9780307474278", 2009, s1);
        b3.getWrotes()
            .add(new Wrote(a2, new Book()));
        b3.setBookid(140);

        Book b4 = new Book("test_Essentials of Finance", "1314241651234", 0, s4);
        b4.getWrotes()
            .add(new Wrote(a3, new Book()));
        b4.getWrotes()
            .add(new Wrote(a5, new Book()));
        b4.setBookid(150);

        Book b5 = new Book("test_Calling Texas Home", "1885171382134", 2000, s3);
        b5.getWrotes()
            .add(new Wrote(a4, new Book()));
        b5.setBookid(160);

        bookList.add(b1);
        bookList.add(b2);
        bookList.add(b3);
        bookList.add(b4);
        bookList.add(b5);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void listAllBooks() throws
            Exception
    {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll()).thenReturn(bookList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult request =mockMvc.perform(requestBuilder).andReturn();
        String actualReturn = request.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedReturn = mapper.writeValueAsString(bookList);

        assertEquals(expectedReturn, actualReturn);
    }

    @Test
    public void getBookById() throws
            Exception
    {
        String apiUrl = "/books/book/140";
        Mockito.when(bookService.findBookById(140)).thenReturn(bookList.get(2));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult request = mockMvc.perform(requestBuilder).andReturn();
        String actualReturn = request.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedReturn = mapper.writeValueAsString(bookList.get(2));
        assertEquals(expectedReturn, actualReturn);
    }

    @Test
    public void getNoBookById() throws
            Exception
    {
        String apiUrl = "book/book/140";
        Mockito.when(bookService.findBookById(140)).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult request = mockMvc.perform(requestBuilder).andReturn();
        String actualReturn = request.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedReturn = "";
        assertEquals(expectedReturn, actualReturn);
    }

    @Test
    public void addNewBook() throws
            Exception
    {
        String apiUrl = "/books/book";

        Section s1 = new Section("Fiction");
        s1.setSectionid(70);
        Author a1 = new Author("test_John", "Mitchell");
        a1.setAuthorid(10);

        Book b1 = new Book("test_Flight", "9780738206999", 2001, s1);
        b1.getWrotes()
            .add(new Wrote(a1, new Book()));
        b1.setBookid(170);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(b1);
        ObjectMapper mapper = new ObjectMapper();
        String expectedReturn = mapper.writeValueAsString(b1);

        RequestBuilder request = MockMvcRequestBuilders.post(apiUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(expectedReturn);
        mockMvc.perform(request).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullBook()
    {
    }

    @Test
    public void deleteBookById() throws
            Exception
    {
    }
}