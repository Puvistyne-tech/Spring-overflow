package uge.fr.ugeoverflow.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.model.Answer;
import uge.fr.ugeoverflow.model.Vote;
import uge.fr.ugeoverflow.repository.AnswerRepository;

import java.beans.Transient;

@Service
public class AnswerOptimisticLockService {

    private final AnswerRepository answerRepository;

    public AnswerOptimisticLockService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Transactional
    public void addVote(Answer answer, Vote vote) {
        var answerWithLock = answerRepository.findById(answer.getId()).orElseThrow();
        answerWithLock.addVote(vote);
        answerRepository.save(answerWithLock);
    }

    @Transactional
    public void removeVote(Answer answer,Vote vote){
        var answerWithLock = answerRepository.findById(answer.getId()).orElseThrow();
        answerWithLock.removeVote(vote);
        answerRepository.save(answerWithLock);
    }
}
