package it.adawant.demo.warehouse.utils;

import org.springframework.data.util.ProxyUtils;

import java.io.Serializable;


public abstract class AbstractEntity<ID extends Serializable> {
    private static final Long serialVersionUID = -43869754L;


    public abstract ID getId();

    @Override
    public String toString() {
        return "@Entity " + this.getClass().getName() + " (id=" + getId() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (getClass() != ProxyUtils.getUserClass(other))
            return false;
        return null != getId() && this.getId() == ((AbstractEntity<?>) other).getId();
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
