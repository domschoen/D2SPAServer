package your.app.controller;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;

import er.extensions.eof.ERXKeyFilter;
import er.rest.routes.ERXRouteController;

/*
case class Menus(menus: List[MainMenu], d2wContext: D2WContext)
case class MainMenu(id: Int, title: String,  children: List[Menu])
case class Menu(id:Int, title: String, entity: String)


case class D2WContext(entity: String, task: String, propertyKey: String)
 */
public class RuleModelController extends ERXRouteController {


    public RuleModelController(WORequest request) {
        super(request);
    }

    public static ERXKeyFilter showFilter() {
        ERXKeyFilter filter = ERXKeyFilter.filterWithAttributes();
        return filter;
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

        Object result = d2wContext.inferValueForKey(key);

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
