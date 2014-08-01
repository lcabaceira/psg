package com.wewebu.ow.server.ecmimpl.opencmis.junit.helper;

import java.util.Collections;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.runtime.util.EmptyItemIterable;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;

public class JUnitObjectType extends AbstractTypeDefinition implements ObjectType
{

    /** serial Version UID  */
    private static final long serialVersionUID = 3287279728839075682L;

    public JUnitObjectType()
    {
        setQueryName("JUnitObjectType");
        setId("junit:objectType");
    }

    @Override
    public boolean isBaseType()
    {
        return true;
    }

    @Override
    public ObjectType getBaseType()
    {
        return null;
    }

    @Override
    public ObjectType getParentType()
    {
        return null;
    }

    @Override
    public ItemIterable<ObjectType> getChildren()
    {
        return new EmptyItemIterable<ObjectType>();
    }

    @Override
    public List<Tree<ObjectType>> getDescendants(int depth)
    {
        return Collections.emptyList();
    }

}
