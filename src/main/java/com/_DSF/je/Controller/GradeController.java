package com._DSF.je.Controller;

import com._DSF.je.Entity.Grade;
import com._DSF.je.Entity.GradeRequest;
import com._DSF.je.Service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Optional<Grade> grade = gradeService.getGradeById(id);
        return grade.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Grade createGrade(@RequestBody Grade grade) {
        return gradeService.createGrade(grade);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody GradeRequest grade) {
        Grade updatedGrade = gradeService.updateGrade(id, grade);
        return updatedGrade != null ? ResponseEntity.ok(updatedGrade) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}/quiz/{quizId}")
    public ResponseEntity<Grade> getGradeByStudentAndQuiz(@PathVariable Long studentId, @PathVariable Long quizId) {
        Grade grade = gradeService.getGradeByStudentAndQuiz(studentId, quizId);
        return grade != null ? ResponseEntity.ok(grade) : ResponseEntity.notFound().build();
    }

}
