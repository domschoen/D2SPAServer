package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;

public class EOAccessMetaDataUtilities {

    public static EOEntity entityNamed(String entityName) {
        return EOModelGroup.defaultGroup().entityNamed(entityName);
    }

    public static String pkAttributeName(EOEntity entity) {
        return entity.primaryKeyAttributeNames().lastObject();
    }
}
