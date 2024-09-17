package com._DSF.je.Repository;

import com._DSF.je.Entity.Qcm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QcmRepository extends JpaRepository<Qcm, Long> {
    List<Qcm> findByQuizId(Long quizId);
}

