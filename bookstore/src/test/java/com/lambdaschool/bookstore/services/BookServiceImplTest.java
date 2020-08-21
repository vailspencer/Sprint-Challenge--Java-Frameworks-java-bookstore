package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{

    @Autowired
    private BookService bookService;

    @Autowired
    private SectionService sectionService;

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
    public void a_findAll()
    {
        assertEquals(5,bookService.findAll().size());
    }

    @Test
    public void b_findBookById()
    {
        assertEquals("Essentials of Finance", bookService.findBookById(29).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void c_notFindBookById()
    {
        assertEquals("Essentials of Finance", bookService.findBookById(296).getTitle());
    }

    @Test
    public void d_delete()
    {
        assertEquals(5, bookService.findAll().size());
        bookService.delete(26);
        assertEquals(4, bookService.findAll().size());
    }

    @Test
    public void e_save()
    {
        Book book = new Book();
        book.setTitle("Atlantis is real. The True Story");
        book.setIsbn("8967156891");
        book.setCopy(700);
        assertEquals(4, bookService.findAll().size());
        bookService.save(book);
        assertEquals(5, bookService.findAll().size());

    }

    @Test
    public void update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}