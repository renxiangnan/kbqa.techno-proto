package dictionary.io;

import dictionary.relationmention.PredicateIDSupport;
import dictionary.relationmention.PredicateIdPair;
import common.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class PredicateLoadHelper {

    static void processInitFileLine(String line,
                                    PredicateIdPair predicateIdPair,
                                    Map<String, List<PredicateIDSupport>> predicateNLPattern,
                                    Set<String> notFoundInDBP2014) {
        String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
        String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);
        String predicateKey = splitted[0];

        if (!predicateIdPair.getPredicateToId().containsKey(predicateKey)) {
            notFoundInDBP2014.add(predicateKey);
            return;
        }

        int predicateId = predicateIdPair.getPredicateToId().get(predicateKey);
        String nlPattern = splitted[1].toLowerCase();
        int support = Integer.parseInt(splitted[2]);
        String[] selectivitiesStr = splitted[3].split(IOUtils.lineSeparator);
        double[] selectivities = new double[selectivitiesStr.length];

        for (int i = 0; i < selectivitiesStr.length; i ++) {
            selectivities[i] = Double.parseDouble(selectivitiesStr[i]);
        }

        if (!predicateNLPattern.containsKey(nlPattern)) {
            predicateNLPattern.put(nlPattern, new ArrayList<>());
        }

        predicateNLPattern.get(nlPattern).
                add(new PredicateIDSupport(predicateId, support, selectivities));
    }


    static void processHandwriteFileLine(String line,
                                         Map<String, Integer> predicateToId,
                                         Map<String, List<PredicateIDSupport>> predicateNLPattern) {
        String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
        String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);
        String predicateKey = splitted[0];

        if (!predicateToId.containsKey(predicateKey)) return;
        int predicateId = predicateToId.get(predicateKey);
        String nlPattern = splitted[1].toLowerCase();
        int support = Integer.parseInt(splitted[2]);
        int patternSize = nlPattern.split(IOUtils.lineSeparator).length;

        if (!predicateNLPattern.containsKey(nlPattern))
            predicateNLPattern.put(nlPattern, new ArrayList<>());

        predicateNLPattern.get(nlPattern).
                add(new PredicateIDSupport(
                        predicateId,
                        support,
                        PredicateIDSupport.initSelectivies(patternSize)));
    }

}
