package your.app.model;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PubEOAttribute {


	public static NSArray<PubEOAttribute> attributesWithEntity(EOEntity entity) {
        NSMutableArray<PubEOAttribute> result = new NSMutableArray<PubEOAttribute>();

		// System.out.println("Attributes for entity: " + entity.name());
        for (EOAttribute attribute : entity.attributes()) {
			String attributeName = attribute.name();
			// System.out.println("attributeName: " + attributeName);

			result.addObject(new PubEOAttribute(attributeName));
        }
		// System.out.println("Pub Attributes for entity: " + result);

        return result;
    }

	public static NSArray<PubEOAttribute> pubEOAttributeWithRelationship(EORelationship relationship) {
        NSMutableArray<PubEOAttribute> result = new NSMutableArray<PubEOAttribute>();
        for (EOAttribute sourceAttribute : relationship.sourceAttributes()) {

			result.addObject(new PubEOAttribute(sourceAttribute.name()));
        }
        return result;
    }

    private String _name;

    public PubEOAttribute() {
    }

	public PubEOAttribute(String name)
    {
        setName(name);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

	@Override
	public String toString() {
		return getName();
	}
}
