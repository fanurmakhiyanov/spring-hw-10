package ru.gb.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.model.Book;
import ru.gb.service.BookService;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "book")
public class BookController {

    /**
     * Book service.
     */
    private final BookService bookService;

    /**
     * @param bookId - id book
     * @return ResponseEntity
     */
    @GetMapping("/{bookId}")
    @Operation(summary = "Get information about book")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "Ok"),
            @ApiResponse(responseCode = "404",description = "Book not found",content = @Content)})
    public ResponseEntity<Book> getBookInfo(@PathVariable final long bookId) {
        log.info("Запрос на информацию о книге: bookId = {}", bookId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getBookById(bookId));
    }

    @DeleteMapping("/{bookId}")
    @Operation(summary = "Delete book from library")
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "Book delete from library"),
            @ApiResponse(responseCode = "404",description = "Book not found",content = @Content)})
    public ResponseEntity<Void> deleteBook(@PathVariable final long bookId) {
        log.info("Запрос на удаления книги: bookId = {}", bookId);
        bookService.removeBookById(bookId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping
    @Operation(summary = "Add book to library")
    @ApiResponse(responseCode = "201",description = "Book add to library")
    public ResponseEntity<Book> addBook(@RequestBody final Book book) {
        log.info("Запрос на добавления книги: bookId = {}, bookName = {}", book.getId(), book.getTitle());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.addBook(book));
    }
}