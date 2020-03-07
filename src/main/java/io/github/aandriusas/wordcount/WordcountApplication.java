package io.github.aandriusas.wordcount;

import io.github.aandriusas.wordcount.util.UploadConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class WordcountApplication {

    public static void main(String[] args) {
        new File(UploadConstants.UPLOADING_DIR).mkdirs();
        SpringApplication.run(WordcountApplication.class, args);
    }

}
