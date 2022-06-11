package com.pse.thinder.backend.database.features.account;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Supervisor extends User {


    private String acedemicDegree;
    private String location;
    private String institute;
    private String phoneNumber;

    @OneToMany(mappedBy="supervisor")
    private Set<Thesis> theses;

    protected Supervisor(){}

    public Supervisor(String firstName, String lastName, String password,
                      String mail, String acedemicDegree, String location,
                      String institute, String phoneNumber){
        super(firstName, lastName, password, mail);
        this.acedemicDegree = acedemicDegree;
        this.location = location;
        this.institute = institute;
        this.phoneNumber = phoneNumber;
    }

    public String getAcedemicDegree() {
        return acedemicDegree;
    }

    public void setAcedemicDegree(String acedemicDegree) {
        this.acedemicDegree = acedemicDegree;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
