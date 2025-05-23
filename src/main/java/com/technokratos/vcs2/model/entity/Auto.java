package com.technokratos.vcs2.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "auto")
public class Auto {

    @Id
    private UUID id;

    private String model;
    private Integer year;
    private Integer price;
    private Integer mileage;
    private String city;
    private String description;
    private String phone;



    @OneToMany
    private Set<Image> images;

    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "auto")
    private Set<Like> like;
}