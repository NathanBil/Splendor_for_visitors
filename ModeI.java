package fr.umlv.game.mode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


import fr.umlv.players.*;
import fr.umlv.objects.*;
import fr.umlv.affichage.*;
import fr.umlv.copie.Copie;
import fr.umlv.saisie.*;
import fr.umlv.game.Partie;

/**
 * Declaration of the type ModeI which is a Splendor mode
 * 
 * @author dylandejesus
 */
public class ModeI implements Mode {
	/**
	 * Number of victory points
	 */
	private static final int VICTORY_POINTS = 15;
	
	/**
	 * Players list on the game
	 */
	private ArrayList<Participant> joueurs;
	
	/**
	 * Pick  of dev cards
	 */
	private HashMap <Integer, List<CarteDev>> pioche;
	
	/**
	 * Pick size
	 */
	private int taille_pioche;
	
	/**
	 * Board of the game
	 */
	private HashMap <Integer, List<CarteDev>> board;
	
	/**
	 * Tokens available
	 */
	private HashMap<String, Integer> jetons_disponibles;	/*On met une Map pour représenter les piles de jetons*/
	
	/**
	 * Nobles cards on the board
	 */
	private ArrayList<Tuile> tuiles_board;
	
	
	/**
	 * Constructor of the type Partie.
	 */
	public ModeI() {
		
		joueurs = new ArrayList<Participant>();
		
		// Pioche des 4 niveaux de développement
		pioche = new HashMap<>();
		pioche.put(1, new ArrayList<>());
		pioche.put(2, new ArrayList<>());
		pioche.put(3, new ArrayList<>());
		
		taille_pioche = 0;
		
		// Board des 4 niveaux de développement
		board = new HashMap<>();
		board.put(1, Arrays.asList(null, null, null, null));
		board.put(2, Arrays.asList(null, null, null, null));
		board.put(3, Arrays.asList(null, null, null, null));
		
		jetons_disponibles = new HashMap<String, Integer>();
		
		tuiles_board = new ArrayList<>();
	}
	
	/**
	 * Returns the players list
	 * 
	 * @return Returns the players list
	 */
	public ArrayList<Participant> joueurs() {
		return this.joueurs;
	}
	
	/**
	 * Returns the board cards
	 * 
	 * @return the list of the cards on the board
	 */
	public HashMap<Integer, List<CarteDev>> board() {
		return this.board;
	}
	
	/**
	 * Returns the tokens
	 * 
	 * @return the token of the game
	 */
	public HashMap<String, Integer> jetons_disponibles() {
		return this.jetons_disponibles;
	}
	
	/**
	 * Returns the nobles on the board
	 * 
	 * @return the nobles of the game
	 */
	public ArrayList<Tuile> tuiles_board() {
		return this.tuiles_board;
	}
	
	/**
	 * Returns the deck
	 * 
	 * @return deck
	 */
	public HashMap <Integer, List<CarteDev>> pioche(){
		return this.pioche;
	}
	
	/**
	 * Returns the deck length
	 * 
	 * @return an int which represents the leght of the deck
	 */
	public int taille_pioche(){
		return this.taille_pioche;
	}
	
	/**
	 * Initialize a game
	 * 
	 * 
	 *       
	 */
	public void initialisePartie() {
		
		this.initialiseOrdreJoueurs();
		this.initialiseJetons();
	
		this.initialisePioche();
		this.piocheFourCards(1);
	}
	
	/**
	 * Gives the number of players possible on a game
	 * 
	 * @return the number of players
	 */
	public int giveNbPlayersPossible() {
		return 2;
	}
	
	/**
	 * Change the order of the players list to make the the younger player start.
	 */
	private void initialiseOrdreJoueurs() {
		
		int i = 0;
		ArrayList<Participant> extrait = new ArrayList<Participant>();
		ArrayList<Participant> base = new ArrayList<Participant>();
		
		int indice_younger = findYounger(this.joueurs);
		
		
		for(var joueur : this.joueurs) {
			
			if(i < indice_younger) {
				extrait.add(joueur);
			}else {
				base.add(joueur);
			}
			
			i++;
		}
		
		for(var joueur_extrait : extrait) {
			base.add(joueur_extrait);
		}
		
		this.joueurs = base;
	}
	/**
	 * Do a deep copy of a game mode.
	 * 
	 * @return the deep copy of the game mode.
	 */
	@Override
	protected Object clone(){
		
		Objects.requireNonNull(this);
		
		Copie copie1 = new Copie();
		ModeI copie2 = new ModeI(); 
		
		/*on permet à la liste d'avoir la bonne taille car sinon la copie échoue*/
		for(var i = 0; i < this.joueurs.size(); i++) {
			copie2.joueurs.add(this.joueurs.get(0));
		}
		
		Collections.copy(copie2.joueurs, this.joueurs);
		copie2.jetons_disponibles = copie1.copieHashmap(this.jetons_disponibles);
		copie2.taille_pioche = this.taille_pioche;
		
		/* cast inévitable, à éviter*/
		copie2.pioche = (HashMap <Integer, List<CarteDev>>) this.pioche.clone();
		/* cast inévitable, à éviter*/
		copie2.board = (HashMap <Integer, List<CarteDev>>) this.board.clone();
		
		for(var i = 0; i < this.tuiles_board.size(); i++) {
			copie2.tuiles_board.add(this.tuiles_board.get(0));
		}
		Collections.copy(copie2.tuiles_board, this.tuiles_board);
		return copie2;	
	}
	
