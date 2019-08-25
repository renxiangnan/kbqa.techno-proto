package lucene.index;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.util.Optional;

public abstract class IndexBuilder {

    int mergeFactor = 100000;
    int maxMergeDoc = Integer.MAX_VALUE;

    IndexWriter indexWriter;
    IndexWriterConfig indexWriterConfig;
    Logger logger;

    public final static Analyzer analyzer = new StandardAnalyzer();

    private IndexWriterConfig initIndexWriterConfig() {
        int maxBufferedDoc = 1000;
        indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setMaxBufferedDocs(maxBufferedDoc);
        return indexWriterConfig;
    }

    Field buildField(String name,
                     String value,
                     Optional<IndexOptions> indexOptions,
                     Optional<Boolean> storeOption,
                     Optional<Boolean> tokenizedOption,
                     Optional<Boolean> storeTermVectorOption,
                     Optional<Boolean> storeTermVectorOffsetOption) {
        FieldType fieldType = new FieldType();

        indexOptions.ifPresent(fieldType :: setIndexOptions);
        storeOption.ifPresent(fieldType :: setStored);
        tokenizedOption.ifPresent(fieldType :: setTokenized);
        storeTermVectorOption.ifPresent(fieldType :: setStoreTermVectors);
        storeTermVectorOffsetOption.ifPresent(fieldType :: setStoreTermVectorOffsets);

        return new Field(name, value, fieldType);
    }

    public IndexBuilder() {

        indexWriterConfig = initIndexWriterConfig();
    }

    public void buildIndex() {}



}
