package com.gtihub.Luythen.MP4_Backend.game;

import java.util.HashMap;
import java.util.Map;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionModel;

public class GameHandler {

    private QuestionModel currentQuestion;
    private QuestionModel formerQuestion;

    private Map<String, String> sessiontoName = new HashMap();
    private Map<String, PlayerInformation> players = new HashMap();

    private final float speed = 15;

    public Map<String, PlayerInformation> addPlayer(String name, String sessionId) throws Exception {
        if (players.containsKey(name)) {
            throw new Exception("Player with that name already exits");
            // return new ResponseStatusException(HttpStatus.CONFLICT)
        }

        PlayerInformation playerInformation = new PlayerInformation();
        playerInformation.setColor("White");
        playerInformation.setScore(0);
        playerInformation.setPosX(500);
        playerInformation.setPosY(350);

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

    public Map<String, PlayerInformation> getPlayers() {
        return players;
    }

    public QuestionModel getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(QuestionModel currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Map<String, PlayerInformation> gameComplete() {
        Map<String, PlayerInformation> gameCompletedInfo = players;
        sessiontoName = new HashMap<>();
        players = new HashMap();

        return gameCompletedInfo;
    }

}
