package ru.gb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Запись о факте выдачи книги (в БД)
 */
@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long bookId;
    private long readerId;

    @Schema(name = "received", type = "date-time")
    private LocalDateTime received;

    @Schema(name = "returned",type = "date-time")
    private LocalDateTime returned;

    public Issue(long bookId, long readerId) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.received = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", readerId=" + readerId +
                ", received=" + received +
                ", returned=" + returned +
                '}';
    }
}