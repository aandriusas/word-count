package io.github.aandriusas.wordcount.services.helper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileContentConsumer implements Runnable {

    private BlockingQueue<String> textLines;
    private ConcurrentHashMap<String, Integer> resultMap;
    private AtomicInteger runningProducers;

    public FileContentConsumer(BlockingQueue<String> textLines, ConcurrentHashMap<String, Integer> resultMap, AtomicInteger runningProducers) {
        this.textLines = textLines;
        this.resultMap = resultMap;
        this.runningProducers = runningProducers;
    }

    @Override
    public void run() {
        while (true) {
            String line = null;
            try {
                line = textLines.poll(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (line != null) {
                for (String word : line.toLowerCase().replaceAll("[^A-Za-z\\s]+", "").split(" ")) {
                    resultMap.merge(word, 1, Integer::sum);
                }
            }
            if (runningProducers.get() < 1 && textLines.isEmpty()) {
                return;
            }
        }
    }
}
