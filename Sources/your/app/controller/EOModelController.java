package your.app.controller;

import your.app.model.PubEOModel;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;

import er.extensions.eof.ERXKeyFilter;
import er.rest.routes.ERXRouteController;

public class EOModelController extends ERXRouteController {

    public EOModelController(WORequest request) {
        super(request);
    }

    public static ERXKeyFilter showFilter() {
        ERXKeyFilter eoModelfilter = ERXKeyFilter.filterWithNone();
        ERXKeyFilter eoEntityfilter = eoModelfilter.include("entities");
        ERXKeyFilter eoRelationshipfilter = eoEntityfilter.include("relationships");

        eoEntityfilter.include("name");
        eoEntityfilter.include("primaryKeyAttributeNames");

        eoRelationshipfilter.include("name");
        eoRelationshipfilter.include("destinationEntityName");

        return eoModelfilter;
    }

    public WOActionResults indexAction() throws Throwable {
        return response(PubEOModel.eomodels(), showFilter());
    }

}