	/**
	 * Make a copy of the type Mode
	 * 
	 * @return a copy of the game mode
	 */
	@Override
	public Mode deepClone(){
		return (ModeI) this.clone();
	}
	

	/**
	 * 
	 * Return the index of the youngest player in the list. Returns the value -1 if there is a problem.
	 * 
	 * @param joueurs
	 *        List of the players
	 *        
	 * @return Index of the the youngest player
	 */
	private static int findYounger(ArrayList<Participant> joueurs) {
		
		Objects.requireNonNull(joueurs);
		
		int i = 0;
		int indice_younger = -1;
		
		Participant younger = joueurs.get(i);
		
		for(var joueur : joueurs) {
			
			if(younger.age() > joueur.age() && younger instanceof Joueur) {
				younger = joueur;
				indice_younger = i;
			}
			
			i++;
		}
		
		return indice_younger;
	}
	
	
	/**
	 * Initialise all the available tokens. There are 7 tokens available by color.
	 */
	private void initialiseJetons() {
		
		this.jetons_disponibles.put("Rouge", 7);	/*On mettra une constante dans la classe pour 7 jetons*/
		this.jetons_disponibles.put("Vert", 7);
		this.jetons_disponibles.put("Bleu", 7);
		this.jetons_disponibles.put("Noir", 7);
		this.jetons_disponibles.put("Blanc", 7);
	}
	
	/**
	 * Initialize the deck made up of cards in a random manner. Each card has one of the following colors
	 * following colors: Red, Green, Black, Blue, White, Yellow, White.
	 */
	private void initialisePioche() {
		
		/*Les cartes de la pioche ne peuvent que possèder ces couleurs*/
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		/*Pour chaque couleur on crée 8 cartes*/
		for(var elem : couleurs) {
			for(int i = 0; i < 8 ;i++) {
				
				var map = new HashMap<String, Integer>();
				map.put(elem, 1);
				
				this.pioche.get(1).add(new CarteDev(0,elem,3,"mine"+ "(" +  elem + ")",map));
				this.taille_pioche += 1;
			}
		}
		
		/*On mélange la pioche*/
		Collections.shuffle(this.pioche.get(1));
	}

	
	
	/**
	 *  This method validate or invalidate a player's buying attempt
	 * 
	 * @param joueur
	 * 		   The player whom we will look at the reserve and the ressources
	 * 
	 * @param carte
	 *        The numbers of the card that the user wants to buy. It allows to identify it.
	 * @param affichage
	 *        this variable is used to show the game
	 *        
	*  @return int
	*  		An int which the value indicates either the operation succeed or failed
	*  
	*  -1 = failure
	*  1 = success
	*  0 = the player wanted to cancel
	 */
	private int validationAchatNonReserve(Joueur joueur, HashMap<Integer, Integer> carte, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(carte);
		
		var niveau = carte.entrySet().stream().findFirst().get().getKey();
		var choosen_card = carte.entrySet().stream().findFirst().get().getValue();
		System.out.println("\n La valeur de niveau est : "+ niveau);
		System.out.println("\n La valeur de choosen_card est : " + choosen_card);
		HashMap<Integer,Partie> res = new HashMap<Integer,Partie>();
		if(choosen_card <= 3 && choosen_card >= 0 && niveau >= 1 && niveau <= 3) {
		
			if(joueur.acheteCarte(this.board().get(niveau).get(choosen_card), this)) {
				affichage.affichageMessageActions("\nVotre carte a été achetée avec succès !\n");
				this.piocheOneCard(niveau, choosen_card);	
				return 1;
			}
			/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte*/
			else {
				affichage.affichageMessageActions("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
				return -1;
			}
		}
		/* cas où la carte n'existe pas, l'utilisateur revient au menu précédent de force car le numéro de carte n'existe pas*/
		affichage.affichageMessageActions("\n Ce numéro de carte n'existe pas !\n");
		return 0;
	}
	/**
	 * This method handles the whole process necessary to enable a card reservation for the user
	 * 
	 * @param joueur
	 *       the player who his concerned by the rservation
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	public int reservationCarte(Participant joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		return -1; 	/*Aucune reservation*/
	}
	
	
	/**
	 *  This method validate or invalidate a player's buying attempt
	 * 
	 * @param joueur
	 * 		   The player whom we will look at the reserve and the ressources
	 * 
	 * @param carte
	 *        The numbers of the card that the user wants to buy. It allows to identify it.
	 *        
	 *  @param affichage
	 *        it is used to display the game
	 *        
	*  @return int
	*  		An int which the value indicates either the operation succeed or failed
	*  
	*  -1 = failure
	*  1 = success
	*  0 = the player wanted to cancel
	 */
	private int validationAchatNonReserve(Participant joueur, HashMap<Integer, Integer> carte, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(carte);
		
		var niveau = carte.entrySet().stream().findFirst().get().getKey();
		var chosen_card = carte.entrySet().stream().findFirst().get().getValue();
		/*System.out.println("\n La valeur de niveau est : "+ niveau);
		System.out.println("\n La valeur de choosen_card est : " + choosen_card);*/
		HashMap<Integer,Partie> res = new HashMap<Integer,Partie>();
		if(chosen_card <= 3 && chosen_card >= 0 && niveau >= 1 && niveau <= 3) {
		
			if(joueur.acheteCarte(this.board().get(niveau).get(chosen_card), this)) {
				affichage.affichageMessageActions("\nVotre carte a été achetée avec succès !\n");
				this.piocheOneCard(niveau, chosen_card);	
				return 1;
			}
			/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte*/
			else {
				affichage.affichageMessageActions("\nVous n'avez pas assez de ressources pour acheter cette carte !\n");
				return -1;
			}
		}
		/* cas où la carte n'existe pas, l'utilisateur revient au menu précédent de force car le numéro de carte n'existe pas*/
		affichage.affichageMessageActions("\n Ce numéro de carte n'existe pas !\n");
		return 0;
	}
	
