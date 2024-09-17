package com._DSF.je.Controller;

import com._DSF.je.Entity.AnswerRequest;
import com._DSF.je.Entity.AnswerResponse;
import com._DSF.je.Entity.Qcm;
import com._DSF.je.Service.QcmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qcm")
public class QcmController {

    private final QcmService qcmService;

    public QcmController(QcmService qcmService) {
        this.qcmService = qcmService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Qcm>> getAllQcms(){
        List<Qcm> result = qcmService.getAllQcms();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Qcm>> getQcmsByQuizId(@PathVariable Long quizId) {
        List<Qcm> qcms = qcmService.getQcmsByQuizId(quizId);
        return ResponseEntity.ok(qcms);
    }

    @PostMapping("/check/{qcmId}")
    public ResponseEntity<AnswerResponse> checkAnswer(@PathVariable Long qcmId, @RequestBody AnswerRequest request) {
        try {
            boolean isCorrect = qcmService.checkAnswer(qcmId, request.getAnswer(), request.getStudentId());
            System.out.println("------------------------------------------------");
            System.out.println("------------------------------------------------");
            System.out.println("The answer entered by user : " + request.getStudentId() + " for question : " + qcmId + " is : " + request.getAnswer() + " correct ? : " + isCorrect);
            System.out.println("------------------------------------------------");
            System.out.println("------------------------------------------------");

            return ResponseEntity.ok(new AnswerResponse(qcmId, "Your answer was: " + (isCorrect? "correct":"incorrect"), isCorrect));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new AnswerResponse(qcmId, "Some error happened in check answer", false));
        }
    }

    @PostMapping
    public ResponseEntity<Qcm> createQCM(@RequestBody Qcm qcm) {
        Qcm createdQCM = qcmService.createQCM(qcm);
        return ResponseEntity.ok(createdQCM);
    }
}

