package com.wewebu.ow.server.field;

/**
 *<p>
 * Interface for Wild character definitions used in searches.<br/><br/>
 * To be implemented with the specific ECM system.
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
public interface OwWildCardDefinition
{
    /** wild character definition */
    public static final int WILD_CARD_TYPE_MULTI_CHAR = 1;
    /** wild character definition */
    public static final int WILD_CARD_TYPE_SINGLE_CHAR = 2;

    /** own wild character definitions start here */
    public static final int WILD_CARD_TYPE_CUSTOM_START = 0x1000;

    /** get the wildcard character that is used by the client and will be replaced by the native wildcard
     * 
     * @return String representing the wildcard character placeholder like "*"
     */
    public abstract String getWildCard();

    /** get the native wildcard character that is used internally by the system
     * 
     * @return String representing the native wildcard character like "%"
     */
    public abstract String getNativeWildCard();

    /** get the type of wildcard defined by this character
     * 
     * @return int as defined with WILD_CARD_TYPE_...
     */
    public abstract int getType();

    /** get a description of this wild character */
    public abstract String getDescription(java.util.Locale locale_p);
}