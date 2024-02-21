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
import ru.gb.model.dto.IssueRequest;
import ru.gb.repository.BookRepository;
import ru.gb.repository.IssueRepository;
import ru.gb.repository.ReaderRepository;
import ru.gb.util.IssueRejectedException;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IssuerServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    ReaderRepository readerRepository;
    @Mock
    IssueRepository issueRepository;
    @InjectMocks
    IssuerService issuerService;

    Book testBook;
    Reader testReader;
    Issue testIssue;

    @BeforeEach
    void setUp() {
        testBook = new Book(1,"title_1");
        testReader = new Reader("reader");
        testIssue = new Issue(1,1);
    }

    @Test
    void addIssue() throws IssueRejectedException {
        issuerService.addIssue(new IssueRequest(1,1));

        Mockito.verify(issueRepository).save(Mockito.any());
    }

    @Test
    void validateRequest_Positive() throws IssueRejectedException {
        Mockito.doReturn(Optional.of(testBook)).when(bookRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.of(testReader)).when(readerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(List.of(new Issue(2,2))).when(issueRepository).findAll();
        Mockito.doReturn(Collections.EMPTY_LIST).when(issueRepository).findByReturned(Mockito.any());

        issuerService.validateRequest(new IssueRequest(1,1));
    }

    @Test
    void validateRequest_BookNotFind() {
        Mockito.doReturn(Optional.empty()).when(bookRepository).findById(Mockito.anyLong());

        assertThrows(NoSuchElementException.class,() -> {issuerService.validateRequest(new IssueRequest(1,1));});
    }

    @Test
    void validateRequest_ReaderNotFind() {
        Mockito.doReturn(Optional.of(testBook)).when(bookRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.empty()).when(readerRepository).findById(Mockito.anyLong());

        assertThrows(NoSuchElementException.class,() -> {issuerService.validateRequest(new IssueRequest(1,1));});
    }

    @Test
    void validateRequest_BookNotAccessible() {
        Mockito.doReturn(Optional.of(testBook)).when(bookRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.of(testReader)).when(readerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(List.of(testIssue)).when(issueRepository).findAll();

        Throwable throwException = assertThrows(IssueRejectedException.class,
                () -> {issuerService.validateRequest(new IssueRequest(1,1));});

        assertEquals("Нет свободной книги",throwException.getMessage());

    }

    @Test
    void validateRequest_ReaderCanTakeBook() {
        Mockito.doReturn(Optional.of(testBook)).when(bookRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Optional.of(testReader)).when(readerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(Collections.EMPTY_LIST).when(issueRepository).findAll();
        Mockito.doReturn(List.of(testIssue)).when(issueRepository).findByReturned(null);

        Throwable throwException = assertThrows(IssueRejectedException.class,() -> {issuerService.validateRequest(new IssueRequest(1,1));});

        assertEquals("Читателю отказанно в получение книги",throwException.getMessage());
    }

    @Test
    void getIssueById() {
        Mockito.when(issueRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testIssue));

        Issue issue = issuerService.getIssueById(Mockito.anyLong());

        assertNotNull(issue);
        assertEquals(testIssue,issue);
    }

    @Test
    void getIssueById_NotFind() {
        Mockito.doReturn(Optional.empty()).when(issueRepository).findById(Mockito.anyLong());

        assertThrows(NoSuchElementException.class,() -> {issuerService.getIssueById(Mockito.anyLong());});
    }

    @Test
    void returnedBook() {
        Mockito.doReturn(Optional.of(testIssue)).when(issueRepository).findById(Mockito.anyLong());

        issuerService.returnedBook(Mockito.anyLong());

        Mockito.verify(issueRepository).save(testIssue);
    }

    @Test
    void returnedBook_IssueNotFind() {
        Mockito.doReturn(Optional.empty()).when(issueRepository).findById(Mockito.anyLong());

        assertThrows(NoSuchElementException.class, () -> {issuerService.returnedBook(Mockito.anyLong());});
    }

    @Test
    void getIssues() {
        issuerService.getIssues();

        Mockito.verify(issueRepository).findAll();
    }
}