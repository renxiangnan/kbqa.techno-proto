package queryanswering.io;

import common.io.LoadPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExtractionIOUtils {

    private ExtractionIOUtils() {}

    public static List<String> loadStopEntities(String stopEntityFilePath) {
        File entityNameToIdFile = new File(stopEntityFilePath);
        List<String> stopEntities = new ArrayList<>();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(entityNameToIdFile))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String utf8Encoded = new String(line.getBytes(), StandardCharsets.UTF_8);
                stopEntities.add(utf8Encoded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stopEntities;
    }


}




