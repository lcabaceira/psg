package com.wewebu.expression.testmodel;

/**
*<p>
* OwExprAddress. 
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
public class OwExprAddress
{
    private String m_postalCode;
    private String m_street;
    private int m_number;
    private String m_city;

    public OwExprAddress(String postalCode_p, String street_p, int number_p, String city_p)
    {
        super();
        this.m_postalCode = postalCode_p;
        this.m_street = street_p;
        this.m_number = number_p;
        this.m_city = city_p;
    }

    public final String getPostalCode()
    {
        return m_postalCode;
    }

    public final String getStreet()
    {
        return m_street;
    }

    public final int getNumber()
    {
        return m_number;
    }

    public final String getCity()
    {
        return m_city;
    }

}
