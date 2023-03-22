package uge.fr.ugeoverflow.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uge.fr.ugeoverflow.dto.answer.NewAnswerDTO;
import uge.fr.ugeoverflow.dto.question.NewQuestionDTO;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.answer.AnswerDTO;
import uge.fr.ugeoverflow.dto.CommentDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;
import uge.fr.ugeoverflow.error.question.*;
import uge.fr.ugeoverflow.error.user.UserNotFoundException;
import uge.fr.ugeoverflow.filters.*;
import uge.fr.ugeoverflow.model.*;
import uge.fr.ugeoverflow.repository.*;
import uge.fr.ugeoverflow.security.UserRole;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    private final AnswerRepository answerRepository;

    private final CommentRepository commentRepository;

    private final Map<QuestionFilterType, QuestionFilterStrategy> filterStrategies;

    private final FollowRelationshipRepository followRelationshipRepository;
    private final VoteRepository voteRepository;

    public QuestionService(QuestionRepository questionRepository,
                           UserRepository userRepository,
                           AnswerRepository answerRepository,
                           CommentRepository commentRepository,
                           TagRepository tagRepository,
                           FollowRelationshipRepository followRelationshipRepository,
                           VoteRepository voteRepository) {
        List<QuestionFilterStrategy> filterStrategies = new ArrayList<>();
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        filterStrategies.add(new NewestQuestionsFilterStrategy(this.questionRepository));
        filterStrategies.add(new OldestQuestionsFilterStrategy(this.questionRepository));
        filterStrategies.add(new UnansweredQuestionsFilterStrategy(this.questionRepository));
        filterStrategies.add(new AnsweredQuestionsFilterStrategy(this.questionRepository));
        this.filterStrategies = filterStrategies.stream().collect(Collectors.toMap(QuestionFilterStrategy::getFilterName, Function.identity()));
        this.followRelationshipRepository = followRelationshipRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    public List<OneQuestionDTO> getAllQuestions() {
        // think to use page and size to limit the number of questions
        // spring data (Pageable)
        var questions = questionRepository.findAll();
        return questions.stream().map(OneQuestionDTO::fromQuestion).collect(Collectors.toList());
    }


    public UUID createQuestion(OneQuestionDTO oneQuestionDTO) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
//        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) throw new UserNotFoundException();
        var question = new Question();
        question.setTitle(oneQuestionDTO.getTitle());
        question.setBody(oneQuestionDTO.getBody());
        question.setUser(user);
        question.setLocation(oneQuestionDTO.getLocation());
        var tags = oneQuestionDTO.getTags().stream().map(tag -> {
            var t = tagRepository.findById((long) Tag.TAG_TYPE.valueOf(tag).ordinal() + 1).orElseThrow();
            t.increaseQuestionCount();
            return tagRepository.save(t);
        }).collect(Collectors.toSet());
        question.setTags(tags);
        question = questionRepository.save(question);
        return question.getId();
    }

    public UUID createQuestion(NewQuestionDTO questionDTO) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
