package com.pse.thinder.backend.databaseFeatures.account;

import com.fasterxml.jackson.annotation.*;
import com.pse.thinder.backend.databaseFeatures.token.PasswordResetToken;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.token.VerificationToken;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = false)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Student.class, name = "STUDENT"),
    @JsonSubTypes.Type(value = Supervisor.class, name = "SUPERVISOR"),
    @JsonSubTypes.Type(value = PlainUser.class, name = "USER")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(30) not null")
    private String firstName;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(30) not null")
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min=8, max=50, groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(50) not null")
    private String password;

    @NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(30) unique not null")
    private String mail;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Role userGroup;
    
    @JsonIgnore
    @ElementCollection(targetClass=Authorities.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="user_authorities")
    @Column(name="authorities") // Column name in person_interest
    private Collection<Authorities> authorities;

    @JsonIgnore
    private Boolean active;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isComplete;
    
    
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty(value = "uni_id", access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<PasswordResetToken> passwordResetTokens;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<VerificationToken> verificationTokens;


    //this is necessary due to JPA requirements for a non arg constructor.
    protected User(){}

    public User(String firstName, String lastName, String password,
                 String mail, University university, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mail = mail;
        this.university = university;
        this.userGroup = role;
        this.active = false;
        this.isComplete = false;
        this.passwordResetTokens = new HashSet<>();
        this.verificationTokens = new HashSet<>();
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
    
    public void removePasswordResetTokens(PasswordResetToken passwordResetToken) {
        this.passwordResetTokens.remove(passwordResetToken);
    }

    public Set<VerificationToken> getVerificationTokens() {
        return verificationTokens;
    }

    public void addVerificationTokens(VerificationToken verificationToken) {
        this.verificationTokens.add(verificationToken);
    }
    
    public void removeVerificationTokens(VerificationToken verificationToken) {
        this.verificationTokens.remove(verificationToken);
    }

    public Role getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Role userGroup) {
        this.userGroup = userGroup;
    }

	public Collection<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authorities> authorities) {
		this.authorities = authorities;
	}

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}
