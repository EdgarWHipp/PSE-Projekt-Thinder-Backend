package com.pse.thinder.backend.database.features.account;

import javax.persistence.*;
import java.util.UUID;

@Entity @Table(name="users")
public abstract class User {


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition="character varying (100)")
    private UUID id;

    private String firstName;
    private String lastName;
    private String password;
    private String mail;

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
}
