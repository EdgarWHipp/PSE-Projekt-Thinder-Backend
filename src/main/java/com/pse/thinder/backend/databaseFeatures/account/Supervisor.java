package com.pse.thinder.backend.databaseFeatures.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true) // todo added because without this jackson throws an error because of the role field in the json
public class Supervisor extends User {



    private String acedemicDegree;

    @Column(columnDefinition = "character varying(20)")
    private String building;
    @Column(columnDefinition = "character varying(10)")
    private String officeNumber;
    @Column(columnDefinition = "character varying(50)")
    private String institute;
    @Column(columnDefinition = "character varying(15) unique")
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy="supervisor", cascade = CascadeType.REMOVE)
    private List<Thesis> theses;

    protected Supervisor(){}

    public Supervisor(String firstName, String lastName, String password, String mail, University university, Role role){
        super(firstName, lastName, password, mail, university, role);
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

    public List<Thesis> getTheses() {
        return theses;
    }

    public void addThesis(Thesis thesis) {
        this.theses.add(thesis);
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
