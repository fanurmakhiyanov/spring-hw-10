package ru.gb.model.security;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Component
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "list_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Role> roleList = new ArrayList<>();

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public boolean setRole(Role role) {
        return roleList.add(role);
    }

    public boolean removeRole(Role role) {
        return roleList.remove(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", roleList=" + roleList +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}