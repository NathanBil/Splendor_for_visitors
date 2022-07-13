package fr.umlv.affichage;

import java.util.HashMap;
import java.util.Scanner;

import fr.umlv.game.mode.*;
import fr.umlv.players.*;

/**
 * This represents all the types of display possible. There are two possible displays
 * one in the console and the other is a graphic display
 * 
 * @author dylandejesus
 */
public interface Affichage {

	/**
	 * Print the game board of a game.
	 * 
	 * @param game
	 *        Game to print its board
	 * 
	 * @param mode
	 *        Game mode
	 */
	public void showPlateau(Mode game, int mode);
	
	/**
	 * Print the Nobles Cards on the board.
	 * 
	 * @param game
	 *        Game given to show its nobles card on the board
	 */
	public void showTuiles(Mode game);
	
	/** 
     * Print the available tokens. It is represented as a board of tokens and can print a 
     * sentence.
     * 
     * @param banque
     * 		  Bank of tokens on the board
     * 
     * @param ressources
     *        Resources of tokens
     *        
     * @param message
     *        Sentence to print
     */
	public void showJeton(HashMap<String, Integer> banque, HashMap<String, Integer> ressources, String message);
	
	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	public void showBoard(Mode game);
	
	/**
	 * Print the cards reserved by a player.
	 * 
	 * @param joueur
	 * 		  Player given.
	 */
	public void showReserved(Participant joueur);
	
	/**
	 * Print the player informations and other informations of the game to complete player's one.
	 * 
	 * @param joueur
	 * 	      Player given to show its game infomations 
	 * 
	 * @param game
	 *        Game needed to complete informations
	 * @return int
	 * it indicates that the player successfully did its action
	 */
	public int showJoueur(Participant joueur, Mode game);
	
	/**
	 * It shows a message on the display.
	 * 
	 * @param message
	 *        message to print
	 * @param nb_choices
	 * 		ffgeg
	 * 
	 */
	public void affichageMessage(String message, int nb_choices);
	
	/**
	 * It shows the instructions with the message.
	 * 
	 * @param message
	 *        message to print
	 */
	public void affichageMessageInstructionsBox(String message);
	
	/**
	 * It shows the actions with the message.
	 * 
	 * @param message
	 *        message to print
	 */
	public void affichageMessageActions(String message);
	
	/**
	 * It displays the fact of cancelling an action
	 * 
	 * @param game
	 *        Game mode
	 */
	public void afficheAnnulation(Mode game);
	
	/**
	 * Display of the turn switch between two players
	 * 
	 * @param scan
	 *        Scanner to use to recup the new line
	 */
	public void turnChange(Scanner scan);
	
	/**
	 * Returns the number of the instruction
	 * 
	 * @return Index of the Instruction
	 */
	public int recupTouche();
	
	/**
	 * Returns the number of the mode choosen
	 * 
	 * @return Number of the mode (1, 2)
	 */
	public int recupMode();
	
	/**
	 * Returns the number of players
	 * 
	 * @return Number of players(2,4)
	 */
	public int recupParticipants();
	
	/**
	 * Launches the game with the display(Affichage)
	 */
	public void launchAffichage();
	
}
