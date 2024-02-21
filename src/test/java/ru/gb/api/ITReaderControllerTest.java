package ru.gb.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.gb.model.Book;
import ru.gb.model.Issue;
import ru.gb.model.Reader;
import ru.gb.repository.IssueRepository;
import ru.gb.repository.ReaderRepository;

import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITReaderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    IssueRepository issueRepository;

    Reader testReader;
    Issue testIssue;

    @BeforeEach
    void setUp() {
        testReader = new Reader("name");
        testReader.setId(1);

        testIssue = new Issue(1,1);
    }

    @AfterEach
    public void resetDb() {
        readerRepository.deleteAll();
        issueRepository.deleteAll();
    }

    @Test
    void addReader() {
        ResponseEntity<Reader> responseReader =
                restTemplate.postForEntity("/reader",testReader.getName(), Reader.class);

        assertNotNull(responseReader);
        assertEquals(HttpStatusCode.valueOf(201),responseReader.getStatusCode());
        assertEquals(testReader.getName(),responseReader.getBody().getName());
    }

    @Test
    void getReaderInfo() {
        readerRepository.save(testReader);

        ResponseEntity<Reader> response = restTemplate.getForEntity("/reader/" + testReader.getId(),
                Reader.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
        assertEquals(testReader,response.getBody());
    }

    @Test
    void getReaderInfo_NotFind() {
        ResponseEntity<Reader> response = restTemplate.getForEntity("/reader/" + testReader.getId(),Reader.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404),response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllIssuesReader() {
        issueRepository.save(testIssue);

        ResponseEntity<List<Issue>> responseReader =
                restTemplate.exchange("/reader/" + testReader.getId() + "/issue",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Issue>>(){
                        });

        assertNotNull(responseReader);
        assertEquals(HttpStatusCode.valueOf(200),responseReader.getStatusCode());
        assertEquals(testIssue,responseReader.getBody().get(0));
    }

    @Test
    void deleteReader() {
        readerRepository.save(testReader);

        ResponseEntity<Void> response =
                restTemplate.exchange("/reader/" + testReader.getId(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        Void.class);

        assertTrue(readerRepository.findById(1L).isEmpty());
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(204),response.getStatusCode());
    }

    @Test
    void deleteReader_NotFind() {
        ResponseEntity<Void> response =
                restTemplate.exchange("/reader/" + testReader.getId(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        Void.class);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(404),response.getStatusCode());
        assertNull(response.getBody());
    }


}