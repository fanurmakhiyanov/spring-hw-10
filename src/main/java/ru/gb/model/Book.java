package ru.gb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable{

    @Id
    private long id;

    @Schema(maxLength = 255)
    private String title;
}