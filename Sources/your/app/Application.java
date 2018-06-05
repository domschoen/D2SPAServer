package your.app;

import your.app.controller.RuleModelController;

import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSDictionary;

import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.navigation.ERXNavigationManager;
import er.rest.routes.D2SPAController;
import er.rest.routes.ERXRoute;
import er.rest.routes.ERXRouteRequestHandler;

public class Application extends ERXApplication {
    public static void main(String[] argv) {
        ERXApplication.main(argv, Application.class);
    }

    // http://localhost:1666/cgi-bin/WebObjects/D2SPAServer.woa

    public Application() {
        ERXApplication.log.info("Welcome to " + name() + " !");
        setDefaultRequestHandler(requestHandlerForKey(directActionRequestHandlerKey()));
        setAllowsConcurrentRequestHandling(true);

        setInMemoryDatabase();
        setupGenericWebServices();

    }

	@Override
	public WOResponse dispatchRequest(WORequest request) {
		String method = request.valueForKey("method").toString();
		String formsValues = method.equals("POST") ? request.valueForKey("formValues").toString() : "";
		System.out.println(method + " " + request.valueForKey("uri") + " " + formsValues);
		return super.dispatchRequest(request);
	}

    private void setupGenericWebServices() {
        ERXRouteRequestHandler routeRequestHandler = new ERXRouteRequestHandler(ERXRouteRequestHandler.WO);

        routeRequestHandler.addDefaultRoutes("Menu");
        routeRequestHandler.addDefaultRoutes("EOModel");
        routeRequestHandler.addRoute(new ERXRoute("RuleFiring", "/fireRuleForKey", ERXRoute.Method.Get, RuleModelController.class, "fireRuleForKey"));
        // routeRequestHandler.addRoute(new ERXRoute("EOModel", "/eomodel",
        // ERXRoute.Method.Get, EOModelController.class, "eomodel"));


        // Generic web service for each entity !
        for (EOModel eachModel : EOModelGroup.defaultGroup().models()) {
            for (EOEntity eachEntity : eachModel.entities()) {
                System.out.println(eachEntity.name());
                routeRequestHandler.addDefaultRoutes(eachEntity.name(), D2SPAController.class);
                routeRequestHandler.addRoute(new ERXRoute(eachEntity.name(), "/propertyValues", ERXRoute.Method.Get, D2SPAController.class, "propertyValues"));

            }
        }

        ERXRouteRequestHandler.register(routeRequestHandler);
    }

    private void setInMemoryDatabase() {
        for (EOModel model : EOModelGroup.defaultGroup().models()) {
            model.setConnectionDictionary(new NSDictionary());
            model.setAdaptorName("Memory");
        }
    }

    @Override
    public void finishInitialization() {
        super.finishInitialization();

        // Setup main navigation
        ERXNavigationManager.manager().configureNavigation();

    }
}
