package ru.school21.cleaningwebsite.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name="usertelegram")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="firstname")
    private String firstName;
    @Column(name="lastname")
    private String lastName;
    @Column(name="username")
    private String userName;
    @Column(name="idtelegram")
    private Integer idTelegram;
    public User() {
    }

    public Integer getIdTelegram() {
        return idTelegram;
    }

    public void setIdTelegram(Integer idTelegram) {
        this.idTelegram = idTelegram;
    }

    public User(Integer id, String firstName, String lastName, String userName, Integer idTelegram) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.idTelegram = idTelegram;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
