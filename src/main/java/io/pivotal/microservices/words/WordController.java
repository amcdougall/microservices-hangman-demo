package io.pivotal.microservices.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

/**
 * A RESTFul controller for accessing word information.
 * 
 * @author Andrew McDougall
 */
@RestController
@RequestMapping(path="/api/v1/hangman/")
public class WordController {

	protected Logger logger = Logger.getLogger(WordController.class
			.getName());

	protected WordRepository wordRepository;

	protected ActionRepository actionRepository;
	private HashMap<Integer, String> letterPositions = new HashMap<Integer,String>();

	protected Integer guesses;
	protected String word;

	/**
	 * Create an instance plugging in the respository of
	 * Words.
	 *
	 * @param wordRepository
	 *            An word repository implementation.
	 */
	@Autowired
	public WordController(WordRepository wordRepository, ActionRepository actionRepository) {
		this.wordRepository = wordRepository;
		this.actionRepository = actionRepository;

		logger.info("WordRepository says system has "
				+ wordRepository.countWords() + " words");
	}


	@ApiOperation(value = "word", nickname = "word")
	@RequestMapping(method = RequestMethod.GET, path="/word/all", produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = false, dataType = "string", paramType = "path", defaultValue="")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Word.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public List<Word> loadWord() {

		logger.info("words-service loadWord() invoked: " );
		List<Word> words =	wordRepository.findAll();
		logger.info("words-service loadWord() found: " + words);

		return words;
	}

	@ApiOperation(value = "word", nickname = "word")
	@RequestMapping(method = RequestMethod.GET, path="/word", produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = false, dataType = "string", paramType = "path", defaultValue="")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Word.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public Word getWord() {
		String word = getARandomWord();
		Word newWord = new Word(word);
		logger.info("words-service loadWord() found: " + word);
		return newWord;
	}

	public String getARandomWord() {
		Random r = new Random();
		List<Word> list = wordRepository.findAll();
		Object[] arrayOfObjects = list.toArray();
		Object randomWord = arrayOfObjects[r.nextInt(arrayOfObjects.length)];
		//int count = randomWord.length();
		return randomWord.toString();
	}

	public Action getGame(Game game){
		Action action = new Action(game.getId(),game.getWord());
		//action.setId(game.getId());
		//action.setWord(game.getWord());
		action.setGuesses(game.getGuesses());
		action.setLetters(game.getLetters());

		return action;
	}

	@ApiOperation(value = "new", nickname = "newGame")
	@RequestMapping(method = RequestMethod.GET, path="/game/", produces = "application/json")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = HashMap.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public Token playGame() {
		String word = getARandomWord();//gets random word from db
		Action myAction = new Action(randomUUID(),
				format("Welcome - Begin"), word, actionRepository);
		Token token = new Token(myAction);
		return token;

	}

	@ApiOperation(value = "guess", nickname = "validateGuess")
	@RequestMapping(method = RequestMethod.GET, path="/{id}/guess/{letter}", produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "id", required = false, dataType = "string", paramType = "path", defaultValue=""),
			@ApiImplicitParam(name = "letter", value = "letter", required = false, dataType = "string", paramType = "path", defaultValue="")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = Action.class),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Failure")})
	public Action guess(@PathVariable(value="id") String id, @PathVariable(value="letter") String letter) {
		UUID uuid = UUID.fromString(id);
		Game game = actionRepository.findById(uuid);
		Game rgame = game.validateGuess(letter);

		actionRepository.save(rgame);//Save game to db in order to check it for next guess.
		Action action = new Action();
		if(game.gameOver()) {
			action.setRespMessage("Game Over!");//is set if game is over
		}
		if(game.remaingLetters() == 0){
			action.setRespMessage("You win!");//if no letters remain game is over and you win!
		}
		action.setGuessesRemain(game.remainingGuesses());//number of guesses left
		action.setNumLettersRemain(game.remaingLetters());//number of letters remaining
		action.setId(rgame.getId());
		action.setWord(rgame.getWord());
		action.setGuesses(rgame.getGuesses());
		action.setLetters(rgame.getLetters());
		//
		String[] letterPositionString = rgame.getLetterPlaceholder().split(",");
		//
		for (int i = 0; i < letterPositionString.length; i++)
			letterPositions.put(new Integer(i),letterPositionString[i]);

		action.setLetterPositions(letterPositions);

		return action;
	}

	/**
	 *
	 */
	public class Token {
		private UUID id;
		private HashMap<Integer, String> letterPositions = new HashMap<Integer,String>();

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public HashMap<Integer, String> getLetterPositions() {
			return letterPositions;
		}

		public void setLetterPositions(HashMap<Integer, String> letterPositions) {
			this.letterPositions = letterPositions;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Token token = (Token) o;

			if (id != null ? !id.equals(token.id) : token.id != null) return false;
			return !(letterPositions != null ? !letterPositions.equals(token.letterPositions) : token.letterPositions != null);

		}

		@Override
		public int hashCode() {
			int result = id != null ? id.hashCode() : 0;
			result = 31 * result + (letterPositions != null ? letterPositions.hashCode() : 0);
			return result;
		}

		Token(Action action){

			this.id = action.getId();
			this.letterPositions = action.getLetterPositions();
		}

	}
}
