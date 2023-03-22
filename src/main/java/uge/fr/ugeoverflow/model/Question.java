package uge.fr.ugeoverflow.model;

import jakarta.persistence.*;

import java.util.*;


//TODO : if the user is deleted, all his questions and answers should be deleted too

@Entity
@Table(name = "questions")
public class Question extends Overflow {

    @Column(nullable = false, length = 1000, unique = true)
    private String title;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();




    public Question() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }



}
