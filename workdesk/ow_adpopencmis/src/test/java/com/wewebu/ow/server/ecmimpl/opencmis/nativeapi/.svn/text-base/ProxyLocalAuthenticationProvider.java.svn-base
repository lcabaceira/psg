package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.chemistry.opencmis.client.bindings.spi.BindingSession;
import org.apache.chemistry.opencmis.client.bindings.spi.SessionAwareAuthenticationProvider;
import org.apache.chemistry.opencmis.commons.spi.AuthenticationProvider;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class ProxyLocalAuthenticationProvider implements SessionAwareAuthenticationProvider
{
    /**URI for the international support in web services*/
    public static String WS_I18N = "http://www.w3.org/2005/09/ws-i18n";
    /**prefix for internationalization: i18n*/
    public static String WS_I18N_PREFIX = "i18n";

    private AuthenticationProvider provider;
    private Locale locale;

    public ProxyLocalAuthenticationProvider(AuthenticationProvider prov, Locale locale)
    {
        this.provider = prov;
        this.locale = locale;
    }

    @Override
    public Map<String, List<String>> getHTTPHeaders(String url)
    {
        Map<String, List<String>> map = this.provider.getHTTPHeaders(url);
        if (map == null)
        {
            map = new HashMap<String, List<String>>();
        }

        LinkedList<String> lstLocale = new LinkedList<String>();
        lstLocale.add(this.locale.getLanguage());
        map.put("Accepted-Language", lstLocale);

        return map;
    }

    @Override
    public Element getSOAPHeaders(Object portObject)
    {
        Element header = this.provider.getSOAPHeaders(portObject);
        try
        {//do not cache, portObject is created for every call as new instance
            BindingProvider bindingProv = (BindingProvider) portObject;
            List<javax.xml.ws.handler.Handler> handler = bindingProv.getBinding().getHandlerChain();
            handler.add(new WsI18nHandler(this.locale));
            bindingProv.getBinding().setHandlerChain(handler);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return header;
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory()
    {
        return this.provider.getSSLSocketFactory();
    }

    @Override
    public HostnameVerifier getHostnameVerifier()
    {
        return this.provider.getHostnameVerifier();
    }

    @Override
    public void putResponseHeaders(String url, int statusCode, Map<String, List<String>> headers)
    {
        this.provider.putResponseHeaders(url, statusCode, headers);
        //TODO
    }

    @Override
    public void setSession(BindingSession session)
    {
        if (this.provider instanceof SessionAwareAuthenticationProvider)
        {
            ((SessionAwareAuthenticationProvider) this.provider).setSession(session);
        }
    }

    private static final class WsI18nHandler implements SOAPHandler<SOAPMessageContext>
    {
        private Locale locale;

        public WsI18nHandler(Locale locale)
        {
            this.locale = locale;
        }

        @Override
        public boolean handleMessage(SOAPMessageContext msgCtx)
        {
            if ((Boolean) msgCtx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))
            {
                try
                {
                    SOAPMessage msg = msgCtx.getMessage();
                    SOAPEnvelope envelope;
                    envelope = msg.getSOAPPart().getEnvelope();
                    SOAPHeader soapHeader;
                    if (envelope.getHeader() != null)
                    {
                        soapHeader = envelope.getHeader();
                    }
                    else
                    {
                        soapHeader = envelope.addHeader();
                    }

                    SOAPHeaderElement headerElement = soapHeader.addHeaderElement(new QName(WS_I18N, "international", WS_I18N_PREFIX));
                    headerElement.setMustUnderstand(false);

                    SOAPElement locElem = headerElement.addChildElement(new QName(WS_I18N, "locale", WS_I18N_PREFIX));
                    locElem.addTextNode(this.locale.toString());

                }
                catch (SOAPException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return true;
        }

        @Override
        public boolean handleFault(SOAPMessageContext context)
        {
            return false;
        }

        @Override
        public void close(MessageContext context)
        {

        }

        @Override
        public Set<QName> getHeaders()
        {
            return Collections.emptySet();
        }
    }
}
