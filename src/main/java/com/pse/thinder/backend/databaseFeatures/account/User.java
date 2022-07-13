package com.pse.thinder.backend.databaseFeatures.account;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pse.thinder.backend.databaseFeatures.token.PasswordResetToken;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.token.VerificationToken;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Enumerated(EnumType.STRING)
    private Role role;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(30) not null")
    private String firstName;

    @Column(columnDefinition = "character varying(30) not null")
    private String lastName;

    @Size(min=8, max=20)
    @Column(columnDefinition = "character varying(50) not null")
    private String password;
    @Column(columnDefinition = "character varying(30) unique not null")
    private String mail;

    private Boolean active;

    public Role getRole() {
        return role;
    }

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<PasswordResetToken> passwordResetTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<VerificationToken> verificationTokens;

    //this is necessary due to JPA requirements for a non arg constructor.
    protected User(){}

    public User (String firstName, String lastName, String password,
                 String mail, University university, Role role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mail = mail;
        this.university = university;
        this.active = false;
        this.role = role;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<PasswordResetToken> getPasswordResetTokens() {
        return passwordResetTokens;
    }

    public void addPasswordResetTokens(PasswordResetToken passwordResetToken) {
        this.passwordResetTokens.add(passwordResetToken);
    }

    public Set<VerificationToken> getVerificationTokens() {
        return verificationTokens;
    }

    public void addVerificationTokens(VerificationToken verificationToken) {
        this.verificationTokens.add(verificationToken);
    }
}
