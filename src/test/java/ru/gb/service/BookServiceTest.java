package ru.gb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gb.model.Book;
import ru.gb.model.Issue;
import ru.gb.repository.BookRepository;
import ru.gb.repository.IssueRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    IssueRepository issueRepository;
    @InjectMocks
    BookService bookService;
    Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book(1,"title_1");
    }

    @Test
    void addBook() {
        Mockito.when(bookRepository.save(testBook)).thenReturn(testBook);

        Book addBook = bookService.addBook(testBook);

        assertNotNull(addBook);
        assertEquals(testBook,addBook);
    }

    @Test
    void getBookById() {
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testBook));

        Book bookById = bookService.getBookById(Mockito.anyLong());

        assertNotNull(bookById);
        assertEquals(testBook,bookById);
    }

    @Test
    void getBookById_notFind() {
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,() -> {bookService.getBookById(Mockito.anyLong());});
    }

    @Test
    void getAllAccessibleBooks() {
        List<Book> books = List.of(testBook,new Book(2,"title_2"));
        Mockito.when(bookRepository.findAll()).thenReturn(books);
        Mockito.when(issueRepository.findByReturned(null))
                .thenReturn(List.of(new Issue(1L,1)));

        List<Book> allAccessibleBooks = bookService.getAllAccessibleBooks();

        assertNotNull(allAccessibleBooks);
        assertEquals(List.of(books.get(1)),allAccessibleBooks);
    }

    @Test
    void removeBookById() {
        Mockito.doReturn(Optional.of(testBook)).when(bookRepository).findById(Mockito.anyLong());
        bookService.removeBookById(Mockito.anyLong());

        Mockito.verify(bookRepository).delete(testBook);
    }
}