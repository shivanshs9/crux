CREATE TABLE IF NOT EXISTS test
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  createdTime DATETIME NOT NULL,
  summary VARCHAR(80) NOT NULL,
  description VARCHAR(256) NOT NULL,
  organizer VARCHAR(90) NULL,
  startTime DATETIME NOT NULL,
  endTime DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS business
(
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL UNIQUE,
  hq_country CHAR(2) NOT NULL DEFAULT 'IN',
  hq_state VARCHAR(30) NULL,
  hq_city VARCHAR(30) NULL,
  hq_address VARCHAR(128) NULL,
  category VARCHAR(26) NULL,
  logo VARCHAR(256) NULL,
  createdTime DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user
(
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(30) NOT NULL UNIQUE,
  firstName VARCHAR(30) NOT NULL,
  lastName VARCHAR(30) NULL,
  country CHAR(2) NOT NULL DEFAULT 'IN',
  state VARCHAR(30) NULL,
  city VARCHAR(30) NULL,
  gender ENUM('Male', 'Female') NOT NULL,
  password VARCHAR(512) NOT NULL,
  address VARCHAR(128) NULL,
  DOB DATE NULL,
  joinedTime DATETIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS education_qualification
(
  id INT NOT NULL AUTO_INCREMENT,
  degree VARCHAR(128) NOT NULL,
  course VARCHAR(128) NOT NULL,
  institute VARCHAR(256) NOT NULL,
  grade VARCHAR(4) NOT NULL,
  year INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS business_member
(
  id INT NOT NULL AUTO_INCREMENT,
  joinedTime DATETIME NOT NULL,
  position ENUM('Owner', 'Employee') NOT NULL,
  businessId INT NOT NULL,
  userId INT NOT NULL UNIQUE,
  PRIMARY KEY (id),
  FOREIGN KEY (businessId) REFERENCES business(id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS participant
(
  id INT NOT NULL AUTO_INCREMENT,
  registeredTime DATETIME NOT NULL,
  startTime DATETIME NOT NULL,
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
  id INT NOT NULL AUTO_INCREMENT,
  language VARCHAR(30) NOT NULL,
  compiler VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS problem_setter
(
  id INT NOT NULL AUTO_INCREMENT,
  isCreator TINYINT NOT NULL,
  joinedTime DATETIME NOT NULL,
  testId INT NOT NULL,
  businessMemberId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (businessMemberId) REFERENCES business_member(id)
);

CREATE TABLE IF NOT EXISTS account
(
  id INT NOT NULL AUTO_INCREMENT,
  providerType ENUM('EMAIL', 'PHONE') NOT NULL,
  createdTime DATETIME NOT NULL,
  providerId VARCHAR(512) NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS coding_question
(
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(56) NOT NULL,
  problemStatement VARCHAR(512) NOT NULL,
  marks INT NOT NULL,
  testId INT NOT NULL,
  problemSetterId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (problemSetterId) REFERENCES problem_setter(id)
);

CREATE TABLE IF NOT EXISTS mcq_question
(
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(56) NOT NULL,
  description VARCHAR(512) NOT NULL,
  marks INT NOT NULL,
  testId INT NOT NULL,
  problemSetter INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES test(id),
  FOREIGN KEY (problemSetter) REFERENCES problem_setter(id)
);

CREATE TABLE IF NOT EXISTS test_case
(
  id INT NOT NULL AUTO_INCREMENT,
  input VARCHAR(1024) NOT NULL,
  output VARCHAR(1024) NOT NULL,
  score INT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (questionId) REFERENCES coding_question(id)
);

CREATE TABLE IF NOT EXISTS mcq_option
(
  id INT NOT NULL AUTO_INCREMENT,
  statement VARCHAR(256) NOT NULL,
  isCorrect TINYINT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (questionId) REFERENCES mcq_question(id)
);

CREATE TABLE IF NOT EXISTS coding_submission
(
  id INT NOT NULL AUTO_INCREMENT,
  code VARCHAR(4096) NOT NULL,
  submittedTime DATETIME NOT NULL,
  status ENUM('pro', 'ac', 'tle', 'ce', 're', 'wa', 'mle') NOT NULL,
  score INT NOT NULL,
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
  id INT NOT NULL AUTO_INCREMENT,
  submittedTime DATETIME NOT NULL,
  score INT NOT NULL,
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

# SET FOREIGN_KEY_CHECKS = 1;

CREATE VIEW test_with_registrations AS (
    SELECT t.*, COUNT(pt.id) registrationCount
    FROM test t
             LEFT OUTER JOIN participant pt ON t.id = pt.testId
    GROUP BY t.id
);