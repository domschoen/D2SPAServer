package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class Menu {
    private static NSMutableArray<Menu> _menus;

    public static NSArray<Menu> menus() {
        if (_menus == null) {
            NSMutableArray<Menu> menus = new NSMutableArray<>();
            Menu mainMenu = new Menu(null,"Project Management",null, 0);

            menus.addObject(mainMenu);

            EOEntity customerEntity = EOAccessMetaDataUtilities.entityNamed(_Customer.ENTITY_NAME);
            EOEntity projectEntity = EOAccessMetaDataUtilities.entityNamed(_Project.ENTITY_NAME);
            EOEntity productEntity = EOAccessMetaDataUtilities.entityNamed(_Product.ENTITY_NAME);

            menus.addObject(new Menu(mainMenu, "Customer", new EntityDef(customerEntity.name(), EOAccessMetaDataUtilities.pkAttributeName(customerEntity)), 1));
            menus.addObject(new Menu(mainMenu, "Project", new EntityDef(projectEntity.name(), EOAccessMetaDataUtilities.pkAttributeName(projectEntity)), 2));
            menus.addObject(new Menu(mainMenu, "Product", new EntityDef(productEntity.name(), EOAccessMetaDataUtilities.pkAttributeName(productEntity)), 3));
            _menus = menus;
        }
        return _menus;
    }

    private String _title;
    private EntityDef _entity;
    private Integer _id;
    private Menu _parent;

    public Menu() {
    }

    public Menu(Menu parent, String title, EntityDef entity, Integer id) {
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

    public EntityDef getEntity() {
        return _entity;
    }

    public void setEntity(EntityDef entity) {
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
