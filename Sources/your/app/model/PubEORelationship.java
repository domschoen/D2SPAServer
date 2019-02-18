package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;

public class PubEORelationship {

	public static NSArray<PubEORelationship> relationshipsWithEntity(EOEntity entity)
    {
        NSMutableArray<PubEORelationship> result = new NSMutableArray<PubEORelationship>();
        for (EORelationship relationship : entity.relationships()) {
			NSArray<PubEOAttribute> entityAttributes = PubEOAttribute.pubEOAttributeWithRelationship(relationship);
			result.addObject(new PubEORelationship(
			        relationship.name(), relationship.definition(), relationship.isToMany(), relationship.destinationEntity().name(), relationship)
			        );
        }
        return result;
    }

    public static NSArray<PubEOEntity> pubEOEntitiesWithModel(EOModel model) {
        NSMutableArray<PubEOEntity> result = new NSMutableArray<PubEOEntity>();
        for (EOEntity entity : model.entities()) {

            result.addObject(new PubEOEntity(entity.name(), entity.primaryKeyAttributeNames(), PubEOAttribute.attributesWithEntity(entity),
            		PubEORelationship.relationshipsWithEntity(entity)));
        }
        return result;
    }

    private String _name;
	private String _definition;
	private Boolean _isToMany;

    private String _destinationEntityName;
	private NSArray<PubEOJoin> _joins;
	private EORelationship _eoRelationship;

    public PubEORelationship() {
    }

	public PubEORelationship(String name, String definition, Boolean isToMany, String destinationEntityName, EORelationship eoRelationship)
    {
		_eoRelationship = eoRelationship;
        setName(name);
		setDefinition(definition);
		setIsToMany(isToMany);
        setDestinationEntityName(destinationEntityName);
    }

	public void updateJoins(NSDictionary<String, PubEOEntity> entityByName) {
		NSArray<PubEOJoin> joins = PubEOJoin.pubEOJoinWithRelationship(_eoRelationship, entityByName);
		setJoins(joins);
	}

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

	public void setIsToMany(Boolean isToMany) {
		_isToMany = isToMany;
	}

	public Boolean getIsToMany() {
		return _isToMany;
	}

	public void setDefinition(String name) {
		_definition = name;
	}

	public String getDefinition() {
		return _definition;
	}

    public void setDestinationEntityName(String destinationEntityName) {
        _destinationEntityName = destinationEntityName;
    }

    public String getDestinationEntityName() {
        return _destinationEntityName;
    }
	public void setJoins(NSArray<PubEOJoin> joins) {
		_joins = joins;
	}

	public NSArray<PubEOJoin> getJoins() {
		return _joins;
	}



}
