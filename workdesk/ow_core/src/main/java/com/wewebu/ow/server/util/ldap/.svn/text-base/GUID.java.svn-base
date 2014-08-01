package com.wewebu.ow.server.util.ldap;

/**
 *<p>
 * Represents a GUID
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
public class GUID
{
    private byte[] binary;
    private String asString;

    public GUID(byte[] binary)
    {
        this.binary = binary;
        this.asString = GUID.convert(this.binary);
    }

    private static String convert(byte[] binary)
    {
        byte[] GUID = binary;
        StringBuilder byteGUID = new StringBuilder();
        //Convert the GUID into string using the byte format
        for (int c = 0; c < GUID.length; c++)
        {
            byteGUID.append("\\").append(addLeadingZero(GUID[c] & 0xFF));
        }
        //convert the GUID into string format
        //        String strGUID = "{";
        //        strGUID = strGUID + addLeadingZero(GUID[3] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[2] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[1] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[0] & 0xFF);
        //        strGUID = strGUID + "-";
        //        strGUID = strGUID + addLeadingZero(GUID[5] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[4] & 0xFF);
        //        strGUID = strGUID + "-";
        //        strGUID = strGUID + addLeadingZero(GUID[7] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[6] & 0xFF);
        //        strGUID = strGUID + "-";
        //        strGUID = strGUID + addLeadingZero(GUID[8] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[9] & 0xFF);
        //        strGUID = strGUID + "-";
        //        strGUID = strGUID + addLeadingZero(GUID[10] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[11] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[12] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[13] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[14] & 0xFF);
        //        strGUID = strGUID + addLeadingZero(GUID[15] & 0xFF);
        //        strGUID = strGUID + "}";

        return byteGUID.toString();
    }

    private static String addLeadingZero(int k)
    {
        return (k < 0xF) ? "0" + Integer.toHexString(k) : Integer.toHexString(k);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.asString;
    }
}
