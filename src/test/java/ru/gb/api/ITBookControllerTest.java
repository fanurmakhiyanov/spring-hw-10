package ru.gb.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.gb.model.Book;
import ru.gb.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITBookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    BookRepository bookRepository;

    Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book(1,"test");
    }

    @AfterEach
    public void resetDb() {
        bookRepository.deleteAll();
    }

    @Test
    void addBook() {
        ResponseEntity<Book> responseBook = restTemplate.postForEntity("/book",testBook,Book.class);

        assertNotNull(responseBook);
        assertEquals(HttpStatusCode.valueOf(201),responseBook.getStatusCode());
        assertEquals(testBook,responseBook.getBody());
    }

    @Test
    void getBookInfo() {
        bookRepository.save(testBook);

        ResponseEntity<Book> response = restTemplate.getForEntity("/book/" + testBook.getId(),Book.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
        assertEquals(testBook,response.getBody());
    }

    @Test
    void getBookInfo_NotFind() {
        ResponseEntity<Book> response = restTemplate.getForEntity("/book/" + testBook.getId(),Book.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404),response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteBook() {
        bookRepository.save(testBook);

        ResponseEntity<Void> response = restTemplate.exchange("/book/1", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertTrue(bookRepository.findById(1L).isEmpty());
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(204),response.getStatusCode());
    }

    @Test
    void deleteBook_NotFind() {
        ResponseEntity<Void> response = restTemplate.exchange("/book/123", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404),response.getStatusCode());
        assertNull(response.getBody());
    }
}