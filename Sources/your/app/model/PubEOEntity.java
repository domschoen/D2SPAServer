package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PubEOEntity {


	public static NSArray<PubEOEntity> pubEOEntitiesWithModel(EOModel model, NSArray<String> allowedEntities) {
        NSMutableArray<PubEOEntity> result = new NSMutableArray<PubEOEntity>();
		for (EOEntity entity : model.entities()) {

			if (allowedEntities == null || allowedEntities.contains(entity.name())) {
				result.addObject(new PubEOEntity(entity.name(), entity.primaryKeyAttributeNames(), PubEOAttribute.attributesWithEntity(entity), PubEORelationship.relationshipsWithEntity(entity)));
			}
        }
        return result;
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


}
