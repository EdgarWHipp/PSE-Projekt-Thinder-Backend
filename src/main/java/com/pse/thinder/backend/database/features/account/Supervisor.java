package com.pse.thinder.backend.database.features.account;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity @Table(name="supervisors")
public class Supervisor extends User {


    private String acedemicDegree;
    private String building;
    private String officeNumber;
    private String institute;
    private String phoneNumber;

    @OneToMany(mappedBy="supervisor")
    private Set<Thesis> theses;

    protected Supervisor(){}

    public Supervisor(String firstName, String lastName, String password,
                      String mail, String acedemicDegree, String building,
                      String officeNumber, String institute, String phoneNumber){
        super(firstName, lastName, password, mail);
        this.acedemicDegree = acedemicDegree;
        this.building = building;
        this.officeNumber = officeNumber;
        this.institute = institute;
        this.phoneNumber = phoneNumber;
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
