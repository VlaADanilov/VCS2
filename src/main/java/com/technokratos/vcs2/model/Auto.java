package com.technokratos.vcs2.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "auto")
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String model;
    private Integer year;
    private Integer price;
    private Integer mileage;
    private String city;
    private String description;

    @OneToMany
    private Set<AutoImage> images;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private User user;
}
