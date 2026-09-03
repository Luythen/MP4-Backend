package com.gtihub.Luythen.MP4_Backend.Websocket;

import java.time.Instant;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameSocket {

    @MessageMapping("/setname")
    @SendTo("/topic/lobby")
    // public PlayerParty playerParty(Playername playername)
    public String setName (@Payload String name) {
        return name;
    }

    // Move player
    @MessageMapping("/move")
    @SendTo("/topic/move")
    // public PlayerModel movePlayer (@Payload String keypressed)
    public void movePlayer (@Payload String keypressed) {

    }

    // Start game
    @MessageMapping("/startgame")
    @SendTo("/topic/startgame")
    public void startGame () {

    }

    // Get question
    @MessageMapping("/get-random-question")
    @SendTo("/topic/random-quesiton")
    public String getRandomQuestion () {
        return "Question";
    }

    // Player answer
    @MessageMapping("/send-player-answer")
    @SendTo("/topic/player-answers")
    public void getPlayerAnswer (@Payload String answer) {
        
    }

    // Countdown timer
    @Scheduled(fixedRate = 100)
    @MessageMapping("/send-time-left")
    @SendTo("/topic/send-timer")
    public Instant timeLeft () {
        return new Date().toInstant();
    }
}
