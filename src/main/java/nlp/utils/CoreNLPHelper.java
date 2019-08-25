package nlp.utils;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.log4j.Logger;

import java.util.Properties;

public abstract class CoreNLPHelper {
    Logger logger;
    StanfordCoreNLP coreNLP;

    CoreNLPHelper() {
        Properties properties = new Properties();
        properties.put("annotators", "tokenize, ssplit, pos, lemma");

        coreNLP = new StanfordCoreNLP(properties);
    }

}
