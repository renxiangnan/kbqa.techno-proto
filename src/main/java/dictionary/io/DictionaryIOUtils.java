package dictionary.io;


import dictionary.relationmention.PredicateIDSupport;
import dictionary.relationmention.PredicateIdPair;
import nlp.utils.StanfordCoreNLP;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dictionary.io.PredicateLoadHelper.processHandwriteFileLine;
import static dictionary.io.PredicateLoadHelper.processInitFileLine;
import static common.io.IOUtils.*;

public final class DictionaryIOUtils {
    private static final Logger logger = Logger.getLogger(DictionaryIOUtils.class);
    private static Set<String> omittedPredicates =
            new HashSet<>(Arrays.asList("state", "states"));

    private DictionaryIOUtils() {
    }

    private static boolean needDeletionForDigits(char firstCh,
                                                 char secondCh,
                                                 char thirdCh) {
        return Character.isDigit(firstCh) &&
                secondCh == ' ' &&
                Character.isDigit(thirdCh);
    }

    private static boolean needDeletionForLetters(char firstToMatch,
                                                  char thirdToMatch,
                                                  char firstCh,
                                                  char secondCh,
                                                  char thirdCh,
                                                  char fourthCh) {
        return firstCh == firstToMatch &&
                secondCh == ' ' &&
                thirdCh == thirdToMatch &&
                fourthCh == ' ';
    }

    /*
        Remove the prefix URIs. E.g.,:
        Work/runtime 11, SpaceStation/volume,
        gameW/l	1974

     */
    private static String discardPatternPrefix(String predicate) {
        if (predicate.contains("/")) {
            if (Character.isUpperCase(predicate.charAt(0))) {
                predicate = predicate.substring(predicate.indexOf("/") + 1);
            } else {
                predicate = predicate.replace("/", "");
            }
        }
        return predicate;
    }

    private static void breakByNonLowerCase(String prefixDiscardedPred,
                                            StringBuilder nlPattern) {
        int lastInd = 0, currInd;

        for (currInd = 0; currInd < prefixDiscardedPred.length(); currInd++) {
            if (!Character.isLowerCase(prefixDiscardedPred.charAt(currInd))) {
                nlPattern.
                        append(prefixDiscardedPred.substring(lastInd, currInd).toLowerCase()).
                        append(" ");
                lastInd = currInd;
            }
        }
        nlPattern.append(prefixDiscardedPred.substring(lastInd, currInd).toLowerCase());
    }

    public static PredicateIdPair loadPredicateIdPair(String path) {
        Map<String, Integer> predicateToId = new HashMap<>();
        Map<Integer, String> idToPredicate = new HashMap<>();
        File predicateIdPairFile = new File(path);

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(predicateIdPairFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] words = utf8Encoded.split(globalFileSeparator);

                predicateToId.put(words[0], Integer.parseInt(words[1]));
                idToPredicate.put(Integer.parseInt(words[1]), words[0]);
            }
        } catch (IOException e) {
            logger.error("Predicate Id pair file load failure.");
            e.printStackTrace();
        }

        return new PredicateIdPair(predicateToId, idToPredicate);
    }

    public static Set<Integer> loadPredicateId(String path, PredicateIdPair predIdPair) {
        Set<Integer> predicateId = new HashSet<>();
        File predicateIdFile = new File(path);

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(predicateIdFile))) {
            String predicate;
            while ((predicate = buffer.readLine()) != null) {
                String utf8Encoded = new String(predicate.getBytes(), StandardCharsets.UTF_8);
                if (!predIdPair.getPredicateToId().containsKey(utf8Encoded)) {
                    continue;
                }

                logger.debug("Predicate not found in file: " + predicate);
                predicateId.add(predIdPair.getPredicateToId().get(predicate));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return predicateId;
    }

    public static Map<String, List<PredicateIDSupport>> initPredicateNLPattern(String path,
                                                                               PredicateIdPair predicateIdPair) {
        File predicateIdFile = new File(path);
        Map<String, List<PredicateIDSupport>> predicateNLPattern = new HashMap<>();
        Set<String> notFoundInDBP2014 = new HashSet<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(predicateIdFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.startsWith(commentToken) || line.isEmpty()) {
                    continue;
                }
                processInitFileLine(line, predicateIdPair, predicateNLPattern, notFoundInDBP2014);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.warn("The number of predicates not found in DBpedia 2014 count: " +
                notFoundInDBP2014.size());
        return predicateNLPattern;
    }

    public static Map<String, List<PredicateIDSupport>>
    loadPredicateNLPattern(PredicateIdPair predicateIdPair,
                           Map<String, List<PredicateIDSupport>> predicateNLPattern,
                           StanfordCoreNLP stanfordCoreNLP) {
        int support = 200, predicateId;
        for (String predicate : predicateIdPair.getPredicateToId().keySet()) {
            if (omittedPredicates.contains(predicate)) {
                continue;
            }

            predicateId = predicateIdPair.getPredicateToId().get(predicate);
            StringBuilder nlPattern = new StringBuilder();

            String prefixDiscardedPredicate = discardPatternPrefix(predicate);
            breakByNonLowerCase(prefixDiscardedPredicate, nlPattern);

            for (int ind = 3; ind < nlPattern.length(); ind++) {
                char firstCh = nlPattern.charAt(ind), secondCh = nlPattern.charAt(ind - 1),
                        thirdCh = nlPattern.charAt(ind - 2), fourthCh = nlPattern.charAt(ind - 3);

                if (needDeletionForDigits(firstCh, secondCh, thirdCh) ||
                        needDeletionForLetters('d', 'i', firstCh, secondCh, thirdCh, fourthCh) ||
                        needDeletionForLetters('b', 'd', firstCh, secondCh, thirdCh, fourthCh)) {
                    nlPattern.deleteCharAt(ind - 1);
                }
            }

            String stem = stanfordCoreNLP.lemmatizeByStandfordNLPCore(nlPattern.toString());
            int patternSize = stem.split(lineSeparator).length;

            if (!predicateNLPattern.containsKey(stem)) {
                predicateNLPattern.put(stem, new ArrayList<>());
            }
            predicateNLPattern.get(stem).add(
                    new PredicateIDSupport(
                            predicateId,
                            support,
                            PredicateIDSupport.initSelectivies(patternSize)));
        }

        return predicateNLPattern;
    }


    public static Map<String, List<PredicateIDSupport>>
    loadHandwritePredicateNLPattern(String path,
                                    PredicateIdPair predIdPair) {
        File handwritePredicateFile = new File(path);
        Map<String, Integer> predicateToId = predIdPair.getPredicateToId();
        Map<String, List<PredicateIDSupport>> predicateNLPattern = new HashMap<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(handwritePredicateFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.startsWith(commentToken) || line.isEmpty()) {
                    continue;
                }
                processHandwriteFileLine(line, predicateToId, predicateNLPattern);
            }
        } catch (IOException e) {
            logger.error("Load handwrite predicate NLPattern from file failed.");
            e.printStackTrace();
        }

        return predicateNLPattern;
    }

}