	/**
	 * This method handles the whole process necessary for a transaction
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 *       
	 * @param affichage
	 *       it is used to show the game state
	 *       
	 * @return an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	public int achatCarte(Participant joueur, Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(joueur);
		
		affichage.showBoard(this);
		
		var carte = joueur.achatCarteNonReservee(1, affichage);
		return validationAchatNonReserve(joueur, carte, affichage);
	}
	
	/**
	 * Removes a set amount of available tokens.
	 * 
	 * @param jeton
	 * 		  Type of tokens to remove
	 * 
	 * @param quantite
	 * 		  Number of tokens to remove
	 * 
	 * @return True if it has been successfully removed or false.		
	 *
	 */
	@Override
	public boolean enleveRessource(Jeton jeton, int quantite) {
		
		Objects.requireNonNull(jeton);
		
		if(quantite < 0) {
			throw new IllegalArgumentException();
		}
		
		int quantite_total = this.jetons_disponibles().get(jeton.couleur()) - quantite;
		
		if(quantite_total < 0) {
			return false;
		}
		
		this.jetons_disponibles().put(jeton.couleur(), quantite_total);
		
		return true;
	}
	
	/**
	 * This method handles the whole process necessary to enable a card reservation for the user
	 * 
	 * @param joueur
	 *       the player who his concerned by the rservation
	 *  @param affichage
	 *  		it is used to show the game
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	public int reservationCarte(Joueur joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		return -1; 	/*Aucune reservation*/
	}
	
	
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * 
	 * @return
	 * the number of player choosen. In this specific mode it must be the maximum allowed by this mode, 2, there is no choice.
	 */
	@Override
	public int choixNbJoueurs(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		return this.giveNbPlayersPossible();	
	}
	
	
	/**
	 * this function handles a noble entrance from the possibility for the player to choose a noble until prestige add.
	 *  In this specific mode there's not noble visit so, the function doesn't do anything here.
	 *  
	 *  @param joueur
	 *  	the player who will receive the noble visit.
	 *  @param affichage
	 *  	it is used to display the game state
	 */
	@Override
	public void nobleVisiting(Participant joueur, Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(joueur);
		
		return;
	}
	
	/**
	 * Makes the end of a turn.
	 * 
	 * @param affichage
	 *        Display used
	 * 
	 * @param player
	 *        Player who plays
	 * 
	 */
	public void endOfTurn(Affichage affichage, Participant player){
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(player);
		
		affichage.showJoueur(player, this);
		Saisie.saisieFinTour(affichage, player);
	}
	
	/**
	 * Check if the card is pickable
	 * 
	 *
	 * @param niveau_carte
	 * 		the card level
	 *  @param num_carte
	 *  	the card number
	 * @return int
	 * 1 : the card is pickable.
	 * 0 : the card isn't pickaable
	 */ 
	public int carteReserveValide2Arg(int niveau_carte, int num_carte) {
		
		if(niveau_carte < 0 || num_carte < 0) {
			throw new IllegalArgumentException();
		}
		
		try {
			this.board().get(num_carte);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	
	}
}