package com.pse.thinder.backend.databaseFeatures.thesis;

import com.pse.thinder.backend.databaseFeatures.thesis.Thesis;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private byte[] image;

    @ManyToOne
    @JoinColumn(name="thesis_id", nullable=false)
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
}
