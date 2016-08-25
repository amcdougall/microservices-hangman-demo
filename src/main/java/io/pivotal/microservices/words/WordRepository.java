package io.pivotal.microservices.words;

import io.pivotal.microservices.words.Word;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository for Account data implemented using Spring Data JPA.
 * 
 * @author Andrew McDougall
 */
public interface WordRepository extends Repository<Word, Long> {
	/**
	 * Find a word .
	 *
	 * @return a word, null otherwise.
	 */
	//public String getARandomWord();

	/**
	 * Find all words
	 *
	 *            Any alphabetic string.
	 * @return The list of words - always non-null, but may be
	 *         empty.
	 */
	List<Word> findAll();

	/**
	 * Fetch the number of words known to the system.
	 * 
	 * @return The number of words.
	 */
	@Query("SELECT count(*) from Word")
	public int countWords();
}
