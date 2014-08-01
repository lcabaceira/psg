package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.wewebu.ow.server.ao.OwSearchTemplateFactory;
import com.wewebu.ow.server.ao.OwSearchTemplatesManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.util.OwTestObject;

/**
 *<p>
 * Search Templates Manager Test.
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
public class OwSearchTemplatesManagerTest extends TestCase implements OwSearchTemplateFactory
{

    private List<OwObject> templateObjects;

    @Override
    protected void setUp() throws Exception
    {
        templateObjects = new LinkedList<OwObject>();
    }

    public void testEmptyPaths() throws Exception
    {
        OwTestAOSupport aoSupport = new OwTestAOSupport();

        OwSearchTemplatesManager manager = new OwSearchTemplatesManager(aoSupport, "", this, "", "");

        final String foo = "foo";

        OwObject fooObject = new OwTestObject(null);
        OwObject barObject = new OwTestObject(null);

        aoSupport.put(foo, fooObject);
        aoSupport.add(foo, fooObject);
        aoSupport.add(foo, barObject);

        manager.getApplicationObject(foo, false, false);
        OwObject lastTemplateObject = templateObjects.get(templateObjects.size() - 1);
        assertNotNull(lastTemplateObject);
        assertSame(fooObject, lastTemplateObject);

        Collection<OwSearchTemplate> foos = manager.getApplicationObjects(foo, false);
        assertNotNull(foos);
        assertEquals(2, foos.size());

    }

    public void testNonEmptyPaths() throws Exception
    {
        OwTestAOSupport aoSupport = new OwTestAOSupport();

        OwSearchTemplatesManager manager = new OwSearchTemplatesManager(aoSupport, "", this, "single", "multiple");

        final String foo = "foo";
        final String singleFoo = "single/foo";
        final String multiFoo = "multiple/foo";

        OwObject fooObject = new OwTestObject(null);
        OwObject barObject = new OwTestObject(null);

        aoSupport.put(singleFoo, fooObject);
        aoSupport.add(multiFoo, fooObject);
        aoSupport.add(multiFoo, barObject);

        manager.getApplicationObject(foo, false, false);
        OwObject lastTemplateObject = templateObjects.get(templateObjects.size() - 1);
        assertNotNull(lastTemplateObject);
        assertSame(fooObject, lastTemplateObject);

        Collection<OwSearchTemplate> foos = manager.getApplicationObjects(foo, false);
        assertNotNull(foos);
        assertEquals(2, foos.size());
    }

    public void testBasePath() throws Exception
    {
        OwTestAOSupport aoSupport = new OwTestAOSupport();

        OwSearchTemplatesManager manager = new OwSearchTemplatesManager(aoSupport, "base", this, "", "multiple");

        final String foo = "foo";
        final String singleFoo = "base/foo";
        final String multiFoo = "base/multiple/foo";

        OwObject fooObject = new OwTestObject(null);
        OwObject barObject = new OwTestObject(null);

        aoSupport.put(singleFoo, fooObject);
        aoSupport.add(multiFoo, fooObject);
        aoSupport.add(multiFoo, barObject);

        manager.getApplicationObject(foo, false, false);
        OwObject lastTemplateObject = templateObjects.get(templateObjects.size() - 1);
        assertNotNull(lastTemplateObject);
        assertSame(fooObject, lastTemplateObject);

        Collection<OwSearchTemplate> foos = manager.getApplicationObjects(foo, false);
        assertNotNull(foos);
        assertEquals(2, foos.size());
    }

    public OwSearchTemplate createSearchTemplate(OwObject obj_p) throws OwException
    {
        templateObjects.add(obj_p);

        return null;
    }

}
