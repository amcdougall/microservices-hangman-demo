package io.pivotal.microservices.words;

import io.pivotal.microservices.words.Action;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.UUID;

/**
 * Repository for Account data implemented using Spring Data JPA.
 * 
 * @author Andrew McDougall
 */
@org.springframework.stereotype.Repository
public interface ActionRepository extends Repository<Game, Long> {
	/**
	 * Find a word .
	 *
	 * @return void.
	 */
	public void save(Game action);

	/**
	 * Find a game
	 *
	 *            Any alphabetic string.
	 * @return The game - always non-null, but may be
	 *         empty.
	 */
	//Game find(Game action);
	Game findById(UUID id);

	/**
	 * Fetch the number of games known to the system.
	 * 
	 * @return The number of games.
	 */
	@Query("SELECT count(*) from Game")
	public int countGames();
}
