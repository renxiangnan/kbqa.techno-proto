package lucene.index;

public class FieldNameLabels {

    private FieldNameLabels(){}

    public interface EntityFieldLabel {
        String EntityId = "EntityId";
        String EntityName = "EntityName";
        String EntityFragment = "EntityFragment";
    }

    public interface TypeShortNameFieldLabel {
        String typeShortName = "TypeShortName";
        String splittedTypeShortName = "SplittedTypeShortName";
    }
}



