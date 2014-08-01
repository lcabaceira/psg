package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinitionContainer;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinitionList;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.spi.CmisBinding;
import org.apache.chemistry.opencmis.commons.spi.RepositoryService;

public class TypeWeightTest extends AbstractNativeTest
{
    private static int DEF_ARRAY_LENGTH = 32;
    private static int DEF_STRING_LENGTH = 32;
    private static int DEF_INT_SIZE = 4;
    private static int DEF_BOOL_SIZE = 4;
    private static int DEF_DATE_SIZE = 4;
    private static int DEF_DECIMAL_SIZE = 4;
    private static int DEF_HTML_LENGTH = 1024;
    private static int DEF_URI_LENGTH = 80;
    private static int DEF_ID_LENGTH = 80;
    private static int DEF_CHAR_SIZE = 32;

    private int sizeOf(TypeDefinition typeDefinition)
    {
        Map<String, PropertyDefinition<?>> definitionsMap = typeDefinition.getPropertyDefinitions();
        if (definitionsMap == null)
        {
            return 0;
        }
        Collection<PropertyDefinition<?>> definitions = definitionsMap.values();
        int size = 0;
        for (PropertyDefinition<?> definition : definitions)
        {

            int thisSize = 0;
            if (PropertyType.STRING == definition.getPropertyType())
            {
                thisSize += DEF_STRING_LENGTH * DEF_CHAR_SIZE;
            }
            else if (PropertyType.INTEGER == definition.getPropertyType())
            {
                thisSize += DEF_INT_SIZE;
            }
            else if (PropertyType.BOOLEAN == definition.getPropertyType())
            {
                thisSize += DEF_BOOL_SIZE;
            }
            else if (PropertyType.DATETIME == definition.getPropertyType())
            {
                thisSize += DEF_DATE_SIZE;
            }
            else if (PropertyType.DECIMAL == definition.getPropertyType())
            {
                thisSize += DEF_DECIMAL_SIZE;
            }
            else if (PropertyType.HTML == definition.getPropertyType())
            {
                thisSize += DEF_HTML_LENGTH * DEF_CHAR_SIZE;
            }
            else if (PropertyType.URI == definition.getPropertyType())
            {
                thisSize += DEF_URI_LENGTH * DEF_CHAR_SIZE;
            }
            else if (PropertyType.ID == definition.getPropertyType())
            {
                thisSize += DEF_ID_LENGTH * DEF_CHAR_SIZE;
            }

            if (Cardinality.MULTI.equals(definition.getCardinality()))
            {
                thisSize *= DEF_ARRAY_LENGTH;
            }

            size += thisSize;
        }

        return size;
    }

    public void testTraceWeights() throws Exception
    {
        CmisBinding binding = getSession().getBinding();
        RepositoryService rs = binding.getRepositoryService();
        String repoId = getSession().getRepositoryInfo().getId();

        int total = 0;
        int count = 0;

        TypeDefinitionList lst = rs.getTypeChildren(repoId, null, Boolean.TRUE, new BigInteger("10"), BigInteger.ZERO, null);
        System.out.println();
        for (TypeDefinition baseType : lst.getList())
        {
            int size = sizeOf(baseType);
            System.out.println("BaseType = " + baseType.getDisplayName() + "=" + size / (1024.0 * 1024.0) + " MB");
            total += size;
            count++;
            List<TypeDefinitionContainer> subs = rs.getTypeDescendants(repoId, baseType.getId(), BigInteger.ONE, Boolean.TRUE, null);
            for (TypeDefinitionContainer subNode : subs)
            {
                TypeDefinition subType = subNode.getTypeDefinition();
                size = sizeOf(subType);
                System.out.println("Subtype = " + subType.getId() + " " + subType.getDisplayName() + " " + subType.getQueryName() + "=" + size / (1024.0 * 1024.0));
                total += size;
                count++;
            }
        }

        double average = ((double) total / (double) count);
        System.err.println("AVG = " + average + " B = " + average / (1024 * 1024) + " MB");
    }
}
