CREATE TABLE IF NOT EXISTS Test
(
  name VARCHAR(40) NOT NULL,
  createdTime DATE NOT NULL,
  summary VARCHAR(80) NOT NULL,
  description VARCHAR(256) NOT NULL,
  id INT NOT NULL,
  startTime DATE NOT NULL,
  endTime DATE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Business
(
  name INT NOT NULL,
  logo VARCHAR(256) NOT NULL,
  joinedTime DATE NOT NULL,
  id INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS User
(
  firstName VARCHAR(30) NOT NULL,
  lastName VARCHAR(30) NULL,
  country CHAR(2) NOT NULL DEFAULT 'IN',
  state VARCHAR(30) NULL,
  city VARCHAR(30) NULL,
  pincode CHAR(6) NULL,
  password VARCHAR(512) NOT NULL,
  address VARCHAR(128) NOT NULL,
  DOB DATE NOT NULL,
  joinedTime DATE NOT NULL,
  ID INT NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS EducationQualification
(
  degree VARCHAR(128) NOT NULL,
  course VARCHAR(128) NOT NULL,
  institute VARCHAR(256) NOT NULL,
  grade VARCHAR(4) NOT NULL,
  year INT NOT NULL,
  id INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userId) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS BusinessMember
(
  joinedTime DATE NOT NULL,
  position VARCHAR(30) NOT NULL,
  id INT NOT NULL,
  businessId INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (businessId) REFERENCES Business(id),
  FOREIGN KEY (userId) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS Participant
(
  registeredTime DATE NOT NULL,
  startTime DATE NOT NULL,
  id INT NOT NULL,
  testId INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES Test(id),
  FOREIGN KEY (userId) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS TestTag
(
  tag VARCHAR(30) NOT NULL,
  testId INT NOT NULL,
  PRIMARY KEY (tag, testId),
  FOREIGN KEY (testId) REFERENCES Test(id)
);

CREATE TABLE IF NOT EXISTS CodingLanguage
(
  language VARCHAR(30) NOT NULL,
  id INT NOT NULL,
  compiler VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ProblemSetter
(
  isCreator INT NOT NULL,
  joinedTime INT NOT NULL,
  id INT NOT NULL,
  testId INT NOT NULL,
  businessMemberId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES Test(id),
  FOREIGN KEY (businessMemberId) REFERENCES BusinessMember(id)
);

CREATE TABLE IF NOT EXISTS Account
(
  providerType INT NOT NULL,
  id INT NOT NULL,
  createdTime DATE NOT NULL,
  providerId VARCHAR(512) NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (userId) REFERENCES User(ID)
);

CREATE TABLE IF NOT EXISTS CodingQuestion
(
  title VARCHAR(56) NOT NULL,
  problemStatement VARCHAR(512) NOT NULL,
  marks INT NOT NULL,
  id INT NOT NULL,
  testId INT NOT NULL,
  problemSetter INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES Test(id),
  FOREIGN KEY (problemSetter) REFERENCES ProblemSetter(id)
);

CREATE TABLE IF NOT EXISTS MCQQuestion
(
  title VARCHAR(56) NOT NULL,
  description VARCHAR(512) NOT NULL,
  marks INT NOT NULL,
  id INT NOT NULL,
  testId INT NOT NULL,
  problemSetter INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES Test(id),
  FOREIGN KEY (problemSetter) REFERENCES ProblemSetter(id)
);

CREATE TABLE IF NOT EXISTS TestCase
(
  input VARCHAR(1024) NOT NULL,
  output VARCHAR(1024) NOT NULL,
  score INT NOT NULL,
  id INT NOT NULL,
  testId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (testId) REFERENCES CodingQuestion(id)
);

CREATE TABLE IF NOT EXISTS Option
(
  statement VARCHAR(256) NOT NULL,
  isCorrect INT NOT NULL,
  id INT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (questionId) REFERENCES MCQQuestion(id)
);

CREATE TABLE IF NOT EXISTS CodingSubmission
(
  code VARCHAR(4096) NOT NULL,
  submittedTime DATE NOT NULL,
  status INT NOT NULL,
  score INT NOT NULL,
  id INT NOT NULL,
  participantId INT NOT NULL,
  codingLanguage INT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (participantId) REFERENCES Participant(id),
  FOREIGN KEY (codingLanguage) REFERENCES CodingLanguage(id),
  FOREIGN KEY (questionId) REFERENCES CodingQuestion(id)
);

CREATE TABLE IF NOT EXISTS MCQSubmission
(
  submittedTime DATE NOT NULL,
  score INT NOT NULL,
  id INT NOT NULL,
  participantId INT NOT NULL,
  optionId INT NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (participantId) REFERENCES Participant(id),
  FOREIGN KEY (optionId) REFERENCES Option(id),
  FOREIGN KEY (questionId) REFERENCES MCQQuestion(id)
);

CREATE TABLE IF NOT EXISTS CodingQuestionTag
(
  tag VARCHAR(30) NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (tag, questionId),
  FOREIGN KEY (questionId) REFERENCES CodingQuestion(id)
);

CREATE TABLE IF NOT EXISTS MCQQuestionTags
(
  tag VARCHAR(30) NOT NULL,
  questionId INT NOT NULL,
  PRIMARY KEY (tag, questionId),
  FOREIGN KEY (questionId) REFERENCES MCQQuestion(id)
);

CREATE TABLE IF NOT EXISTS CodingQuestion_AllowedLanguage
(
  codingQuestionId INT NOT NULL,
  allowedLanguageId INT NOT NULL,
  PRIMARY KEY (codingQuestionId, allowedLanguageId),
  FOREIGN KEY (codingQuestionId) REFERENCES CodingQuestion(id),
  FOREIGN KEY (allowedLanguageId) REFERENCES CodingLanguage(id)
);

CREATE TABLE IF NOT EXISTS PassedTestCase
(
  testCaseId INT NOT NULL,
  submissionId INT NOT NULL,
  PRIMARY KEY (testCaseId, submissionId),
  FOREIGN KEY (testCaseId) REFERENCES TestCase(id),
  FOREIGN KEY (submissionId) REFERENCES CodingSubmission(id)
);
