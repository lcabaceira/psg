package com.wewebu.ow.server.ao;

/**
 *<p>
 * Interface which should be implemented for type retrieval.
 * Basically an interface for Parameter object of {@link com.wewebu.ow.server.ao.OwAOProvider OwAOProvider}
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
 *@since 4.0.0.0
 */
public interface OwAOType<T>
{
    /**
     * Integer representing an specific
     * value for the requested application object.
     * @return int
     */
    int getType();

    /**
     * Class which the type will be cast to.
     * @return Class
     */
    Class<T> getClassType();

}
