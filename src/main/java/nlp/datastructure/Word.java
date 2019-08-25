package nlp.datastructure;

import rdf.TriplePattern;
import rdf.mapping.EntityMapping;
import rdf.mapping.TypeMapping;

import java.util.List;

/**
 * A word is a node paraphrase.
 */
public class Word extends AbstractWord implements Comparable<Word>{
    private boolean mayCategory;
    private boolean mayLiteral;
    private boolean mayEntity;
    private boolean mayRdfType;
    private boolean mayExtendVariable;
    private boolean isConstant;
    private String category;
    private List<EntityMapping> entityMappings;
    private List<TypeMapping> typeMappings;
    private TriplePattern embbededTriple;

    private String key;
    private String baseForm;
    private String originalForm;
    private String posTag ;
    private int position = -1;	    // Notice the first word's position = 1

    /*
        Notice: These variables are not used
        because we merge a phrase to a word if it is a node now.
     */
    private String ner;	            // record NER result
    private Word nnNext;
    private Word nnPrev;
    private Word coreference;		// coreference resolution result

    private Word modifiedWord;
    private Word represent;         // This word is represented by others, eg, "which book is ..." "which"

    private boolean isCovered;
    private boolean isIgnored;
    private boolean omitNode;       // This word can not be node


    public Word(String baseForm, String originalForm, String posTag, int position) {
        this.baseForm = baseForm;
        this.originalForm = originalForm;
        this.posTag = posTag;
        this.position = position;
        this.key = originalForm + "[" + position + "]";
    }

    public String getBaseFormEntityName() {
        if (mayEntity || mayRdfType) {
            return this.baseForm;
        }

        StringBuilder entityName = new StringBuilder();
        Word nextWord = this;

        while (nextWord != null) {
            entityName.append(nextWord.baseForm).append(' ');
            nextWord = nextWord.getNnNext();
        }
        entityName.deleteCharAt(entityName.length() - 1);
        return entityName.toString();
    }

    @Override
    public String toString() {

        return key;
    }

    @Override
    public int compareTo(Word other) {

        return this.position - other.position;
    }

    @Override
    public int hashCode() {

        return key.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Word) {
            return this.originalForm.equals(((Word) other).getOriginalForm()) &&
                    this.position == ((Word) other).position;
        } else {
            return false;
        }
    }

    //TODO: check all literal words
    // Cardinal Number, etc.
    public boolean isLiteral() {

        return this.posTag.equals("CD");
    }

    public boolean getMayExtendVariable() { return mayExtendVariable; }
    public boolean isConstant() { return isConstant; }
    public boolean getMayRdfType() { return mayRdfType; }
    public String getKey() { return key; }
    public String getBaseForm() { return baseForm; }
    public String getOriginalForm() { return originalForm; }
    public String getPosTag() { return posTag; }
    public int getPosition() { return position; }
    public String getNer() { return ner; }
    public Word getNnNext() { return nnNext; }
    public Word getNnPrev() { return nnPrev; }
    public Word getCoreference() { return coreference; }
    public Word getModifiedWord() { return modifiedWord; }
    public Word getRepresent() { return represent; }
    public boolean getIsCovered() { return isCovered; }
    public boolean getIsIgnored() { return isIgnored; }
    public boolean getOmitNode() { return omitNode; }
    public TriplePattern getEmbbededTriple() { return embbededTriple; }

    public void setMayExtendVariable(boolean mayExtendVariable) { this.mayExtendVariable = mayExtendVariable; }
    public void setIsConstant(boolean isConstant) { this.isConstant = isConstant; }
    public void setMayRdfType(boolean mayRdfType) { this.mayRdfType = mayRdfType; }
    public void setKey(String key) { this.key = key; }
    public void setBaseForm(String baseForm) { this.baseForm = baseForm; }
    public void setOriginalForm(String originalForm) { this.originalForm = originalForm; }
    public void setPosTag(String posTag) { this.posTag = posTag; }
    public void setPosition(int position) { this.position = position; }
    public void setNer(String ner) { this.ner = ner; }
    public void setNnNext(Word nnNext) { this.nnNext = nnNext; }
    public void setNnPrev(Word nnPrev) { this.nnPrev = nnPrev; }
    public void setCoreference(Word coreference) { this.coreference = coreference; }
    public void setModifiedWord(Word modifiedWord) { this.modifiedWord = modifiedWord; }
    public void setRepresent(Word represent) { this.represent = represent; }
    public void setIsCovered(boolean isCovered) { this.isCovered = isCovered; }
    public void setIsIgnored(boolean isCovered) { this.isIgnored = isCovered; }
    public void setOmitNode(boolean omitNode) { this.omitNode = omitNode; }
    public void setEmbbededTriple(TriplePattern embbededTriple) { this.embbededTriple = embbededTriple; }


}
