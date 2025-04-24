package com.technokratos.vcs2.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "like_table")
public class Like {

    @Id
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Auto auto;
}