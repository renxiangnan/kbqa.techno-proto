package fragment.io;

import fragment.Fragment;
import fragment.TypeFragment;
import fragment.TypeShortNameIdPair;
import common.io.IOUtils;
import common.Tuple2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static fragment.Fragment.literalTypeId;

public class TypeFragmentIOUtils extends FragmentIOUtils {
    private static final Set<String> yagoStopTypes = new HashSet<>(Arrays.asList(
            "Amazon", "Earth", "TheHungerGames", "SparklingWine",
            "Type", "Flow", "Owner", "Series", "Shot", "Part", "Care",
            "Peace", "Vice", "Dodo", "CzechFilms", "ChineseFilms"
    ));

    private static final String literalTypeShortName = "literal_HRZ";

    private static final List<Tuple2<Integer, String>> additionalTypeIdPair =
            new ArrayList<>(Collections.singletonList(
                    new Tuple2<>(literalTypeId, literalTypeShortName)
            ));

    private static TypeShortNameIdPair
        addAdditionalShortNameIdPair(TypeShortNameIdPair oldPairs) {
        for (Tuple2<Integer, String> pair: additionalTypeIdPair) {
            if (!oldPairs.getShortNameToId().containsKey(pair.t)) {
                oldPairs.getShortNameToId().put(pair.t, new ArrayList<>());
            }
            oldPairs.getShortNameToId().get(pair.t).add(pair.s);
            oldPairs.getIdToShortName().put(pair.s, pair.t);
        }

        return oldPairs;
    }

    public static TypeShortNameIdPair loadTypeShortNameIdPair(String basicTypeIdPath) {
        File basicTypeFile = new File(basicTypeIdPath);
        Map<String, List<Integer>> shortNameToId = new HashMap<>();
        Map<Integer, String> idToShortName = new HashMap<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(basicTypeFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);
                String typeShortName = splitted[0];
                int typeId = Integer.parseInt(splitted[1]);

                if (!shortNameToId.containsKey(typeShortName)) {
                    shortNameToId.put(typeShortName, new ArrayList<>());
                }

                shortNameToId.get(typeShortName).add(typeId);
                idToShortName.put(typeId, typeShortName);
            }
        } catch (IOException e) {
            logger.error("");
            e.printStackTrace();
        }

        return addAdditionalShortNameIdPair(
                new TypeShortNameIdPair(shortNameToId, idToShortName));
    }

    public static Set<String> RemoveStopTypesFromYago(String yaoTypePath) {
        File yagoTypeFile = new File(yaoTypePath);
        Set<String> yagoTypes = new HashSet<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(yagoTypeFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);
                String typeName = splitted[0];

                if (!yagoStopTypes.contains(typeName))
                    yagoTypes.add(typeName);
            }

        } catch (IOException e) {
            logger.error("Types load from Yago failed.");
            e.printStackTrace();
        }

        return yagoTypes;
    }

    public static Map<Integer, TypeFragment> loadTypeFragmentMap(String typeFragmentPath) {
        File typeFragmentFile = new File(typeFragmentPath);
        Map<Integer, TypeFragment> typeFragmentMap = new HashMap<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(typeFragmentFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                String[] splitted = utf8Encoded.split(IOUtils.globalFileSeparator);
                if (splitted[0].length() > 0 && !splitted[0].equals(Fragment.nonEncodedLable)) {
                    int typeId = Integer.parseInt(splitted[0]);
                    TypeFragment typeFragment = new TypeFragment(typeId, splitted[1]);
                    typeFragmentMap.put(typeId, typeFragment);
                }
            }
        } catch (IOException e) {
            logger.error("");
            e.printStackTrace();
        }

        assert typeFragmentMap.size() > 0;
        return typeFragmentMap;
    }

}
