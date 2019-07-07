package com.petpool.db.dao;

import com.petpool.db.entity.Entity;

/**
 * Defines the basic procedures available in all dao classes that implement this interface.
 * @param <T>
 */
public interface Dao<T extends Entity> {
    T get(int id);
    T delete(int id);
    T delete(T item);
    T update(T item);
}
