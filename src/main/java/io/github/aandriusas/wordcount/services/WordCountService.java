package io.github.aandriusas.wordcount.services;

import io.github.aandriusas.wordcount.services.helper.CountedWords;
import io.github.aandriusas.wordcount.services.helper.FileContentConsumer;
import io.github.aandriusas.wordcount.services.helper.FileReadProducer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WordCountService {

    private static final int MAX_CONSUMER_THREADS = 3;

    public CountedWords countWordsInFiles(List<String> filePaths) {
        BlockingQueue<String> filesLinesQueue = new LinkedBlockingQueue<>(1000);
        AtomicInteger activeProducersCount = new AtomicInteger(filePaths.size());

        for (String filePath : filePaths) {
            Thread producer = new Thread(new FileReadProducer(filesLinesQueue, filePath, activeProducersCount));
            producer.start();
        }

        ConcurrentHashMap<String, Integer> countedWordsMap = new ConcurrentHashMap<>();
        List<Thread> consumerList = new ArrayList<>();
        for (int i = 0; i < MAX_CONSUMER_THREADS; i++) {
            Thread consumer = new Thread(new FileContentConsumer(filesLinesQueue, countedWordsMap, activeProducersCount));
            consumer.start();
            consumerList.add(consumer);
        }

        try {
            for (Thread consumer : consumerList) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TreeMap<String, Integer> countedWordsTreeMap = new TreeMap<>(countedWordsMap);

        return CountedWords.builder()
                .aToG(countedWordsTreeMap.tailMap("a").headMap("h"))
                .hToN(countedWordsTreeMap.tailMap("h").headMap("o"))
                .oToU(countedWordsTreeMap.tailMap("o").headMap("v"))
                .vToZ(countedWordsTreeMap.tailMap("v"))
                .build();
    }
}
