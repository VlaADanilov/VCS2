package com.technokratos.vcs2.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "auto_image")
public class AutoImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String image;



}