//        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) throw new UserNotFoundException();
        var question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setBody(questionDTO.getBody());
        question.setUser(user);
        var tags = questionDTO.getTags().stream().map(tag -> {

            try {
                var type = Tag.TAG_TYPE.valueOf(tag);
                var t = tagRepository.findById((long) type.ordinal() + 1).orElseThrow();
                t.increaseQuestionCount();
                return tagRepository.save(t);
            } catch (IllegalArgumentException e) {
                throw new TagQuestionNotFoundException();
            }


        }).collect(Collectors.toSet());
        question.setTags(tags);
        question = questionRepository.save(question);
        return question.getId();
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public OneQuestionDTO updateQuestion(UUID id, OneQuestionDTO oneQuestionDTO) {
        var existingQuestion = questionRepository.findById(oneQuestionDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + oneQuestionDTO.getId()));
        Set<Tag> oldTags = existingQuestion.getTags();
        existingQuestion.setTitle(oneQuestionDTO.getTitle());
        existingQuestion.setBody(oneQuestionDTO.getBody());
        existingQuestion.setTags(oneQuestionDTO.getTags().stream().map(tag -> tagRepository.findByTagType(Tag.TAG_TYPE.valueOf(tag)).orElseThrow(TagNotFoundException::new)).collect(Collectors.toSet()));
        var updatedQuestion = questionRepository.save(existingQuestion);
        // update questionCount for tags
        Set<Tag> newTags = updatedQuestion.getTags();
        for (Tag tag : oldTags) {
            if (!newTags.contains(tag)) {
                tag.setQuestionCount(tag.getQuestionCount() - 1);
            }
        }
        for (Tag tag : newTags) {
            if (!oldTags.contains(tag)) {
                tag.setQuestionCount(tag.getQuestionCount() + 1);
            }
        }
        return new OneQuestionDTO(updatedQuestion);
    }


    @Transactional
    public void deleteQuestion(UUID id) {
        var question = questionRepository.findById(id)
                .orElseThrow(QuestionNotFoundException::new);
        question.getTags().forEach(tag -> {
            tag.decreaseQuestionCount();
            tagRepository.save(tag);
        });
        questionRepository.delete(question);
    }

    @Transactional
    public OneQuestionDTO getQuestionById(UUID id) {
        var question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        OneQuestionDTO tempQuestion = OneQuestionDTO.fromQuestion(question);

        if(auth!=null && !auth.getPrincipal().equals(UserRole.USER_ANONYMOUS.name())){

            User user = userRepository.findByUsername(auth.getName()).orElseThrow(UserNotFoundException::new);
            tempQuestion.setAnswers(getAnswersSortedList(question.getAnswers(),user).stream()
                    .map(AnswerDTO::new).collect(Collectors.toList()));
        }else{

            tempQuestion.setAnswers(question.getAnswers().stream()
                    .sorted(Comparator.comparingDouble(answer->computeAnswerBasicScore((Answer) answer)).reversed()
                    ).map(AnswerDTO::new).collect(Collectors.toList()));
        }

        return tempQuestion;
    }


    public List<OneQuestionDTO> getQuestionsByUser(UUID userId) {
        List<Question> questions = questionRepository.findAllByUserId(userId);
        return questions.stream().map(OneQuestionDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public List<QuestionDTO> getQuestionsByUser(User user) {
        var question = questionRepository.findAllQuestionsByUser(user);
        return question.stream().map(QuestionDTO::new).collect(Collectors.toList());
    }


    public List<QuestionDTO> getQuestionsDTOByUserId(UUID userId) {
        List<QuestionDTO> questions = questionRepository.findAllByUserId(userId).stream().map(QuestionDTO::new).collect(Collectors.toList());
        return questions;
    }

    public void validateQuestion(OneQuestionDTO oneQuestionDTO) {
        validate(oneQuestionDTO);
        if (questionRepository.existsByTitle(oneQuestionDTO.getTitle())) {
            throw new QuestionAlreadyExistsException();
        }
    }

    public boolean checkIfQuestionExist(UUID questionId) {
        return questionRepository.existsById(questionId);
    }

    public boolean existsByTitle(String title) {
        return questionRepository.existsByTitle(title);
    }



    public UUID answer(UUID id, NewAnswerDTO answerDTO) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        var question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        //  answer must be added to database before adding it to the question
        var answer = answerRepository.save(AnswerDTO.toNewAnswer(user, question, answerDTO));
        return answer.getId();
    }


    public UUID comment(UUID id, String body ) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        var question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        var c = CommentDTO.toNewComment(user, question, body);
        var comment = commentRepository.save(c);
        return comment.getId();
    }


    @Transactional
    public Page<OneQuestionDTO> findAllQuestionByPage(Pageable pageable) {
        //todo add input check, null elements handling
        return questionRepository.findAll(pageable).map(OneQuestionDTO::new);
    }


    @Transactional
    public List<QuestionDTO> getQuestionsByFilter(String filterName) {
        QuestionFilterStrategy filterStrategyChosen = filterStrategies.get(QuestionFilterType.valueOf(filterName.toUpperCase()));
        if (filterStrategyChosen == null) {
            throw new IllegalArgumentException("Invalid filter name: " + filterName);
        }
        return filterStrategyChosen.filterQuestions();
    }

    @Transactional
    public List<UUID> getFollowedUsers() {
        //get current user
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(UserNotFoundException::new);
        var list = followRelationshipRepository.findAllFollowedUsersByGivenUser(user.getId());
        return list;
    }

    @Transactional
    public void getFollowedUsersQuestions(UUID userId, Integer level, TreeMap<Integer, Set<QuestionDTO>> map, Integer maxLevel) {
        if (level <= maxLevel) {
            Set<QuestionDTO> questions;
            Set<QuestionDTO> l = new HashSet<>();
            List<UUID> followedUsers = followRelationshipRepository.findAllFollowedUsersByGivenUser(userId);

            followedUsers.forEach(fu -> {
                if (!fu.equals(userId)) {

                    l.addAll(getQuestionsDTOByUserId(fu));

                }
            });
            if (map.containsKey(level)) {
                questions = map.get(level);
                questions.addAll(l);
                map.put(level, questions);
            } else {
                map.put(level, l);
            }
            followedUsers.forEach(fu -> {
                if (!fu.equals(userId)) {
                    getFollowedUsersQuestions(fu, level + 1, map, maxLevel);
                }
            });
        }

    }



    @Transactional
    public Set<QuestionDTO> findAllQuestionDTO(){
        return questionRepository.findAll().stream().map(QuestionDTO::new).collect(Collectors.toSet());
    }

    public Boolean existsByUserIds(List<UUID> userIds){
        return questionRepository.existsByUserIdIn(userIds);
    }

    public void validateUpdateQuestion(OneQuestionDTO oneQuestionDTO) {
        validate(oneQuestionDTO);
    }

    private void validate(OneQuestionDTO oneQuestionDTO) {
        if (oneQuestionDTO.getTitle() == null || oneQuestionDTO.getTitle().isEmpty()) {
            throw new TitleNotFoundException();
        }
        if (oneQuestionDTO.getBody() == null || oneQuestionDTO.getBody().isEmpty()) {
            throw new BodyNotFoundException();
        }
        if (oneQuestionDTO.getTags() == null || oneQuestionDTO.getTags().isEmpty()) {
            throw new TagNotFoundException();
        }
    }

    public List<Answer> getAnswersSortedList(List<Answer> answers, User user) {
        Comparator<Answer> comparator = Comparator.comparingDouble(answer -> ComputeUserScoreForGivenAnswer(answer, user, 1) + computeAnswerBasicScore(answer));
        answers.sort(comparator.reversed());
        return answers;
    }

    public Double computeAnswerBasicScore(Answer answer) {
        long countUps = answer.getUpVotes();
        long countDowns = answer.getDownVotes();
        return Double.valueOf(countUps - countDowns);
    }

    private Double ComputeUserScoreForGivenAnswer(Answer answer, User user, Integer layer) {

        AtomicReference<Double> score = new AtomicReference(0.0);
        answer.getVotes().forEach(vote -> {
            User u = vote.getUser();
            FollowRelationship relation = followRelationshipRepository.findFollowRelationshipByFollowerAndFollowed(user, u);
            if (relation != null) {
                // 1pt reputation equivalent à 2 votes
                score.updateAndGet(v -> v + Double.valueOf((vote.getVoteType().equals(VOTE_TYPE.UPVOTE) ? 1 : -1) * relation.getReputation() * 2 + (vote.getVoteType().equals(VOTE_TYPE.UPVOTE) ? 1 : -1)));
                score.set(score.get() + ComputeUserScoreForGivenAnswer(answer, u, layer + 1));
            }

        });

        if (score.get() != Double.valueOf(0)) {
            return Double.valueOf(score.get() / layer);
        }
        return Double.valueOf(0);
    }


}