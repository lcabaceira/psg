package com.wewebu.ow.unittest.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.ui.viewer.OwInfoRequest;

public class OwMockInfoRequest implements OwInfoRequest
{
    private Map<String, String> parameter;
    private InputStream requestBody;

    public OwMockInfoRequest(Map<String, String> params, String requestBody)
    {
        parameter = params;
        this.requestBody = new ByteArrayInputStream(requestBody.getBytes());
    }

    @Override
    public List<String> getParameterNames()
    {
        if (parameter == null)
        {
            return Collections.emptyList();
        }
        else
        {
            return new LinkedList<String>(parameter.keySet());
        }
    }

    @Override
    public String getParameter(String paramName_p)
    {
        if (parameter != null)
        {
            return parameter.get(paramName_p);
        }
        else
        {
            return null;
        }
    }

    @Override
    public InputStream getRequestBody() throws IOException
    {
        return requestBody;
    }

}
