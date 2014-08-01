package com.wewebu.ow.server.util.ldap;

import java.util.Properties;
import java.util.Random;

import junit.framework.TestCase;

/**
 * OwLdapConnectionTest.
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
public class OwLdapConnectionTest extends TestCase
{

    public void testRecursionLevel()
    {
        int level = new Random().nextInt();
        OwLdapConnection con = new OwLdapConnection(new Properties(), level);

        assertEquals(level, con.getRecursionLevel());
    }

    public void testAnonymousHandling()
    {
        Properties prop = new Properties();
        prop.put("anonymousLogin", "true");

        OwLdapConnection con = new OwLdapConnection(prop, 9);
        assertEquals("none", con.getProperty("java.naming.security.authentication", "anonymous authentication removed"));
    }

}
