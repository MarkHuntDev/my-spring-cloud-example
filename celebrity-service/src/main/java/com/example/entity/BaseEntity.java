package com.example.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.domain.Persistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract class BaseEntity implements Persistable<Long> {

    @Override
    public boolean isNew() {
        return this.getId() == null;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BaseEntity
                && new EqualsBuilder()
                .append(this.getClass(), ((BaseEntity) other).getClass())
                .append(this.getId(), ((BaseEntity) other).getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getClass())
                .append(this.getId())
                .toHashCode();
    }
}
