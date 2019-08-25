package queryanswering.mapping;

import common.MathUtils;
import lucene.EntityIdPair;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import queryanswering.ExtendSynsetForExtraction;
import rdf.mapping.EntityMapping;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBpediaLookup {
    private Logger logger = Logger.getLogger(this.getClass());
    private HttpClient httpClient;
    private String lookupURL;
    private Map<String, String> entityMentionDict;

    public DBpediaLookup(String lookupURL) {
        this.lookupURL = lookupURL;
        httpClient = new HttpClient();
        httpClient.getParams().setConnectionManagerTimeout(3000);
        entityMentionDict = new HashMap<>();
        entityMentionDict.putAll(ExtendSynsetForExtraction.EXTEND_ENTITY_SYNSET);
    }

    public List<String> lookupEntityName(String toSearch) {
        List<String> resEntites = new ArrayList<>();
        String resBeginPattern = "<Result>\n      <Label>";
        String resEndPattern = "</Label>";
        int resBeginPatternLength = resBeginPattern.length();
        GetMethod getMethod = new GetMethod(
                (lookupURL + toSearch).replaceAll(" ", "%20"));

        int statusCode;
        String response;
        try {
            statusCode = httpClient.executeMethod(getMethod);
            response = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        if (response == null || response.isEmpty() || statusCode != 200) { return new ArrayList<>(); }
        /*
          Response Example:
          <?xml version="1.0" encoding="utf-8"?>
                <ArrayOfResult xmlns="http://lookup.dbpedia.org/" ...
          */

        int ind1 = response.indexOf(resBeginPattern);


        while (ind1 != -1) {
            int ind2 = response.indexOf(resEndPattern, ind1 + resBeginPatternLength);
            resEntites.add(response.substring(ind1 + resBeginPatternLength, ind2));
            ind1 = response.indexOf(resBeginPattern, ind2 + resEndPattern.length());
        }

        return resEntites;
    }


    public List<EntityMapping> getEntityMappings(EntityIdPair entityIdPair,
                                                 String toSearch) {
        if (toSearch == null || toSearch.length() == 0) {
            return new ArrayList<>();
        }

        List<String> entMentions = new ArrayList<>();
        List<EntityMapping> entityMappings = new ArrayList<>();

        if (entityMentionDict.containsKey(toSearch)) {
            entMentions.add(entityMentionDict.get(toSearch));
        } else {
            entMentions.addAll(lookupEntityName(toSearch));
        }

        if (entMentions.size() == 0 && toSearch.contains(". ")) {
            entMentions.addAll(lookupEntityName(toSearch.replaceAll(". ", ".")));
        }

        String[] splits = toSearch.split("_");
        int numUpperCase = 0, count = 40;

        for (String str: splits) {
            if (Character.isUpperCase(str.charAt(0)) || Character.isDigit(str.charAt(0))) {
                numUpperCase ++;
            }
        }

        boolean onlyUpperCase = numUpperCase < splits.length;
        int edToDrop = toSearch.length()/2;

        for (String entMention: entMentions) {
            if (onlyUpperCase &&  // TODO verify this condition
                    MathUtils.editDistance(entMention, toSearch.replace("_", "")) > edToDrop) {
                continue;
            }

            entMention = entMention.replace(" ", "_");
            if (entityIdPair.getEntityNameToId().containsKey(entMention)) {
                entityMappings.add(
                        new EntityMapping(entityIdPair.getEntityNameToId().get(entMention), entMention, count));
                count -= 2;
            } else {
                logger.warn("Drop " + entMention  + ", entity not found in dictionary");
            }
        }

        return entityMappings;
    }



}
