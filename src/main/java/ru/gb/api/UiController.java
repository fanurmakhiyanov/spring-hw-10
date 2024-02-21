package ru.gb.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.model.dto.IssueResponse;
import ru.gb.model.Issue;
import ru.gb.service.BookService;
import ru.gb.service.IssuerService;
import ru.gb.service.ReaderService;
import ru.gb.util.aop.Timer;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
@Tag(name = "UI")
public class UiController {

    private final BookService bookService;
    private final ReaderService readerService;
    private final IssuerService issuerService;

    @Timer()
    @GetMapping("/books")
    public String available(Model model) {
        model.addAttribute("books",bookService.getAllAccessibleBooks());
        return "available";
    }

    @Timer()
    @GetMapping("/readers")
    public String readers(Model model) {
        model.addAttribute("readers", readerService.getAllReaders());
        return "readers";
    }

    @Timer()
    @GetMapping("/readers/{id}")
    public String readers(@PathVariable long id, Model model) {
        model.addAttribute("reader", readerService.getReaderById(id));
        model.addAttribute("books", readerService.getAllBooksReader(id));
        return "reader_books";
    }

    @Timer()
    @GetMapping("/issues")
    public String issues (Model model) {
        List<IssueResponse> issueResponseList = new ArrayList<>();
        for (Issue issue : issuerService.getIssues()) {
            issueResponseList.add( new IssueResponse(
                    bookService.getBookById(issue.getBookId()).getTitle(),
                    readerService.getReaderById(issue.getReaderId()).getName(),
                    issue.getReceived(),
                    issue.getReturned()
            ));
        }
        model.addAttribute("rows", issueResponseList);
        return "table";
    }
}