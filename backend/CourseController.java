package com.example.E.learning.Platform.Controller;

import com.example.E.learning.Platform.Model.Course;
import com.example.E.learning.Platform.Repository.CourseRepository;
import com.example.E.learning.Platform.Service.CourseService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses/upload")
    public String openUploadForm(){
        return "upload-course";
    }

    @PostMapping("/courses/upload")
    public String uploadCourse(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            @RequestParam("descriptions") String descriptions,
            Model model) {

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "upload-course";
        }

        try {
            String uploadDir = "uploads/";
            String originalFileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + originalFileName);

            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Course course = new Course();
            course.setTitle(title);
            course.setFileName(originalFileName);
            course.setFileType(file.getContentType());
            course.setFilePath(path.toString());
            course.setDescriptions(descriptions);

            courseService.saveCourse(course);

            return "redirect:/admin-dashboard";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload file.");
            return "upload-course";
        }
    }

    @GetMapping("/courses/manage")
    public String getAllCourses(Model model){
        model.addAttribute("courses",courseService.getAllCourse());
        return "manage-courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourses(@PathVariable Long id, Model model){
        model.addAttribute("course", courseService.getCourseById(id));
        return "edit-course";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id){
        courseService.deleteCourseById(id);
        return "redirect:/courses/manage";
    }

    @GetMapping("/courses/view")
    public String viewCourses(Model model){
        model.addAttribute("courses", courseService.getAllCourse());
        return "view-courses";
    }

    @GetMapping("/search")
    public String searchCourse(@RequestParam("keyword") String keyowrd, Model model){
        List<Course> matchesCourse = courseService.searchCoursesByTitle(keyowrd);
        model.addAttribute("courses", matchesCourse);
        return "student-dashboard";
    }

    @GetMapping("/view/files/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
        Path filePath = Paths.get("uploads").resolve(fileName).normalize();
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // or detect type dynamically
                .body(resource);
    }


    @GetMapping("/courses/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Course course = courseRepository.findById(id).orElse(null);

        if (course == null || course.getFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get("uploads").resolve(course.getFileName()).normalize();
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + course.getFileName() + "\"")
                .body(resource);
    }


}
