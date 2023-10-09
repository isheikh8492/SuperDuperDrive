package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/home/files")
public class FileController {
    private FileService fileService;
    private UserMapper userMapper;

    public FileController(FileService fileService, UserMapper userMapper) {
        this.fileService = fileService;
        this.userMapper = userMapper;
    }

    @PostMapping()
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
                                 Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        String uploadError = null;
        String loggedInUserName = authentication.getPrincipal().toString();

        User user = userMapper.getUser(loggedInUserName);

        if (fileUpload.isEmpty()) {
            uploadError = "Please select a non-empty file.";
        } else if (fileService.isFileAvailable(fileUpload.getOriginalFilename(), user.getUserId())) {

            uploadError = "The file already exists.";
        }
        System.out.println(uploadError);
        if(uploadError!=null) {
            redirectAttributes.addFlashAttribute("error", uploadError);
            return "redirect:/result?error";
        }

        fileService.addFile(fileUpload, user.getUserId());
        return "redirect:/result?success";
    }

    public List<File> getUploadedFiles(Integer userId) {
        return fileService.getAllFiles(userId);
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String loggedInUsername = authentication.getPrincipal().toString();
        User user = userMapper.getUser(loggedInUsername);

        if (fileId > 0) {
            fileService.deleteFile(fileId, user.getUserId());
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the file.");
        return "redirect:/result?error";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFileName()+"\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
}
