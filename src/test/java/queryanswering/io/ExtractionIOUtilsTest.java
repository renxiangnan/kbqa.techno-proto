package queryanswering.io;

import common.io.LoadPath;
import org.junit.Test;

import java.util.List;

public class ExtractionIOUtilsTest {

    @Test
    public void loadStopEntitiesTest() {
        List<String> stopEntities = ExtractionIOUtils.loadStopEntities(LoadPath.ExtractionPath.stopEntityFilePath);

        for (String str: stopEntities) System.out.println(str);

    }



}
