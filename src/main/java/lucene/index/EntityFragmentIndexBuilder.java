package lucene.index;

import lucene.EntityFragmentFields;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import common.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Optional;

public class EntityFragmentIndexBuilder extends IndexBuilder {
    private String sourceDirPath;
    private EntityFragmentFields entityFragmentFields;

    private Document buildDocument (int entityId,
                                    String entityName,
                                    String entityFragment) {
        Document document = new Document();

        Field entityIdField = buildField(
                FieldNameLabels.EntityFieldLabel.EntityId,
                String.valueOf(entityId),
                Optional.of(IndexOptions.NONE),
                Optional.of(true),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());

        Field entityNameField = buildField(
                FieldNameLabels.EntityFieldLabel.EntityName,
                entityName,
                Optional.of(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS),
                Optional.of(true),
                Optional.of(true),
                Optional.of(true),
                Optional.of(true));

        Field entityFragmentField = buildField(
                FieldNameLabels.EntityFieldLabel.EntityFragment,
                entityFragment,
                Optional.of(IndexOptions.NONE),
                Optional.of(true),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());

        document.add(entityIdField);
        document.add(entityNameField);
        document.add(entityFragmentField);

        return document;
    }

    public EntityFragmentIndexBuilder(String indexDirPath,
                                      String sourceDirPath,
                                      String entityIdPairPath,
                                      String entityFragmentPath){
        this.entityFragmentFields = new EntityFragmentFields(entityIdPairPath, entityFragmentPath);

        try {
            Directory indexDir = FSDirectory.open(Paths.get(indexDirPath));
            this.sourceDirPath = sourceDirPath;
            indexWriter = new IndexWriter(indexDir, super.indexWriterConfig);
        } catch (IOException e) {
            logger.error("The initialization of EntityFragment index directory failed.");
            e.printStackTrace();
        }

        LogMergePolicy logMergePolicy = new LogByteSizeMergePolicy();
        logMergePolicy.setMergeFactor(super.mergeFactor);
        logMergePolicy.setMaxMergeDocs(super.maxMergeDoc);
        indexWriter.getConfig().setMergePolicy(logMergePolicy);
        buildIndex();
    }

    @Override
    public void buildIndex(){
        File predicateIdPairFile = new File(sourceDirPath);

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(predicateIdPairFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);

                int entityId = Integer.parseInt(splitted[0]);
                String entityFragment = splitted[1];
                String entityName = IOUtils.removeUnderscore(
                        entityFragmentFields.getEntityIdPair().getEntityIdToName().get(entityId));
                Document document = buildDocument(entityId, entityName, entityFragment);
                this.indexWriter.addDocument(document);
            }

            this.indexWriter.commit();
        } catch (IOException e) {
            logger.error("Predicate Id pair index build failed.");
            e.printStackTrace();
        }
    }

}
