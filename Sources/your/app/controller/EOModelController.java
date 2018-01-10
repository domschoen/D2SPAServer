package your.app.controller;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXKey;
import er.extensions.eof.ERXKeyFilter;
import er.rest.routes.ERXRouteController;

public class EOModelController extends ERXRouteController {

    public EOModelController(WORequest request) {
        super(request);
    }

    public static ERXKeyFilter showFilter() {
        ERXKeyFilter filter = ERXKeyFilter.filterWithAttributes();
        filter.include(new ERXKey<EOEntity>("entities"));
        filter.include("entities").include("name");
        filter.include("entities").include("primaryKeyAttributeNames");

        return filter;
    }

    public WOActionResults indexAction() throws Throwable {
        if (isSchemaRequest()) {
            return schemaResponse(showFilter());
        }
        NSMutableArray<EOModel> filteredModels = new NSMutableArray<EOModel>();
        for (EOModel model : EOModelGroup.defaultGroup().models()) {
            if (!model.name().equals("erprototypes")) {
                filteredModels.addObject(model);
            }
        }
        return response(filteredModels, showFilter());
    }

}
