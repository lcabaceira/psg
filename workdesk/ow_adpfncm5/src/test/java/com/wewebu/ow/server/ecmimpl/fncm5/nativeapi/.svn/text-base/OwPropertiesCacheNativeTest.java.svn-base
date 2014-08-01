package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Ignore;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Folder;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.security.AccessPermission;

/**
 *<p>
 * OwPropertiesCacheNativeTest.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 */
@Ignore("This is just a scrapbook")
public class OwPropertiesCacheNativeTest extends OwFNCM5LoggedInNativeTest
{

    public void testPropertyCache()
    {
        CustomObject cObj = (CustomObject) defaultObjectStore.getObject("CustomObject", "/Test/getByPath/TestCustomObject");

        long start = System.currentTimeMillis();
        PropertyFilter propFilter = new PropertyFilter();
        propFilter.setMaxRecursion(5);
        propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSIONS, null);
        //        propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSION_TYPE, null);
        //        propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSION_SOURCE, null);
        //        propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSION_DESCRIPTIONS, null);
        //        propFilter.addIncludeProperty(0, null, null, PropertyNames.GRANTEE_TYPE, null);

        cObj.fetchProperties(propFilter);
        System.out.println("fetch time (ms) = " + (System.currentTimeMillis() - start));
        AccessPermissionList lstPerm = cObj.get_Permissions();
        Iterator<?> it = lstPerm.iterator();
        try
        {
            while (it.hasNext())
            {
                AccessPermission perm = (AccessPermission) it.next();
                System.out.println(perm);
                StringBuilder builder = new StringBuilder();
                builder.append(perm.get_GranteeName());
                builder.append(" ").append(perm.get_PermissionSource());
                builder.append(" ").append(perm.get_AccessType());
                builder.append(" ").append(perm.get_GranteeType());
                builder.append(" ").append(perm.get_InheritableDepth());
                builder.append(" ").append(perm.get_ClassDescription());
                System.out.print(builder.toString());
            }
        }
        catch (EngineRuntimeException ex)
        {
            ex.printStackTrace(System.err);
            fail(ex.getMessage());
        }
    }

    public void testAccessMask()
    {
        CustomObject cObj = (CustomObject) defaultObjectStore.getObject("CustomObject", "/Test/getByPath/TestCustomObject");
        PropertyFilter propFilter = new PropertyFilter();
        propFilter.setMaxRecursion(5);
        propFilter.addIncludeProperty(0, null, null, PropertyNames.PERMISSIONS, null);
        cObj.fetchProperties(propFilter);

        try
        {
            renderAccessMask(cObj.get_Permissions());
        }
        catch (EngineRuntimeException ex)
        {
            ex.printStackTrace(System.err);
            fail(ex.getMessage());
        }
        Folder fObj = (Folder) defaultObjectStore.getObject("Folder", "/Test/getByPath/");
        fObj.fetchProperties(propFilter);
        try
        {
            renderAccessMask(fObj.get_Permissions());
        }
        catch (EngineRuntimeException ex)
        {
            ex.printStackTrace(System.err);
            fail(ex.getMessage());
        }
    }

    public void renderAccessMask(AccessPermissionList acl)
    {
        Iterator<?> it = acl.iterator();
        /*OWNER_RIGHT, VERSIONING, CONTENT_VIEW, CONTENT_MOD, PROPS_EDIT, PROPS_VIEW, Publish*/
        //        System.out.println("OWNER_RIGHT, VERSIONING, CONTENT_VIEW, CONTENT_MOD, PROPS_EDIT, PROPS_VIEW, Publish, CreateSub, FileDoc");
        while (it.hasNext())
        {
            AccessPermission perm = (AccessPermission) it.next();
            StringBuilder builder = new StringBuilder(perm.get_GranteeName()).append(Integer.toBinaryString(perm.get_AccessMask()));
            int mask = perm.get_AccessMask();
            builder.append("=\t");
            builder.append((perm.get_AccessMask() & AccessRight.WRITE_OWNER_AS_INT) != 0).append(",\t");
            builder.append((perm.get_AccessMask() & AccessRight.CHANGE_STATE_AS_INT) != 0).append(",\t");
            builder.append((perm.get_AccessMask() & AccessRight.VIEW_CONTENT_AS_INT) != 0).append(",\t");
            builder.append(((perm.get_AccessMask() & AccessRight.MINOR_VERSION_AS_INT) != 0) || ((perm.get_AccessMask() & AccessRight.MAJOR_VERSION_AS_INT) != 0)).append(",\t");
            builder.append((perm.get_AccessMask() & AccessRight.WRITE_AS_INT) != 0).append(",\t");
            builder.append((perm.get_AccessMask() & AccessRight.READ_AS_INT) != 0).append(",\t");
            builder.append((mask & AccessRight.READ_AS_INT) != 0).append(",\t");
            builder.append((mask & AccessRight.LINK_AS_INT) != 0).append(",\t");
            builder.append((mask & AccessRight.CREATE_CHILD_AS_INT) != 0);
            System.out.println(builder.toString());
        }

    }
}
