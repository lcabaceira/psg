package com.wewebu.expression.docsamples;

import java.util.Calendar;

/**
*<p>
* OwExprOrderDocument. 
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
public class OwExprOrderDocument
{
    private String m_clientId;
    private String m_documentStatus;
    private int m_amount;
    private Calendar m_uploadDate;
    private double m_finalPrize;
    private double m_listPrize;
    private double m_trasholdPrize;
    private boolean m_force;
    private boolean[] m_approved;

    public OwExprOrderDocument(String clientId_p, String documentStatus_p, int amount_p, Calendar uploadDate_p, double finalPrize_p, double listPrize_p, double trasholdPrize_p, boolean force_p, boolean[] approved_p)
    {
        super();
        this.m_clientId = clientId_p;
        this.m_documentStatus = documentStatus_p;
        this.m_amount = amount_p;
        this.m_uploadDate = uploadDate_p;
        this.m_finalPrize = finalPrize_p;
        this.m_listPrize = listPrize_p;
        this.m_trasholdPrize = trasholdPrize_p;
        this.m_force = force_p;
        this.m_approved = approved_p;
    }

    public final String getClientId()
    {
        return m_clientId;
    }

    public final String getDocumentStatus()
    {
        return m_documentStatus;
    }

    public final int getAmount()
    {
        return m_amount;
    }

    public final Calendar getUploadDate()
    {
        return m_uploadDate;
    }

    public final double getFinalPrize()
    {
        return m_finalPrize;
    }

    public final double getListPrize()
    {
        return m_listPrize;
    }

    public final double getTrasholdPrize()
    {
        return m_trasholdPrize;
    }

    public final boolean getForce()
    {
        return m_force;
    }

    private int getUpDateDay()
    {
        return m_uploadDate.get(Calendar.DAY_OF_MONTH);
    }

    private int getUpDateMonth()
    {
        return m_uploadDate.get(Calendar.MONTH) + 1;
    }

    private int getUpDateYear()
    {
        return m_uploadDate.get(Calendar.YEAR);
    }

    public final boolean[] getApproved()
    {
        return m_approved;
    }

    public int hashCode()
    {
        return m_clientId.hashCode();
    }

    public boolean equals(Object obj_p)
    {
        if (obj_p instanceof OwExprOrderDocument)
        {
            OwExprOrderDocument odObj = (OwExprOrderDocument) obj_p;

            return m_clientId.equals(odObj.m_clientId) && m_uploadDate.equals(odObj.m_uploadDate) && m_amount == odObj.m_amount && m_finalPrize == odObj.m_finalPrize && m_listPrize == odObj.m_listPrize && m_trasholdPrize == odObj.m_trasholdPrize
                    && m_approved[0] == odObj.m_approved[0] && m_approved[1] == odObj.m_approved[1] && m_approved[2] == odObj.m_approved[2];
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {

        return "" + m_clientId + "\t" + m_documentStatus + "\t" + m_amount + "\t" + getUpDateDay() + "/" + getUpDateMonth() + "/" + getUpDateYear() + "\t" + getFinalPrize() + "\t" + m_listPrize + "\t" + m_trasholdPrize + "\t" + m_force + "\t"
                + m_approved[0] + "," + m_approved[1] + "," + m_approved[2];
    }

}
