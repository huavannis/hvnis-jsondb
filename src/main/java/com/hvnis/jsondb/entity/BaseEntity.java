package com.hvnis.jsondb.entity;

import java.io.Serializable;

/**
 * @author hvnis
 */
public abstract class BaseEntity<ID> implements Serializable {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
