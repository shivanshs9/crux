CREATE TABLE IF NOT EXISTS test
(
  name VARCHAR(40) NOT NULL,
  createdTime DATE NOT NULL,
  summary VARCHAR(80) NOT NULL,
  description VARCHAR(256) NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  startTime DATE NOT NULL,
  endTime DATE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS business
(
  name VARCHAR(30) NOT NULL UNIQUE,
  logo VARCHAR(256) NULL,
  createdTime DATE NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user
(
  username VARCHAR(30) NOT NULL UNIQUE,
  firstName VARCHAR(30) NOT NULL,
  lastName VARCHAR(30) NULL,
  country CHAR(2) NOT NULL DEFAULT 'IN',
  state VARCHAR(30) NULL,
  city VARCHAR(30) NULL,
  gender ENUM('Male', 'Female') NOT NULL,
  pincode CHAR(6) NULL,
  password VARCHAR(512) NOT NULL,
  address VARCHAR(128) NULL,
  DOB DATE NULL,
  joinedTime DATE NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS education_qualification
(
  degree VARCHAR(128) NOT NULL,
  course VARCHAR(128) NOT NULL,
  institute VARCHAR(256) NOT NULL,
  grade VARCHAR(4) NOT NULL,
  year INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS business_member
(
  joinedTime DATE NOT NULL,
  position ENUM('Owner', 'Employee') NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  businessId INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (businessId) REFERENCES business(id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS participant
(
  registeredTime DATE NOT NULL,
  startTime DATE NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  testId INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS test_tag
(
  tag VARCHAR(30) NOT NULL,
  testId INT NOT NULL,
  PRIMARY KEY (tag, testId),
  FOREIGN KEY (testId) REFERENCES test(id)
);

CREATE TABLE IF NOT EXISTS coding_language
(
  language VARCHAR(30) NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  compiler VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS problem_setter
(
  isCreator INT NOT NULL,
  joinedTime INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  testId INT NOT NULL,
  businessMemberId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (businessMemberId) REFERENCES business_member(id)
);

CREATE TABLE IF NOT EXISTS account
(
  providerType ENUM('EMAIL', 'PHONE') NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  createdTime DATE NOT NULL,
  providerId VARCHAR(512) NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS coding_question
(
  title VARCHAR(56) NOT NULL,
  problemStatement VARCHAR(512) NOT NULL,
  marks INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  testId INT NOT NULL,
  problemSetter INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (problemSetter) REFERENCES problem_setter(id)
);

CREATE TABLE IF NOT EXISTS mcq_question
(
  title VARCHAR(56) NOT NULL,
  description VARCHAR(512) NOT NULL,
  marks INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  testId INT NOT NULL,
  problemSetter INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (problemSetter) REFERENCES problem_setter(id)
);

CREATE TABLE IF NOT EXISTS test_case
(
  input VARCHAR(1024) NOT NULL,
  output VARCHAR(1024) NOT NULL,
  score INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  testId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES coding_question(id)
);

CREATE TABLE IF NOT EXISTS mcq_option
(
  statement VARCHAR(256) NOT NULL,
  isCorrect INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (questionId) REFERENCES mcq_question(id)
);

CREATE TABLE IF NOT EXISTS coding_submission
(
  code VARCHAR(4096) NOT NULL,
  submittedTime DATE NOT NULL,
  status INT NOT NULL,
  score INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  participantId INT NOT NULL,
  codingLanguage INT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (participantId) REFERENCES participant(id),
  FOREIGN KEY (codingLanguage) REFERENCES coding_language(id),
  FOREIGN KEY (questionId) REFERENCES coding_question(id)
);

CREATE TABLE IF NOT EXISTS mcq_submission
(
  submittedTime DATE NOT NULL,
  score INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  participantId INT NOT NULL,
  optionId INT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (participantId) REFERENCES participant(id),
  FOREIGN KEY (optionId) REFERENCES mcq_option(id),
  FOREIGN KEY (questionId) REFERENCES mcq_question(id)
);

CREATE TABLE IF NOT EXISTS coding_question_tag
(
  tag VARCHAR(30) NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (tag, questionId),
  FOREIGN KEY (questionId) REFERENCES coding_question(id)
);

CREATE TABLE IF NOT EXISTS mcq_question_tag
(
  tag VARCHAR(30) NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (tag, questionId),
  FOREIGN KEY (questionId) REFERENCES mcq_question(id)
);

CREATE TABLE IF NOT EXISTS coding_question_allowed_language
(
  codingQuestionId INT NOT NULL,
  allowedLanguageId INT NOT NULL,
  PRIMARY KEY (codingQuestionId, allowedLanguageId),
  FOREIGN KEY (codingQuestionId) REFERENCES coding_question(id),
  FOREIGN KEY (allowedLanguageId) REFERENCES coding_language(id)
);

CREATE TABLE IF NOT EXISTS passed_test_case
(
  testCaseId INT NOT NULL,
  submissionId INT NOT NULL,
  PRIMARY KEY (testCaseId, submissionId),
  FOREIGN KEY (testCaseId) REFERENCES test_case(id),
  FOREIGN KEY (submissionId) REFERENCES coding_submission(id)
);
