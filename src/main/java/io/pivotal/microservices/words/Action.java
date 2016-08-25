package io.pivotal.microservices.words;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * This is my json serialized class.
 *
 * @author Andrew McDougall
 */

public class Action {

	public static Long nextId = 0L;
	protected boolean success;

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The response message.", required = true)
	protected int guessesRemain;

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The response message.", required = true)
	protected int numLettersRemain;


	@JsonIgnore
	public ActionRepository actionRepository;

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The letter position.", required = true)
	private HashMap<Integer, String> letterPositions = new HashMap<Integer,String>();

	private HashMap<Integer, String> positions = new HashMap<Integer,String>();

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The response message.", required = true)
	private String respMessage = "Continue - next guess.";

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The unique identifier for the game.", required = true)
	protected UUID id;
	@JsonIgnore
	protected String word;

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The number of letters guessed.", required = true)
	protected Integer guesses;

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "The number of letters in the word to be guessed.", required = true)
	protected Integer letters;

	public int getGuessesRemain() {
		return guessesRemain;
	}

	public void setGuessesRemain(int guessesRemain) {
		this.guessesRemain = guessesRemain;
	}

	public int getNumLettersRemain() {
		return numLettersRemain;
	}

	public void setNumLettersRemain(int numLettersRemain) {
		this.numLettersRemain = numLettersRemain;
	}

	@Autowired
	public Action(UUID id, String begin, String word, ActionRepository actionRepository) {
		this.id = id;

		this.letters = word.length();
		this.guesses = 0;
		this.word = word;

		for(int position = 0; position < this.word.length(); position++){
			this.letterPositions.put(position, null);
		}
		//saves the game
		actionRepository.save(new Game(this.id, this.letters, this.guesses, this.word));
	}


	public boolean validate(String word){
		if(this.word.equalsIgnoreCase(word)){
			return true;
		}
		return false;
	}

	public HashMap<Integer, String> getLetterPositions() {
		return letterPositions;
	}

	public void setLetterPositions(HashMap<Integer, String> letterPositions) {
		this.letterPositions = letterPositions;
	}


	/**
	 * Default constructor for JPA only.
	 */
	public Action() {}

	public Action(UUID id, String word) {
		this.id = id;
		this.word = word;
		this.letters = word.length();
		this.guesses = 0;
	}

	public UUID getId() {
		return id;
	}

	/**
	 * Set JPA id - for testing and JPA only. Not intended for normal use.
	 * 
	 * @param id
	 *            The new id.
	 */
	protected void setId(UUID id) {
		this.id = id;
	}

	public String getWord() { return word; }

	public void setWord(String word) { this.word = word; }

	public Integer getGuesses() {
		return guesses;
	}

	public void setGuesses(Integer guesses) {
		this.guesses = guesses;
	}

	public Integer getLetters() {
		return letters;
	}

	public void setLetters(Integer letters) {
		this.letters = letters;
	}

	public String getRespMessage() {
		return respMessage;
	}

	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}


	@Override
	public String toString() {
		return id + " " +word + " " + guesses;
	}


}
