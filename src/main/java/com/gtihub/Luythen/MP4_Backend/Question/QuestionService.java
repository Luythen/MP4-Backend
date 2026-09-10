package com.gtihub.Luythen.MP4_Backend.Question;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service 
public class QuestionService {
    private final Random random = new Random();
    private final MongoOperations mongoOperations;
    private final long questionCount;
    private QuestionModel currentQuestion;
    private boolean fastestAnswered;

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

          // Ny fråga = ingen har svarat snabbast ännu
        fastestAnswered = false;
        return currentQuestion;
    }

    public int calculatePoints(String answer) {

        // Ingen svarade
        if (answer == null || answer.isBlank()) {
            return -2;
        }

        // När en spelare svarar fel
        if (!currentQuestion.getCorrectAnswer().equals(answer)) {
            return -1;
        }

        // När en spelare svarar rätt och först
        if (!fastestAnswered) {
            fastestAnswered = true;
            return 2;
        }
        return 1; // När en spelare svarar rätt men inte först

    }
}
