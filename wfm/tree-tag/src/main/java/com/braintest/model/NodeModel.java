package com.braintest.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class NodeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final boolean hasChild;

    public NodeModel(String name, boolean hasChild) {
        this.name = name;
        this.hasChild = hasChild;
    }

    public String getName() {
        return name;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCode = new HashCodeBuilder();
        return hashCode.append(name).append(hasChild).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        NodeModel that = (NodeModel) obj;
        EqualsBuilder equals = new EqualsBuilder();
        return equals.append(that.getName(), getName())
               .append(that.isHasChild(), isHasChild()).isEquals();
    }
}