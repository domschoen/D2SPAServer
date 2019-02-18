package your.app.model;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class PubEOModel {
    private static NSMutableArray<PubEOModel> _eomodels;


	public static NSArray<PubEOModel> eomodels(NSArray<String> allowedEntities) {
        if (_eomodels == null) {
			NSMutableDictionary<String, PubEOEntity> entityByName = new NSMutableDictionary<String, PubEOEntity>();

            NSMutableArray<PubEOModel> eomodels = new NSMutableArray<PubEOModel>();
            for (EOModel model : EOModelGroup.defaultGroup().models()) {
                String name = model.name();
                if (!name.equals("erprototypes")) {
					NSDictionary<String, PubEOEntity> entities = PubEOEntity.pubEOEntitiesWithModel(model, allowedEntities);
					PubEOModel pubModel = new PubEOModel(name, entities.allValues());
					if (pubModel != null) {
						eomodels.addObject(pubModel);
						entityByName.addEntriesFromDictionary(entities);
					}
                }
            }
			updateRelationshipJoins(entityByName);

            _eomodels = eomodels;
        }
        return _eomodels;
    }


	private static void updateRelationshipJoins(NSDictionary<String, PubEOEntity> entities) {
		for (PubEOEntity entity : entities.allValues()) {
			entity.updateRelationshipJoins(entities);
		}
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
