package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

public class PubEOEntity {


	public static NSDictionary<String, PubEOEntity> pubEOEntitiesWithModel(EOModel model, NSArray<String> allowedEntities) {
        NSMutableDictionary<String, PubEOEntity> result = new NSMutableDictionary<String, PubEOEntity>();
		for (EOEntity entity : model.entities()) {

			if (allowedEntities == null || allowedEntities.contains(entity.name())) {
				String entityName = entity.name();
				PubEOEntity pubEntity = new PubEOEntity(entity.name(), entity.primaryKeyAttributeNames(), PubEOAttribute.attributesWithEntity(entity), PubEORelationship.relationshipsWithEntity(entity));
				result.setObjectForKey(pubEntity, entityName);
			}
        }
		return result;
    }


	public void updateRelationshipJoins(NSDictionary<String, PubEOEntity> entityByName) {
		for (PubEORelationship relationship : getRelationships()) {
			relationship.updateJoins(entityByName);
		}
	}

	private String _name;
    private NSArray<PubEORelationship> _relationships;
	private NSArray<PubEOAttribute> _attributes;
    private NSArray<String> _primaryKeyAttributeNames;

    public PubEOEntity() {
    }

	public PubEOEntity(String name, NSArray<String> primaryKeyAttributeNames, NSArray<PubEOAttribute> attributes, NSArray<PubEORelationship> relationships)
    {
        setName(name);
        setRelationships(relationships);
		setAttributes(attributes);
        setPrimaryKeyAttributeNames(primaryKeyAttributeNames);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

	public void setAttributes(NSArray<PubEOAttribute> attributes) {
		_attributes = attributes;
	}

	public NSArray<PubEOAttribute> getAttributes() {
		return _attributes;
	}
    public void setRelationships(NSArray<PubEORelationship> relationships) {
        _relationships = relationships;
    }

    public NSArray<PubEORelationship> getRelationships() {
        return _relationships;
    }

    public void setPrimaryKeyAttributeNames(NSArray<String> primaryKeyAttributeNames)
    {
        _primaryKeyAttributeNames = primaryKeyAttributeNames;
    }

    public NSArray<String> getPrimaryKeyAttributeNames() {
        return _primaryKeyAttributeNames;
    }

	public PubEOAttribute attributeNamed(String name) {
		for (PubEOAttribute attribute : getAttributes()) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}

}
