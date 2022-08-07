package com.pse.thinder.backend.databaseFeatures;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name="universities")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class University {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(min=1, max=50)
    @Column(columnDefinition = "character varying(50) unique not null")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE)
    private Set<User> members;

    @NotBlank
    @Size(min=1, max=50)
    @Column(columnDefinition = "character varying(50) not null")
    private String studentMailRegex;

    @NotBlank
    @Size(min=1, max=50)
    @Column(columnDefinition = "character varying(50) not null")
    private String supervisorMailRegex;

    @JsonIgnore
    @OneToMany(mappedBy = "university", cascade =  CascadeType.REMOVE)
    private List<Degree> degrees;

    protected University(){}

    public University(String name, String studentMailRegex, String supervisorMailRegex) {
		super();
		this.name = name;
		this.studentMailRegex = studentMailRegex;
		this.supervisorMailRegex = supervisorMailRegex;
		this.members = new HashSet<>();
		this.degrees = new ArrayList<>();
	}

	public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return new HashSet<>(this.members);
    }

    public void addMember(User member) {
        this.members.add(member);
    }
    
    public void removeMember(User member) {
        this.members.remove(member);
    }

    public String getStudentMailRegex() {
        return studentMailRegex;
    }

    public void setStudentMailRegex(String studentMailRegex) {
        this.studentMailRegex = studentMailRegex;
    }

    public String getSupervisorMailRegex() {
        return supervisorMailRegex;
    }

    public void setSupervisorMailRegex(String supervisorMailRegex) {
        this.supervisorMailRegex = supervisorMailRegex;
    }

    public List<Degree> getDegrees() {
        return new ArrayList<>(this.degrees);
    }

    public void addDegree(Degree degree) {
        this.degrees.add(degree);
    }
    
    public void removeDegree(Degree degree) {
        this.degrees.remove(degree);
    }
}
