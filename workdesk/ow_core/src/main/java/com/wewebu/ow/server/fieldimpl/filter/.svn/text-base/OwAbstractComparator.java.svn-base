package com.wewebu.ow.server.fieldimpl.filter;

import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.filter.OwComparator;

/**
 *<p>
 * Abstract comparator implementation.
 * Provides a process to implement only the operation
 * depending functionality, and automatically negation
 * if NOT operation.
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
 *@since 3.2.0.0
 */
public abstract class OwAbstractComparator<T> implements OwComparator<T>
{
    public boolean match(OwSearchCriteria filter, T value)
    {
        boolean retVal = false;
        switch (filter.getOperator())
        {
            case OwSearchOperator.CRIT_OP_NOT_EQUAL:
            case OwSearchOperator.CRIT_OP_EQUAL:
            {
                retVal = matchEquals(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            case OwSearchOperator.CRIT_OP_LIKE:
            {
                retVal = matchLike(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_GREATER:
            {
                retVal = matchGreater(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_LESS:
            {
                retVal = matchLess(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_GREATER_EQUAL:
            {
                retVal = matchGreaterEquals(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_LESS_EQUAL:
            {
                retVal = matchLessEquals(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_NOT_BETWEEN:
            case OwSearchOperator.CRIT_OP_BETWEEN:
            {
                retVal = matchBetween(filter, value);
            }
                break;

            case OwSearchOperator.CRIT_OP_IS_NOT_NULL:
            case OwSearchOperator.CRIT_OP_IS_NULL:
            {
                retVal = value == null;
            }
        }

        if (OwSearchOperator.isCriteriaOperatorNot(filter.getOperator()))
        {
            retVal = !retVal;
        }
        return retVal;
    }

    /**
     * Between value compare, where both limits must be include
     * for comparison.<b> Neither the minimum limit nor the maximum
     * limit should be excluded.</b>
     * @param filter OwSearchCriteria providing the limit(s)
     * @param value T to match with criteria
     * @return boolean if matches criteria
     */
    protected abstract boolean matchBetween(OwSearchCriteria filter, T value);

    /**
     * Less or equals compare of the value and filter.
     * @param filter OwSearchCriteria to restrict value
     * @param value T to match against filter
     * @return boolean if matches criteria 
     */
    protected abstract boolean matchLessEquals(OwSearchCriteria filter, T value);

    /**
     * Greater or equals compare of given value and filter.
     * @param filter OwSearchCriteria to restrict value
     * @param value T to match against filter
     * @return boolean if matches criteria 
     */
    protected abstract boolean matchGreaterEquals(OwSearchCriteria filter, T value);

    /**
     * Value must be less than the defined filter.
     * @param filter OwSearchCriteria to restrict value
     * @param value T to match against filter
     * @return boolean if matches criteria
     */
    protected abstract boolean matchLess(OwSearchCriteria filter, T value);

    /**
     * Value restriction where it must be greater as the filter.
     * @param filter OwSearchCriteria to restrict value
     * @param value T to match against filter
     * @return boolean if matches criteria
     */
    protected abstract boolean matchGreater(OwSearchCriteria filter, T value);

    /**
     * A like compare of the value, the filter criteria
     * can be used to get the representation of wildcard characters.
     * @param filter OwSearchCriteria to restrict value
     * @param value T to match against filter
     * @return boolean if matches criteria
     */
    protected abstract boolean matchLike(OwSearchCriteria filter, T value);

    /**
     * Value and filter value must be equal! But equal operation is 
     * depending on the type and restriction of the given value.
     * <p>For special cases (Date, Number,...) an equal compare will still return 
     * true even if the filter and given value not match fully each other .</p>
     * @param filter OwSearchCriteria to restrict value
     * @param value T to match against filter
     * @return boolean if matches criteria
     */
    protected abstract boolean matchEquals(OwSearchCriteria filter, T value);

}
