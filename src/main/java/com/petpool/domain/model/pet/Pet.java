package com.petpool.domain.model.pet;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.petpool.application.util.Age;
import com.petpool.domain.model.user.User;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    private Long version;

    @Column(name = "pet_name", nullable = false, length = 20)
    private String name;

    @Column
    private String breed = "undefined";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private PetType type;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    @Max(10)
    @Min(1)
    @Column(nullable = false)
    private int healthRate;

    @Column(nullable = false)
    private boolean tamed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host")
    private User host;

    @Column(nullable = false)
    private boolean dead;

    @Transient
    public Age getAge() {
        return Age.createByDate(birthDate);
    }

}
