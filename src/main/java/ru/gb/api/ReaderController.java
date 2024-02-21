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

import ru.gb.model.Issue;
import ru.gb.model.Reader;

import ru.gb.service.ReaderService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reader")
@RequiredArgsConstructor
@Tag(name = "reader")
public class ReaderController {

    private final ReaderService readerService;

    @PostMapping
    @Operation(summary = "Add reader to library")
    @ApiResponse(responseCode = "201",description = "Reader add to library")
    public ResponseEntity<Reader> addReader(@RequestBody String readerName ) {
        log.info("Запрос на добавления читателя: readerName = {}", readerName);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readerService.addReader(new Reader(readerName)));
    }

    @GetMapping("/{readerId}")
    @Operation(summary = "Get information about reader")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Ok"),
            @ApiResponse(responseCode = "404",description = "Reader not found",content = @Content)})
    public ResponseEntity<Reader> getReaderInfo(@PathVariable long readerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readerService.getReaderById(readerId));
    }

    @GetMapping("/{readerId}/issue")
    @Operation(summary = "Get all the reader's issue")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Ok"),
            @ApiResponse(responseCode = "404",description = "Reader not found",content = @Content)})
    public ResponseEntity<List<Issue>> getAllIssuesReader(@PathVariable long readerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readerService.getAllIssueReader(readerId));
    }

    @DeleteMapping("/{readerId}")
    @Operation(summary = "Delete reader from library")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "Reader delete from library"),
            @ApiResponse(responseCode = "404",description = "Reader not found",content = @Content)})
    public ResponseEntity<HttpStatus> deleteReader(@PathVariable long readerId) {
        readerService.removeReaderById(readerId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}