package com.wewebu.expression.language;

/**
 *<p>
 * Scope defining interface.
 * Scopes are indexed property and function domains.
 * Scope implementations provide function and property interrogating methods as well as     
 * well as indexed access helper methods.
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
public interface OwExprScope
{
    /**
     * Function access method.
     * 
     * @param functionName_p  the name of the requested function   
     * @param argunmentTyes_p array of {@link OwExprExpressionType} that defines the argument signature 
     *                        of the requested function 
     * @return an {@link OwExprFunction} with the requested name and argument signature residing in this scope
     *         
     * @throws OwExprEvaluationException if the requested function is not found in this scope or 
     *                                   the creation of the corresponding {@link OwExprFunction} has failed
     */
    OwExprFunction function(String functionName_p, OwExprExpressionType[] argunmentTyes_p) throws OwExprEvaluationException;

    /**
     * Property access method.
     * 
     * @param propertyName_p  the name of the requested property   

     * @return an {@link OwExprProperty} with the requested name residing in this scope
     *         
     * @throws OwExprEvaluationException if the requested property is not found in this scope or 
     *                                   the creation of the corresponding {@link OwExprProperty} has failed
     */
    OwExprProperty property(String propertyName_p) throws OwExprEvaluationException;

    /**
     * Indexed scope access method
     * 
     * @param index_p <code>int</code> index of the requested property
     * 
     * @return the {@link OwExprProperty} property found at the requested index 
     * 
     * @throws OwExprEvaluationException if the requested indexed access has failed (index out of bounds, 
     *         the creation of {@link OwExprProperty} has failed)  
     */
    OwExprProperty at(int index_p) throws OwExprEvaluationException;

    /**
     * Indexed scope access helper method
     * 
     * @return the maximum index range  for which indexed access is possible in this scope
     * 
     * @throws OwExprEvaluationException   
     */
    int length() throws OwExprEvaluationException;

    /**
     * Property access helper method.
     * Checks if a property is valid in this scope. 
     * 
     * @param propertyName_p the name of the requested property
     *    
     * @return <code>true</code> if this scope can perform property access for the requested property 
     *         - an access via {@link #property(String)} will NOT fail on missing property grounds, 
     *         <code>false</code> otherwise
     *  
     * @throws OwExprEvaluationException if the property validity check has failed
     */
    boolean hasProperty(String propertyName_p) throws OwExprEvaluationException;
}
