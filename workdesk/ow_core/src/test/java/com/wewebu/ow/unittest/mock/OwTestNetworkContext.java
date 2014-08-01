package com.wewebu.ow.unittest.mock;

import java.util.TimeZone;

import com.wewebu.ow.server.util.OwTimeZoneInfo;
import com.wewebu.ow.unittest.util.AbstractNetworkContextAdapter;

public class OwTestNetworkContext extends AbstractNetworkContextAdapter
{

    @Override
    public OwTimeZoneInfo getClientTimeZoneInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeZone getClientTimeZone()
    {
        // TODO Auto-generated method stub
        return TimeZone.getDefault();
    }

}
