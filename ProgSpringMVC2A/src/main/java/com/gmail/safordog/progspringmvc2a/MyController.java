package com.gmail.safordog.progspringmvc2a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MyController {

    private PhotoRepository photoRepository;

    @Autowired
    public MyController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @GetMapping({"/", "/main"})
    public ModelAndView getIndex() {
        return new ModelAndView("index");
    }

    @RequestMapping(value="/add_photo", method= RequestMethod.POST)
    public ModelAndView addPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new MyException();
        } else {
            try {
                long id = System.currentTimeMillis();
                Photo image = new Photo(id, photo.getBytes());
                photoRepository.save(image);

                model.addAttribute("photo_id", id);
                return new ModelAndView("result");
            } catch (IOException e) {
                System.out.println(e);
            }
            return new ModelAndView("");
        }
    }

    @GetMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("photo_id") long id) {
        Photo newPhoto = photoRepository.findPhotoById(id);
        byte[] bytes = newPhoto.getImage();
        if (bytes == null) {
            throw new MyException();
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        }
    }

    @Transactional
    @RequestMapping("/delete/{photo}")
    public ModelAndView deletePhoto(@PathVariable("photo") long id) {
        photoRepository.deletePhotoById(id);
            return new ModelAndView("index");
    }
    @Transactional
    @RequestMapping("/deleteAll")
    public ModelAndView deleteAll(HttpServletRequest req) {
        if (req.getParameter("id") != null) {
            String[] s = req.getParameterValues("id");
            List<Long> l = new ArrayList<>();
            for (int i = 0; i < s.length; i++) {
                l.add(Long.parseLong(s[i]));
            }
            for (Long temp : l) {
                photoRepository.deletePhotoById(temp);
            }
            s = null;
        }
        return new ModelAndView("index");
    }

    @RequestMapping(value="/view", method=RequestMethod.POST)
    public ResponseEntity<byte[]> getView(@RequestParam("photo_id") long id) {
        Photo newPhoto = photoRepository.findPhotoById(id);
        byte[] bytes = newPhoto.getImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("/all_photos")
    public ResponseEntity<String> getAllPhotos() {
        List<Photo> list = photoRepository.findAll();
        String str = "<!DOCTYPE html><html lang=\"en\"><head><style>form {text-align: center;} " +
                "table {border-collapse: collapse;margin: auto;color: darkslategrey;}" +
                "table, td, th {border: 1px solid #999;padding: 5px;}" +
                "</style></head><body><form action=\"/deleteAll\"><table><tr><th>delete</th><th>id</th><th>image</th></tr>";
        for (Photo temp : list) {
            str += "<tr><td><input type=\"checkbox\" name=\"id\" value=\"" + temp.getId() + "\"></td><td>"
                    + temp.getId() + "</td><td><img src='/photo/" + temp.getId() + "' width=100/></td></tr>";
        }
        str += "</table><input type=\"submit\" value=\"delete all\"></form></body></html>";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<String>(str, header, HttpStatus.OK);
    }

}
