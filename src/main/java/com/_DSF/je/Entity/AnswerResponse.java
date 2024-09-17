package com._DSF.je.Entity;


import lombok.*;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AnswerResponse {
    private Long qcmId;
    private String message;
    private boolean isCorrect;
}
