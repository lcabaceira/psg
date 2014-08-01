package com.wewebu.expression.testmodel;

import java.util.Date;

/**
*<p>
* OwExprPerson. 
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
public class OwExprPerson
{
    private String m_firstName;
    private String m_lastName;

    private OwExprAddress[] m_addresses;

    private String[][] m_phoneNumbers = new String[][] {};

    private Date m_birthDate;

    public OwExprPerson(String firstName_p, String lastName_p, OwExprAddress[] addresses_p, Date birthDate_p, String[][] phoneNumbers_p)
    {
        super();
        this.m_firstName = firstName_p;
        this.m_lastName = lastName_p;
        this.m_addresses = addresses_p;
        this.m_birthDate = birthDate_p;
        this.m_phoneNumbers = phoneNumbers_p;
    }

    public final String getFirstName()
    {
        return m_firstName;
    }

    public final String getLastName()
    {
        return m_lastName;
    }

    public final OwExprAddress[] getAddresses()
    {
        return m_addresses;
    }

    public final OwExprAddress getMainAddress()
    {
        if (m_addresses != null && m_addresses.length > 0)
        {
            return m_addresses[0];
        }
        else
        {
            return null;
        }
    }

    public final Date getBirthDate()
    {
        return m_birthDate;
    }

    public final String[][] getPhoneNumbers()
    {
        return m_phoneNumbers;
    }
}
