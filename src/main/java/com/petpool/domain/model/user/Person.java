package com.petpool.domain.model.user;

import javax.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String city;

    //TODO: write CountryEnum
    private String country;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="person_users",
            joinColumns = @JoinColumn( name="person_id"),
            inverseJoinColumns = @JoinColumn( name="user_id")
    )
    private Set<User> users;

    public Person() {
    }

    public Person(String firstName, String lastName, String city, String country, Set<User> users) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.users = users;
    }
}
