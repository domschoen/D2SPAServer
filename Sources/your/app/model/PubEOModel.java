package your.app.model;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PubEOModel {
    private static NSMutableArray<PubEOModel> _eomodels;


	public static NSArray<PubEOModel> eomodels(NSArray<String> allowedEntities) {
        if (_eomodels == null) {
            NSMutableArray<PubEOModel> eomodels = new NSMutableArray<PubEOModel>();
            for (EOModel model : EOModelGroup.defaultGroup().models()) {
                String name = model.name();
                if (!name.equals("erprototypes")) {
					PubEOModel pubModel = new PubEOModel(name, PubEOEntity.pubEOEntitiesWithModel(model, allowedEntities));
					if (pubModel != null) {
						eomodels.addObject(pubModel);
					}
                }
            }

            _eomodels = eomodels;
        }
        return _eomodels;
    }


    private String _name;
    private NSArray<PubEOEntity> _entities;

    public PubEOModel() {
    }

    public PubEOModel(String name, NSArray<PubEOEntity> entities) {
        setName(name);
        setEntities(entities);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setEntities(NSArray<PubEOEntity> entities) {
        _entities = entities;
    }


    public NSArray<PubEOEntity> getEntities() {
        return _entities;
    }



}
