package com.wewebu.ow.server.plug.efilekey.pattern;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.plug.efilekey.generator.OwKeyPropertyResolver;

/**
 *<p>
 * Interface for eFile key pattern.<br/>
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
 *@since 3.1.0.0
 */
public interface OwKeyPattern
{
    /**
     * Create a {@link String} representation for the given pattern, based on the given {@link OwKeyPropertyResolver} object
     * @param resolver_p the {@link OwKeyPropertyResolver} object.
     * @return - the {@link String} object created from properties resolver, according with the pattern.
     * @throws OwInvalidOperationException thrown when the property cannot be resolved.
     */
    String createStringImage(OwKeyPropertyResolver resolver_p) throws OwInvalidOperationException;
}
