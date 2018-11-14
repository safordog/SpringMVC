package com.gmail.safordog.demoprogspringmvczip;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.*;
import java.util.zip.*;

@RestController
public class MyController {

    @GetMapping({"/", "/main"})
    public ModelAndView getIndex() {

        return new ModelAndView("index");
    }

    @RequestMapping(value = "/add_file")
    public ModelAndView getFile(@RequestParam("file") MultipartFile[] files) throws IOException {

        FileOutputStream fout = new FileOutputStream("/home/safordog/IdeaProjects/ProgSpringMVCzip/src/main/resources/static/archive.zip");
        CheckedOutputStream checksum = new CheckedOutputStream(fout, new Adler32());
        ZipOutputStream zout = new ZipOutputStream(checksum);
        for (MultipartFile temp : files) {
            FileInputStream fin = new FileInputStream(temp.getOriginalFilename());
            ZipEntry zipEntry = new ZipEntry(temp.getOriginalFilename());
            zout.putNextEntry(zipEntry);
            int length;
            byte[] buffer = new byte[1024];
            while((length = fin.read(buffer)) > 0) {
                zout.write(buffer, 0, length);
            }
            fin.close();
        }
        zout.closeEntry();
        zout.finish();
        zout.close();
        return new ModelAndView("loadZip");
    }

    @GetMapping("/load_zip")
    public ResponseEntity<InputStreamResource> downloadZip() throws IOException {

        File file = new File("/home/safordog/IdeaProjects/ProgSpringMVCzip/src/main/resources/static/archive.zip");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
                .body(resource);
    }

}

