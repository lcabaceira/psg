package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters;

import java.util.Collection;
import java.util.Collections;

import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Converts a {@link OwUserInfo} instance into String and vice versa.
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
 *@since 4.2.0.0.0
 */
public class OwUserInfoNativeValueConverter implements NativeValueConverter
{

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromNative(java.lang.Object)
     */
    @Override
    public Object fromNative(Object nativeValue) throws OwException
    {
        //        throw new RuntimeException("Not implemented yet!");
        return new OwBasicUserInfo((String) nativeValue);

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.converters.NativeValueConverter#fromJava(java.lang.Object)
     */
    @Override
    public Object fromJava(Object javaValue) throws OwException
    {
        return ((OwUserInfo) javaValue).getUserID();
    }

    private static class OwBasicUserInfo implements OwUserInfo
    {
        String name;

        protected OwBasicUserInfo(String name)
        {
            this.name = name;
        }

        @Override
        public String getUserLongName() throws Exception
        {
            return name;
        }

        @Override
        public String getUserName() throws Exception
        {
            return name;
        }

        @Override
        public String getUserDisplayName() throws Exception
        {
            return name;
        }

        @Override
        public String getUserShortName() throws Exception
        {
            return name;
        }

        @Override
        public String getUserEmailAdress() throws Exception
        {
            return name;
        }

        @Override
        public String getUserID()
        {
            return name;
        }

        @Override
        public Collection getRoleNames() throws Exception
        {
            // TODO Auto-generated method stub
            return Collections.emptyList();
        }

        @Override
        public Collection getGroups() throws Exception
        {
            return Collections.EMPTY_LIST;
        }

        @Override
        public boolean isGroup() throws Exception
        {
            // TODO Auto-generated method stub
            return false;
        }

    }
}
