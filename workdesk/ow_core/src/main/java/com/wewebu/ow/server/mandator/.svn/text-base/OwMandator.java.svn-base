package com.wewebu.ow.server.mandator;

import com.wewebu.ow.server.util.OwAttributeBag;

/**
 *<p>
 * Interface for mandators (multitenancy, multi-tenant) to specify the mandator configuration data.<br/>
 * To be implemented with the specific ECM system.<br/><br/>
 * You get a instance of the mandator manager by calling getContext().getMandatorManager().getUserMandator()
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
public interface OwMandator extends OwAttributeBag
{
    /** get a unique ID for this mandator
     * 
     * @return a {@link String}
     */
    public abstract String getID();

    /** get a name for this mandator
     * 
     * @return a {@link String}
     */
    public abstract String getName();

    /** get a description for this mandator
     * 
     * @return a {@link String}
     */
    public abstract String getDescription();

    /** create a unique role name for the given role name 
     * 
     * @param rolename_p
     * @return String rolename
     */
    public abstract String filterRoleName(String rolename_p);
}
