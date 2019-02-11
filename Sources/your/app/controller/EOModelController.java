package your.app.controller;

import your.app.Application;
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
        ERXKeyFilter eoAttributefilter = eoEntityfilter.include("attributes");

        eoEntityfilter.include("name");
        eoEntityfilter.include("primaryKeyAttributeNames");

		ERXKeyFilter sourceAttributesFilter = eoRelationshipfilter.include("sourceAttributes");

		sourceAttributesFilter.include("name");

        eoRelationshipfilter.include("name");
		eoRelationshipfilter.include("definition");
		eoRelationshipfilter.include("isToMany");
        eoRelationshipfilter.include("destinationEntityName");

        eoAttributefilter.include("name");

        return eoModelfilter;
    }

    public WOActionResults indexAction() throws Throwable {
		return response(PubEOModel.eomodels(Application.allowedEntities), showFilter());
    }

}
