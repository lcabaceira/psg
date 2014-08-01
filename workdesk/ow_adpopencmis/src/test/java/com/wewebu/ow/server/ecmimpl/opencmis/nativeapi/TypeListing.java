package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.math.BigInteger;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinitionList;
import org.apache.chemistry.opencmis.commons.spi.CmisBinding;
import org.apache.chemistry.opencmis.commons.spi.RepositoryService;

public class TypeListing extends AbstractNativeTest
{
    public TypeListing()
    {
        super(false);//atom = true, SOAP = false
    }

    public void testTypeTrace()
    {
        ObjectType type = getSession().getTypeDefinition("D:owd:hrdocument");
        traceType(type);
    }

    public void testTypeListing()
    {
        ItemIterable<ObjectType> types = getSession().getTypeChildren(null, true);
        for (ObjectType type : types)
        {
            System.out.println(type.getId() + " " + type.getDisplayName() + " " + type.getQueryName());
        }
    }

    public void testTypeDecendants()
    {
        //Premature EOF if atom
        ItemIterable<ObjectType> types = getSession().getTypeChildren(null, true);
        for (ObjectType type : types)
        {
            System.out.println(type.getId() + " " + type.getDisplayName() + " " + type.getQueryName());
            List<Tree<ObjectType>> children = getSession().getTypeDescendants(type.getId(), 1, true);
            for (Tree<ObjectType> subItem : children)
            {
                ObjectType subType = subItem.getItem();
                System.out.println(subType.getId() + " " + subType.getDisplayName() + " " + subType.getQueryName());
            }
        }
    }

    public void testBindingTypeDescendants()
    {
        CmisBinding binding = getSession().getBinding();
        RepositoryService rs = binding.getRepositoryService();
        String repoId = getSession().getRepositoryInfo().getId();

        TypeDefinitionList lst = rs.getTypeChildren(repoId, null, Boolean.TRUE, new BigInteger("10"), BigInteger.ZERO, null);
        System.out.println();
        for (org.apache.chemistry.opencmis.commons.definitions.TypeDefinition baseType : lst.getList())
        {
            System.out.println("BaseType = " + baseType.getDisplayName());
            List<org.apache.chemistry.opencmis.commons.definitions.TypeDefinitionContainer> subs = rs.getTypeDescendants(repoId, baseType.getId(), BigInteger.ONE, Boolean.FALSE, null);
            for (org.apache.chemistry.opencmis.commons.definitions.TypeDefinitionContainer subNode : subs)
            {
                org.apache.chemistry.opencmis.commons.definitions.TypeDefinition subType = subNode.getTypeDefinition();
                System.out.println("Subtype = " + subType.getId() + " " + subType.getDisplayName() + " " + subType.getQueryName());
            }
        }
    }

}
