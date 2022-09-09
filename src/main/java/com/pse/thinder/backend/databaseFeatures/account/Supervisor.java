package com.pse.thinder.backend.databaseFeatures.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pse.thinder.backend.databaseFeatures.InputValidation;
import com.pse.thinder.backend.databaseFeatures.University;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import javax.persistence.*;
import java.util.List;

/**
 * This class holds all necessary data for supervisors.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Supervisor extends User {

	@NotBlank(groups = {InputValidation.class})
    private String academicDegree;

	@NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(20)")
    private String building;
	
	@NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(10)")
    private String officeNumber;
	
	@NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(50)")
    private String institute;
	
	@NotBlank(groups = {InputValidation.class})
    @Column(columnDefinition = "character varying(15) unique")
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy="supervisor", cascade = CascadeType.REMOVE)
    private List<Thesis> theses;

    /**
     * Spring boot requires a no args constructor and uses setters to set the properties.
     */
    protected Supervisor(){}

    /**
     *
     * @param firstName the first name of the supervisor.
     * @param lastName the last name of the supervisor.
     * @param password the password of the supervisor.
     * @param mail the mail of the supervisor.
     * @param university the {@link University} the supervisor is a member of.
     */
    public Supervisor(String firstName, String lastName, String password, String mail, University university){
        super(firstName, lastName, password, mail, university, Role.ROLE_SUPERVISOR);
    }

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
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
    
    @Override
    public void updateIsComplete() {
    	boolean notNull = academicDegree != null &&
    						building != null &&
    						officeNumber != null &&
    						institute != null &&
    						phoneNumber != null;
    	if(notNull) {
    		super.setComplete(!academicDegree.isBlank() &&
    							!building.isBlank() &&
    							!officeNumber.isBlank() &&
    							!institute.isBlank() &&
    							!phoneNumber.isBlank());
    	}
    	else 
    		super.setComplete(false);
    }
}
