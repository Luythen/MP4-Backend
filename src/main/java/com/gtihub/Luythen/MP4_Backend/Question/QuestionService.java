package com.gtihub.Luythen.MP4_Backend.Question;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service 
public class QuestionService {
    private final Random random = new Random();
    private final List<QuestionModel> questions = new ArrayList<>();
    
    private QuestionModel currentQuestion;

    private boolean fastestAnswered;

    public QuestionService() {

        addQuestion("1", "Wich city is located on two continents?", "Geography",
            List.of("Philippines", "Indonesia", "Russia", "Turkey"),
             "Turkey");

        addQuestion("2", "Which city is the smallest in the world?", "Geography",
            List.of("Monaco", "San Marino", "Vatican City", "Liechtenstein"),
             "Vatican City");
             

        addQuestion("3", "How many bones does an adult human body usually have?", "General Knowledge",
            List.of("206", "208", "210", "212"),
             "206");

        addQuestion("4","What is the hardest natural substance on earth?", "General Knowledge",
            List.of("Iron", "Quartz", "Diamond", "Titanium"),
             "Diamond");
             
        addQuestion("5","What year was Google founded?", "Technology",
            List.of("1996", "1997", "1998", "1999"),
             "1998");


        addQuestion("6","Which companty owns GitHub?", "Technology",
            List.of("Google", "Apple", "Microsoft", "Amazon"),
             "Microsoft");     



    }

    private void addQuestion(String id, String question, String category, List<String>options, String correctAnswer) {

        QuestionModel q = new QuestionModel();

        q.setId(id);
        q.setQuestion(question);
        q.setCategory(category);
        q.setOptions(options);
        q.setCorrectAnswer(correctAnswer);

        questions.add(q);
    }

    public QuestionModel getRandomQuestion() {
        int index = random.nextInt(questions.size());

        currentQuestion = questions.get(index);

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
