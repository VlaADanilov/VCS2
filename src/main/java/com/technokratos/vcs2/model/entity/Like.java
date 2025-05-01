package com.technokratos.vcs2.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
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