package your.app.model;

public class EntityDef {
    public String name;
    public String pkAttributeName;

    public EntityDef(String name, String pkAttributeName) {
        this.name = name;
        this.pkAttributeName = pkAttributeName;
    }

    public String name() {
        return name;
    }

    public String pkAttributeName() {
        return pkAttributeName;
    }
}

