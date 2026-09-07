package com.gtihub.Luythen.MP4_Backend.game;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;

@Service
public class GameService {

    private Map<String, PlayerInformation> players = new HashMap();
    private Instant timer;
    private boolean gameOn;
    private final float speed = 15;
    private final int seconds = 15;

    public Map<String, PlayerInformation> addPlayer(String name) throws Exception {
        if (players.containsKey(name)) {
            throw new Exception("Player with that name already exits");
            // return new ResponseStatusException(HttpStatus.CONFLICT)
        }

        PlayerInformation playerInformation = new PlayerInformation();
        playerInformation.setColor("White");
        playerInformation.setScore(0);

        players.put(name, playerInformation);

        return players;
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

     public long gameLoop() {
        if (!gameOn) {
            startTime();
        }
        return gameTimer();
     }
    
    public void startTime() {
        gameOn = true;
        timer = Instant.now().plusSeconds(10);
    }

    public void gameStop() {
        gameOn = false;
    }

    public long gameTimer() {
        long timeLeft = Duration.between(Instant.now(), timer).getSeconds();
        if (timeLeft <= 0) {
            gameStop();
            return 0;
        }
        return timeLeft;
    }

    public int addPoint(String name, int points){
        PlayerInformation playerInformation = players.get(name);
        int newScore = playerInformation.getScore() + points;
        playerInformation.setScore(newScore);
        return newScore;
    }

}
