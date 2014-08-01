package com.wewebu.ow.server.util;

/**
 *<p>
 * Interface for writing Attributes, extends the OwAttributeBag interface.
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
public interface OwAttributeBagWriteable extends OwAttributeBag
{
    /** set a object
     * 
     * @param strName_p
     * @param value_p
     * @throws Exception
     */
    public abstract void setAttribute(String strName_p, Object value_p) throws Exception;

    /** save the contents of the attribute bag */
    public abstract void save() throws Exception;

    /** clears the contents of the attribute bag */
    public abstract void clear() throws Exception;

    /** remove the given attribute 
     * 
     * @param strName_p String name of attribute
     */
    public abstract void remove(String strName_p);

}