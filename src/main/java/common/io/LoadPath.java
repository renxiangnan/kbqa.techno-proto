package common.io;

public interface  LoadPath {

    String globalPath = "/Users/xiangnanren/IDEAWorkspace/gAnswer-master/data/DBpedia2016/";

    interface DictionaryLoadPath {

        String dbpediaPredicateIdPath = globalPath + "parapharse/16predicate_id.txt";
        String dbpediaDBPredicatePath = globalPath + "parapharse/16dbo_predicates.txt";
        String dbpediaHandwritePredicatePath = globalPath + "parapharse/dbpedia-relation-paraphrase-handwrite.txt";
        String dbpediaRelParaBaseRerankPath = globalPath + "parapharse/dbpedia-relation-paraphrases-withScore-baseform-merge-sorted-rerank-slct.txt";
    }

    interface FragmentLoadPath {

        String entityIdPairPath = globalPath + "fragments/id_mappings/16entity_id.txt";
        String entityFragmentPath = globalPath + "fragments/entity_RDF_fragment/16entity_fragment.txt";
        String sourceDirPath = globalPath + "fragments/entity_RDF_fragment/16entity_fragment.txt";

        /*
            Source files of type fragment (DBpedia & Yago)
         */
        String typeFragmentPath = globalPath + "fragments/class_RDF_fragment/16type_fragment.txt";
        String dbpediaBasicTypePath = globalPath + "fragments/id_mappings/16basic_types_id.txt";
        String yagoTypePath = globalPath + "fragments/id_mappings/16yago_types_list.txt";

        /*
            Source files of relation fragment
        */

        String predicateFragmentPath = globalPath + "fragments/predicate_RDF_fragment/predicate_fragment.txt";
        String predicateIdPath = globalPath + "fragments/id_mappings/16predicate_id.txt";


        /*
            Lucene index directory
         */
        String entityFragmentIndexDirPath = "/Users/xiangnanren/IDEAWorkspace/kbqa-datasets/lucene/entity_fragment_index";
        String typeShortNameIndexDirPath = "/Users/xiangnanren/IDEAWorkspace/kbqa-datasets/lucene/type_short_name_index";
    }


    interface ExtractionPath {

        String stopEntityFilePath = globalPath + "parapharse/stopEntDict.txt";
    }

    interface DBpediaLookupPath {

        String DBpediaLookupURL = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?MaxHits=5&QueryString=";
    }

    interface StopEntityFilePath {

        String stopEntityFilePath = globalPath + "parapharse/stopEntDict.txt";
    }



}
