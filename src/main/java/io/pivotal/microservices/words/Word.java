package io.pivotal.microservices.words;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Persistent account entity with JPA markup. Accounts are stored in an H2
 * relational database.
 * 
 * @author Andrew McDougall
 */
@Entity
@Table(name = "T_WORD")
public class Word implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 0L;

	@Id
	//@JsonProperty(required = true)
	//@ApiModelProperty(notes = "The the random word for the game.", required = true)
	protected Long id;

	@Column(name = "word")
	protected String word;


	/**
	 * This is a very simple, and non-scalable solution to generating unique
	 * ids. Not recommended for a real application. Consider using the
	 * <tt>@GeneratedValue</tt> annotation and a sequence to generate ids.
	 *
	 * @return The next available id.
	 */
	protected static Long getNextId() {

		synchronized (nextId) {
			return nextId++;
		}
	}

	/**
	 * Default constructor for JPA only.
	 */
	protected Word() {

	}

	public Word(String word) {
		id = getNextId();
		this.word = word;
	}

	public long getId() {
		return id;
	}

	/**
	 * Set JPA id - for testing and JPA only. Not intended for normal use.
	 * 
	 * @param id
	 *            The new id.
	 */
	protected void setId(long id) {
		this.id = id;
	}

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The the random word for the game.", required = true)
	public String getWord() { return word; }

	public void setWord(String word) { this.word = word; }

	public void addWord(String word) { this.word = word; }


	@Override
	public String toString() {
		return word;
	}

}
