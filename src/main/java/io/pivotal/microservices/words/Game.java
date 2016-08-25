package io.pivotal.microservices.words;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by amcdougall on 5/30/16.
 */
@Entity
@Table(name = "T_GAME")
public class Game implements Serializable{

    @Transient
    private HashMap<Integer, String> letterPositions = new HashMap<Integer,String>();

    @Transient
    private ActionRepository actionRepository;
    @Transient
    protected boolean success;
    @Transient
    protected int guessesRemain;
    @Transient
    protected int numLettersRemain;
    public static final int NUM_GUESSES = 6;

    @Id
    protected UUID id;

    @Column(name = "word")
    protected String word;

    @Column(name = "guesses")
    protected Integer guesses;

    @Column(name = "num_letters")
    protected Integer letters;

    @Column(name="LETTERPOSITIONS")
    protected String letterPlaceholder;

    /**
     *
     * @param id
     * @param letters
     * @param guesses
     * @param word
     */
    public Game(UUID id, int letters, int guesses, String word){
        this.id = id;
        this.letters = letters;
        this.guesses = guesses;
        this.word = word;

    }
    //Default constructor for JPA
    public Game() {}

    /**
     * This is how the game takes in the letter guessed and processes it against the selected word.
     *
     * @param letter
     * @return Game
     */
    public Game validateGuess(String letter){
       // guesses++;
        String[] letterPositionString = null;

        int index = this.word.indexOf(letter);
        //Iterates through the positions in the selected word and puts the position into an hashmap.
        for(int position = 0; position < this.word.length(); position++){
            letterPositions.put(position, null);
        }
        if(this.getLetterPlaceholder() != null) {
            letterPositionString = this.getLetterPlaceholder().split(",");
            for (int i = 0; i < letterPositionString.length; i++) {
                if(letterPositionString[i].equals("null")){
                    letterPositions.put(new Integer(i), null);
                }else{
                    letterPositions.put(new Integer(i), letterPositionString[i]);
                }
            }
        }
        if(index > -1){
            this.letterPositions.put(index,letter);
            while(index > -1){
                index = this.word.indexOf(letter, index + 1);
                if(index > -1){
                    this.letterPositions.put(index,letter);
                }
            }
        }else{
            guesses++;//increment the guesses - 6 is the max number.
        }
        this.letterPlaceholder = join(",", letterPositions.values().toArray(new String[letterPositions.size()]));

        return this;
    }

    /**
     * Check to see if the max number of guesses has been reached.
     * @return
     */
    public boolean gameOver(){

        return guesses >= NUM_GUESSES;
    }

    /**
     * Checks to see what the remaining guesses are from the max number.
     * @return int
     */
    public int remainingGuesses(){
        return NUM_GUESSES - guesses;
    }

    /**
     * Calculates the remaining letters in the selected word.
     * @return int of the count of letters left.
     */
    public int remaingLetters(){
        int count  = 0;
        for(int index = 0;  index < letterPositions.size(); index++){
            if(letterPositions.get(index) == null){
                count++;
            }
        }
        return count;
    }

    /**
     * joins the hashmap string to the letterPlaceHolder string.
     * @param join
     * @param strings
     * @return
     */
    public static String join(String join, String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        } else if (strings.length == 1) {
            return strings[0];
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(strings[0]);
            for (int i = 1; i < strings.length; i++) {
                sb.append(join).append(strings[i]);
            }
            return sb.toString();
        }
    }

    /**
     *
     * @param success
     * @param numOfGuesses
     * @param numOfGuessesRemaining
     * @param charPositions
     */
    public Game(boolean success, int numOfGuesses, int numOfGuessesRemaining, HashMap<Integer, String> charPositions){
        this.success = success;
        this.guesses = numOfGuesses;
        this.letterPositions = charPositions;
        this.guessesRemain = numOfGuessesRemaining;
        int numLettersRemain = 0;

        for(int charPosition = 0; charPosition < letterPositions.size(); charPosition++){
            if(letterPositions.isEmpty())//Need to fix
                numLettersRemain++;
        }
        this.numLettersRemain = numLettersRemain;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

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

    public String getLetterPlaceholder() {
        return letterPlaceholder;
    }

    public void setLetterPlaceholder(String letterPlaceholder) {
        this.letterPlaceholder = letterPlaceholder;
    }

}
