package com.technokratos.vcs2.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    private UUID id;

    private String image;


}