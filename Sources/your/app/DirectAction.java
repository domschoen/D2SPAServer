package your.app;

import your.app.components.Main;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSLog;

import er.directtoweb.ERD2WDirectAction;


public class DirectAction extends ERD2WDirectAction {
    public DirectAction(WORequest request) {
        super(request);
    }

    @Override
    public WOActionResults defaultAction() {
        return pageWithName(Main.class.getName());
    }

    public WOActionResults debugRuleAction() {
        EOEntity entity = EOModelGroup.defaultGroup().entityNamed("Project");

        D2WContext d2wContext = new D2WContext();

        d2wContext.setEntity(entity);
        d2wContext.setTask("edit");
        d2wContext.setPropertyKey("customer");

        Object result = d2wContext.inferValueForKey("componentName");
        System.out.println("result: " + result);

        return null;
    }

    /**
     * Checks if a page configuration is allowed to render.
     * Provide a more intelligent access scheme as the default just returns false. And
     * be sure to read the javadoc to the super class.
     * @param pageConfiguration
     * @return
     */
    @Override
    protected boolean allowPageConfiguration(String pageConfiguration) {
        return false;
    }

    public WOActionResults loginAction() {

        String username = request().stringFormValueForKey("username");
        String password = request().stringFormValueForKey("password");

        NSLog.out.appendln("***DirectAction.loginAction - username: " + username + " : password: " + password + "***");

        // ENHANCEME - add appropriate login behaviour here

        return D2W.factory().defaultPage(session());
    }

}
