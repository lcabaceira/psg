package org.alfresco.wd.util.loader;

/**
 *<p>
 * Interface for loading instances of a type.
 * Defining one method which will be used to create corresponding type.
 * Behavior can be different/implementation specific, either loading will generate always a new instance (&quot;factory&quot; pattern)
 * or it will lazy load an instance (&quot;singelton&quot; pattern).
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
 *@since 4.2.0.0
 */
public interface OwLoader<T>
{
    /**
     * Load/create a type specific to current loader type T.
     * Method can either work as lazy instantiation or as factory method for configured type.
     * @return T specific type of loader.
     */
    T load();
}
