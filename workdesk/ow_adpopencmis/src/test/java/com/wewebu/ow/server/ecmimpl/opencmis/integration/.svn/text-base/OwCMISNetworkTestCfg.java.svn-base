package com.wewebu.ow.server.ecmimpl.opencmis.integration;

import java.util.List;

import com.wewebu.ow.server.ecmimpl.opencmis.conf.OwCMISNetworkCfg;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.util.OwXMLUtil;

public class OwCMISNetworkTestCfg extends OwCMISNetworkCfg
{
    private List<String> testPreferedOrder;

    public OwCMISNetworkTestCfg(OwXMLUtil config_p)
    {
        super(config_p);
    }

    @Override
    public List<String> getPreferedPropertyOrder() throws OwException
    {
        if (null == this.testPreferedOrder)
        {
            return super.getPreferedPropertyOrder();
        }
        else
        {
            return this.testPreferedOrder;
        }
    }

    public void setTestPreferedOrder(List<String> testPreferedOrder)
    {
        this.testPreferedOrder = testPreferedOrder;
    }
}
