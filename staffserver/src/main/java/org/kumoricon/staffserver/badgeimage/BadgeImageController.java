package org.kumoricon.staffserver.badgeimage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@SuppressWarnings(value = "unused")
@RestController
public class BadgeImageController {
    private static final Logger log = LoggerFactory.getLogger(BadgeImageController.class);
    private final BadgeImageService badgeImageService;

    @Autowired
    public BadgeImageController(BadgeImageService badgeImageService) {
        this.badgeImageService = badgeImageService;
    }

    @GetMapping("/badgeImages")
    public List<String> getAvailableImages(@RequestParam(value = "after", defaultValue = "0") Long after, HttpServletRequest request) {
        List<String> availableImages = badgeImageService.availableFiles(after);
//        log.info("{} getting badge images changed after {}, found {}", request.getRemoteUser(), after, availableImages.size());
        return availableImages;
    }

    @RequestMapping(value = "/badgeImages/{fileName:.+}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//        log.info("{} getting image {}", request.getRemoteUser(), fileName);
        if (fileName.contains("..") || fileName.contains("\\") || fileName.contains("/")) {
            response.setStatus(400);
            return null;
        }
        File image = badgeImageService.getFileFor(URLDecoder.decode(fileName, "UTF8"));
        if (image == null || !image.exists()) {
            response.setStatus(404);
        }
        return new FileSystemResource(image);
    }
}
