package org.openfast.simple;

import org.lasalletech.entity.EObject;
import org.lasalletech.entity.Entity;
import org.lasalletech.entity.EntityType;
import org.openfast.template.Type;

public class FastEntityType extends EntityType implements Type {
    private static final long serialVersionUID = 1L;
    
    public FastEntityType(Entity entity) {
        this(entity, false);
    }
    
    public FastEntityType(Entity entity, boolean repeating) {
        super(entity, repeating);
    }


    public void parse(EObject o, int index, String value) {
        throw new UnsupportedOperationException();
    }
}
