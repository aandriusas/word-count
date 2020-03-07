package io.github.aandriusas.wordcount.services.helper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FileReadProducer implements Runnable {

    private BlockingQueue<String> textLines;
    private String filePath;
    private AtomicInteger workingProducers;

    public FileReadProducer(BlockingQueue<String> textLines, String filePath, AtomicInteger count) {
        this.textLines = textLines;
        this.filePath = filePath;
        this.workingProducers = count;
    }

    @Override
    public void run() {
        try (Stream<String> stream = Files.lines(Paths.get(filePath), Charset.defaultCharset())) {
            stream.forEach(s -> {
                try {
                    textLines.put(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        workingProducers.decrementAndGet();
        Path fileToDeletePath = Paths.get(filePath);
        try {
            Files.delete(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
