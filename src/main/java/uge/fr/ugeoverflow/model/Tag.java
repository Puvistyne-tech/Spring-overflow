package uge.fr.ugeoverflow.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_type", nullable = false, unique = true)
    private TAG_TYPE tagType;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Question> questions = new HashSet<>();

    @Column(name = "question_count")
    private int questionCount = 0;

    public Tag(TAG_TYPE tagType, String description) {
        this.tagType = tagType;
        this.description = description;
    }

    public Tag() {
    }

    public Tag(TAG_TYPE tagType) {
        this.tagType = tagType;
    }

    public TAG_TYPE getTagType() {
        return tagType;
    }


    public void setTagType(TAG_TYPE tagType) {
        this.tagType = tagType;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagType=" + tagType +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public void increaseQuestionCount() {
        this.questionCount++;
    }

    public void decreaseQuestionCount() {
        if (this.questionCount > 0)
            this.questionCount--;
    }

    public enum TAG_TYPE {
        ANDROID,
        SPRING,
        JAVA,
        JAVASCRIPT,
        PYTHON,
        C,
        CPP,
        CSHARP,
        RUBY,
        SCALA,
        KOTLIN,
        GO,
        RUST,
        SWIFT,
        DART,
        HTML,
        CSS,
        SQL,
        REACT,
        ANGULAR,
        MYSQL,
        POSTGRESQL,
        MONGODB,
        CASSANDRA,
        HADOOP,
        SPARK,
        HIVE,
        PIG,
        HBASE,
        ZOOKEEPER,
        KAFKA,
        FLINK,
        DOCKER,
        KUBERNETES,
        AWS,
        AZURE
    }
}
