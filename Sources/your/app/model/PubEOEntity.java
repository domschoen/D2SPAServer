package your.app.model;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PubEOEntity {

    public static NSArray<PubEOEntity> pubEOEntitiesWithModel(EOModel model) {
        NSMutableArray<PubEOEntity> result = new NSMutableArray<PubEOEntity>();
        for (EOEntity entity : model.entities()) {

            result.addObject(new PubEOEntity(entity.name(), PubEORelationship.relationshipsWithEntity(entity), entity.primaryKeyAttributeNames()));
        }
        return result;
    }

    private String _name;
    private NSArray<PubEORelationship> _relationships;
    private NSArray<String> _primaryKeyAttributeNames;

    public PubEOEntity() {
    }

    public PubEOEntity(String name, NSArray<PubEORelationship> relationships, NSArray<String> primaryKeyAttributeNames)
    {
        setName(name);
        setRelationships(relationships);
        setPrimaryKeyAttributeNames(primaryKeyAttributeNames);
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
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
