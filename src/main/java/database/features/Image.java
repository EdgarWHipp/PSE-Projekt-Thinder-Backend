package database.features;

import database.features.account.Thesis;

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
