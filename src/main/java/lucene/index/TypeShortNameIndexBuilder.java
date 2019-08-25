package lucene.index;

import fragment.collection.TypeFragmentCollection;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TypeShortNameIndexBuilder extends IndexBuilder {
    private Map<String, List<Integer>> shortNameToIdMap;

    private Document buildDocument(String shortName, String splittedShortName) {
        Document document = new Document();

        Field shortNameField = buildField(
                FieldNameLabels.
                        TypeShortNameFieldLabel.
                        typeShortName,
                shortName,
                Optional.of(IndexOptions.NONE),
                Optional.of(true),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());

        Field splittedTypeShortName = buildField(
                FieldNameLabels.
                        TypeShortNameFieldLabel.
                        splittedTypeShortName,
                splittedShortName,
                Optional.of(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS),
                Optional.of(true),
                Optional.of(true),
                Optional.of(true),
                Optional.of(true));

        document.add(shortNameField);
        document.add(splittedTypeShortName);

        return document;
    }

    public TypeShortNameIndexBuilder(TypeFragmentCollection typeFragmentCollection,
                                     String indexDirPath) {
        try {
            Directory indexDir = FSDirectory.open(Paths.get(indexDirPath));
            indexWriter = new IndexWriter(indexDir, super.indexWriterConfig);
        } catch (IOException e) {
            logger.error("The initialization of TypeShortName index directory failed.");
            e.printStackTrace();
        }

        LogMergePolicy logMergePolicy = new LogByteSizeMergePolicy();
        logMergePolicy.setMergeFactor(super.mergeFactor);
        logMergePolicy.setMaxMergeDocs(super.maxMergeDoc);
        indexWriter.getConfig().setMergePolicy(logMergePolicy);
        this.shortNameToIdMap = typeFragmentCollection.getTypeShortNameIdPair().getShortNameToId();
        buildIndex();
    }


    @Override
    public void buildIndex() {
        try {
            for (String typeName: shortNameToIdMap.keySet()) {
                if (typeName.length() == 0) continue;
                StringBuilder splittedName = new StringBuilder();

                if (typeName.contains("_")) {
                    splittedName.append(typeName.replace("_", " ").toLowerCase());
                } else {
                    char[] typeNameArr = typeName.toCharArray();
                    for (int start = 0, i = 0; i < typeNameArr.length; i ++) {
                        if (!Character.isLowerCase(typeNameArr[i])) {
                            splittedName.append(typeName.substring(start, i).toLowerCase()).append(" ");
                            start = i;
                        }

                        if (i == typeNameArr.length - 1) {
                            splittedName.append(typeName.substring(start).toLowerCase());
                        }
                    }

                    while (splittedName.charAt(0) == ' ') {
                        splittedName.deleteCharAt(0);
                    }
                }

                this.indexWriter.addDocument(buildDocument(typeName, splittedName.toString()));
            }
            this.indexWriter.commit();
        } catch (Exception e) {
            logger.error("TypeShortName index build failed. ");
            e.printStackTrace();
        }
    }

}
