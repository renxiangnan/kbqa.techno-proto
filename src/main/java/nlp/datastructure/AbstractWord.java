package nlp.datastructure;

import rdf.mapping.EntityMapping;
import rdf.mapping.TypeMapping;

import java.util.List;

public abstract class AbstractWord {
    boolean mayCategory;
    boolean mayLiteral;
    boolean mayEntity;
    boolean mayRdfType;
    List<EntityMapping> entityMappings;
    List<TypeMapping> typeMappings;
    String category;

    public boolean getMayCategory() { return mayCategory; }
    public boolean getMayLiteral() { return mayLiteral; }
    public boolean getMayEntity() { return mayEntity; }
    public boolean getMayRdfType() { return mayRdfType; }
    public List<EntityMapping> getEntityMappings() { return entityMappings; }
    public List<TypeMapping> getTypeMappings() { return typeMappings; }
    public String getCategory() { return category; }

    public void setMayCategory(boolean mayCategory) { this.mayCategory = mayCategory; }
    public void setMayLiteral(boolean mayLiteral) { this.mayLiteral = mayLiteral; }
    public void setMayEntity(boolean mayEntity) { this.mayEntity = mayEntity; }
    public void setMayRdf(boolean mayRdfType) { this.mayRdfType = mayRdfType; }
    public void setEntityMappings(List<EntityMapping> entityMappings) { this.entityMappings = entityMappings; }
    public void setTypeMappings(List<TypeMapping> typeMappings) { this.typeMappings = typeMappings; }
    public void setCategory(String category) { this.category = category; }

}
