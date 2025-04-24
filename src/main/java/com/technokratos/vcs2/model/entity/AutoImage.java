package com.technokratos.vcs2.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "auto_image")
public class AutoImage {

    @Id
    private UUID id;

    private String image;


}