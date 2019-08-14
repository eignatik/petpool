package com.petpool.domain.model.pet;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "pet_name", unique = true, nullable = false, length = 20)
    private String name;

    @Column(unique = true, nullable = false)
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PetType type;
}
