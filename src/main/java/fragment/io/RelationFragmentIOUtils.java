package fragment.io;

import fragment.Fragment;
import fragment.RelationFragment;
import fragment.RelationShortNameIdPair;
import common.io.IOUtils;
import common.Tuple2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RelationFragmentIOUtils extends FragmentIOUtils {

    public static Tuple2<RelationShortNameIdPair, Set<Integer>>
        loadRelationShortNameIdPair(String predicateFragmentPath,
                                    String predicateIdPath) {

        File predFgmtFile = new File(predicateFragmentPath);
        File predIdFile = new File(predicateIdPath);

        Map<Integer, List<RelationFragment>> relationIdToShortName = new HashMap<>();
        Map<String, List<Integer>> relationShortNameToId = new HashMap<>();
        Set<Integer> literalRelationSet = new HashSet<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(predFgmtFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);
                String innerStr = splitted[0].substring(1, splitted[0].length() - 1),
                        outerStr = splitted[2].substring(1, splitted[2].length() - 1);
                int predicateId = Integer.parseInt(splitted[1]);

                if (outerStr.equals(Fragment.nonEncodedLable.
                        substring(1, Fragment.nonEncodedLable.length() - 1))) {
                    literalRelationSet.add(predicateId);
                }

                if (!relationIdToShortName.containsKey(predicateId)) {
                    relationIdToShortName.put(predicateId, new ArrayList<>());
                }

                relationIdToShortName.get(predicateId).
                        add(new RelationFragment(predicateId, innerStr, outerStr));
            }
        } catch (IOException e) {
            logger.error("");
            e.printStackTrace();
        }


        try (BufferedReader buffer = new BufferedReader(
                new FileReader(predIdFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);

                String shortName = splitted[0], idStr = splitted[1];

                if (!relationShortNameToId.containsKey(shortName)) {
                    relationShortNameToId.put(shortName, new ArrayList<>());
                }

                relationShortNameToId.get(shortName).add(Integer.parseInt(idStr));
            }
        } catch (IOException e) {
            logger.error("");
            e.printStackTrace();
        }

        return new Tuple2<>(
                new RelationShortNameIdPair(
                        relationIdToShortName,
                        relationShortNameToId),
                literalRelationSet);
    }



}
