package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PubEORelationship {

    public static NSArray<PubEORelationship> relationshipsWithEntity(EOEntity entity)
    {
        NSMutableArray<PubEORelationship> result = new NSMutableArray<PubEORelationship>();
        for (EORelationship relationship : entity.relationships()) {

			result.addObject(new PubEORelationship(PubEOAttribute.pubEOAttributeWithRelationship(relationship), relationship.name(), relationship.destinationEntity().name()));
        }
        return result;
    }

    public static NSArray<PubEOEntity> pubEOEntitiesWithModel(EOModel model) {
        NSMutableArray<PubEOEntity> result = new NSMutableArray<PubEOEntity>();
        for (EOEntity entity : model.entities()) {

            result.addObject(new PubEOEntity(entity.name(), PubEORelationship.relationshipsWithEntity(entity), entity.primaryKeyAttributeNames()));
        }
        return result;
    }

    private String _name;
    private String _destinationEntityName;
	private NSArray<PubEOAttribute> _sourceAttributes;

    public PubEORelationship() {
    }

	public PubEORelationship(NSArray<PubEOAttribute> sourceAttributes, String name, String destinationEntityName)
    {
		setSourceAttributes(sourceAttributes);
        setName(name);
        setDestinationEntityName(destinationEntityName);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setDestinationEntityName(String destinationEntityName) {
        _destinationEntityName = destinationEntityName;
    }

    public String getDestinationEntityName() {
        return _destinationEntityName;
    }
	public void setSourceAttributes(NSArray<PubEOAttribute> sourceAttributes) {
		_sourceAttributes = sourceAttributes;
	}

	public NSArray<PubEOAttribute> getSourceAttributes() {
		return _sourceAttributes;
	}


}
