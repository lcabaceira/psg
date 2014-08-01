package com.wewebu.ow.server.util;

import junit.framework.TestCase;

import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView;
import com.wewebu.ow.server.dmsdialogs.views.OwObjectPropertyFormularView.OwPropertyPlaceholder;

public class OwPropertyPlaceholderTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected OwPropertyPlaceholder newTestPlaceHolder(String strPlaceholder_p)
    {
        return new OwObjectPropertyFormularView.OwPropertyPlaceholder(strPlaceholder_p);
    }

    public void testPlaceholder1() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("Customer");

        assertFalse(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder2() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("Customer.Address");

        assertFalse(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertTrue(ph.hasSubIdentifier());
        assertEquals("Address", ph.getSubIdentifier());
    }

    public void testPlaceholder3() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("ow_ro_Customer.Address");

        assertTrue(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertTrue(ph.hasSubIdentifier());
        assertEquals("Address", ph.getSubIdentifier());
    }

    public void testPlaceholder4() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("ow_ro_Customer");

        assertTrue(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder5() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("ow_err_Customer");

        assertFalse(ph.isReadOnly());
        assertTrue(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder6() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("ow_validate_Customer");

        assertFalse(ph.isReadOnly());
        assertFalse(ph.isError());
        assertTrue(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder7() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("{Customer}");

        assertFalse(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("Customer", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder8() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("{owd:Customer.owd:Address}");

        assertFalse(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("owd:Customer.owd:Address", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder9() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("{owd:Customer.owd:Address}.{owd:AddressType.owd:Street}");

        assertFalse(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("owd:Customer.owd:Address", ph.getIdentifier());
        assertTrue(ph.hasSubIdentifier());
        assertEquals("owd:AddressType.owd:Street", ph.getSubIdentifier());
    }

    public void testPlaceholder10() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("ow_ro_{owd:Customer.owd:Address}");

        assertTrue(ph.isReadOnly());
        assertFalse(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("owd:Customer.owd:Address", ph.getIdentifier());
        assertFalse(ph.hasSubIdentifier());
        assertNull(ph.getSubIdentifier());
    }

    public void testPlaceholder11() throws Exception
    {
        OwPropertyPlaceholder ph = newTestPlaceHolder("ow_err_{owd:Customer.owd:Address}.{owd:AddressType.owd:Street}");

        assertFalse(ph.isReadOnly());
        assertTrue(ph.isError());
        assertFalse(ph.isValidation());

        assertEquals("owd:Customer.owd:Address", ph.getIdentifier());
        assertTrue(ph.hasSubIdentifier());
        assertEquals("owd:AddressType.owd:Street", ph.getSubIdentifier());
    }
}
