package com.pse.thinder.backend.databaseFeatures;

import com.pse.thinder.backend.databaseFeatures.account.User;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name="universities")
public class University {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "character varying(50) unique not null")
    private String name;

    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE)
    private Set<User> members;

    @Column(columnDefinition = "character varying(50) not null")
    private String studentMailRegex;

    @Column(columnDefinition = "character varying(50) not null")
    private String supervisorMailRegex;

    protected University(){}

    public University(String name){
        this.name = name;
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
        return members;
    }

    public void addMember(User member) {
        this.members.add(member);
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
}