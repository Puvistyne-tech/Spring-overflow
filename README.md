# J2E project M2 Logiciel


### Prerequisites

* Java 17
* Maven 3.6.3 or higher

### Getting startedg

To get started, clone this repository to your local machine and navigate to the project directory:

<i> https://gitlab.com/Puvistyne-tech/j2e-project-m2-logiciel.git </i>

cd j2e-project-m2-logiciel

Build the project using Maven:

<code> <b> mvn clean install <b/> </code>

Start the application using the following command:

> java -jar target\UGEOverflow-0.0.1-SNAPSHOT.jar

The application will be available at http://localhost:8080.

By default, the application uses an H2 in-memory database.

### About the project

This project aims to create an improved version of the StackOverflow website that improves the way relevant answers are highlighted. On Stack Overflow, answers are sorted by their score, which is simply the difference between their number of up-votes and down-votes. In the version of the site, we enhanced the notion of score by allowing users to give confidence scores to other users.
The site will also display questions to the authenticated user based on the user network of followers.
The user will initially see the questions posted by the users they follow, followed by questions posted by users that are followed by their network, and so on.

Users can an account and sign in to the site. Authenticated users have additional features beyond those available to non-authenticated users, like:

* Change their password
* Create questions
* Delete an answer
* Vote on answers
* Follow a user
* View the list of users they follow and give them a score

## Login

- [ ]  login verifications
- [ ]  username existing in BDD
- [ ]  password not less than 8 characters
- [ ]  verify email format
- [ ]  Error messages corresponding to each entry
- [ ]  language support. EN as default, an option to change to FR
- [ ]  a notification on login page for successful sign up and user already exists

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


## Integrate with your tools

- [ ]  [Set up project integrations](https://gitlab.com/Puvistyne-tech/j2e-project-m2-logiciel/-/settings/integrations)

## Collaborate with your team

- [ ]  [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ]  [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ]  [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ]  [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ]  [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)


## Name

<b> UGE Overflow </b>


## Badges

On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals

Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation

Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.


## Contributing

Members :
* @Puvistyne (Puvistyne RAJASEGAR)
* @redjradj.wahid99 (Wahid redjradj)
* @ouali.fares01 (Fares OUALI)
* @issamgrn1 (Issam GRINI)

Maintainer :

@acarayol





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
