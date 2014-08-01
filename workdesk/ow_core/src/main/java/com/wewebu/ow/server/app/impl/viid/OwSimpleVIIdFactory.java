package com.wewebu.ow.server.app.impl.viid;

import com.wewebu.ow.server.app.id.OwIdDecoder;
import com.wewebu.ow.server.app.id.viid.OwVIId;
import com.wewebu.ow.server.app.id.viid.OwVIIdFactory;
import com.wewebu.ow.server.app.id.viid.OwVIIdType;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.exceptions.OwInaccessibleException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.util.OwString;

public class OwSimpleVIIdFactory implements OwVIIdFactory
{

    @Override
    public OwVIId createVersionIndependentId(OwObject obj) throws OwNotSupportedException
    {
        OwVIId objVID = null;
        String resourceId;
        try
        {
            resourceId = obj.getResourceID();
        }
        catch (Exception e1)
        {
            throw new OwInaccessibleException("Could not retrieve resource/repository Id", "owd.core");
        }
        switch (obj.getType())
        {
            case OwObjectReference.OBJECT_TYPE_ECM_ROOT_FOLDER:
            case OwObjectReference.OBJECT_TYPE_FOLDER:
                objVID = createOwVIId(obj.getID(), resourceId, OwVIIdType.FOLDER);
                break;

            case OwObjectReference.OBJECT_TYPE_DOCUMENT:

                try
                {
                    objVID = createOwVIId(obj.getVersionSeries().getId(), resourceId, OwVIIdType.DOCUMENT);
                }
                catch (Exception e)
                {
                    throw new OwInaccessibleException("VersionSeries cannot be retrieved from object", "owd.core");
                }
                break;

            case OwObjectReference.OBJECT_TYPE_LINK:
                objVID = createOwVIId(obj.getID(), resourceId, OwVIIdType.LINK);
                break;

            case OwObjectReference.OBJECT_TYPE_ALL_TUPLE_OBJECTS:
            case OwObjectReference.OBJECT_TYPE_CUSTOM:
                objVID = createOwVIId(obj.getID(), resourceId, OwVIIdType.CUSTOM);
                break;

            default:
                throw new OwNotSupportedException(new OwString("viid.OwSimpleVIIdFactory.createVersionIndependentId.unsupported", "ObjectType not supported for VIId creation"));
        }
        return objVID;
    }

    protected OwVIId createOwVIId(String objId, String resourceId, OwVIIdType type)
    {
        return new OwSimpleVIId(objId, resourceId, type);
    }

    @Override
    public OwIdDecoder<OwVIId> createViidDecoder()
    {
        return new OwSimpleVIIdDecoder();
    }

}
