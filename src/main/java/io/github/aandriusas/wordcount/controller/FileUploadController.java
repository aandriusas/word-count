package io.github.aandriusas.wordcount.controller;

import io.github.aandriusas.wordcount.services.WordCountService;
import io.github.aandriusas.wordcount.services.helper.CountedWords;
import io.github.aandriusas.wordcount.util.StringManipulationUtils;
import io.github.aandriusas.wordcount.util.UploadConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionScope
public class FileUploadController {

    private CountedWords cw;
    @Autowired
    private WordCountService wordCountService;

    @RequestMapping("/")
    public String uploading(Model model) {
        model.addAttribute("wordCountResult", cw);
        return "uploadForm";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws IOException {
        List<String> filePaths = new ArrayList<>();
        for (MultipartFile uploadedFile : uploadingFiles) {
            File file = new File(UploadConstants.UPLOADING_DIR + uploadedFile.getOriginalFilename());
            uploadedFile.transferTo(file);
            filePaths.add(file.getAbsolutePath());
        }
        cw = wordCountService.countWordsInFiles(filePaths);
        return "redirect:/";
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFile(@RequestParam("range") String range, HttpServletResponse response) throws IOException {
        if (range == null || "".equals(range.trim())) {
            response.sendRedirect("/");
            return null;
        }
        String fileName = StringManipulationUtils.generateFileName(range);
        response.addHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType("txt/plain");

        String content;
        switch (range) {
            case "A-G":
                content = StringManipulationUtils.convertToString(cw.getAToG());
                break;
            case "H-N":
                content = StringManipulationUtils.convertToString(cw.getHToN());
                break;
            case "O-U":
                content = StringManipulationUtils.convertToString(cw.getOToU());
                break;
            case "V-Z":
                content = StringManipulationUtils.convertToString(cw.getVToZ());
                break;
            default:
                response.sendRedirect("/");
                return null;
        }
        return content.getBytes();
    }

}
