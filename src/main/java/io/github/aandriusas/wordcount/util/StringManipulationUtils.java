package io.github.aandriusas.wordcount.util;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class StringManipulationUtils {

    public static String convertToString(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect((Collectors.joining("\n")));
    }

    public static String generateFileName(String wordRange) {
        return wordRange + "_" + UUID.randomUUID() + ".txt";
    }
}
