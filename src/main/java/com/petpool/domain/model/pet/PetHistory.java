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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.petpool.domain.model.user.User;
import lombok.Data;

@Data
@Entity
@Table(name = "pet_history")
public class PetHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "keeper_type", nullable = false)
    private KeeperType keeperType;

    @Column(name = "start_keeping_date", nullable = false)
    private LocalDate startKeepingDate;

    @Column(name = "end_keeping_date")
    private LocalDate endKeepingDate;

}
