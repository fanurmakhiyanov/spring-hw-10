package ru.gb.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.service.IssuerService;
import ru.gb.model.Issue;
import ru.gb.model.dto.IssueRequest;
import ru.gb.util.IssueRejectedException;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/issue")
@RequiredArgsConstructor
@Tag(name = "issue")
public class IssuerController {

    private final IssuerService issuerService;

    @GetMapping("/{issueId}")
    @Operation(summary = "Get information about issue")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Ok"),
            @ApiResponse(responseCode = "404",description = "Issue not found",content = @Content)})
    public ResponseEntity<Issue> getInfo(@PathVariable long issueId) {
        log.info("Запрос на информацию выдачи : issueId = {}", issueId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(issuerService.getIssueById(issueId));
    }

    @PutMapping("/{issueId}")
    @Operation(summary = "Return a book to library")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Ok"),
            @ApiResponse(responseCode = "404",description = "Issue not found",content = @Content)})
    public ResponseEntity<HttpStatus> returnedBook(@PathVariable long issueId) {
        log.info("Возвращение книги: issueId = {}", issueId);
        issuerService.returnedBook(issueId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping
    @Operation(summary = "Request book")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Request status Ok"),
            @ApiResponse(responseCode = "404",description = "Incorrect request data",content = @Content),
            @ApiResponse(responseCode = "403",description = "You are denied or there is no book available",content = @Content)})
    public ResponseEntity<Issue> issueBook(@RequestBody IssueRequest request) {
        log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.readerId(), request.bookId());
        try {
            issuerService.validateRequest(request);
        } catch (NoSuchElementException e) {
            log.info("Запрос на выдачу отклонен причина: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IssueRejectedException e) {
            log.info("Запрос на выдачу отклонен причина: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(issuerService.addIssue(request));
    }


}