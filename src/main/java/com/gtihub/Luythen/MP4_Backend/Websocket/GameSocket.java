package com.gtihub.Luythen.MP4_Backend.Websocket;

import java.time.Instant;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerAnswer;
import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionModel;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionService;
import com.gtihub.Luythen.MP4_Backend.game.GameService;

@RestController
public class GameSocket {

    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;
    private QuestionService questionService;

    public GameSocket (GameService gameService, SimpMessagingTemplate messagingTemplate, QuestionService questionService) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
        this.questionService = questionService;
    }

    @MessageMapping("/setname")
    // public PlayerParty playerParty(Playername playername)
    public void setName (@Payload String name, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        try {
            messagingTemplate.convertAndSend("/topic/lobby", gameService.addPlayer(name, sessionId));
        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/error."+ sessionId,e.getMessage());
        }
    }

    // Move player
    @MessageMapping("/move")
    // public PlayerModel movePlayer (@Payload String keypressed)
    public void movePlayer (@Payload String keypressed, SimpMessageHeaderAccessor headerAccessor) {
        if (gameService.getPlayers().size() > 0 && gameService.isGameOn()) {
            String sessionId = headerAccessor.getSessionId();
            String name = gameService.getPlayerBySessionId(sessionId);
            messagingTemplate.convertAndSend("/topic/move", gameService.movePlayer(keypressed, name));
        }
    }

    // Start game
    @MessageMapping("/startgame")
    @SendTo("/topic/startgame")
    // Kan skapa en räknare som räknar hur många "startgame" kommit in för att starta timern när alla klienter är "redo"
    // Spelare borde inte kunna starta själva, men startgame kan skickas automatiskt från alla klienter när nästa fråga är uppdaterad
    public void startGame () {
        if (!gameService.isGameOn() && gameService.getPlayers().size() > 0) {
            gameService.startTime();
            for (PlayerInformation playerInformation : gameService.getPlayers().values()) {
                playerInformation.setPosX(500);
                playerInformation.setPosY(350);
            }
            messagingTemplate.convertAndSend("/topic/move", gameService.getPlayers());
        } 
    }

    // Get the current question
    @MessageMapping("/get-current-question")
    @SendTo("/topic/current-question")
    public QuestionModel getCurrentQuestion () {
        return gameService.getCurrentQuestion();
    }

    // Player answer
    @MessageMapping("/send-player-answer")
    public void getPlayerAnswer (@Payload String answer, SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();
        String playerName = gameService.getPlayerBySessionId(sessionId);

        if (playerName != null) {
            questionService.addPlayerAnswer(new PlayerAnswer(playerName, answer, Instant.now()));
        }
    }

    @Scheduled(fixedRate = 6000)
    public void gameOn() {
        messagingTemplate.convertAndSend("/topic/startgame", gameService.isGameOn());
    }

    // Countdown timer
    @Scheduled(fixedRate = 500)
    public void timeLeft () {
        long timeLeft = gameService.gameLoop();
        if (gameService.isGameOn()) {
            messagingTemplate.convertAndSend("/topic/send-timer", timeLeft);
        }
    }
}
