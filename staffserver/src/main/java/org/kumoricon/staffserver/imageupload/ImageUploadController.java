package org.kumoricon.staffserver.imageupload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ImageUploadController {
    private static final Logger log = LoggerFactory.getLogger(ImageUploadController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        for (MultipartFile file : files) {
            String fileName = fileStorageService.storeFile(file);
            log.info("{} uploaded file {} ({} bytes) saved to {}",
                    request.getRemoteUser(),
                    file.getOriginalFilename(),
                    file.getSize(),
                    fileName);
        }

        return ResponseEntity.accepted().body("Accepted");
    }


//    @GetMapping("/downloadFile/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
//        // Load file as Resource
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            logger.info("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
}
