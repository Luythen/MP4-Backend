package com.gtihub.Luythen.MP4_Backend.game;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionModel;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionService;

@Service
public class GameService {
    private Map<String, String> sessiontoName = new HashMap();
    private Map<String, PlayerInformation> players = new HashMap();
    private QuestionModel currentQuestion;
    private Instant timer;
    private boolean gameOn;
    private final float speed = 15;
    private final int seconds = 15;
    private final QuestionService questionService;

    public GameService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public Map<String, PlayerInformation> addPlayer(String name, String sessionId) throws Exception {
        if (players.containsKey(name)) {
            throw new Exception("Player with that name already exits");
            // return new ResponseStatusException(HttpStatus.CONFLICT)
        }

        PlayerInformation playerInformation = new PlayerInformation();
        playerInformation.setColor("White");
        playerInformation.setScore(0);
        playerInformation.setPosX(50);
        playerInformation.setPosY(50);

        players.put(name, playerInformation);
        sessiontoName.put(sessionId, name);

        return players;
    }

    public String getNameBySessionId(String sessionId) {
        return sessiontoName.get(sessionId);
    }
    public Map<String, PlayerInformation> movePlayer(String keyPressed, String name) {
        PlayerInformation playerInformation = players.get(name);

        float posX = playerInformation.getPosX();
        float posY = playerInformation.getPosY();

        switch (keyPressed) {
            case "ArrowUp":
                playerInformation.setPosY(posY -= speed);
                break;
            case "ArrowDown":
                playerInformation.setPosY(posY += speed);
                break;
            case "ArrowRight":
                playerInformation.setPosX(posX += speed);
                break;
            case "ArrowLeft":
                playerInformation.setPosX(posX -= speed);
                break;
            default:
                break;
        }

        return players;
    }

    public Map<String, PlayerInformation> getPlayers () {
        return players;
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
        currentQuestion = questionService.getRandomQuestion();
    }

    public QuestionModel currentQuestion() {
        return currentQuestion;
    }

    public long gameTimer() {
        long timeLeft = Duration.between(Instant.now(), timer).getSeconds();
        if (timeLeft <= 0) {
            gameStop();
            return 0;
        }
        return timeLeft;
    }

    public boolean isGameOn () {
        return gameOn;
    }

    public int addPoint(String name, int points){
        PlayerInformation playerInformation = players.get(name);
        int newScore = playerInformation.getScore() + points;
        playerInformation.setScore(newScore);
        return newScore;
    }

}
