package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.security.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}