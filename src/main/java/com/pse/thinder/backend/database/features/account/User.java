package com.pse.thinder.backend.database.features.account;

import com.pse.thinder.backend.database.features.University;

import javax.persistence.*;
import java.util.UUID;

@Entity @Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(30) not null")
    private String firstName;

    @Column(columnDefinition = "character varying(30) not null")
    private String lastName;

    @Column(columnDefinition = "character varying(50) not null")
    private String password;
    @Column(columnDefinition = "character varying(30) unique not null")
    private String mail;

    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    //this is necessary due to JPA requirements for a non arg constructor.
    protected User(){}

    public User (String firstName, String lastName, String password,
                 String mail){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mail = mail;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
