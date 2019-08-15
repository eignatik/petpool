package com.petpool.domain.model.user;

import javax.persistence.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "person")
@RequiredArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "first_name", nullable = false)
    private final String firstName;

    @Column(name = "last_name", nullable = false)
    private final String lastName;

    private final String city;

    //TODO: write CountryEnum
    private final String country;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private final User user;
}
