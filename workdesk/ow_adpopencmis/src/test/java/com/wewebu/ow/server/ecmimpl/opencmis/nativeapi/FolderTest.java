package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;

public class FolderTest extends AbstractNativeTest
{
    public void testSort() throws Exception
    {
        Folder folder = (Folder) getSession().getObjectByPath("/ImmutableBigDataFolder_" + System.getProperty("user.name"));

        boolean includeAcls = true;
        boolean includeAllowableActions = true;
        boolean includePolicies = true;
        IncludeRelationships includeRelationships = IncludeRelationships.NONE;
        Set<String> renditionFilter = null;
        boolean includePathSegments = true;
        //        String orderBy = "cmis:lastModificationDate DESC,cmis:name ASC";
        //        String orderBy = null;
        String orderBy = "cmis:lastModificationDate DESC";
        boolean cacheEnabled = true;
        int maxItemsPerPage = 10;

        OperationContext ctx = getSession().createOperationContext(null, includeAcls, includeAllowableActions, includePolicies, includeRelationships, renditionFilter, includePathSegments, orderBy, cacheEnabled, maxItemsPerPage);

        ItemIterable<CmisObject> children = folder.getChildren(ctx);

        Iterator<CmisObject> i = children.iterator();
        while (i.hasNext())
        {
            CmisObject o = i.next();
            Property<Object> p1 = o.getProperty("cmis:name");
            Property<GregorianCalendar> p2 = o.getProperty("cmis:lastModificationDate");
            GregorianCalendar p2v = p2.getValue();

            System.out.println(p1.getValue() + "," + p2v.getTime());

        }

    }
}
