package com.wewebu.ow.server.ecmimpl.fncm5.nativeapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.Property;
import com.wewebu.ow.server.ecmimpl.fncm5.unittest.log.JUnitLogger;

/**
 *<p>
 * Native test for the retrieval of path information of an object.
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
@Ignore
public class OwFNCM5NativePathTests extends OwFNCM5LoggedInNativeTest
{
    private static final Logger LOG = JUnitLogger.getLogger(OwFNCM5NativePathTests.class);

    @SuppressWarnings("deprecation")
    @Test
    public void matchMasks() throws Exception
    {
        Assert.assertEquals(AccessLevel.READ_AS_INT, AccessRight.READ_AS_INT | AccessRight.READ_ACL_AS_INT);
        matchTarget(AccessLevel.WRITE_DEFAULT_AS_INT);
        Assert.assertEquals(AccessLevel.WRITE_DEFAULT_AS_INT, AccessRight.WRITE_AS_INT | AccessRight.READ_ACL_AS_INT | AccessRight.CREATE_INSTANCE_AS_INT | AccessRight.READ_AS_INT);

        matchTarget(AccessLevel.FULL_CONTROL_ANNOTATION_AS_INT);
        Assert.assertEquals(AccessLevel.FULL_CONTROL_ANNOTATION_AS_INT, AccessRight.DELETE_AS_INT | AccessRight.WRITE_AS_INT | AccessRight.WRITE_ACL_AS_INT | AccessRight.READ_ACL_AS_INT | AccessRight.VIEW_CONTENT_AS_INT
                | AccessRight.WRITE_OWNER_AS_INT | AccessRight.CREATE_INSTANCE_AS_INT | AccessRight.READ_AS_INT);
    }

    private boolean matchTarget(int targetValue) throws IllegalAccessException
    {
        List<Field> matchedFields = new ArrayList<Field>();
        Field[] fields = AccessRight.class.getFields();
        for (Field field : fields)
        {
            if (Modifier.isStatic(field.getModifiers()))
            {
                if (Integer.TYPE.isAssignableFrom(field.getType()))
                {
                    Integer fieldValue = (Integer) field.get(null);
                    if (0 != (targetValue & fieldValue.intValue()))
                    {
                        matchedFields.add(field);
                    }
                }
            }
        }

        if (matchedFields.isEmpty())
        {
            System.err.println("Complete failure ...");
            return false;
        }

        int matchedValue = 0;
        for (Field field : matchedFields)
        {
            Integer fieldValue = (Integer) field.get(null);
            matchedValue = matchedValue | fieldValue;
            System.err.println(field.getName());
        }

        if (targetValue == matchedValue)
        {
            System.err.println("BINGO");
            return true;
        }
        else
        {
            System.err.println("We were close ....");
            return false;
        }
    }

    public void testCustomObjectPath()
    {
        String rPath = "/Test/getByPath/TestCustomObject";
        CustomObject c = (CustomObject) defaultObjectStore.fetchObject("CustomObject", rPath, null);
        assertNotNull("Missing test custom object /Test/getByPath/TestCustomObject", c);

        //        assertEquals("TestCustomObject", c.get_Name());//we don't have a name only an Id
        ReferentialContainmentRelationshipSet rcrSet = c.get_Containers();
        ReferentialContainmentRelationship rcr = (ReferentialContainmentRelationship) rcrSet.iterator().next();
        assertEquals("TestCustomObject", rcr.get_Name());
        assertEquals(rcr.get_Head().getObjectReference().getObjectIdentity().toString(), c.get_Id().toString());

        try
        {
            Folder f = (Folder) defaultObjectStore.fetchObject("Folder", rcr.get_Tail().getObjectReference().getObjectIdentity(), null);
            String path = f.getProperties().getStringValue(PropertyNames.PATH_NAME);
            path = path + "/" + rcr.get_Name();

            assertEquals(rPath, path);
        }
        catch (Exception ex)
        {
            LOG.error("Cannot create path from tail object", ex);
            fail("Cannot create path from tail object");
        }
    }

    public void testDocumentPath()
    {
        Document d = (Document) defaultObjectStore.fetchObject("Document", "{AD33DC81-4ED6-47F0-B1C4-FBB2859E33C2}", null);
        assertNotNull("Missing test document with id = {AD33DC81-4ED6-47F0-B1C4-FBB2859E33C2}", d);

        assertEquals("getById", d.get_Name());
        FolderSet set = d.get_FoldersFiledIn();
        Folder parent = (Folder) set.iterator().next();
        assertEquals("Parent not matching", "getByPath", parent.get_Name());
        try
        {
            String path = parent.getProperties().getStringValue(PropertyNames.PATH_NAME);
            path = path + "/" + d.get_Name();
            assertEquals("/Test/getByPath/getById", path);
        }
        catch (Exception ex)
        {
            LOG.error("Cannot create path for document", ex);
            fail("Cannot get Path from document");
        }
    }

    public void testFolderPath()
    {
        String rPath = "/Test/getByPath";
        Folder f = (Folder) defaultObjectStore.fetchObject("Folder", "/Test/getByPath", null);
        assertNotNull("Missing test folder " + rPath, f);

        assertEquals(rPath.split("/")[2], f.get_FolderName());
        try
        {
            Property prop = f.getProperties().find(PropertyNames.PATH_NAME);
            assertEquals(rPath, prop.getStringValue());
        }
        catch (Exception ex)
        {
            LOG.error("Failed to retrieve PathName property", ex);
            fail("Failed to retrieve PathName property");
        }
    }

}
