package ru.gb.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.gb.model.Book;
import ru.gb.model.Issue;
import ru.gb.model.Reader;
import ru.gb.repository.BookRepository;
import ru.gb.repository.IssueRepository;
import ru.gb.repository.ReaderRepository;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITIssuerControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;

    Issue testIssue;

    @BeforeEach
    void setUp() {
        testIssue = new Issue(1,1);
        issueRepository.deleteAll();
        bookRepository.deleteAll();
        readerRepository.deleteAll();
    }
    @Test
    void getInfo() {
        issueRepository.save(testIssue);

        ResponseEntity<Issue> responseIssue =
                restTemplate.getForEntity("/issue/" + testIssue.getId(),Issue.class);

        assertNotNull(responseIssue);
        assertEquals(HttpStatusCode.valueOf(200),responseIssue.getStatusCode());
        assertEquals(testIssue,responseIssue.getBody());
    }

    @Test
    void getInfo_NotFind() {
        ResponseEntity<Issue> responseIssue =
                restTemplate.getForEntity("/issue/" + testIssue.getId(),Issue.class);

        assertNotNull(responseIssue);
        assertEquals(HttpStatusCode.valueOf(404),responseIssue.getStatusCode());
        assertNull(responseIssue.getBody());
    }

    @Test
    void returnedBook() {
        issueRepository.save(testIssue);

        ResponseEntity<Void> responseIssue =
                restTemplate.exchange("/issue/" + testIssue.getId(),HttpMethod.PUT, HttpEntity.EMPTY,Void.class);

        assertNotNull(responseIssue);
        Issue closeIssue = issueRepository.findById(testIssue.getId()).get();
        assertNotNull(closeIssue.getReturned());
    }

    @Test
    void returnedBook_NotFind() {
        ResponseEntity<Void> responseIssue =
                restTemplate.exchange("/issue/" + testIssue.getId(),HttpMethod.PUT, HttpEntity.EMPTY,Void.class);

        assertNotNull(responseIssue);
        assertEquals(HttpStatusCode.valueOf(404),responseIssue.getStatusCode());
        assertNull(responseIssue.getBody());
    }

    @Test
    void issueBook() {
        Book testBook = bookRepository.save(new Book(1,"title"));
        Reader testReader = readerRepository.save(new Reader("name"));

        ResponseEntity<Issue> response =
                restTemplate.postForEntity("/issue",new Issue(testBook.getId(),testReader.getId()),Issue.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
    }

    @Test
    void issueBook_BookNotFind() {
        ResponseEntity<Issue> response =
                restTemplate.postForEntity("/issue",testIssue,Issue.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404),response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void issueBook_ReaderNotFind() {
        bookRepository.save(new Book(1,"title"));

        ResponseEntity<Issue> response =
                restTemplate.postForEntity("/issue",testIssue,Issue.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404),response.getStatusCode());
        assertNull(response.getBody());
    }
}