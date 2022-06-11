package com.pse.thinder.backend.database.features;

import com.pse.thinder.backend.database.features.account.Thesis;

import javax.persistence.*;

@Entity
public class Image {

    @Id @GeneratedValue
    Long id;
    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name="thesis_id", nullable=false)
    private Thesis thesis;


}
