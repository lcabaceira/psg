package com.wewebu.ow.server.ecmimpl.opencmis.propertyclass;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractPropertyDefinition;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyBooleanDefinitionImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyDecimalDefinitionImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyHtmlDefinitionImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyIdDefinitionImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyIntegerDefinitionImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyStringDefinitionImpl;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.PropertyUriDefinitionImpl;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativePropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNativeSession;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimplePropertyClassFactory;
import com.wewebu.ow.server.ecmimpl.opencmis.junit.helper.JUnitObjectClass;
import com.wewebu.ow.server.ecmimpl.opencmis.junit.helper.JUnitObjectType;
import com.wewebu.ow.server.ecmimpl.opencmis.junit.helper.JUnitOwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.objectclass.OwCMISNativeObjectClass;
import com.wewebu.ow.server.exceptions.OwException;

@SuppressWarnings("rawtypes")
public class OwPropertyClassTest extends TestCase
{
    private OwCMISNativePropertyClassFactory propFactory;
    private OwCMISNativeObjectClass objClass;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        OwCMISNativeSession session = new JUnitOwCMISSession();
        propFactory = new OwCMISSimplePropertyClassFactory(session);

        objClass = new JUnitObjectClass(new JUnitObjectType(), session);
    }

    public void testBoolean() throws OwException
    {
        AbstractPropertyDefinition<Boolean> propDef = new PropertyBooleanDefinitionImpl();
        propDef.setPropertyType(PropertyType.BOOLEAN);
        propDef.setId("propDef:Boolean");

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(Boolean.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + ".propDef:Boolean", owDef.getClassName());
    }

    public void testString() throws Exception
    {
        AbstractPropertyDefinition<String> propDef = new PropertyStringDefinitionImpl();
        propDef.setPropertyType(PropertyType.STRING);
        propDef.setId(PropertyIds.NAME);

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(String.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + "." + PropertyIds.NAME, owDef.getClassName());
        assertTrue(owDef.isNameProperty());
    }

    public void testBigInteger() throws OwException
    {
        AbstractPropertyDefinition<BigInteger> propDef = new PropertyIntegerDefinitionImpl();
        propDef.setPropertyType(PropertyType.INTEGER);
        propDef.setId("propDef:Integer");

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(BigInteger.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + ".propDef:Integer", owDef.getClassName());
    }

    public void testBigDecimal() throws OwException
    {
        AbstractPropertyDefinition<BigDecimal> propDef = new PropertyDecimalDefinitionImpl();
        propDef.setPropertyType(PropertyType.DECIMAL);
        propDef.setId("propDef:Decimal");

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(BigDecimal.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + ".propDef:Decimal", owDef.getClassName());
    }

    public void testHtml() throws OwException
    {
        AbstractPropertyDefinition<String> propDef = new PropertyHtmlDefinitionImpl();
        propDef.setPropertyType(PropertyType.HTML);
        propDef.setId("propDef:Html");

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(String.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + ".propDef:Html", owDef.getClassName());
    }

    public void testUri() throws OwException
    {
        AbstractPropertyDefinition<String> propDef = new PropertyUriDefinitionImpl();
        propDef.setPropertyType(PropertyType.URI);
        propDef.setId("propDef:Uri");

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(String.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + ".propDef:Uri", owDef.getClassName());
    }

    public void testId() throws OwException
    {
        AbstractPropertyDefinition<String> propDef = new PropertyIdDefinitionImpl();
        propDef.setPropertyType(PropertyType.ID);
        propDef.setId(PropertyIds.BASE_TYPE_ID);

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(String.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + "." + PropertyIds.BASE_TYPE_ID, owDef.getClassName());
    }

    public void testIncorrectType()
    {
        AbstractPropertyDefinition<String> propDef = new PropertyIdDefinitionImpl();
        propDef.setPropertyType(PropertyType.BOOLEAN);
        propDef.setId("Fake-Id");
        propDef.setQueryName("No queryName");
        try
        {
            propFactory.createPropertyClass(null, propDef, objClass);
            fail("Incorrect type for definition should throw exception");
        }
        catch (OwException e)
        {

        }
    }

    public void testAttributes() throws Exception
    {
        AbstractPropertyDefinition<String> propDef = new PropertyStringDefinitionImpl();
        propDef.setPropertyType(PropertyType.STRING);
        propDef.setId(PropertyIds.NAME);
        propDef.setCardinality(Cardinality.SINGLE);
        propDef.setIsQueryable(Boolean.TRUE);
        propDef.setUpdatability(Updatability.ONCREATE);
        propDef.setQueryName("junitQueryName");

        OwCMISNativePropertyClass owDef = propFactory.createPropertyClass(null, propDef, objClass);
        assertNotNull(owDef);
        assertEquals(String.class.getCanonicalName(), owDef.getJavaClassName());
        assertEquals(objClass.getClassName() + "." + PropertyIds.NAME, owDef.getClassName());
        assertTrue(owDef.isNameProperty());
        assertEquals(false, owDef.isArray());

        assertTrue(owDef.isQueryable());
        assertEquals("junitQueryName", owDef.getQueryName());

        assertFalse(owDef.isReadOnly(OwPropertyClass.CONTEXT_ON_CREATE));
        assertTrue(owDef.isReadOnly(OwPropertyClass.CONTEXT_NORMAL));

        assertFalse(owDef.isRequired());
    }

}
