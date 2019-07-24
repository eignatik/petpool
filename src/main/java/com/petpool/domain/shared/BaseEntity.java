package com.petpool.domain.shared;

import lombok.Data;

import javax.persistence.*;

/**
 * Base entity for all db entity objects in project.
 *
 * Required as it will be used for dao objects.
 */
@Data
@Entity
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;
}
