package ru.gb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import ru.gb.util.aop.Timer;

@Getter
@Setter
@Entity
@Timer
@EqualsAndHashCode
@NoArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Schema(maxLength = 255)
    private String name;

    public Reader(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}