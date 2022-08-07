package com.pse.thinder.backend.databaseFeatures.thesis;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.UUID;

@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Size(min=1, max=50)
    @Column(columnDefinition = "character varying(50) not null")
    private String name;

    @NotNull
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @NotNull
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
