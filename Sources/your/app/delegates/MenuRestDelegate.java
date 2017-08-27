package your.app.delegates;

import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;
import er.rest.ERXAbstractRestDelegate;
import er.rest.ERXRestContext;
import your.app.model.Menu;


public class MenuRestDelegate extends ERXAbstractRestDelegate {
	public MenuRestDelegate() {
	}
	
	public Object createObjectOfEntityWithID(EOClassDescription entity, Object id, ERXRestContext context) {
		return new Menu();
	}
	
	public Object primaryKeyForObject(Object obj, ERXRestContext context) {
		return ((Menu) obj).getId();
	}
	
	public Object objectOfEntityWithID(EOClassDescription entity, Object id, ERXRestContext context) {
		NSArray<Menu> menus = ERXQ.filtered(Menu.menus(), ERXQ.is("id", id));
		return menus.size() == 0 ? null : menus.objectAtIndex(0);
	}
}
