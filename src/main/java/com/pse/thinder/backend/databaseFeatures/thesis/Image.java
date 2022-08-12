package com.pse.thinder.backend.databaseFeatures.thesis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pse.thinder.backend.databaseFeatures.InputValidation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(groups = {InputValidation.class})
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @NotNull(groups = {InputValidation.class})
    @ManyToOne
    @JoinColumn(name="thesis_id", nullable=false)
    @JsonIgnore
    private Thesis thesis;

    public Image(){}

    public Image(byte[] image, Thesis thesis){
        this.image = image;
        this.thesis = thesis;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Thesis getThesis() {
        return thesis;
    }

    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
    }

    }
