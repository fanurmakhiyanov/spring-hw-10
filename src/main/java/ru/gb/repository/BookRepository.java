package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
}