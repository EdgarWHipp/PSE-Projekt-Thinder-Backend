package com.pse.thinder.backend.database.features.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.pse.thinder.backend.database.features.University;
import com.pse.thinder.backend.database.features.thesis.Thesis;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Supervisor extends User {



    private String acedemicDegree; //could this be an enum?

    @Column(columnDefinition = "character varying(20)")
    private String building;
    @Column(columnDefinition = "character varying(10)")
    private String officeNumber;
    @Column(columnDefinition = "character varying(50)")
    private String institute;
    @Column(columnDefinition = "character varying(15) unique")
    private String phoneNumber;

    @OneToMany(mappedBy="supervisor", cascade = CascadeType.REMOVE)
    private Set<Thesis> theses;

    protected Supervisor(){}

    public Supervisor(String firstName, String lastName, String password, String mail, University university){
        super(firstName, lastName, password, mail, university);
    }

    public String getAcedemicDegree() {
        return acedemicDegree;
    }

    public void setAcedemicDegree(String acedemicDegree) {
        this.acedemicDegree = acedemicDegree;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Thesis> getTheses() {
        return theses;
    }

    public void setTheses(Set<Thesis> theses) {
        this.theses = theses;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
