package com._DSF.je.Service;

import com._DSF.je.Entity.Grade;
import com._DSF.je.Entity.GradeRequest;
import com._DSF.je.Repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Grade updateGrade(Long id, GradeRequest updatedGrade) {
        Optional<Grade> gradeToUpdate = gradeRepository.findAll().stream()
                .filter(g -> Objects.equals(g.getStudent().getId(), id) && g.getQuiz().getId().equals(updatedGrade.getQuizId()))
                .findFirst();
        Double previousValue = gradeToUpdate.get().getValue();
        Grade newGrade = gradeToUpdate.get();
        newGrade.setValue(previousValue + updatedGrade.getValue());

        System.out.println("*************************************************");
        System.out.println("*************************************************");
        System.out.println("*************************************************");
        System.out.println("the grade to updated belongs to : " + gradeToUpdate.get().getStudent().getUsername());
        System.out.println("Previous Value of the grade : " + previousValue);
        System.out.println("The value to be added is : " + updatedGrade.getValue());
        System.out.println("The new value is : " + newGrade.getValue());
        System.out.println("*************************************************");
        System.out.println("*************************************************");
        System.out.println("*************************************************");

        return gradeRepository.save(newGrade);
    }

    public Grade getGradeByStudentAndQuiz(Long studentId, Long quizId) {
        return gradeRepository.findByStudentIdAndQuizId(studentId, quizId).orElse(null);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}
