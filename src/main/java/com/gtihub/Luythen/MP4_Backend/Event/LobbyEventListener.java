package com.gtihub.Luythen.MP4_Backend.Event;

import java.util.List;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.gtihub.Luythen.MP4_Backend.game.GameService;

@Component 
public class LobbyEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    public LobbyEventListener (SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @EventListener
    public void handleLobbyEvent (SessionSubscribeEvent event) {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> nHeader = (Map<String, List<String>>) event.getMessage().getHeaders().get("nativeHeaders");
        String destination = nHeader.get("destination").getFirst();

        if (destination.startsWith("/topic/lobby")) {
            messagingTemplate.convertAndSend("/topic/lobby", gameService.getPlayers());
        }
    }
}
