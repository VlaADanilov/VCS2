package com.technokratos.vcs2.model.entity;


import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "brand")
@Data
@NoArgsConstructor
public class Brand {

    @Id
    private UUID id;

    private String name;

    private String country;
}