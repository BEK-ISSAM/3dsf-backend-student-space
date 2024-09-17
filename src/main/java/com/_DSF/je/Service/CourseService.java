package com._DSF.je.Service;

import com._DSF.je.Entity.Category;
import com._DSF.je.Entity.Course;
import com._DSF.je.Entity.User;
import com._DSF.je.Repository.CategoryRepository;
import com._DSF.je.Repository.CourseRepository;
import com._DSF.je.Repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Course createCourse(Course course, MultipartFile pdfFile) throws IOException {
        // Check and set teacher
        if (course.getTeacher() != null && course.getTeacher().getId() != null) {
            User teacher = userRepository.findById(course.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            course.setTeacher(teacher);
        }

        // Check and set category
        if (course.getCategory() != null && course.getCategory().getId() != null) {
            Category category = categoryRepository.findById(course.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            course.setCategory(category);
        }

        // Handle PDF file
        if (pdfFile != null && !pdfFile.isEmpty()) {
            course.setPdfName(pdfFile.getOriginalFilename());
            course.setPdfType(pdfFile.getContentType());
            course.setPdfData(pdfFile.getBytes());
        }

        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setTitle(courseDetails.getTitle());
                    course.setTeacher(courseDetails.getTeacher());
                    course.setCategory(courseDetails.getCategory());
                    course.setAssignments(courseDetails.getAssignments());
                    course.setQuizzes(courseDetails.getQuizzes());
                    course.setVideos(courseDetails.getVideos());
                    course.setDescription(courseDetails.getDescription());
                    course.setPrice(courseDetails.getPrice());
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
    }

    public void deleteCourse(Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();

            // Remove related entities
            course.getAssignments().clear();
            course.getQuizzes().clear();
            course.getVideos().clear();

            // Save changes
            courseRepository.save(course);

            // Now delete the course
            courseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Course not found with id " + id);
        }
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.searchCourses(keyword);
    }

    public List<Course> filterByPrice(Double minPrice, Double maxPrice, String sortOrder) {
        Sort sort = Sort.by("price");
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        return courseRepository.filterByPrice(minPrice, maxPrice, sort);

    }

    public ResponseEntity<byte[]> getPdfFile(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            byte[] pdfData = course.getPdfData();
            if (pdfData != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(course.getPdfType()));
                headers.setContentDispositionFormData("attachment", course.getPdfName());

                return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}