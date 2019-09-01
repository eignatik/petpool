package com.petpool.domain.model.pet;

import com.petpool.domain.model.user.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pet_id")
    private Long petId;

    @Version
    private Long version;

    @Column(name = "pet_name", nullable = false, length = 20)
    private String name;

    @Column
    private String breed = "undefined";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pet_user",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
