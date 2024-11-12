USE exam_ninja;

INSERT INTO questions (question, option1, option2, option3, option4, correct_answer, answer_description, category, level, test_id, question_type) VALUES
('What is Java?', 'A language', 'A platform', 'A coffee', 'A car', 'A language', 'Java is a programming language.', 'Programming', 'Easy', 3, 'MCQ'),
('What is OOP?', 'Object-Oriented', 'Procedure', 'Functional', 'Sequential', 'Object-Oriented', 'OOP stands for Object-Oriented Programming.', 'Programming', 'Easy', 3, 'MCQ'),
('What does HTML stand for?', 'HyperText', 'HighText', 'HyperTool', 'HyperMedia', 'HyperText', 'HTML stands for HyperText Markup Language.', 'Web Development', 'Easy', 3, 'MCQ'),
('What is polymorphism in Java?', 'Method overloading', 'Inheritance', 'Multiple forms', 'Abstract', 'Multiple forms', 'Polymorphism allows methods to perform differently.', 'Programming', 'Medium', 3, 'MCQ'),
('Explain encapsulation.', 'Data hiding', 'Function grouping', 'Code separation', 'File packaging', 'Data hiding', 'Encapsulation is about data hiding.', 'Programming', 'Medium', 3, 'MCQ');

INSERT INTO questions (question, option1, option2, option3, option4, correct_answer, answer_description, category, level, test_id, question_type) VALUES
('What is Java?', 'A language', 'A platform', 'A coffee', 'A car', 'A language', 'Java is a programming language.', 'Programming', 'Easy', 2, 'MCQ'),
('What is OOP?', 'Object-Oriented', 'Procedure', 'Functional', 'Sequential', 'Object-Oriented', 'OOP stands for Object-Oriented Programming.', 'Programming', 'Easy', 2, 'MCQ'),
('What does HTML stand for?', 'HyperText', 'HighText', 'HyperTool', 'HyperMedia', 'HyperText', 'HTML stands for HyperText Markup Language.', 'Web Development', 'Easy', 2, 'MCQ'),
('What is polymorphism in Java?', 'Method overloading', 'Inheritance', 'Multiple forms', 'Abstract', 'Multiple forms', 'Polymorphism allows methods to perform differently.', 'Programming', 'Medium', 2, 'MCQ'),
('Explain encapsulation.', 'Data hiding', 'Function grouping', 'Code separation', 'File packaging', 'Data hiding', 'Encapsulation is about data hiding.', 'Programming', 'Medium', 2, 'MCQ');

INSERT INTO questions (question, option1, option2, option3, option4, correct_answer, answer_description, category, level, test_id, question_type) VALUES
('What is Java?', 'A language', 'A platform', 'A coffee', 'A car', 'A language', 'Java is a programming language.', 'Programming', 'Easy', 1, 'MCQ'),
('What is OOP?', 'Object-Oriented', 'Procedure', 'Functional', 'Sequential', 'Object-Oriented', 'OOP stands for Object-Oriented Programming.', 'Programming', 'Easy', 1, 'MCQ'),
('What does HTML stand for?', 'HyperText', 'HighText', 'HyperTool', 'HyperMedia', 'HyperText', 'HTML stands for HyperText Markup Language.', 'Web Development', 'Easy', 1, 'MCQ'),
('What is polymorphism in Java?', 'Method overloading', 'Inheritance', 'Multiple forms', 'Abstract', 'Multiple forms', 'Polymorphism allows methods to perform differently.', 'Programming', 'Medium', 1, 'MCQ'),
('Explain encapsulation.', 'Data hiding', 'Function grouping', 'Code separation', 'File packaging', 'Data hiding', 'Encapsulation is about data hiding.', 'Programming', 'Medium', 1, 'MCQ');

-- Insert test names (half)
INSERT INTO test (test_name) VALUES
('Test 1'),
('Test 2'),
('Test 3');
