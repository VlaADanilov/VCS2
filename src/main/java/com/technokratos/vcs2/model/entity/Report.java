package com.technokratos.vcs2.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "report")
public class Report {

    @Id
    private UUID id;

    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private Auto auto;
}