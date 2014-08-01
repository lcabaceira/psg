package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * Role managed access annotation.
 * Annotated entities are subjected to role access management.
 * @see OwFNCM5RoleManagement
 * @see OwRoleManager#hasAccessMaskRight(int, String, int)
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
@Retention(RetentionPolicy.RUNTIME)
public @interface Access
{
    int mask();

    int category() default -1;

    String resource() default "";

    /**
     * 
     * @return name of this action (used for generic action logging)
     */
    String name() default " perform this action ";
}
