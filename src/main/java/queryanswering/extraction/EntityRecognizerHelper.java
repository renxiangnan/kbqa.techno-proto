package queryanswering.extraction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EntityRecognizerHelper {

    static class LenComparator implements Comparator<Integer> {
        int baseSize;   // input word list size + 1 >= 1

        public LenComparator(int baseSize) { this.baseSize = baseSize; }

        @Override
        public int compare(Integer size1, Integer size2) {
            return Integer.compare(
                    (size1 % baseSize) - size1 / baseSize,
                    (size2 % baseSize) - size2 / baseSize);
        }
    }

    static class ValueComparator implements Comparator<Integer> {
        Map<Integer, Double> baseScoreMap;
        int baseSize;
        double threshold = 1e-8;

        public ValueComparator(Map<Integer, Double> baseScoreMap, Integer baseSize) {
            this.baseScoreMap = baseScoreMap;
            this.baseSize = baseSize;
        }

        private int thresholdComparator(double num1, double num2) {
            if (num1 + threshold < num2 ) {
                return -1;
            } else {
                return num2 + threshold < num1 ? 1 : 0;
            }
        }

        @Override
        public int compare(Integer num1, Integer num2) {
            if (!baseScoreMap.containsKey(num1) || !baseScoreMap.containsKey(num2)) {
                return 0;
            }

            if (thresholdComparator(baseScoreMap.get(num1), baseScoreMap.get(num2)) < 0) {
                return 1;
            } else if (thresholdComparator(baseScoreMap.get(num1), baseScoreMap.get(num2)) == 0) {
                return Integer.compare(
                        (num1 % baseSize - num1 / baseSize),
                        (num2 % baseSize - num2 / baseSize));
            } else {
                return -1;
            }
        }
    }

    public static void searchHelper(List<Integer> keys,
                                    List<Integer> selected,
                                    int depth,
                                    int size,
                                    List<List<Integer>> selectedList) {
        if (depth == keys.size()) {
            selectedList.add(new ArrayList<>(selected));
            return;
        }

        searchHelper(keys, selected, depth + 1, size, selectedList);
        boolean conflict = false;

        for (int prevKey: selected) {
            int currKey = keys.get(depth);
            int prevEnd = prevKey % size, prevStart = (prevKey - prevEnd) / size;
            int currEnd = currKey % size, currStart = (currKey - currEnd) / size;

            if (!(prevStart < prevEnd && prevEnd <= currStart && currStart < currEnd) &&
                    !(currStart < currEnd && currEnd <= prevStart && prevStart < prevEnd)) {
                conflict = true;
            }

            if (!conflict) {
                selected.add(keys.get(depth));
                searchHelper(keys, selected, depth + 1, size, selectedList);
                selected.remove(keys.get(depth));
            }
        }
    }


}
