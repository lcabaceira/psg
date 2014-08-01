package com.wewebu.ow.server.util;

/**
 *<p>
 * Interface for attribute bags, which describe the attribute of an object.
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
public interface OwAttributeBag
{
    /** get a object at the specified index or throws OwObjectNotFoundException
     * 
     * @param iIndex_p int index
     * @return Object
     */
    public abstract Object getAttribute(int iIndex_p) throws Exception;

    /** get the attribute with the given name  or throws OwObjectNotFoundException */
    public abstract Object getAttribute(String strName_p) throws Exception;

    /** get the attribute with the given name, returns default if not found. */
    public abstract Object getSafeAttribute(String strName_p, Object default_p);

    /** check if attribute exists */
    public abstract boolean hasAttribute(String strName_p);

    /** get the number of attributes, or -1 if unknown */
    public abstract int attributecount();

    /** get all attribute names in the bag */
    public abstract java.util.Collection getAttributeNames();
}