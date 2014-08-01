package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwStandardUnresolvedReference;
import com.wewebu.ow.server.ecmimpl.opencmis.OwCMISSession;
import com.wewebu.ow.server.ecmimpl.opencmis.log.OwLog;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * String CMIS id to {@link OwObjectReference} converter.
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
public class OwCMISIdObjectConverter implements OwCMISValueConverter<String, OwObjectReference>
{
    private static final Logger LOG = OwLog.getLogger(OwCMISIdObjectConverter.class);

    private OwCMISValueConverter<String, String> dmsidConverter;
    private OwCMISSession session;

    public OwCMISIdObjectConverter(OwCMISSession session_p, OwCMISValueConverter<String, String> dmsidConverter_p)
    {
        super();
        this.dmsidConverter = dmsidConverter_p;
        this.session = session_p;
    }

    public List<String> fromArrayValue(Object[] owdValue_p) throws OwInvalidOperationException
    {
        try
        {
            String[] owdDmsidValues = null;

            if (owdValue_p != null)
            {
                owdDmsidValues = new String[owdValue_p.length];
                for (int i = 0; i < owdDmsidValues.length; i++)
                {
                    if (owdValue_p[i] != null)
                    {
                        OwObjectReference referenceValue = (OwObjectReference) owdValue_p[i];
                        owdDmsidValues[i] = referenceValue.getDMSID();
                    }
                    else
                    {
                        owdDmsidValues[i] = null;
                    }
                }

            }
            return this.dmsidConverter.fromArrayValue(owdDmsidValues);
        }
        catch (OwInvalidOperationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISIdObjectConverter.fromArrayValue(): Could not retrieve DMSID!", e);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISIdObjectConverter.err.invalid.object", "Invalid object!"), e);
        }
    }

    public List<String> fromValue(OwObjectReference owdValue_p) throws OwInvalidOperationException
    {
        try
        {
            String owdDmsidValue = null;

            if (owdValue_p != null)
            {
                owdDmsidValue = owdValue_p.getDMSID();
                //TODO: Remove this. Temporary hack to cope with an Alfresco bug in the new WS/Atom bindings
                if (owdDmsidValue.contains(";"))
                {
                    owdDmsidValue = owdDmsidValue.split(";")[0];
                }
            }
            return this.dmsidConverter.fromValue(owdDmsidValue);
        }
        catch (OwInvalidOperationException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOG.error("OwCMISIdObjectConverter.fromArrayValue(): Could not retrieve DMSID!", e);
            throw new OwInvalidOperationException(new OwString("opencmis.OwCMISIdObjectConverter.err.invalid.object", "Invalid object!"), e);
        }
    }

    public OwObjectReference[] toArrayValue(List<String> cmisValue_p) throws OwInvalidOperationException
    {
        String[] owdDmsidValues = this.dmsidConverter.toArrayValue(cmisValue_p);
        if (owdDmsidValues != null)
        {
            OwObjectReference[] objects = new OwObjectReference[owdDmsidValues.length];
            for (int i = 0; i < owdDmsidValues.length; i++)
            {
                if (owdDmsidValues[i] != null)
                {
                    objects[i] = toObjectReference(owdDmsidValues[i], true);
                }
                else
                {
                    objects[i] = null;
                }
            }
            return objects;
        }
        else
        {
            return null;
        }
    }

    public OwObjectReference toValue(List<String> cmisValue_p) throws OwInvalidOperationException
    {
        String owdDmsidValue = this.dmsidConverter.toValue(cmisValue_p);
        return toObjectReference(owdDmsidValue, true);
    }

    /**
     * Will return every time a reference, even in exception case an OwUnresolvedReference is returned.
     * <p>Notice: Method will return null, if provided DMSID parameter is null.</p>
     * @param dmsid String to retrieve object
     * @param refresh boolean (cached instance or fresh from back-end)
     * @return OwObjectReference (can return null if DMSID is null)
     */
    protected OwObjectReference toObjectReference(String dmsid, boolean refresh)
    {
        if (dmsid == null)
        {
            return null;
        }
        try
        {
            return session.getObject(dmsid, refresh);
        }
        catch (Exception e)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.warn("Could not create object from DMSID = " + dmsid, e);
            }
            else
            {
                LOG.warn("OwCMISIdObjectConverter.toObjectReference(): Could not create object from DMSID = " + dmsid);
            }
            return new OwStandardUnresolvedReference(e, e.getMessage(), dmsid, null, null, 0);
        }
    }
}
