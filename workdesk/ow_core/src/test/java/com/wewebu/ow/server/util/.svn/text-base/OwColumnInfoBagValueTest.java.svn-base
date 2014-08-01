package com.wewebu.ow.server.util;

import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectListViewEXTJSGrid.OwColumnInfoBagValue;

public class OwColumnInfoBagValueTest extends TestCase
{
    public void testEmptyBagValue() throws Exception
    {
        OwColumnInfoBagValue value = new OwColumnInfoBagValue();

        assertEquals(0, value.getHeaders().size());

        assertTrue(value.columnsOf("foo").isEmpty());
        assertNull(value.widthOf("foo", "bar"));
        assertTrue(value.getDefaultColumns().isEmpty());
    }

    public void testLegacyBagValue() throws Exception
    {
        OwColumnInfoBagValue value = new OwColumnInfoBagValue("ow_icon_column=282,cmis:document.cmis:name=275,cmis:document.cmis:creationDate=100,cmis:document.cmis:baseTypeId=102");

        assertEquals(1, value.getHeaders().size());
        Set<String> defaultColumns = value.getDefaultColumns();
        assertEquals(4, defaultColumns.size());

        assertEquals((Integer) 282, value.widthOf("ow_icon_column"));
        assertEquals((Integer) 275, value.widthOf("cmis:document.cmis:name"));
        assertEquals((Integer) 100, value.widthOf("cmis:document.cmis:creationDate"));
        assertEquals((Integer) 102, value.widthOf("cmis:document.cmis:baseTypeId"));

        assertNull(value.widthOf("FOO"));

        value.put("FOO", 63);

        assertEquals((Integer) 63, value.widthOf("FOO"));
    }

    public void testBagValueAccess() throws Exception
    {
        OwColumnInfoBagValue value = new OwColumnInfoBagValue();

        value.put("dossiers", "name", 101);
        value.put("dossiers", "tip", 102);
        value.put("dossiers", "date", 103);

        assertEquals(1, value.getHeaders().size());
        assertEquals(3, value.columnsOf("dossiers").size());
        assertNull(value.widthOf("journal", "title"));
        assertEquals((Integer) 0, value.indexOf("dossiers", "name"));
        assertEquals((Integer) 2, value.indexOf("dossiers", "date"));
        assertTrue(value.columnsOf("dossiers").containsAll(Arrays.asList(new String[] { "name", "tip", "date" })));

        value.put("journal", "title", 201);
        value.put("journal", "publisher", 202);
        value.put("journal", "date", 203);

        assertEquals(2, value.getHeaders().size());

        assertEquals((Integer) 201, value.widthOf("journal", "title"));
        assertEquals((Integer) 203, value.widthOf("journal", "date"));
        assertEquals((Integer) 0, value.indexOf("journal", "title"));

        assertNull(value.widthOf("date"));
        assertTrue(value.getDefaultColumns().isEmpty());

        value.put("name", 11);
        value.put("date", 12);

        assertTrue(value.getDefaultColumns().containsAll(Arrays.asList(new String[] { "name", "date" })));

        assertEquals(3, value.getHeaders().size());

        assertEquals((Integer) 11, value.widthOf("name"));
        assertEquals((Integer) 12, value.widthOf("date"));

        value.put("journal", "date", 2032);
        assertEquals((Integer) 2032, value.widthOf("journal", "date"));
    }

    public void testBagValueString() throws Exception
    {
        OwColumnInfoBagValue value = new OwColumnInfoBagValue();

        assertEquals("", value.toString());

        value.put("name", 11);
        value.put("date", 12);

        assertEquals("name=11,date=12", value.toString());

        OwColumnInfoBagValue value2 = new OwColumnInfoBagValue();

        value2.put("dossiers", "name", 101);
        value2.put("dossiers", "tip", 102);
        value2.put("dossiers", "date", 103);

        assertEquals("dossiersname=101,tip=102,date=103", value2.toString());

        value2.put("dossiers", "tip", 1022);

        assertEquals("dossiersname=101,tip=1022,date=103", value2.toString());

        OwColumnInfoBagValue valueOfValue = new OwColumnInfoBagValue(value2.toString());

        assertEquals("dossiersname=101,tip=1022,date=103", valueOfValue.toString());
        assertEquals((Integer) 1022, value2.widthOf("dossiers", "tip"));
        assertEquals((Integer) 103, value2.widthOf("dossiers", "date"));
    }

    public void testClear() throws Exception
    {
        OwColumnInfoBagValue value = new OwColumnInfoBagValue();

        value.put("dossiers", "name", 101);
        value.put("dossiers", "tip", 102);
        value.put("dossiers", "date", 103);

        assertNull(value.widthOf("journal", "title"));
        assertEquals((Integer) 102, value.widthOf("dossiers", "tip"));

        value.put("journal", "title", 201);
        value.put("journal", "publisher", 202);
        value.put("journal", "date", 203);

        assertEquals((Integer) 201, value.widthOf("journal", "title"));

        value.clear("journal");

        assertNull(value.widthOf("journal", "title"));
        assertEquals((Integer) 102, value.widthOf("dossiers", "tip"));

        value.clear("dossiers");
        assertNull(value.widthOf("dossiers", "tip"));

    }

}
