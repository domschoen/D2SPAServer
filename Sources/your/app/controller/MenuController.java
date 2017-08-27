package your.app.controller;
import your.app.model.Menu;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;

import er.extensions.eof.ERXKey;
import er.extensions.eof.ERXKeyFilter;
import er.rest.routes.ERXDefaultRouteController;
import er.rest.routes.ERXRouteController;
import er.rest.routes.jsr311.PathParam;

/*
case class Menus(menus: List[MainMenu], d2wContext: D2WContext)
case class MainMenu(id: Int, title: String,  children: List[Menu])
case class Menu(id:Int, title: String, entity: String)


case class D2WContext(entity: String, task: String, propertyKey: String)
*/
public class MenuController extends ERXRouteController {
	
	
	public MenuController(WORequest request) {
		super(request);
	}

	
	public static ERXKeyFilter showFilter() {
		ERXKeyFilter filter = ERXKeyFilter.filterWithAttributes();
		filter.include(new ERXKey<Menu>("parent"));
		return filter;
	}

	public WOActionResults indexAction() throws Throwable {
		if (isSchemaRequest()) {
			return schemaResponse(showFilter());
		}
		return response(Menu.menus(), showFilter());
	}



	public WOActionResults showAction(@PathParam("menu") Menu menu) throws Throwable {
		return response(menu, showFilter());
	}


}
