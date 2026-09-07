package com.gtihub.Luythen.MP4_Backend.Websocket;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;
import com.gtihub.Luythen.MP4_Backend.game.GameService;

@RestController
public class GameSocket {

    private GameService gameService;

    public GameSocket (GameService gameService) {
        this.gameService = gameService;
    }

    @MessageMapping("/setname")
    @SendTo("/topic/lobby")
    // public PlayerParty playerParty(Playername playername)
    public ResponseEntity<?> setName (@Payload String name) {
        try {
            return ResponseEntity.ok(gameService.addPlayer(name));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Move player
    @MessageMapping("/move")
    @SendTo("/topic/move")
    // public PlayerModel movePlayer (@Payload String keypressed)
    public Map<String, PlayerInformation> movePlayer (@Payload String keypressed, @Payload String namn) {
        return gameService.movePlayer(keypressed, namn);
    }

    // Start game
    @MessageMapping("/startgame")
    @SendTo("/topic/startgame")
    public void startGame () {
        gameService.startTime();
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
    public long timeLeft () {
    long timeLeft = gameService.gameLoop();
    if (timeLeft >= 0) {
        return timeLeft;    
    }
        return 0;
    }
}
