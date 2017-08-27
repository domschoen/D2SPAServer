package your.app.model;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class Menu {
	private static NSMutableArray<Menu> _menus;

	
	public static NSArray<Menu> menus() {
		if (_menus == null) {
			NSMutableArray<Menu> menus = new NSMutableArray<>();
			Menu mainMenu = new Menu(null,"Project Management",null, 0);
			
			menus.addObject(mainMenu);
			
			menus.addObject(new Menu(mainMenu, _Customer.ENTITY_NAME, _Customer.ENTITY_NAME, 1));
			menus.addObject(new Menu(mainMenu, _Project.ENTITY_NAME, _Project.ENTITY_NAME, 2));
			_menus = menus;
		}
		return _menus;
	}
	
	private String _title;
	private String _entity;
	private Integer _id;
	private Menu _parent;

	public Menu() {
	}

	public Menu(Menu parent, String title, String entity, Integer id) {
		setId(id);
		setTitle(title);
		setEntity(entity);
		setParent(parent);
	}

	public void setTitle(String title) {
		_title = title;
	}

	public String getTitle() {
		return _title;
	}
	
	public String getEntity() {
		return _entity;
	}
	public void setEntity(String entity) {
		_entity = entity;
	}

	public void setId(Integer id) {
		_id = id;
	}

	public Integer getId() {
		return _id;
	}

	
	public void setParent(Menu parent) {
		_parent = parent;
	}

	public Menu getParent() {
		return _parent;
	}

	
}
