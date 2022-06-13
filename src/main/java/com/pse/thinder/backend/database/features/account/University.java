package com.pse.thinder.backend.database.features.account;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name="universities")
public class University {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "university")
    private Set<User> members;

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

    public void setMembers(Set<User> members) {
        this.members = members;
    }
}
