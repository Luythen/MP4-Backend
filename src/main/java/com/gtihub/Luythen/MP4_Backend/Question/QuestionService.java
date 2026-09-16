package com.gtihub.Luythen.MP4_Backend.Question;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtihub.Luythen.MP4_Backend.Player.PlayerAnswer;
import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;

@Service 
public class QuestionService {
    private final Random random = new Random();
    private final MongoOperations mongoOperations;
    private final long questionCount;
    private QuestionModel currentQuestion;

    private List<PlayerAnswer> answers = new ArrayList<>();

    public QuestionService(MongoOperations mongoOperations) throws IOException {
        this.mongoOperations = mongoOperations;
        // To ease during development, this will reload the questions.json into the database on startup
        addQuestions();
        this.questionCount = questionCount();
    }

    // dropcollection removes all the previous questions from the database
    // Inputstream loads the questions.json and objectmapper maps it to the QuestionModel
    private void addQuestions() throws IOException {
        mongoOperations.dropCollection(QuestionModel.class);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InputStream questions = new ClassPathResource("questions.json")
            .getInputStream();
            
            List<QuestionModel> mongoQuestions = objectMapper
            .readValue(questions, objectMapper.getTypeFactory()
            .constructCollectionType(List.class, QuestionModel.class));

            mongoOperations.insert(mongoQuestions, QuestionModel.class);
            System.out.println("QUESTIONS INSERTED: \n" + mongoQuestions.size());

        } catch (Exception e) {
            System.out.println("Error: \n" + e);
        }

    }

    private long questionCount() {
        return mongoOperations.count(new Query(), QuestionModel.class);
    }

    public QuestionModel getRandomQuestion() {
        long randomQuestion = random.nextLong(questionCount) + 1;
        currentQuestion = mongoOperations.findById(randomQuestion, QuestionModel.class);
        return currentQuestion;
    }

    public void addPlayerAnswer (PlayerAnswer playerAnswer) {
        synchronized (this) {
            answers.removeIf(ap -> ap.getName().equals(playerAnswer.getName()));
            answers.add(playerAnswer);
        }
    }

    public synchronized void calculateAllPlayerPoints (Map<String, PlayerInformation> players) throws Exception {
        List<PlayerAnswer> correctPlayerAnswers = answers.stream().filter(a -> currentQuestion.getCorrectAnswer().equals(a.getAnswer())).collect(Collectors.toList());
        PlayerAnswer fastestAnswer = null;

        if (!correctPlayerAnswers.isEmpty()) fastestAnswer = correctPlayerAnswers.stream().min(Comparator.comparing(PlayerAnswer::getTime)).orElseThrow();

        for (PlayerAnswer playerAnswer : answers) {
            PlayerInformation playerInformation = players.get(playerAnswer.getName());
            int score = playerInformation.getScore();

            if (correctPlayerAnswers.stream().anyMatch(p -> p.getName().equals(playerAnswer.getName()))) {
                playerInformation.setScore(score + (fastestAnswer.getName().equals(playerAnswer.getName()) ? 2 : 1));
            } else {
                playerInformation.setScore(score - (!playerAnswer.getAnswer().equals("blank") ? 1 : 2));
            }
        }
        
        answers.clear();
    }
}
