package org.jdesktop.jdic.desktop.internal;

/*
 * Copyright (C) 2004 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

import org.jdesktop.jdic.desktop.internal.impl.ServiceManagerStub_windows;
import org.jdesktop.jdic.init.JdicManager;

/**
 * The <code>ServiceManager</code> class provides static fields to refer to 
 * the available services, and static methods to get the approprate service 
 * objects with the given service name. This class is abstract and final and 
 * cannot be instantiated.
 * 

 * @see     LaunchService
 * @see     BrowserService
 * @see     MailerService
 */
public class ServiceManager
{

    /**
     * Constant name for looking up the launch service object.
     */
    public static final String LAUNCH_SERVICE = "LaunchService";

    /**
     * Constant name for looking up the browser service object.
     */
    public static final String BROWSER_SERVICE = "BrowserService";

    /**
     * Constant name for looking up the mailer service object.
     */
    public static final String MAILER_SERVICE = "MailerService";

    /**
     * Suppress default constructor for noninstantiability.
     */
    private ServiceManager()
    {
    }

    static java.lang.reflect.Method mdServiceManagerStub_getService = null;

    /**
     * Gets a service object with the given name. The given service name should be one 
     * of the pre-defined service names.
     * 
     * @param  serviceName_p the given service name.
     * @return the appropriate service object.
     * @throws NullPointerException if the given service name is null.
     * @see    LaunchService 
     * @see    BrowserService 
     * @see    MailerService
     */
    //    public static Object getService(String serviceName) throws NullPointerException
    //    {
    //        if (serviceName == null)
    //        {
    //            throw new NullPointerException("Service name is null.");
    //        }
    //        //return ServiceManagerStub.getService(serviceName);    
    //        //works really dynamic now
    //        try
    //        {
    //            if (null == mdServiceManagerStub_getService)
    //            {
    //                mdServiceManagerStub_getService = ClassLoader.getSystemClassLoader().loadClass("org.jdesktop.jdic.desktop.internal.impl.ServiceManagerStub_" + JdicManager.getPlatformSuffix()).getMethod("getService", new Class[] { String.class });
    //                String suffix = JdicManager.getPlatformSuffix();
    //            }
    //            return mdServiceManagerStub_getService.invoke(null, //static method
    //                    new Object[] { serviceName });
    //        }
    //        catch (Exception e)
    //        {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }
    public static Object getService(String serviceName_p) throws NullPointerException
    {
        if (serviceName_p == null)
        {
            throw new NullPointerException("Service name is null.");
        }

        try
        {
            String suffix = JdicManager.getPlatformSuffix();
            if ("windows".equals(suffix))
            {
                return ServiceManagerStub_windows.getService(serviceName_p);
            }
            else if ("unix".equals(suffix))
            {
                throw new RuntimeException("UNIX Service implementation is missing!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}