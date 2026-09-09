package com.gtihub.Luythen.MP4_Backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.gtihub.Luythen.MP4_Backend.game.GameService;

@SpringBootTest
class Mp4BackendApplicationTests {

	private GameService gameService = new GameService();

	@Test
	void checkIfPlayerExits () {
		assertDoesNotThrow(() -> {
			gameService.addPlayer("Andreas", "1234");
		});

		Exception exception = assertThrows(Exception.class, () -> {
			gameService.addPlayer("Andreas", "54321");
		});

		String expectedMessage = "Player with that name already exits";
		String acutalMessage = exception.getMessage();

		assertTrue(acutalMessage.contains(expectedMessage));
	}
}
