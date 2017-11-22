package your.app;

import your.app.controller.RuleModelController;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSDictionary;

import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.navigation.ERXNavigationManager;
import er.rest.routes.ERXRoute;
import er.rest.routes.ERXRouteRequestHandler;
import er.rest.routes.ERXUnsafeReadWriteRouteController;

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

    private void setupGenericWebServices() {
        ERXRouteRequestHandler routeRequestHandler = new ERXRouteRequestHandler(ERXRouteRequestHandler.WO);

        routeRequestHandler.addDefaultRoutes("Menu");
        routeRequestHandler.addRoute(new ERXRoute("RuleFiring", "/fireRuleForKey", ERXRoute.Method.Get, RuleModelController.class, "fireRuleForKey"));


        // Generic web service for each entity !
        for (EOModel eachModel : EOModelGroup.defaultGroup().models()) {
            for (EOEntity eachEntity : eachModel.entities()) {
                System.out.println(eachEntity.name());
                routeRequestHandler.addDefaultRoutes(eachEntity.name(), ERXUnsafeReadWriteRouteController.class);

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
