package fragment;

import common.MathUtils;
import common.io.LoadPath;
import lucene.EntityFragmentFields;
import lucene.EntityNameWithScore;
import lucene.index.IndexBuilder;
import lucene.search.EntityFragmentSearcher;
import lucene.search.LCNPara;
import org.apache.log4j.Logger;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import rdf.mapping.EntityMapping;

import java.nio.file.Paths;
import java.util.*;

public class EntityFragment extends Fragment {

    // EntityFragment adds Type info set, TypeFragment adds Entity info set
    private Set<Integer> innerEdges, outerEdges, typeSet;
    private Map<Integer, List<Integer>> innerEntMap, outerEntMap;

    private static double getScore(String str1, String str2, double luceneScore) {

        return luceneScore * 100.0/(Math.log(MathUtils.editDistance(str1, str2) * 1.5 + 1) + 1);
    }

    private static Map<Integer, Double> getCandEntityNames(String phrase) {

        Map<Integer, Double> res = new HashMap<>();
        List<EntityNameWithScore> sbjEntName = getCandEntityNamesForSubjectFromSearcher(phrase);
        int sbjEntNameSize = 0;

        if (sbjEntName.size() <= LCNPara.EntityPara.K) {
            sbjEntNameSize = sbjEntName.size();
        } else {
            sbjEntNameSize = sbjEntName.
                    get(LCNPara.EntityPara.K - 1).getScore() >= LCNPara.EntityPara.TH2 ? sbjEntName.size() : LCNPara.EntityPara.K ;
        }

        for (int i = 0; i < sbjEntNameSize; i ++) {
            if (i < LCNPara.EntityPara.K || sbjEntName.get(i).getScore() >= LCNPara.EntityPara.TH2) {
                res.put(sbjEntName.get(i).getId(),
                        getScore(phrase, sbjEntName.get(i).getName(), sbjEntName.get(i).getScore()));
            } else break;
        }
        return res;
    }


    private static List<EntityNameWithScore> getCandEntityNamesForSubjectFromSearcher(String phrase) {
        List<EntityNameWithScore> res = new ArrayList<>();

        try {
            Directory indexDir = FSDirectory.open(
                    Paths.get(LoadPath.FragmentLoadPath.entityFragmentIndexDirPath));
            EntityFragmentSearcher entFgmtSearcher = new EntityFragmentSearcher(indexDir, IndexBuilder.analyzer);
            res.addAll(entFgmtSearcher.search(phrase));
        } catch (Exception e) {
//            logger.error("Search entity name for subject failed.");
            e.printStackTrace();
        }

        return res;
    }


    public EntityFragment(int id, String entityFgmtStr) {
        logger = Logger.getLogger(this.getClass());
        this.id = id;
        innerEdges = new HashSet<>();
        outerEdges = new HashSet<>();
        typeSet = new HashSet<>();

        innerEntMap = new HashMap<>();
        outerEntMap = new HashMap<>();

        entityFgmtStr = entityFgmtStr.replace('|', '#');
        String[] fields = entityFgmtStr.split("#");

        if (fields.length > 0 && fields[0].length() > 0) {
            String[] entEdges = fields[0].split(",");
            for (String edge : entEdges) {
                String[] nums = edge.split(":");
                if (nums.length != 2) continue;

                int intEntId = Integer.valueOf(nums[0]);
                String[] intEdges = nums[1].split(";");
                ArrayList<Integer> intEdgeList = new ArrayList<Integer>();

                for (String outEdge : intEdges) intEdgeList.add(Integer.valueOf(outEdge));

                if (intEdgeList.size() > 0) innerEntMap.put(intEntId, intEdgeList);
            }
        }

        if (fields.length > 1 && fields[1].length() > 0) {
            String[] entEdgesArr = fields[1].split(",");
            for (String edge : entEdgesArr) {
                String[] nums = edge.split(":");

                if (nums.length != 2) continue;

                int outEntId = Integer.valueOf(nums[0]);
                String[] outEdges = nums[1].split(";");
                ArrayList<Integer> outEdgeList = new ArrayList<Integer>();

                for (String outEdge : outEdges) outEdgeList.add(Integer.valueOf(outEdge));

                if (outEdgeList.size() > 0) outerEntMap.put(outEntId, outEdgeList);
            }
        }

        if (fields.length > 2 && fields[2].length() > 0) {
            String[] nums = fields[2].split(",");
            for (String num: nums) {
                if (num.length() > 0) innerEdges.add(Integer.parseInt(num));
            }
        }

        if (fields.length > 3 && fields[3].length() > 0) {
            String[] nums = fields[3].split(",");
            for (String num: nums) {
                if (num.length() > 0) outerEdges.add(Integer.parseInt(num));
            }
        }

        if (fields.length > 4 && fields[4].length() > 0) {
            String[] nums = fields[4].split(",");
            for (String num: nums) {
                if (num.length() > 0) typeSet.add(Integer.parseInt(num));
            }
        }
    }

    public Set<Integer> getInnerEdges() { return this.innerEdges; }
    public Set<Integer> getOuterEdges() { return this.outerEdges; }
    public Set<Integer> getTypeSet() { return this.typeSet; }
    public Map<Integer, List<Integer>> getInnerEntMap() { return this.innerEntMap; }
    public Map<Integer, List<Integer>> getOuterEntMap() { return this.outerEntMap; }

    public static List<EntityMapping> getEntityMappings(String phrase,
                                                        EntityFragmentFields entFgmtFields) {
        Map<Integer, Double> entMappings = getCandEntityNames(phrase);
        List<EntityMapping> res = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry: entMappings.entrySet()) {
            int id = entry.getKey();
            res.add(new EntityMapping(id, entFgmtFields.getEntityIdPair().getEntityIdToName().get(id), entMappings.get(id)));
        }

        Collections.sort(res);
        return res;
    }


    @Override
    public String toString() {

        return "";
    }



}
