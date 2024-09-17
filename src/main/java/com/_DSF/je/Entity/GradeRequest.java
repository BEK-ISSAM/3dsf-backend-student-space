package com._DSF.je.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GradeRequest {
    private Long studentId;
    private Long quizId;
    private Double value;
}
