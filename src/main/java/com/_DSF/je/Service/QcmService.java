package com._DSF.je.Service;

import com._DSF.je.Entity.Grade;
import com._DSF.je.Entity.Qcm;
import com._DSF.je.Entity.Quiz;
import com._DSF.je.Repository.GradeRepository;
import com._DSF.je.Repository.QcmRepository;
import com._DSF.je.Repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class QcmService {

    private final QcmRepository qcmRepository;
    private final GradeRepository gradeRepository;
    private final QuizRepository quizRepository;  // Add QuizRepository

    public QcmService(QcmRepository qcmRepository, GradeRepository gradeRepository, QuizRepository quizRepository) {
        this.qcmRepository = qcmRepository;
        this.gradeRepository = gradeRepository;
        this.quizRepository = quizRepository;  // Initialize QuizRepository
    }

    // Method to check the answer
    public boolean checkAnswer(Long qcmId, String answer) {
        Optional<Qcm> qcm = qcmRepository.findById(qcmId);

        // Handle case where QCM is not found
        if (qcm.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "QCM not found for the given ID.");
        }

        Qcm question = qcm.get();
        System.out.println("Stored Answer: " + question.getCorrectAnswer());
        System.out.println("Provided Answer: " + answer);

        // Case-insensitive and trimmed comparison
        if (question.getCorrectAnswer().trim().equalsIgnoreCase(answer.trim())) {
            Grade grade = question.getGrade();
            grade.setValue(grade.getValue() + 1); // Increase grade by 1 for a correct answer
            gradeRepository.save(grade);
            return true;
        }

        return false;
    }

    // Method to create a QCM
    public Qcm createQCM(Qcm qcm) {
        // Fetch the Grade by gradeId from the database
        Optional<Grade> grade = gradeRepository.findById(qcm.getGrade().getId());
        if (grade.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found");
        }

        // Fetch the Quiz by quizId from the database
        Optional<Quiz> quiz = quizRepository.findById(qcm.getQuiz().getId());
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

        // Set the Grade and Quiz in the QCM entity
        qcm.setGrade(grade.get());
        qcm.setQuiz(quiz.get());

        // Save and return the QCM entity
        return qcmRepository.save(qcm);
    }
}
