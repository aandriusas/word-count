package io.github.aandriusas.wordcount.services.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class CountedWords {

    private Map<String, Integer> aToG;
    private Map<String, Integer> hToN;
    private Map<String, Integer> oToU;
    private Map<String, Integer> vToZ;

}
