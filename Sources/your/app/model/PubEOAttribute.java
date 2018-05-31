package your.app.model;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PubEOAttribute {

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


}
