package com.pse.thinder.backend.databaseFeatures.thesis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Image implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @ManyToOne
    @JoinColumn(name="thesis_id", nullable=false)
    @JsonIgnore
    private Thesis thesis;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
