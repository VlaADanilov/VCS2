package com.technokratos.vcs2.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    private UUID id;

    private String name;
    private String profession;
    private String description;
    private String phone;

    @OneToOne
    private User account;

}