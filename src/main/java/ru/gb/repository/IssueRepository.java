package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.Issue;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long> {
    public List<Issue> findByReturned(LocalDateTime dateTime);
}