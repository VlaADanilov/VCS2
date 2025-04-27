package com.technokratos.vcs2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
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
    private Set<AutoImage> images;

    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}