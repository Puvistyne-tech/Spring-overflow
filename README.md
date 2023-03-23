# Mapping

- Entity Overflow

  - On a utilisé une entité Overflow qui est commune pour l'entité Question et l'entité Answer. grace à le stratergie '@Inheritance(strategy = InheritanceType.JOINED)'. Parce que les classe Answer et question ont plusieur champs en commun et aussi des relations communes. ex : Comment - Question, Comment- Answer
- Location

  - C'est une classe emmbedded avec l'entité Overflow.
- FollowRelationShip

  - This class signify the following and the follower relationship between users. This helps to search a user from two perpespectives.
- Answer - Vote
- A answer can have many votes, It is a Uni directional mapping. with Fetchtype Lazy and CascadeType All, Becuase due to a deletion of an anwser all the votes related to it should be also be removed.

```mermaid
   @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   public Set<Vote> votes;
```


Question - Tag

- These entities have bi directional mapping between them, because One question may have many tags and a tag can have many questions. So it is best to have a many-to-many bi directional mapping.
- It's equally needed to have a Cascade type Merge and refresh in Question entity "cascade = {CascadeType.MERGE, CascadeType.REFRESH}" because when deleting a question, related tags hould not be delted but it should be merged and refreshed.


Question -Answers

- they have a one-to-many uni-directional mapping. because one question can have many question. But we needed cascade type Remove. Which means when deleting a question, related answers should be delted. Since the creation of the answers are not done at the same time, wxe didn't want the cascade type persist. We also needed orphanRemoval, to remove the related entities automatically not needing to delete the answer manually for example.

```
@OneToMany(fetch = FetchType.LAZY, mappedBy = "question", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();
```



#### Security

1. @PreAuthorizeAdmin - Only admin has access
2. @PreAuthorizeAuthUser - Only authenticated user has access ( Not for Admin )
3. @PreAuthorizeAdminAndAuthUser - Access for both admin and authenticated user
4. @PreAuthorizeAnonymousUser - Only for anonymous user (propably not useful, for anonymous user, no need to add any conditions, because all the unauthorized access are considered as anom

ex: api authenticated user

> /auth/api/v1/..

#### Authentification

* login - /signin
* register - /register
* logout - /logout

#### profile

* user profile - /users/{username}
* my profile - /users/profile
* edit my profile - /users/profile/edit
* change my password - /users/profile/edit/password
* follow a user - /users/{username}/follow
* unfollow a user - /users/{username}/unfollow
* give reputation score to a user - /users/{username}/reputation

#### questions

* all questions(get) / create new questoin(Post) - /questions
* get/upsate/delete One questions - /questions/{question_id}
* get questions of a user by username - /questions/user/{username} //TODO
* get questions of a user by userId - /questions/user/{user_id} //TODO
* get/create answer(s) of a question - /questions/{question_id}/answers

#### Answers

* all answers - /answers
* get/update/delete one answer - /answers/{answer_id}
* vote(+/-) an answer - /answers/{answer_id}/vote

#### Comments

* get all comments of an overflow - /comments/{overflow_id
* add/update/delete comment to an overflow(question/answers) - /comments/{overflow_id}

#### Tags

* all tags - /tags

#### Images

* get an image(not secured) - /image/{image_id}
* upload an image - /auth/../image
