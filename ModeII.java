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
 * Declaration of the type ModeII which is a Splendor mode
 * 
 * @author dylandejesus
 */
public class ModeII implements Mode {
	
	
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
	 * Toeksn available
	 */
	private HashMap<String, Integer> jetons_disponibles;	/*On met une Map pour représenter les piles de jetons*/
	
	/**
	 * Nobles cards on the board
	 */
	private ArrayList<Tuile> tuiles_board;
	
	/**
	 * Returns the deck length
	 * 
	 * @return an int which represents the leght of the deck
	 */
	public int taille_pioche(){
		return this.taille_pioche;
	}
	
	
	/**
	 * Constructor of the type Partie.
	 */
	public ModeII() {
		
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
	 * Initialize a game
	 * 
	 */
	public void initialisePartie() {
		
		this.initialiseOrdreJoueurs();
		this.initialiseJetons();
	
		try{
			this.loadDeck(Path.of("src/fr/umlv/data_files/Cartes_Devs.txt"));
			
		}catch(IOException e) {
	    	System.out.println(e.getMessage());
	    	System.exit(1);
	    
	    }
		
		this.piocheFourCards(1);
		this.piocheFourCards(2);
		this.piocheFourCards(3);
		
		this.initialiseTuiles();
	}
	
	/**
	 * Gives the number of players possible on a game
	 * 
	 * @return the number of players
	 */
	public int giveNbPlayersPossible() {
		return 4;
	}
	
	/**
	 * Load Noble card in a file.
	 * 
	 * 
	 * @param path
	 *        File path
	 *        
	 * @param tuiles
	 *        Noble Cards
	 *        
	 * @throws IOException If there is a problem in the read of the file
	 */
	private void loadTuiles(Path path, ArrayList<Tuile> tuiles) throws IOException {
		
		Objects.requireNonNull(path, "File path given is null");
		Objects.requireNonNull(tuiles);
		
		Tuile carte;
		
		try(var reader = Files.newBufferedReader(path)) {  //On teste et renvoie une erreur si problème
			 String line;
			 while ((line = reader.readLine()) != null) {
				 
				 carte = Tuile.fromText(line);
				 

				 tuiles.add(carte);
				 
			 }
		} // appelle reader.close()
	}
	
	/**
	 * Initialise Noble cards. There are (nb player + 1) nobles on the board
	 */
	private void  initialiseTuiles() {
	
		var all_tuiles = new ArrayList<Tuile>();
		
		try {
			this.loadTuiles(Path.of("src/fr/umlv/data_files/Tuiles.txt"), all_tuiles);
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		for(var i = 0; i < this.joueurs.size() + 1 ;i++) {
			
			Collections.shuffle(all_tuiles);
			
			this.tuiles_board.add(all_tuiles.get(all_tuiles.size() - 1));
			
			all_tuiles.remove(all_tuiles.size() - 1);
		}
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
			/* on change le joueur qui est prioritaire si et seulement si c'est un joueur et pas une IA*/
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
		this.jetons_disponibles.put("Jaune", 5);
		this.jetons_disponibles.put("Blanc", 7);
	}
	
	/**
	 * Load the development card where the informations are in a file. It makes the picks.
	 * 
	 * @param path
	 *        File path
	 *        
	 * @throws IOException
	 * 		exception launched
	 */
	private void loadDeck(Path path) throws IOException {
		
		Objects.requireNonNull(path, "File path given is null");
		
		CarteDev carte;
		
		try(var reader = Files.newBufferedReader(path)) {  //On teste et renvoie une erreur si problème
			 String line;
			 while ((line = reader.readLine()) != null) {
				 
				 carte = CarteDev.fromText(line);
				 

				 this.pioche.get(carte.niveau()).add(carte);
				 
				 this.taille_pioche += 1;
				 
			 }
		} // appelle reader.close()
		
		Collections.shuffle(this.pioche.get(1));
		Collections.shuffle(this.pioche.get(2));
		Collections.shuffle(this.pioche.get(3));
	}
	
	
	/**
	 * Remove the noble chosen 
	 * 
	 * @param noble_chosen
	 *        Noble chosen to remove
	 *  @param affichage
	 *  	show the game
	 */
	private void efface_noble(Tuile noble_chosen, Affichage affichage) {
		
		Objects.requireNonNull(noble_chosen);
		Objects.requireNonNull(affichage);
		
		var iterator = this.tuiles_board.iterator();
		
		while(iterator.hasNext()) {
			
			var tuile1 = iterator.next();
			
			if(tuile1.equals(noble_chosen)) {
				
				affichage.affichageMessageActions("Vous avez choisi : " + tuile1.name() + " ");
				iterator.remove();
			}
				
		}
	}
	
	
	/**
	 *  This method validate or invalidate a player's buying attempt
	 * 
	 * @param joueur
	 *        the player whom we will look at the reserve and the ressources
	 *        
	 * @param carte_numero
	 *        the number of the card that the user wants to buy. It allows to identify it.
	 *  @param affichage
	 *  	it is used to display the game
	 *  @return int
	 *  	an int which the value indicates either the operation succeed or failed
	 *  
	 *  -1 = failure
	 *  1 = success
	 */
	private int validationAchatReserve(Participant joueur, int carte_numero, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		if(carte_numero <= 0) {
			return -1;
		}
		
		//3 lignes
		if(joueur.acheteCarte(joueur.reserve().get(carte_numero-1), this)) {
			
			var carte_delete = joueur.reserve().get(carte_numero-1);
			joueur.reserve().remove(carte_delete);
			affichage.affichageMessageActions("\n Votre achat a été réalisé avec succès ! \n");
			return 1;
			
		}
		
		/* cas où la carte coûte trop cher, l'utilisateur revient au menu précédent de force car il ne peut pas se payer cette carte:  tour_valide = -1;*/
		affichage.affichageMessageActions("Vous n'avez pas assez de ressources pour acheter cette carte !");
		affichage.showPlateau(this, 2);
		return -1;
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
	 *  	it is used to show the game 
	*  @return int
	*  		an int which the value indicates either the operation succeed or failed
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
		var choosen_card = carte.entrySet().stream().findFirst().get().getValue();
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
	
	/** Do a deep copy of a game mode.
	 * 
	 * @return the deep copy of the game mode.
	 */
	/** Do a deep copy of a game mode.
	 * 
	 * @return the deep copy of the game mode.
	 */
	@Override
	protected Object clone(){
		
		Objects.requireNonNull(this);
		Copie copie1 = new Copie();
		ModeII copie2 = new ModeII(); 
		
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
		
		/*on permet à la liste d'avoir la bonne taille car sinon la copie échoue*/
		for(var i = 0; i < this.tuiles_board.size(); i++) {
			copie2.tuiles_board.add(this.tuiles_board.get(0));
		}
		
		Collections.copy(copie2.tuiles_board, this.tuiles_board);
		return copie2;	
	}
	/**
	 * This method do a deep clone of a game mode.
	 * 
	 * @return the deep clone.
	 */
	@Override
	public Mode deepClone(){
		return (ModeII) this.clone();
	}

	
	/**
	 * This method handles the whole process necessary for a transaction
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @param affichage
	 *      it is used to display the game
	 * @return int
	 * 		an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	public int achatCarte(Participant joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		affichage.showBoard(this);
		
		Objects.requireNonNull(this);
		Objects.requireNonNull(joueur);
		
		var choix = joueur.choixAchatCarte(this, affichage);
		
		/*Carte du Board*/
		if(choix == 1) {
			
			var carte = joueur.achatCarteNonReservee(2, affichage);
			return validationAchatNonReserve(joueur, carte, affichage);
		}
		
		/*Carte Reserve*/
		affichage.showReserved(joueur);
		var carte = joueur.achatCarteReservee(affichage);
		return validationAchatReserve(joueur, carte, affichage);
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
		
		var choix = joueur.choixReservation(affichage);
		
		if(choix ==  1) {
			/*var carte = Saisie.reservationCartePlateau(affichage);*/
			var carte = joueur.reservationCartePlateau(affichage, this);
			
			return validationReservationCartePlateau(joueur,carte, affichage);
		}
		
		
		/*var carte = Saisie.reservationCartePioche(affichage);*/
		var carte = joueur.reservationCartePioche(affichage);
		
		return validationReservationCartePioche(joueur,carte, affichage);
	}
	
	/**
	 * This method validate or nullify the action of a card reservation made by a user
	 * 
	 * @param joueur
	 *       the player who his concerned by the reservation
	 *  @param niveau_carte
	 *       the card that interests the user levels
	 *   @param affichage
	 *   		show the game
	 * @return int
	 *	 an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	private int validationReservationCartePioche(Participant joueur, int niveau_carte, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		if(niveau_carte < 0) {
			throw new IllegalArgumentException();
		}
		
		if(this.carteReserveValide1Arg(niveau_carte) != 0){
			joueur.reserveCarte(this.pioche.get(niveau_carte).get(this.pioche.size() - 1), this.jetons_disponibles);
			return 1;
		}
		
		affichage.affichageMessageActions("Erreur les cartes de ce niveau ne sont plus disponibles! Vous recommencez votre tour ");
		return -1;		

	}
	
	/**
	 * This method handles the whole process necessary to make the user obtain ressources
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 *  @param carte
	 *       the card that the players want to reserve
	 *  @param affichage
	 *  	show the game
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	private int validationReservationCartePlateau(Participant joueur, HashMap<Integer, Integer> carte, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(carte);
		Objects.requireNonNull(affichage);
		
		var niveau_carte = carte.entrySet().stream().findFirst().get().getKey();
		var num_carte =  carte.entrySet().stream().findFirst().get().getValue();
		
		if(carteReserveValide2Arg(niveau_carte,num_carte) != 0){
			
			joueur.reserveCarte(this.board.get(niveau_carte).get(num_carte), this.jetons_disponibles);
			this.piocheOneCard(niveau_carte, num_carte);
			return 1;
		}
		
		affichage.affichageMessageActions("Erreur ce numéro de carte n'existe pas, vous recommencez votre tour !");
		return -1;		
	}
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * @return
	 * the number of players choosen. 
	 */
	@Override
	public int choixNbJoueurs(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		return Saisie.choixNbJoueurs(this.giveNbPlayersPossible(), affichage);	
	}
	
	/**
	 * This function handles a noble entrance from the possibility for the player to choose a noble until prestige add.
	 *    
	 */
	@Override
	public void nobleVisiting(Participant joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		var nobles_visiting = new ArrayList<Tuile>();
		if(joueur.isNobleVisiting(nobles_visiting, this.tuiles_board)) {
			
			Tuile noble_chosen;
			
			
			if(nobles_visiting.size() == 1) {
				
				System.out.println("\n.....Un noble vient à votre visite\nIl s'agit de.... " + nobles_visiting.get(0).name() + " !\n");
				noble_chosen = nobles_visiting.get(0);
				
			}else {
				noble_chosen = joueur.choixNoble(this, affichage,nobles_visiting);
			}
			joueur.addPrestige(noble_chosen.points_prestiges());
			this.efface_noble(noble_chosen, affichage);
			System.out.println("Vous avez maitnenant : " + joueur.points_prestiges());
			System.out.println(" points de prestige ! "); 
			Saisie.passer(affichage);
			
			if(this.jetons_disponibles().get("Jaune") > 0) {
				joueur.addRessource(new Jeton("Jaune"), 1);
				this.jetons_disponibles().put("Jaune", jetons_disponibles().get("Jaune")-1);
			}
		}
	
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
	 * Check if the card is pickable
	 * 
	 * @param niveau_carte
	 * 		the card level
	 * @param num_carte
	 * 		the card number
	 * 
	 * @return int
	 * 1 : the card is pickable.
	 * 0 : the card isn't pickaable
	 */ 
	public int carteReserveValide2Arg(int niveau_carte, int num_carte ) {
		
		if(niveau_carte < 0 || num_carte < 0) {
			throw new IllegalArgumentException();
		}
		
		try {
			this.board().get(niveau_carte).get(num_carte);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	
	}

	/**
	 * Check if the card is pickable
	 * 
	 * @param niveau_carte
	 * 		the card level
	 * @return int
	 * 1 : the card is pickable
	 * 0 the card isn't pickable
	 */
	public int carteReserveValide1Arg(int niveau_carte) {
		
		if(niveau_carte < 0) {
			throw new IllegalArgumentException();
		}
	
		try {
			this.pioche().get(niveau_carte).get(this.pioche().size() - 1);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	}
	
	
	/**
	 * Makes the end of a turn.
	 * 
	 * @param affichage
	 *        Display used
	 * 
	 * @param player
	 *        Player who plays
	 */
	public void endOfTurn(Affichage affichage, Participant player) {
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(player);
		
		this.nobleVisiting(player, affichage);
		
		affichage.showJoueur(player, this);
		Saisie.saisieFinTour(affichage, player);
	}
	
}
