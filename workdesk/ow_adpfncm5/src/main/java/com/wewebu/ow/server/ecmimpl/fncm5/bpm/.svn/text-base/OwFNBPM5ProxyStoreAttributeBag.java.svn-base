package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository.OwProxyInfo;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;

/**
 *<p>
 * Proxy information class.
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
public class OwFNBPM5ProxyStoreAttributeBag implements OwFNBPM5ProxyStore
{

    /** the network to retrieve the attribute bags from */
    protected OwNetwork m_network;

    /** name of the attribute bag with the proxy information */
    private static final String PROXY_ATTRIBUTE_BAG_NAME = "OwFNBPMRepositoryProxySetting";

    /**
     *<p>
     * OwFNBPMProxyInfoAttributeBag.
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
    public static class OwFNBPMProxyInfoAttributeBag implements OwFNBPMProxyInfo
    {
        /** start time */
        private Date m_starttime;
        /** end time - can be null */
        private Date m_endtime;
        /** enable flag*/
        private boolean m_fEnabled = true;
        /** proxy person id*/
        private String m_sproxypersonid;
        /** absent proxy ID */
        private String m_sabsentpersonid;
        /**null time - convention*/
        private static long NULL_TIME = Long.MIN_VALUE;
        /**delimiter*/
        private static final String SERIALIZE_DELIMITER = ";";

        /**
         * Constructor.
         */
        public OwFNBPMProxyInfoAttributeBag()
        {
            m_starttime = new Date();
            m_endtime = new Date();
        }

        /** create a proxy info from a serialized string representation see getSerializeString
         * 
         * @param sProxyPersonID_p String ID of proxy person
         * @param sAbsentPersonID_p String ID of absent person
         * @param sProxyInfo_p String with serialized information
         */
        public OwFNBPMProxyInfoAttributeBag(String sProxyPersonID_p, String sAbsentPersonID_p, String sProxyInfo_p)
        {
            StringTokenizer proxyinfo = new StringTokenizer(sProxyInfo_p, SERIALIZE_DELIMITER);
            m_sproxypersonid = sProxyPersonID_p;
            m_sabsentpersonid = sAbsentPersonID_p;
            m_starttime = new Date(Long.parseLong(proxyinfo.nextToken()));
            long endtime = Long.parseLong(proxyinfo.nextToken());
            if (endtime == NULL_TIME)
            {
                m_endtime = null;
            }
            else
            {
                m_endtime = new Date(endtime);
            }
            m_fEnabled = proxyinfo.nextToken().equals("true");
        }

        public String getProxyPersonID()
        {
            return m_sproxypersonid;
        }

        public String getAbsentPersonID()
        {
            return m_sabsentpersonid;
        }

        public Date getStarttime()
        {
            return m_starttime;
        }

        public Date getEndtime()
        {

            return m_endtime;
        }

        public boolean getEnabled()
        {

            return m_fEnabled;
        }

        public void setProxyPersonID(String sID_p)
        {
            m_sproxypersonid = sID_p;
        }

        public void setStarttime(Date starttime_p)
        {
            m_starttime = starttime_p;
        }

        public void setEndtime(Date endtime_p)
        {
            m_endtime = endtime_p;
        }

        public void setEnabled(boolean fEnable_p)
        {
            m_fEnabled = fEnable_p;
        }

        /** create a serialized string representation of the proxy info, can be de-serialized with constructor */
        public static String getSerializeString(OwProxyInfo proxy_p)
        {
            StringBuffer buf = new StringBuffer();
            buf.append(proxy_p.getStarttime().getTime());
            buf.append(SERIALIZE_DELIMITER);
            Date endtime = proxy_p.getEndtime();
            if (endtime == null)
            {
                buf.append(NULL_TIME);
            }
            else
            {
                buf.append(endtime.getTime());
            }
            buf.append(SERIALIZE_DELIMITER);
            buf.append(proxy_p.getEnabled());
            buf.append(SERIALIZE_DELIMITER);
            return buf.toString();
        }

    }

    public void init(OwNetwork network_p) throws Exception
    {
        if (network_p == null)
        {
            throw new OwInvalidOperationException("OwFNBPMProyStoreAttributeBag.init: Invalid Parameters. network_p == null");
        }
        m_network = network_p;
    }

    public OwProxyInfo createProxy() throws Exception
    {
        return (new OwFNBPMProxyInfoAttributeBag());
    }

    public Collection getAbsents(String proxypersonID_p) throws Exception
    {
        // get inverse attribute bag from network
        OwAttributeBag attrBag = (OwAttributeBag) m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_INVERTED_ATTRIBUTE_BAG, PROXY_ATTRIBUTE_BAG_NAME, proxypersonID_p, true, false);
        // create result list
        ArrayList result = new ArrayList(attrBag.attributecount());
        // copy attributes
        Collection attributeNames = attrBag.getAttributeNames();
        Iterator itAttributeNames = attributeNames.iterator();
        while (itAttributeNames.hasNext())
        {
            String absentPerson = (String) itAttributeNames.next();
            String proxyInfo = (String) attrBag.getAttribute(absentPerson);
            result.add(new OwFNBPMProxyInfoAttributeBag(proxypersonID_p, absentPerson, proxyInfo));
        }
        // return result
        return (result);
    }

    public Collection getProxies(String absentpersonID_p) throws Exception
    {
        // get attribute bag from network
        OwAttributeBag attrBag = (OwAttributeBag) m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, PROXY_ATTRIBUTE_BAG_NAME, absentpersonID_p, true, false);
        // create result list
        ArrayList result = new ArrayList(attrBag.attributecount());
        // copy attributes
        Collection attributeNames = attrBag.getAttributeNames();
        Iterator itAttributeNames = attributeNames.iterator();
        while (itAttributeNames.hasNext())
        {
            String proxyPerson = (String) itAttributeNames.next();
            String proxyInfo = (String) attrBag.getAttribute(proxyPerson);
            result.add(new OwFNBPMProxyInfoAttributeBag(proxyPerson, absentpersonID_p, proxyInfo));
        }
        // return result
        return (result);
    }

    public void setProxies(Collection proxies_p, String absentpersonID_p) throws Exception
    {
        // get attribute bag from network
        OwAttributeBagWriteable attrBag = (OwAttributeBagWriteable) m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, PROXY_ATTRIBUTE_BAG_NAME, absentpersonID_p, true, false);
        // empty attribute bag
        attrBag.clear();
        attrBag.save();
        // set new proxies
        // refresh the bag
        attrBag = (OwAttributeBagWriteable) m_network.getApplicationObject(OwNetwork.APPLICATION_OBJECT_TYPE_ATTRIBUTE_BAG_WRITABLE, PROXY_ATTRIBUTE_BAG_NAME, absentpersonID_p, true, false);
        Iterator it = proxies_p.iterator();
        while (it.hasNext())
        {
            OwProxyInfo info = (OwProxyInfo) it.next();
            attrBag.setAttribute(info.getProxyPersonID(), OwFNBPMProxyInfoAttributeBag.getSerializeString(info));
        }
        // save attribute bag
        attrBag.save();
    }

}
