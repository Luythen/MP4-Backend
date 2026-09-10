package com.gtihub.Luythen.MP4_Backend.game;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionModel;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionService;

@Service
public class GameService {

    
    private Instant timer;
    private boolean gameOn;
    private final int seconds = 15;
    private final QuestionService questionService;

    private final GameHandler gameHandler = new GameHandler();

    public GameService(QuestionService questionService) {
        gameHandler.setCurrentQuestion(questionService.getRandomQuestion());
        this.questionService = questionService;
    }

    public Map<?, ?> addPlayer(String name, String sessionId){
        try {
            return gameHandler.addPlayer(name, sessionId);
        } catch (Exception e) {
            return new HashMap();
        }
    }

    public Map<String, PlayerInformation> movePlayer (String keyPressed, String name) {
        return gameHandler.movePlayer(keyPressed, name);
    }

    public Map<String, PlayerInformation> getPlayers() {
        return gameHandler.getPlayers();
    }

    public String getPlayerBySessionId(String name) {
        return gameHandler.getNameBySessionId(name);
    }

    public QuestionModel getCurrentQuestion() {
        return gameHandler.getCurrentQuestion();
    }

    public long gameLoop() {
        if (gameOn) {
            return gameTimer();
        }

        return 0;
    }

    public void startTime() {
        gameOn = true;
        timer = Instant.now().plusSeconds(10);
    }

    public void gameStop() {
        gameOn = false;
        gameHandler.setCurrentQuestion(questionService.getRandomQuestion());
    }

    public long gameTimer() {
        long timeLeft = Duration.between(Instant.now(), timer).getSeconds();
        if (timeLeft <= 0) {
            gameStop();
            return 0;
        }
        return timeLeft;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public int addPoint(String name, int points) {
        PlayerInformation playerInformation = gameHandler.getPlayers().get(name);
        int newScore = playerInformation.getScore() + points;
        playerInformation.setScore(newScore);
        return newScore;
    }

}
