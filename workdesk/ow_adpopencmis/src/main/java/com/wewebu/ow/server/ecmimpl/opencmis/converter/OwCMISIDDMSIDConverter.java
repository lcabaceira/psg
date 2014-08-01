package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISDMSIDDecoder;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISNetwork;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSimpleDMSID;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * OwCMISIdDMSIDConverter.
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
public class OwCMISIDDMSIDConverter implements OwCMISValueConverter<String, String>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISIDDMSIDConverter.class);

    protected OwCMISDMSIDDecoder dmsidDecoder;
    protected String resourceID;

    public OwCMISIDDMSIDConverter(OwCMISDMSIDDecoder dmsidDecoder_p, String resourceID_p)
    {
        super();
        this.dmsidDecoder = dmsidDecoder_p;
        this.resourceID = resourceID_p;
    }

    public List<String> fromArrayValue(Object[] owdValue_p) throws OwInvalidOperationException
    {
        LinkedList<String> list = new LinkedList<String>();
        if (owdValue_p != null)
        {
            for (int i = 0; i < owdValue_p.length; i++)
            {
                try
                {
                    if (owdValue_p[i] != null)
                    {
                        OwCMISDMSID dmsid = this.dmsidDecoder.createDMSID((String) owdValue_p[i]);
                        list.add(dmsid.getCMISID());
                    }
                    else
                    {
                        list.add(null);
                    }
                }
                catch (OwException e)
                {
                    LOG.error("OwCMISIdDMSIDConverter.fromArrayValue(): Invalid DMSID value : " + owdValue_p[i], e);
                    throw new OwInvalidOperationException(new OwString("opencmis.OwCMISIdDMSIDConverter.err.invalid.dmsid", "Invalid DMSID found!"), e);
                }
            }
        }

        return list;
    }

    public List<String> fromValue(String owdValue_p) throws OwInvalidOperationException
    {
        if (owdValue_p != null)
        {
            try
            {
                OwCMISDMSID dmsid = this.dmsidDecoder.createDMSID(owdValue_p);
                LinkedList<String> list = new LinkedList<String>();
                list.add(dmsid.getCMISID());
                return list;
            }
            catch (OwException e)
            {
                LOG.error("OwCMISIdDMSIDConverter.fromArrayValue(): Invalid DMSID value : " + owdValue_p, e);
                throw new OwInvalidOperationException(new OwString("opencmis.OwCMISIdDMSIDConverter.err.invalid.dmsid", "Invalid DMSID found!"), e);
            }
        }
        else
        {
            return new LinkedList<String>();
        }
    }

    public Class<String> getOClass()
    {
        return String.class;
    }

    public String[] toArrayValue(List<String> cmisValue_p)
    {
        if (cmisValue_p == null || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            String[] stringValues = new String[cmisValue_p.size()];
            for (int i = 0; i < stringValues.length; i++)
            {
                String id = cmisValue_p.get(i);
                if (id != null)
                {
                    OwCMISDMSID dmsid = fromID(id);
                    stringValues[i] = dmsid.getDMSIDString();
                }
                else
                {
                    stringValues[i] = null;
                }
            }
            return stringValues;
        }
    }

    /**(overridable)
     * Create an OwCMISDMSID instance from provided String.
     * @param id_p String representing object id
     * @return OwCMISDMSID instance
     */
    protected OwCMISDMSID fromID(String id_p)
    {
        return new OwCMISSimpleDMSID(this.dmsidDecoder.getDMSIDPrefix(), this.resourceID, id_p);
    }

    public String toValue(List<String> cmisValue_p)
    {
        if (cmisValue_p == null || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            String id = cmisValue_p.get(0);
            OwCMISDMSID dmsid = fromID(id);
            return dmsid.getDMSIDString();
        }
    }

    public OwCMISValueConverter<String, String> newConverter(OwCMISNetwork network_p)
    {
        return this;
    }

    public String[] toStaticArrayValue(List<String> cmisValue_p) throws OwInvalidOperationException
    {
        if (cmisValue_p == null || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            String[] stringValues = new String[cmisValue_p.size()];
            for (int i = 0; i < stringValues.length; i++)
            {
                String id = cmisValue_p.get(i);
                OwCMISDMSID dmsid = new OwCMISSimpleDMSID(this.dmsidDecoder.getDMSIDPrefix(), this.resourceID, id);
                stringValues[i] = dmsid.getDMSIDString();
            }
            return stringValues;
        }
    }

    public String toStaticValue(List<String> cmisValue_p) throws OwInvalidOperationException
    {
        if (null == cmisValue_p || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            String id = cmisValue_p.get(0);
            OwCMISDMSID dmsid = new OwCMISSimpleDMSID(this.dmsidDecoder.getDMSIDPrefix(), this.resourceID, id);
            return dmsid.getDMSIDString();
        }
    }

}