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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "pet_name", nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType type;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    @Max(10)
    @Min(1)
    @Column(nullable = false)
    private int healthRate;

    @Column(nullable = false)
    private boolean tamed;
}
