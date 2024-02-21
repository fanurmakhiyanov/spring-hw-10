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
import ru.gb.model.Reader;
import ru.gb.repository.BookRepository;
import ru.gb.repository.IssueRepository;
import ru.gb.repository.ReaderRepository;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    IssueRepository issueRepository;
    @Mock
    ReaderRepository readerRepository;
    @InjectMocks
    ReaderService readerService;
    Reader testReader;
    Book testBook;

    @BeforeEach
    void setUp() {
        testReader = new Reader("name_1");
        testBook = new Book(1,"title_1");
    }

    @Test
    void addReader() {
        Mockito.doReturn(testReader).when(this.readerRepository).save(testReader);

        Reader addReader = readerService.addReader(testReader);

        assertNotNull(addReader);
        assertEquals(testReader,addReader);
    }

    @Test
    void getReaderById() {
        Mockito.doReturn(Optional.of(testReader)).when(this.readerRepository).findById(Mockito.anyLong());

        Reader readerById = readerService.getReaderById(Mockito.anyLong());

        assertNotNull(readerById);
        assertEquals(testReader,readerById);
    }

    @Test
    void getReaderById_notFind() {
        Mockito.doReturn(Optional.empty()).when(this.readerRepository).findById(Mockito.anyLong());

        assertThrows(NoSuchElementException.class, () -> {readerService.getReaderById(Mockito.anyLong());});
    }

    @Test
    void removeReaderById() {
        Mockito.doReturn(Optional.of(testReader)).when(readerRepository).findById(Mockito.anyLong());
        readerService.removeReaderById(Mockito.anyLong());

        Mockito.verify(readerRepository).deleteById(Mockito.anyLong());
    }

    @Test
    void getAllIssueReader() {
        List<Issue> issues = List.of(new Issue(1,testReader.getId()));
        Mockito.doReturn(issues).when(issueRepository).findByReturned(null);

        List<Issue> allIssueReader = readerService.getAllIssueReader(Mockito.anyLong());

        assertNotNull(allIssueReader);
        assertEquals(issues,allIssueReader);
    }

    @Test
    void getAllReaders() {
        List<Reader> readers = List.of(testReader,new Reader("test_2"));
        Mockito.doReturn(readers).when(readerRepository).findAll();

        List<Reader> serviceAllReaders = readerService.getAllReaders();

        assertNotNull(serviceAllReaders);
        assertEquals(readers,serviceAllReaders);
    }

    @Test
    void getAllBooksReader() {
        List<Issue> issues = List.of(new Issue(testBook.getId(),testReader.getId()));
        Mockito.doReturn(issues).when(issueRepository).findByReturned(null);
        Mockito.doReturn(Optional.of(testBook)).when(bookRepository).findById(Mockito.anyLong());

        List<Book> books = readerService.getAllBooksReader(Mockito.anyLong());

        assertNotNull(books);
        assertEquals(testBook,books.get(0));
    }
}