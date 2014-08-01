package com.wewebu.ow.server.ecmimpl.opencmis.server;

import java.text.ParseException;
import java.util.Date;

import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISDateTime;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.unittest.search.OwSearchTestFieldDefinition;

public class OwCMISSearchTestFieldDefinition extends OwSearchTestFieldDefinition
{
    public OwCMISSearchTestFieldDefinition(String name_p, Class<?> javaClass_p)
    {
        super(name_p, javaClass_p);
    }

    public OwCMISSearchTestFieldDefinition(String name_p, Class<?> javaClass_p, boolean array_p)
    {
        super(name_p, javaClass_p, array_p);
    }

    @Override
    protected Object createDate(String value) throws ParseException, OwInvalidOperationException
    {
        OwCMISDateTime dateTime = new OwCMISDateTime(value);
        Date objectValue = dateTime.getDate(true);
        return objectValue;
    }
}
