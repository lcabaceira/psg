package com.wewebu.ow.server.ecm;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwAttributeBag;
import com.wewebu.ow.server.util.OwAttributeBagWriteable;
import com.wewebu.ow.server.util.OwTransientBagRepository;

/**
 *<p>
 * An application {@link OwAttributeBag} provider.  Implementors of this interface
 * provide application objects as {@link OwAttributeBag}s from different persistence systems 
 * (egg. DB based , in memory attribute bags storage - {@link OwTransientBagRepository}).
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
 *@since 3.1.0.0
 */
public interface OwAttributeBagsSupport
{

    /** get a inverted writable attribute bag based an a attribute name key
     *  i.e.: the attributenames of the bag represent the users
     *  @see OwNetwork#APPLICATION_OBJECT_TYPE_INVERTED_ATTRIBUTE_BAG
     *  
     * @param networkContext_p 
     * @param name_p
     * @param userID_p
     * @return an {@link OwAttributeBag}
     * @throws OwException
     */
    OwAttributeBag getNameKeyAttributeBag(OwNetworkContext networkContext_p, String name_p, String userID_p) throws Exception;

    /** get a writable attribute bag based an a user key
     *  i.e.: the attributenames of the bag represent the attribute names
     *  
     * @param name_p
     * @param userID_p
     * @return an {@link OwAttributeBagWriteable}
     * @throws OwException
     */
    OwAttributeBagWriteable getUserKeyAttributeBagWriteable(OwNetworkContext networkContext_p, String name_p, String userID_p) throws Exception;

}
