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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<Qcm> getAllQcms(){
        return qcmRepository.findAll();
    }

    public List<Qcm> getQcmsByQuizId(Long quizId) {
        return qcmRepository.findByQuizId(quizId);
    }

    public boolean checkAnswer(Long qcmId, String answer, Long studentId) {
        Optional<Qcm> qcm = qcmRepository.findById(qcmId);

        if (qcm.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "QCM not found for the given ID.");
        }

        Qcm question = qcm.get();

        if (question.getCorrectAnswer().trim().equalsIgnoreCase(answer.trim())) {
//            Optional<Grade> gradeOpt = gradeRepository.findByStudentIdAndQuizId(studentId, question.getQuiz().getId());
//
//            if (gradeOpt.isPresent()) {
//                Grade grade = gradeOpt.get();
//                grade.setValue(grade.getValue() + 1);
//                gradeRepository.save(grade);
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade record not found for the student and quiz.");
//            }

            return true;
        }

        return false;
    }


    public Qcm createQCM(Qcm qcm) {
        Optional<Grade> grade = gradeRepository.findById(qcm.getGrade().getId());
        if (grade.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found");
        }

        Optional<Quiz> quiz = quizRepository.findById(qcm.getQuiz().getId());
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

        qcm.setGrade(grade.get());
        qcm.setQuiz(quiz.get());

        return qcmRepository.save(qcm);
    }
}
