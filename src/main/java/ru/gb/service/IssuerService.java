package ru.gb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gb.model.dto.IssueRequest;
import ru.gb.model.Issue;
import ru.gb.repository.BookRepository;
import ru.gb.repository.IssueRepository;
import ru.gb.repository.ReaderRepository;
import ru.gb.util.IssueRejectedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IssuerService {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    private long maxCountBooks;

    @Autowired
    public void setMaxCountBooks(@Value("${application.max-allowed-books:1}") long maxCountBooks) {
        this.maxCountBooks = maxCountBooks;
    }

    public Issue addIssue(IssueRequest request){
        return issueRepository.save(new Issue(request.bookId(), request.readerId()));
    }

    public Issue getIssueById(long issueId) {
        return issueRepository.findById(issueId).orElseThrow(NoSuchElementException::new);
    }

    public void returnedBook(long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(NoSuchElementException::new);
        issue.setReturned(LocalDateTime.now());
        issueRepository.save(issue);
    }

    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    public void validateRequest(IssueRequest request) throws IssueRejectedException {
        bookRepository.findById(request.bookId()).orElseThrow(NoSuchElementException::new);
        readerRepository.findById(request.readerId()).orElseThrow(NoSuchElementException::new);
        if (!isBookAccessible(request.bookId())) {
            throw new IssueRejectedException("Нет свободной книги");
        }
        if (!readerCanTakeBook(request.readerId())) {
            throw new IssueRejectedException("Читателю отказанно в получение книги");
        }
    }

    private boolean isBookAccessible(long bookId) {
        List<Issue> issueList = issueRepository.findAll();
        if (issueList.size() == 0) {return true;}
        return issueList.stream().noneMatch(x -> x.getBookId() == bookId);
    }

    private boolean readerCanTakeBook(long readerId) {
        List<Issue> issueList = issueRepository.findByReturned(null);
        if (issueList.size() == 0) {return true;}
        return issueList.stream().filter(x -> x.getReaderId() == readerId).count() < maxCountBooks;
    }
}