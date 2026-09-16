package com.gtihub.Luythen.MP4_Backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.gtihub.Luythen.MP4_Backend.Player.PlayerInformation;
import com.gtihub.Luythen.MP4_Backend.Question.QuestionService;
import com.gtihub.Luythen.MP4_Backend.game.GameHandler;
import com.gtihub.Luythen.MP4_Backend.game.GameService;

@SpringBootTest
class Mp4BackendApplicationTests {

	private final QuestionService questionService = mock(QuestionService.class);
	private final SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
	private final Environment environment = mock(Environment.class);

	private final GameService gameService = new GameService(questionService, simpMessagingTemplate, environment);
	private final GameHandler gameHandler = new GameHandler();
	
	@Test
	void checkIfPlayerExits () {
		assertDoesNotThrow(() -> {
			gameHandler.addPlayer("Andreas", "1234");
		});

		Exception exception = assertThrows(Exception.class, () -> {
			gameHandler.addPlayer("Andreas", "54321");
		});

		String expectedMessage = "Player with that name already exits";
		String acutalMessage = exception.getMessage();

		assertTrue(acutalMessage.contains(expectedMessage));
	}

	@Test 
	void checkIfPlayerIsSaved() {
		assertDoesNotThrow(() -> {
			gameHandler.addPlayer("Jonas", "1234");
		});

		Map<String, PlayerInformation> players = gameHandler.getPlayers();
		assertEquals(1, players.size());
	}

	@Test
	void checkIfGameStopStops() {
		gameService.startTime();
		assertTrue(gameService.isGameOn());

		gameService.gameStop();
		assertFalse(gameService.isGameOn());
	} 

	@Test 
	void checkIfPlayersAreRemovedAfterFinish() {
		assertDoesNotThrow(() -> {
			gameHandler.addPlayer("Ashour", "1234");
			gameHandler.addPlayer("Matt Murdock", "5678");
		});
		assertEquals(2, gameHandler.getPlayers().size());
		
		gameHandler.gameComplete();
		assertEquals(0, gameHandler.getPlayers().size());
	}

	@Test
	void checkIfPlayerMoveChangesPlayerPosition() {
		assertDoesNotThrow(() -> {
			gameHandler.addPlayer("Matt Smith", "1234");
			gameHandler.addPlayer("Pedro Pascal", "5678");
		});
		
		gameHandler.movePlayer("ArrowUp", "Matt Smith");
		gameHandler.movePlayer("ArrowRight", "Pedro Pascal");

		Map<String, PlayerInformation> players = gameHandler.getPlayers();
		assertEquals(335, players.get("Matt Smith").getPosY());
		assertEquals(515, players.get("Pedro Pascal").getPosX());
	}
}
