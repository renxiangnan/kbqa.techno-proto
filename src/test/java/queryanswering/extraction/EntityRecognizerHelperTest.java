package queryanswering.extraction;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityRecognizerHelperTest {

    @Test
    public void lenComparatorTest() {
        List<Integer> list =  Arrays.asList(10, 20, 30);
        List<Integer> expectedRes = Arrays.asList(30, 20, 10);
        list.sort(new EntityRecognizerHelper.LenComparator(list.size()));
        Assert.assertEquals(expectedRes, list);
    }

    @Test
    public void valueComparatorTest() {
        //TODO

    }

    @Test
    public void searchHelperTest() {
        //TODO, this method may exist bug

        List<List<Integer>> ret = new ArrayList<>();
        EntityRecognizerHelper.searchHelper(
                Arrays.asList(2),  Arrays.asList(2),
                0, 21, ret);
        System.out.println(ret);
    }

}
