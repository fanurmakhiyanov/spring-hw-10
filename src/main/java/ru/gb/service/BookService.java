package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.gb.model.Book;
import ru.gb.model.Issue;
import ru.gb.repository.BookRepository;
import ru.gb.repository.IssueRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final IssueRepository issueRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
    public Book getBookById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(NoSuchElementException::new);
    }
    @Query("select * from issue where returned_at=null;")
    public List<Book> getAllAccessibleBooks() {
        List<Long> issueList = issueRepository.findByReturned(null).stream()
                .map(Issue::getBookId)
                .toList();
        return bookRepository.findAll().stream()
                .filter(book -> !issueList.contains(book.getId()))
                .collect(Collectors.toList());
    }
    public void removeBookById(long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(NoSuchElementException::new);
        bookRepository.delete(book);
    }



}