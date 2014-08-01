package com.wewebu.ow.server.dmsdialogs.views.classes;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import com.wewebu.ow.server.util.OwStandardXMLUtil;

public class OwTestObjectClassSelectionCfg extends TestCase
{
    public void testBackwardsCompatibility1() throws Exception
    {
        StringBuffer strCfg = new StringBuffer();
        strCfg.append("<PlugIn type=\"ow_recordfunction\">");
        strCfg.append("<ObjectClass>DeprecatedObjectClass</ObjectClass>");
        strCfg.append("</PlugIn>");

        ByteArrayInputStream inputStream_p = new ByteArrayInputStream(strCfg.toString().getBytes("UTF-8"));
        OwStandardXMLUtil pluginCfg = new OwStandardXMLUtil(inputStream_p, "PlugIn", OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);

        OwObjectClassSelectionCfg classSelectionCfg = OwObjectClassSelectionCfg.fromPluginXml(pluginCfg);

        assertEquals(1, classSelectionCfg.getRootClasses().size());
        assertNotNull(classSelectionCfg.get("DeprecatedObjectClass"));
        assertFalse(classSelectionCfg.get("DeprecatedObjectClass").isIncludeSubclasses());
    }

    public void testBackwardsCompatibility2() throws Exception
    {
        StringBuffer strCfg = new StringBuffer();
        strCfg.append("<PlugIn type=\"ow_recordfunction\">");
        strCfg.append("<ObjectClassParent>DeprecatedObjectClassParent</ObjectClassParent>");
        strCfg.append("</PlugIn>");

        ByteArrayInputStream inputStream_p = new ByteArrayInputStream(strCfg.toString().getBytes("UTF-8"));
        OwStandardXMLUtil pluginCfg = new OwStandardXMLUtil(inputStream_p, "PlugIn", OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);

        OwObjectClassSelectionCfg classSelectionCfg = OwObjectClassSelectionCfg.fromPluginXml(pluginCfg);

        assertEquals(1, classSelectionCfg.getRootClasses().size());
        assertNotNull(classSelectionCfg.get("DeprecatedObjectClassParent"));
        assertTrue(classSelectionCfg.get("DeprecatedObjectClassParent").isIncludeSubclasses());
    }

    public void testSkipDeprecatedElements1() throws Exception
    {
        StringBuffer strCfg = new StringBuffer();
        strCfg.append("<PlugIn type=\"ow_recordfunction\">");
        strCfg.append("<ObjectClass>DeprecatedObjectClass</ObjectClass>");
        strCfg.append("<ObjectClassSelection>");
        strCfg.append("<ObjectClass includeSubClasses=\"true\">D:dl:dataListItem</ObjectClass>");
        strCfg.append("<ObjectClass includeSubClasses=\"false\">FooBar</ObjectClass>");
        strCfg.append("</ObjectClassSelection>");
        strCfg.append("</PlugIn>");

        ByteArrayInputStream inputStream_p = new ByteArrayInputStream(strCfg.toString().getBytes("UTF-8"));
        OwStandardXMLUtil pluginCfg = new OwStandardXMLUtil(inputStream_p, "PlugIn", OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);

        OwObjectClassSelectionCfg classSelectionCfg = OwObjectClassSelectionCfg.fromPluginXml(pluginCfg);

        assertEquals(2, classSelectionCfg.getRootClasses().size());
        assertNotNull(classSelectionCfg.get("D:dl:dataListItem"));
        assertNotNull(classSelectionCfg.get("FooBar"));
        assertNull("DeprecatedObjectClass should be skipped !!!", classSelectionCfg.get("DeprecatedObjectClass"));

        assertTrue(classSelectionCfg.get("D:dl:dataListItem").isIncludeSubclasses());
        assertFalse(classSelectionCfg.get("FooBar").isIncludeSubclasses());
    }

    public void testSkipDeprecatedElements2() throws Exception
    {
        StringBuffer strCfg = new StringBuffer();
        strCfg.append("<PlugIn type=\"ow_recordfunction\">");
        strCfg.append("<ObjectClassParent>DeprecatedObjectClassParent</ObjectClassParent>");
        strCfg.append("<ObjectClassSelection>");
        strCfg.append("<ObjectClass includeSubClasses=\"true\">D:dl:dataListItem</ObjectClass>");
        strCfg.append("<ObjectClass includeSubClasses=\"false\">FooBar</ObjectClass>");
        strCfg.append("</ObjectClassSelection>");
        strCfg.append("</PlugIn>");

        ByteArrayInputStream inputStream_p = new ByteArrayInputStream(strCfg.toString().getBytes("UTF-8"));
        OwStandardXMLUtil pluginCfg = new OwStandardXMLUtil(inputStream_p, "PlugIn", OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);

        OwObjectClassSelectionCfg classSelectionCfg = OwObjectClassSelectionCfg.fromPluginXml(pluginCfg);

        assertEquals(2, classSelectionCfg.getRootClasses().size());
        assertNotNull(classSelectionCfg.get("D:dl:dataListItem"));
        assertNotNull(classSelectionCfg.get("FooBar"));
        assertNull("DeprecatedObjectClassParent should be skipped !!!", classSelectionCfg.get("DeprecatedObjectClassParent"));

        assertTrue(classSelectionCfg.get("D:dl:dataListItem").isIncludeSubclasses());
        assertFalse(classSelectionCfg.get("FooBar").isIncludeSubclasses());
    }

    public void testNewConfiguration() throws Exception
    {
        StringBuffer strCfg = new StringBuffer();
        strCfg.append("<PlugIn type=\"ow_recordfunction\">");
        strCfg.append("<ObjectClassSelection>");
        strCfg.append("<ObjectClass includeSubClasses=\"true\">D:dl:dataListItem</ObjectClass>");
        strCfg.append("<ObjectClass includeSubClasses=\"false\">FooBar</ObjectClass>");
        strCfg.append("</ObjectClassSelection>");
        strCfg.append("</PlugIn>");

        ByteArrayInputStream inputStream_p = new ByteArrayInputStream(strCfg.toString().getBytes("UTF-8"));
        OwStandardXMLUtil pluginCfg = new OwStandardXMLUtil(inputStream_p, "PlugIn", OwStandardXMLUtil.WIDTH_FIRST_LEVEL_ORDER);

        OwObjectClassSelectionCfg classSelectionCfg = OwObjectClassSelectionCfg.fromPluginXml(pluginCfg);

        assertEquals(2, classSelectionCfg.getRootClasses().size());
        assertNotNull(classSelectionCfg.get("D:dl:dataListItem"));
        assertNotNull(classSelectionCfg.get("FooBar"));
        assertTrue(classSelectionCfg.get("D:dl:dataListItem").isIncludeSubclasses());
        assertFalse(classSelectionCfg.get("FooBar").isIncludeSubclasses());
    }
}
