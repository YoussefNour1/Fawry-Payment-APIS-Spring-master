package com.se.fawry.model.entity;

import com.se.fawry.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int id;

    private String username;
    private String email;
    private String password;

    private double walletBalance;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany
    protected List<CreditCard> cards;

    public List<CreditCard> getCards() {
        return cards;
    }

    public void setCards(List<CreditCard> cards) {
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(int id, String username, String email, String password, double walletBalance, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.walletBalance = walletBalance;
        this.role = role;
    }
    public User(String username, String email, String password, double walletBalance, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.walletBalance = walletBalance;
        this.role = role;
    }
    public User() { }

    // getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    // override toString

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", walletBalance=" + walletBalance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.walletBalance, walletBalance) == 0 && (id == user.id) && username.equals(user.username) && email.equals(user.email) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, walletBalance);
    }
}