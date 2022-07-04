package com.pse.thinder.backend.databaseFeatures;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import javax.persistence.*;

@Entity
public class Image {

    @Id @GeneratedValue
    private Long id;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @ManyToOne
    @JoinColumn(name="thesis_id", nullable=false)
    private Thesis thesis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
