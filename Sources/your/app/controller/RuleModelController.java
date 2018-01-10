package your.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import your.app.model.EOAccessMetaDataUtilities;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXKeyFilter;
import er.rest.routes.ERXRouteController;

/*
case class Menus(menus: List[MainMenu], d2wContext: D2WContext)
case class MainMenu(id: Int, title: String,  children: List[Menu])
case class Menu(id:Int, title: String, entity: String)


case class D2WContext(entity: String, task: String, propertyKey: String)
 */
public class RuleModelController extends ERXRouteController {
    private static final Logger log = LoggerFactory.getLogger(RuleModelController.class);


    public RuleModelController(WORequest request) {
        super(request);
    }

    public static ERXKeyFilter showEORefsFilter() {
        ERXKeyFilter filter = ERXKeyFilter.filterWithNone();
        filter.include("destinationEos").include("id"); // hardcoded to id ??
        filter.include("destinationEos").include("entity").include("name");
        return filter;
    }

    public static ERXKeyFilter showFilter() {
        ERXKeyFilter filter = ERXKeyFilter.filterWithAttributes();
        return filter;
    }

    public static Object oidForEo(EOEnterpriseObject eo) {
        NSDictionary primaryKey = primaryKeyDictionaryForEo(eo);
        if ((primaryKey == null) || (primaryKey.count() != 1)) {
            return null;
        }
        return primaryKey.allValues().lastObject();
    }

    public static NSDictionary primaryKeyDictionaryForEo(EOEnterpriseObject eo)
    {
        EOEditingContext ec = eo.editingContext();
        if (ec == null)
            return null;
        return EOUtilities.primaryKeyForObject(ec, eo);
    }

    private String getDisplayNameForKeyWhenRelationshipWith(D2WContext d2wContext)
    {
        String keyWhenRelationship = (String) d2wContext.inferValueForKey("keyWhenRelationship");
        EOEntity destinationEntity = d2wContext.relationship().destinationEntity();

        D2WContext destinationD2wContext = new D2WContext(d2wContext);
        destinationD2wContext.setPropertyKey(keyWhenRelationship);
        destinationD2wContext.setEntity(destinationEntity);

        return destinationD2wContext.displayNameForProperty();
    }

    String[] eoRefKeys = new String[] { "id", "entity" };

    private NSArray<NSDictionary<String, Object>> eoRefs(D2WContext d2wContext)
    {
        String keyWhenRelationship = (String) d2wContext.inferValueForKey("keyWhenRelationship");
        EOEntity destinationEntity = d2wContext.relationship().destinationEntity();
        EOAttribute pkAttribute = destinationEntity.primaryKeyAttributes().lastObject();
        String destinationEntityName = destinationEntity.name();
        NSArray<EOEnterpriseObject> eos = EOUtilities.objectsForEntityNamed(session().defaultEditingContext(), destinationEntityName);
        NSMutableArray<NSDictionary<String, Object>> result = new NSMutableArray<NSDictionary<String, Object>>();
        for (EOEnterpriseObject eo : eos) {
            Object oid = oidForEo(eo);
            String displayName = (String) eo.valueForKeyPath(keyWhenRelationship);
            result.addObject(new NSDictionary(new Object[] { oid,
                    destinationEntityName }, eoRefKeys));
        }
        return result;
    }


    public WOActionResults fireRuleForKeyAction() {
        String entityName = (String) request().formValueForKey("entity");
        String task = (String) request().formValueForKey("task");
        String propertyKey = (String) request().formValueForKey("propertyKey");
        String key = (String) request().formValueForKey("key");
        EOEntity entity = EOModelGroup.defaultGroup().entityNamed(entityName);

        D2WContext d2wContext = new D2WContext();

        d2wContext.setEntity(entity);
        d2wContext.setTask(task);
        if (propertyKey != null)
            d2wContext.setPropertyKey(propertyKey);

        Object result = null;
        if (key.equals("pkAttributeName")) {
            result = EOAccessMetaDataUtilities.pkAttributeName(entity);

        } else if (key.equals("attributeType")) {
            EOAttribute attr = entity.attributeNamed(propertyKey);
            if (attr == null) {
                EORelationship relationship = entity.relationshipNamed(propertyKey);
                result = relationship.isToMany() ? "eosV" : "eoV";

            } else {
                String valueType = attr.valueType();
                System.out.println("valueType" + valueType);
                result = valueType.equals("c") ? "stringV" : "intV"; // i for
                // int
            }
        } else if (key.equals("displayNameForKeyWhenRelationship")) {
            result = getDisplayNameForKeyWhenRelationshipWith(d2wContext);
        } else if (key.equals("destinationEos")) {
            NSArray eoRefs = eoRefs(d2wContext);
            result = new NSDictionary(eoRefs, key);

            System.out.println("DESTINATION EOS" + result);
            return response(result, showEORefsFilter());

        } else {
            result = d2wContext.inferValueForKey(key);
        }

        if (result instanceof String) {
            System.out.println("D2WContext : "
                    + d2wContext
                    + "  key: "
                    + key
                    + " value: "
                    + result);
            result = new NSDictionary(result, key);
        }
        System.out.println("Result " + result);

        return response(result, showFilter());
    }

    public WOActionResults successResponse(final String responseMessage) {
        return errorResponse(200, responseMessage);
    }

    public WOActionResults errorResponse(final Integer errorCode, final String errorMessage)
    {
        return new WOActionResults() {
            @Override
            public WOResponse generateResponse() {
                WOResponse response = new WOResponse();
                response.setStatus(errorCode);
                String realm = request().applicationName();

                response.appendContentString(errorMessage + "\n");
                log.error("returning " + errorCode + " msg: " + response);
                return response;
            }
        };
    }
}
