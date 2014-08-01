package com.wewebu.ow.server.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.wewebu.ow.csqlc.OwBetweenPredicateTest;
import com.wewebu.ow.server.ecm.OwPropertyComparatorTest;
import com.wewebu.ow.server.ecm.OwStandardCrossMappingsTest;
import com.wewebu.ow.server.ecm.OwStandardObjectCollectionTest;
import com.wewebu.ow.server.fieldctrlimpl.OwTestNoteModel;

/**
 *<p>
 * OwCCUtilTests.
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
public class OwCCUtilTests extends TestCase
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("All Utility Tests");

        suite.addTestSuite(OwEscapedStringTokenizerTest.class);
        suite.addTestSuite(OwFileUtilTest.class);
        suite.addTestSuite(OwMimeTypesTest.class);
        suite.addTestSuite(OwAppContextTest.class);
        suite.addTestSuite(OwSecretEncryptionTest.class);
        suite.addTestSuite(OwStandardDBAttributeBagWriteableFactoryTest.class);
        suite.addTestSuite(OwStandardXMLUtilTest.class);
        suite.addTestSuite(OwDateTimeUtilTest.class);
        suite.addTestSuite(OwTimeZoneInfoTest.class);
        suite.addTestSuite(OwTestNoteModel.class);
        suite.addTestSuite(OwLdapADConnectorTest.class);
        suite.addTestSuite(OwDBAttributeBagTest.class);
        suite.addTestSuite(OwDBAttributeBagCodecTest.class);
        suite.addTestSuite(OwAlphabetCoderTest.class);
        suite.addTestSuite(OwParameterMapTest.class);
        suite.addTestSuite(OwTransientCodecTest.class);
        suite.addTestSuite(OwStandardCrossMappingsTest.class);
        suite.addTestSuite(OwStandardObjectCollectionTest.class);
        suite.addTestSuite(OwPropertyComparatorTest.class);
        suite.addTestSuite(OwPropertyPlaceholderTest.class);
        suite.addTestSuite(OwRelativeDateTest.class);
        suite.addTestSuite(OwColumnInfoBagValueTest.class);
        suite.addTestSuite(OwResourcePropertiesTest.class);
        suite.addTestSuite(OwBetweenPredicateTest.class);
        suite.addTestSuite(OwXMLDOMUtilTest.class);
        return suite;
    }

}
