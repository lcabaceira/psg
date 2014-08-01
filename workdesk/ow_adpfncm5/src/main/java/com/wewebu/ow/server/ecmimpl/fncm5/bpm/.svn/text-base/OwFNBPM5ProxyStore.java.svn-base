package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.ecm.OwNetwork;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository.OwProxyInfo;

/**
 *<p>
 * Base interface for serializing proxy information.
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
public interface OwFNBPM5ProxyStore
{
    /** Overridden interface with additional functions for internal use. */
    public static interface OwFNBPMProxyInfo extends OwProxyInfo
    {
        /** the absent person the proxy is working for */
        public abstract String getAbsentPersonID();

    }

    /** init the store
     * 
     * @param network_p
     */
    public abstract void init(OwNetwork network_p) throws Exception;

    /**
     * create a new proxy info used in setProxies
     */
    public abstract OwProxyInfo createProxy() throws Exception;

    /** set a proxies, to receive the workitems
     * 
     * @param proxies_p Collection of OwProxyInfo
     * @throws Exception
     */
    public abstract void setProxies(java.util.Collection proxies_p, String absentpersonID_p) throws Exception;

    /** 
     * get proxy info of absent persons for given proxy person
     * 
     * @param proxypersonID_p String ID of proxy person
     * @return Collection of OwProxyInfo
     * */
    public abstract java.util.Collection getAbsents(String proxypersonID_p) throws Exception;

    /** 
    * get proxy info for given absent person
    * 
    * @param absentpersonID_p String ID of absent person
    * @return Collection of OwProxyInfo
    * */
    public abstract java.util.Collection getProxies(String absentpersonID_p) throws Exception;
}
