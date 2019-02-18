package your.app.model;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOJoin;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;

public class PubEOJoin {

	public static NSArray<PubEOJoin> pubEOJoinWithRelationship(EORelationship eoRelationship, NSDictionary<String, PubEOEntity> entityByName) {
		NSMutableArray<PubEOJoin> result = new NSMutableArray<PubEOJoin>();
		EOEntity sourceEntity = eoRelationship.entity();
		PubEOEntity pubSourceEntity = entityByName.objectForKey(sourceEntity.name());
		for (EOJoin join : eoRelationship.joins()) {

			EOAttribute sourceAttribute = join.sourceAttribute();
			EOAttribute destinationAttribute = join.destinationAttribute();

			EOEntity destinationEntity = destinationAttribute.entity();
			PubEOEntity pubDestinationEntity = entityByName.objectForKey(destinationEntity.name());

			PubEOAttribute pubSourceAttribute = pubSourceEntity.attributeNamed(sourceAttribute.name());
			PubEOAttribute pubDestinationAttribute = pubDestinationEntity.attributeNamed(destinationAttribute.name());

			result.addObject(new PubEOJoin(pubSourceAttribute, pubDestinationAttribute));
		}
		return result;
	}


	private PubEOAttribute _source;
	private PubEOAttribute _destination;

	public PubEOJoin() {
	}

	public PubEOJoin(PubEOAttribute source, PubEOAttribute destination)
	{
		setSourceAttribute(source);
		setDestinationAttribute(destination);
	}

	public void setSourceAttribute(PubEOAttribute attr) {
		_source = attr;
	}

	public PubEOAttribute getSourceAttribute() {
		return _source;
	}

	public void setDestinationAttribute(PubEOAttribute attr) {
		_destination = attr;
	}

	public PubEOAttribute getDestinationAttribute() {
		return _destination;
	}


}
