package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Before
    public void setUp() throws
            Exception
    {
        MockitoAnnotations.initMocks(this);
        List<Book> books = bookService.findAll();
        for (Book book : books)
        {
            System.out.println("Book ID: " + book.getBookid() + ", Book Title: " + book.getTitle());
        }
    }

    @After
    public void tearDown() throws
            Exception
    {
        System.out.println("********** AFTER ************");
        List<Book> books = bookService.findAll();
        for(Book book:books){
            System.out.println("Book ID: " +book.getBookid() + ", Book Title: "+book.getTitle());
        }
    }

    @Test
    public void findAll()
    {
        assertEquals(5,bookService.findAll().size());
    }

    @Test
    public void findBookById()
    {
        assertEquals("test_Essentials of Finance", bookService.findBookById(29).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFindBookById()
    {
        bookService.findBookById(99);
    }

    @Test
    public void delete()
    {
        assertEquals(5, bookService.findAll().size());
        bookService.delete(26);
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void save()
    {
        Book book = new Book();
        book.setTitle("Hello, World - Java");
        book.setIsbn("dbzahstmnt");
        book.setCopy(500);
        assertEquals(4, bookService.findAll().size());
        bookService.save(book);
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void update()
    {
        Book book = new Book();
        book.setTitle("The Good Book");
        assertEquals("test_Calling Texas Home", bookService.findBookById(30).getTitle());
        assertEquals("The Good Book", bookService.update(book,30).getTitle());
    }

    @Test
    public void deleteAll()
    {
        bookService.deleteAll();
        assertEquals(0, bookService.findAll().size());
    }
}