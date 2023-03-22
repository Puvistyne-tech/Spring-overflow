package uge.fr.ugeoverflow.service;

import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.answer.NewAnswerDTO;
import uge.fr.ugeoverflow.error.answer.AnswerNotFoundException;
import uge.fr.ugeoverflow.error.answer.BodyNotFoundException;
import uge.fr.ugeoverflow.error.question.QuestionNotFoundException;
import uge.fr.ugeoverflow.error.answer.VoteNotFoundException;
import uge.fr.ugeoverflow.model.*;
import uge.fr.ugeoverflow.repository.AnswerRepository;
import uge.fr.ugeoverflow.repository.CommentRepository;
import uge.fr.ugeoverflow.repository.FollowRelationshipRepository;
import uge.fr.ugeoverflow.repository.VoteRepository;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final AnswerOptimisticLockService answerOptimisticLockService;

    public AnswerService(AnswerRepository answerRepository, QuestionService questionService, CommentRepository commentRepository, VoteRepository voteRepository, AnswerOptimisticLockService answerOptimisticLockService, FollowRelationshipRepository followRelationshipRepository) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.answerOptimisticLockService = answerOptimisticLockService;
    }

//    public UUID createAnswer(User user, OneQuestionDTO question, NewAnswerDTO answerDTO) {
//        var questionTDO = questionService.getQuestionById(question.getId());
//        Answer answer = AnswerDTO.toAnswer(user, questionTDO.toNewQuestion(user), answerDTO);
//        return answerRepository.save(answer).getId();
//    }


    // TODO : Optimize this method
    @Transactional
    public AnswerDTO updateAnswer(UUID id, NewAnswerDTO answerDTO) {
        var existingAnswer = answerRepository.findById(id)
                .orElseThrow(AnswerNotFoundException::new);
        existingAnswer.setBody(answerDTO.getBody());

        return new AnswerDTO(answerRepository.save(existingAnswer));
    }

    public void deleteAnswer(UUID id) {
        var answer = answerRepository.findById(id)
                .orElseThrow(AnswerNotFoundException::new);
        answerRepository.delete(answer);
    }

    public void validateAnswer(NewAnswerDTO answerDTO) {
        if (answerDTO.getBody() == null || answerDTO.getBody().isEmpty()) {
            throw new BodyNotFoundException();
        }
    }

    @Transactional
    public List<AnswerDTO> getAllAnswers() {
        return answerRepository.findAll().stream().map(AnswerDTO::new).collect(Collectors.toList());
    }

    public List<AnswerDTO> getAnswersByQuestion(UUID questionId) {
        if (!questionService.checkIfQuestionExist(questionId)) {
            throw new QuestionNotFoundException();
        }
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        return answers.stream().map(AnswerDTO::new).collect(Collectors.toList());
    }

    public AnswerDTO getAnswerById(UUID id) {
        var answer = answerRepository.findById(id)
                .orElseThrow(AnswerNotFoundException::new);
        return new AnswerDTO(answer);

    }

    public boolean checkIfAnswerExist(UUID answerId) {
        return answerRepository.existsById(answerId);
    }

//    public List<AnswerDTO> getAnswersByUser(User user) {
//        var answers = answerRepository.findAllByUserId(user.getId()).stream().map(one -> {
//            var answerDTO = new AnswerDTO(one);
//            var vote = voteRepository.findByUserAndAnswer(user, one);
//            if (!vote.isEmpty())
//                answerDTO.setVote(vote.get(0).getVoteType()==VOTE_TYPE.UPVOTE);
//            return answerDTO;
//        }).toList();
//        return answers;
//    }

    @Transactional
    public CommentDTO comment(UUID answerId, String body, User principal) {
        var answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        var comment = commentRepository.save(CommentDTO.toNewComment(principal, answer, body));
        return new CommentDTO(comment);
    }

    @Transactional
    public void vote(UUID answerId, User user, VOTE_TYPE voteType) {
        var answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        if (!answer.isVoter(user)) {
            var vote = new Vote(voteType, user);
            voteRepository.save(vote);
            addVoteWithLock(answer, vote);
        } else {
            var voteOptional = answer.getVotes().stream().filter(v -> v.getUser().getId().equals(user.getId())).findFirst();
            if (voteOptional.isPresent()) {
                var vote = voteOptional.get();
                if (vote.getVoteType() == voteType) {
                    voteRepository.delete(vote);
                    removeVoteWithLock(answer, vote);
                } else {
                    vote.setVoteType(voteType);
                    voteRepository.save(vote);
                }
            }else {
                throw new VoteNotFoundException();
            }
        }
    }

    //    private void removeVoteWithLock(Answer answer, Vote vote) {
//        var retry = true;
//        while (retry) {
//            retry = false;
//            try {
//                answerOptimisticLockService.removeVote(answer, vote);
//            } catch (ObjectOptimisticLockingFailureException e) {
//                retry = true;
//            }
//        }
//        answerRepository.save(answer);
//    }
    @Retryable(value = {ObjectOptimisticLockingFailureException.class}, maxAttempts = -1, backoff = @Backoff(delay = 500))
    public void removeVoteWithLock(Answer answer, Vote vote) {
        answerOptimisticLockService.removeVote(answer, vote);
        answerRepository.save(answer);
    }

    @Retryable(value = {ObjectOptimisticLockingFailureException.class}, maxAttempts = -1, backoff = @Backoff(delay = 500))
    private void addVoteWithLock(Answer answer, Vote vote) {
        answerOptimisticLockService.addVote(answer, vote);
        answerRepository.save(answer);
    }

    @Transactional
    public List<AnswerDTO> getAnswersByUser(User user) {
        var ans = answerRepository.findAllByUserId(user.getId());
        return ans.stream().map(AnswerDTO::new).collect(Collectors.toList());
    }


//    public List<Answer> getAnswersForQuestion(Question question, User currentUser) {
//        List<Answer> answers = answerRepository.findByQuestion(question);
//        List<AnswerScore> answerScores = new ArrayList<>();
//        for (Answer answer : answers) {
//            int baseScore = answer.getUpVotes() - answer.getDownVotes();
//            int weightedScore = baseScore;
//            for (User followedUser : currentUser.getFollowedUsers()) {
//                List<Vote> userVotes = voteRepository.findByUserAndAnswer(followedUser, answer);
//                for (Vote vote : userVotes) {
//                    int weightFactor = calculateWeightFactor(currentUser, followedUser);
//                    weightedScore += (vote.getVoteType() == VOTE_TYPE.UPVOTE) ? weightFactor : -weightFactor;
//                }
//            }
//            answerScores.add(new AnswerScore(answer, weightedScore));
//        }
//        answerScores.sort(Comparator.comparingInt(AnswerScore::score).reversed());
//        return answerScores.stream().map(AnswerScore::answer).collect(Collectors.toList());
//    }
//
//    private int calculateWeightFactor(User currentUser, User votedUser) {
//        // Implement your logic for calculating the weight factor based on the rating of the voted user.
//        return 1; // Replace with actual implementation.
//    }
//
//    private record AnswerScore(Answer answer, int score) {
//    }
//
//    // (0 , 1000)
//
//    //  + 10 u  -4 d  = 6000
//
//    //  5 * - ( 1 ) = 5x *  = -500
//    //  6000 - 500 = 5500
}

