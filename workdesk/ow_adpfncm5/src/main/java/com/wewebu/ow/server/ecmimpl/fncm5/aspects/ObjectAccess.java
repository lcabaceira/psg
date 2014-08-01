package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.wewebu.ow.server.role.OwRoleManager;

/**
 *<p>
 * Role managed access annotation for content-object methods.
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
public @interface ObjectAccess
{
    int mask();

    String name() default " perform this action ";
}
