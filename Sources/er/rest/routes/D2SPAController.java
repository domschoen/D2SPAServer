package er.rest.routes;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOProperty;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSSet;

import er.extensions.eof.ERXKeyFilter;
import er.rest.ERXRestFetchSpecification;

public class D2SPAController extends ERXUnsafeReadWriteRouteController{

    public D2SPAController(WORequest request) {
        super(request);
        // TODO Auto-generated constructor stub
    }

    public static ERXKeyFilter missingKeysFilter(NSArray<String> missingKeys) {
        ERXKeyFilter filter = ERXKeyFilter.filterWithNone();
        for (String missingKey : missingKeys) {
            filter.include(missingKey);
        }
		// filter.setUnknownKeyIgnored(true);

        return filter;
    }


	public static ERXKeyFilter fatMissingKeysFilter(NSArray<String> missingKeys) {
		ERXKeyFilter filter = ERXKeyFilter.filterWithAttributesAndToOneRelationships();
		for (String missingKey : missingKeys) {
			filter.include(missingKey);
		}
		// filter.setUnknownKeyIgnored(true);
		return filter;
	}


    public WOActionResults propertyValuesAction() {
        String missingKeysString = (String)request().formValueForKey("missingKeys");
        NSArray<String> missingKeys = NSPropertyListSerialization.arrayForString(missingKeysString);
        EOEnterpriseObject obj = object();

        // NSArray projects = (NSArray)obj.valueForKey(Customer.PROJECTS_KEY);
        // System.out.println("obj " + obj.entityName() + " projects " +
        // projects.count());

        return response(obj, missingKeysFilter(missingKeys));
    }

	public NSArray<String> purifyMissingKeys(NSArray<String> missingKeys, String entityName) {
		EOEntity entity = EOModelGroup.defaultGroup().entityNamed(entityName());
		NSMutableSet<String> classPropertyNames = new NSMutableSet<String>();
		for (EOProperty property : entity.classProperties()) {
			classPropertyNames.addObject(property.name());
		}
		NSSet<String> impureKeys = new NSSet(missingKeys);
		NSSet<String> purifiedKeys = impureKeys.setByIntersectingSet(classPropertyNames);
		return purifiedKeys.allObjects();
	}

	@Override
	public WOActionResults indexAction() {
		if (isSchemaRequest()) {
			return schemaResponse(showFilter());
		}
        String missingKeysString = (String)request().formValueForKey("missingKeys");
		NSArray<String> purestKeys = null;
		if (missingKeysString != null) {
			NSArray<String> missingKeys = NSPropertyListSerialization.arrayForString(missingKeysString);
			NSArray<String> pureKeys = purifyMissingKeys(missingKeys, entityName());
			purestKeys = pureKeys.count() > 0 ? pureKeys : null;
		}

		if (purestKeys == null) {
			return super.indexAction();
		} else {
			ERXRestFetchSpecification fetchSpec = new ERXRestFetchSpecification(entityName(), null, null, queryFilter(), null, 1500);
			return response(fetchSpec, fatMissingKeysFilter(purestKeys));
		}
	}


}
