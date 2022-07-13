package fr.umlv.game.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.players.*;
import fr.umlv.objects.*;
import fr.umlv.affichage.*;
import fr.umlv.saisie.*;



/**
 * Declaration of the interface Mode which gathers all the differents
 * Splendor game modes
 * 
 * @author dylandejesus
 *
 */
public interface Mode {

	/**
	 * Number of victory points
	 */
	static final int VICTORY_POINTS = 15;
	
	/**
	 * Returns the players list
	 * 
	 * @return Returns the players list
	 */
	public ArrayList<Participant> joueurs();
	
	/**
	 * Returns the board cards
	 * 
	 * @return the list of the cards on the board
	 */
	public HashMap<Integer, List<CarteDev>> board();
	
	/**
	 * Returns the deck
	 * 
	 * @return deck
	 */
	public HashMap <Integer, List<CarteDev>> pioche();
	
	/**
	 * 
	 * Returns the nobles on the board
	 * 
	 * @return the nobles of the game
	 */
	public ArrayList<Tuile> tuiles_board();
	
	/**
	 * 
	 * Returns the tokens
	 * 
	 * @return the token of the game
	 */
	public HashMap<String, Integer> jetons_disponibles();
	
	/**
	 * 
	 * Returns the number of cards in the deck
	 * 
	 * @return the number of cards in the deck
	 */
	public int taille_pioche();
	
	/**
	 * Makes a clone of the game
	 * 
	 * @return game
	 */
	public Mode deepClone();
	
	/**
	 * Initialize the game
	 */
	void initialisePartie();
	
	/**
	 * Makes the card buy
	 * 
	 * @param joueur
	 *         Player who buys
	 *         
	 * @param affiche
	 *        Display used
	 *        
	 * @return an int for if it was possible or not
	 */
	int achatCarte(Participant joueur, Affichage affiche);
	
	/**
	 * Gives the number of players possible on a game
	 * 
	 * @return the number of players
	 */
	int giveNbPlayersPossible();
	
	/**
	 * Makes the end of a Splendor turn
	 * 
	 * @param affichage
	 *        Display used
	 * @param player
	 * 		the player and their information
	 *      
	 */
	void endOfTurn(Affichage affichage, Participant player);
	
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * @param affichage
	 * 		show the game
	 * @return the number of players choosen. 
	 */
	public int choixNbJoueurs(Affichage affichage);
	
	/**
	 * 
	 * @param joueur
	 * 		the player who will receive the visit
	 * @param affichage
	 * 		show the game
	 * this function handles a noble entrance from the possibility for the player to choose a noble until prestige add.
	 *  In this specific mode there's not noble visit so, the function doesn't do anything here.
	 */
	public void nobleVisiting(Participant joueur, Affichage affichage);
	
	/**
	 * Add a player in the game list of players
	 * 
	 * @param  player
	 * 		   Player to add
	 */
	public default void addPlayer(Participant player) {
		
		Objects.requireNonNull(player);
		
		this.joueurs().add(player);
	}
	
	
	/**
	 * Draw a card from the deck and add it to the board. If you choose to draw a card
	 * remove the card from the board and add the top card of the deck in its place
	 * 
	 * @param ligne
	 * 		the level of the card
	 * @param  index_supp
	 * 		   Index of the card it replaces
	 * @return CarteDev
	 * 		the card that has been chosen
	 */
	public default CarteDev piocheOneCard(int ligne, int index_supp) {
		
		if(ligne < 0 || index_supp < 0) {
			throw new IllegalArgumentException();
		}
		
		int derniere_carte = this.pioche().get(ligne).size() - 1;	
		
		/* cas où il n'y a plus de place dans la pioche*/
		if(this.pioche().get(ligne).size() <= 0)
			return null;
		CarteDev card_picked = this.pioche().get(ligne).remove(derniere_carte);

		
		this.board().get(ligne).set(index_supp, card_picked);
		
		return card_picked;
	}
	
	
	/**
	 * 
	 * @param ligne
	 * 		the card level
	 * Draw 4 cards from the deck.
	 */
	default void piocheFourCards(int ligne) {
		
		for(int i = 0; i < 4 ;i++) {
			
			this.piocheOneCard(ligne, i);
		}
	}
	
	
	/**
	 * Find the player with the most prestige points in the player list.
	 * If two players have the same number of prestige points, the one with the
	 * the less development cards. The Method returns the object of type Player.
	 * 
	 * @param  classement
	 * 		   List of players
	 * @return Participant
	 * 		return the winner
	 */
	private static Participant topClassement(ArrayList<Participant> classement) {
		
		Objects.requireNonNull(classement);
		
		Participant best_player = classement.get(0);
		int i = 0;
		
		while(classement.get(i).points_prestiges() == best_player.points_prestiges() && i < classement.size() - 1) {
			
			if(classement.get(i).cartes() < best_player.cartes()) {
				best_player = classement.get(i);
			}
			
			i++;
		}
		
		return best_player;
	}
	
	
	/**
	 * Exchange two values from the list of players. We exchange the two values located
	 * index1' and 'index2'.
	 * 
	 * @param list
	 * 		the players (IA, human) list
	 * @param  index1
	 *         First index
	 * 
	 * @param  index2
	 * 	       Second index
	 */
	private static void swap(ArrayList<Participant> list, int index1, int index2) {
		
		Objects.requireNonNull(list);
		
		Objects.checkIndex(index1, list.size());
		Objects.checkIndex(index2, list.size());
		
		Participant tmp = list.get(index2);

        list.set(index1, list.get(index2));
        list.set(index2, tmp);
	}
	
	/**
	 * Returns true if j1 has less prestige points than j2
	 * 
	 * @param  j1
	 * 		   First player
	 * 
	 * @param  j2
	 *         Second player
	 *  @return boolean
	 *  	it indicates if player one is better than the second
	 *  (true == yes, false == no)
	 */
	private static boolean compareJoueur(Participant j1, Participant j2){
		
		Objects.requireNonNull(j1);
		Objects.requireNonNull(j2);
		
		if(j1.points_prestiges() < j2.points_prestiges()) {
			return true;
		}
        return false;
    }
	

	/**
	 * Returns the index of the player with the least prestige points in the index interval [start, end[.
	 * 
	 * @param  list
	 * 			List of players
	 * 
	 * @param  debut
	 *         Integer representing the start index
	 * 
	 * @param  fin
	 *         Integer representing the end index
	 * @return int
	 * 		the index of the player with the least prestige points
	 */
	private static int indexOfMin(ArrayList <Participant> list, int debut, int fin){

        Objects.requireNonNull(list);

        if(debut < 0){
            throw new IllegalArgumentException("Debut index given under 0 (indexOfMin)");
        }
        if(fin < 0){
            throw new IllegalArgumentException("End index given under 0 (indexOfMin)");
        }

        Participant min = list.get(debut);
        int index = debut;
        int min_index = debut;

        for(var i = debut; i < fin; i++){
            if(compareJoueur(min, list.get(i))){
                min = list.get(i);
                min_index = index;
            }
            index ++;
        }

        return min_index;
    }
	
	/**
	 * Sorts the list of players in ascending order. The comparison between the players is done on
	 * the number of prestige points, with equal prestige points the player with the least
	 * development cards wins.
	 * 
	 * @param  classement
	 * 	 	   List of players
	 * 
	 * @param  size
	 * 		   List size
	 * @return boolean
	 * 			always true except if there's an exception thrown
	 */
	private static boolean sortClassement(ArrayList<Participant> classement, int size) {
		
		Objects.requireNonNull(classement);
		
		if(size < 0) {
			throw new IllegalArgumentException();
		}
		
		for(var i = 0; i < size ; i++){
            swap(classement, i, indexOfMin(classement, i, size));     /*On va jusqu'à array.length car indexOfMin() va dans l'intervalle [debut, fin[*/
        }
		
		return true;
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
	public boolean enleveRessource(Jeton jeton, int quantite);
	
	
	
	
	/**
	 * This method enables to know either a user succeed or not to acquire two ressources
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @return 
	 * an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	private int choixDeuxJetons(Participant joueur){
		
		Objects.requireNonNull(joueur);
		
		System.out.println("Vous avez choisi de prendre deux jetons de la même couleur, veuillez précisez leur couleur ");
		
		int valid_choice = 0;
		
		while(valid_choice != 1) {
			var jeton = joueur.saisieJeton();
			
			//Faire une fonction qui fait simultanément les deux actions
			if(this.jetons_disponibles().get(jeton.couleur()) < 4) {
				
				System.out.println("\n/!\\ Il n'y a pas assez de ressources pour effectuer cette action\n");
				
			}else {
				joueur.addRessource(jeton,2);
				this.enleveRessource(jeton, 2);
				valid_choice += 1;
			}
		}
		return valid_choice;
		
	}
	
	/**
	 * Determine who from the list of players is the winner. The winner
	 * is the one with the most prestige points. If two players
	 * have the same number of points, the player with the least number of cards is
	 * cards.
	 * 
	 * @return Participant
	 * 		the best player of this party
	 */
	public default Participant isWinner() {
		
		ArrayList<Participant> classement = new ArrayList<Participant>();
		int nb_best_players = 0;
		
		
		for(var joueur : this.joueurs()) {
			
			if(joueur.points_prestiges() >= VICTORY_POINTS) {
				classement.add(joueur);
				nb_best_players += 1;
			}
		}
		
		sortClassement(classement, nb_best_players);
		return topClassement(classement);
	}
	
	
	/**
	 * Checks if the game is in a "endgame" state.
	 * 
	 * @param player
	 *        Player
	 *        
	 * @param player_turn
	 *        Number of turn
	 * 
	 * @param game_state
	 *        game state (True = end, False = not the end)
	 * 
	 * @return True if it is the endgame then returns false
	 */
	default boolean isEndgame(Participant player, int player_turn, boolean game_state) {
		
		Objects.requireNonNull(player);
		
		if(player_turn < 0) {
			throw new IllegalArgumentException();
		}
		
		if(player.points_prestiges() >= VICTORY_POINTS && !game_state && player_turn % this.joueurs().size() == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method handles the whole process necessary to enable a card reservation for the user
	 * 
	 * @param joueur
	 *       the player who his concerned by the rservation
	 * @param affichage
	 * 		it is used to show the game
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * -1 = a problem occured the user has to redo his turn
	 */
	public int reservationCarte(Participant joueur, Affichage affichage);
	
	/**
	 * Check if the card is pickable
	 * 
	 * @param niveau_carte
	 * 		the card level
	 * @param num_carte
	 * 		the card number
	 * @return int
	 * 1 : the card is pickable.
	 * 0 : the card isn't pickaable
	 */ 
	public int carteReserveValide2Arg(int niveau_carte, int num_carte ) ;
	
	/**
	 * This method handles the whole process necessary to acquire 3 tokens
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @return 
	 * an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 */
	private int choixTroisJetons(Participant joueur){
		
		Objects.requireNonNull(joueur);
		
		var already_choosen = new ArrayList<Jeton>();
		boolean suite;
		
		Jeton jeton;
		System.out.println("Vous avez choisi de prendre 3 jetons différents, veuillez précisez leur couleur ");
		System.out.println(" (en les séparant par la touche entrée) : \n");
		var list = joueur.TakesThreeTokens(this);
		for(int i= 0; i < 3;i++){
			
			suite = false;
			
			while(!suite) {
				if(list == null) {
					jeton = Saisie.saisieJeton();	 //On accède à la classe Saisie pour la saisie des jetons si c'et un unser
				}
					
				else {
						jeton = new Jeton(list.get(i)); // on récupère le jeton de l'IA dans la liste des jetons valides si c'est une IA 
						System.out.println(""+list.get(i));
					
				}
					
				
				if(!already_choosen.contains(jeton)) {
					
					
					if(!this.enleveRessource(jeton, 1)) {
							
						System.out.println("\n/ ! \\ Pas assez de ressource pour effectuer cette action\n");
						i --;
						break;
					}
						
					joueur.addRessource(jeton,1);
				    already_choosen.add(jeton);
				    suite = true;
					
				}else {
					System.out.println("\n/ ! \\ Couleur déjà choisie\n");
				}
			}
			  
		}
		return 1;
		
	}
	
	/**
	 * This method handles the whole process necessary to make the user obtain ressources
	 * 
	 * @param joueur
	 *       the player who his concerned by the purchase
	 * @param affichage
	 *       this variable is used to show the game state
	 * @return 
	 * an int which represents either everything occured well or not
	 * 
	 * 1 = everything worked
	 * 0 = the player has canceled his choice
	 * -1 = a problem occured the user has to redo his turn
	 */
	public default int priseRessource(Participant joueur, Affichage affichage){
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);

		affichage.showJeton(this.jetons_disponibles(), joueur.ressources(), "JETON");

		var choix = joueur.choixJeton(affichage, this);
		
		/* cas où l'utilisateur veut prendre 2 jetons identiques*/
		if(choix == 1) {
			var res = choixDeuxJetons(joueur);
			if(!joueur.checkNbJetons())
				defausse(joueur, affichage);
			return res;
		}
		
		/* cas où l'utilisateur veut prendre 3 jetons différents*/
		else if(choix == 2) {
			var res = choixTroisJetons(joueur);
			if(!joueur.checkNbJetons())
				defausse(joueur, affichage);
			return res;
		}
		/* cas où l'utilisateur annule son action*/
		affichage.afficheAnnulation(this);
		return 0;
	}
	/**
	 * 
	 * @param joueur
	 * 		the player chosen
	 * @param affichage
	 * 		it display the game
	 */
	public default void defausse(Participant joueur, Affichage affichage){

		while(!joueur.checkNbJetons()) {
					
					affichage.affichageMessageActions("/!\\ Vous possèdez trop de jetons veuillez en supprimer " + (joueur.NbJetons()-10) + "  pour en avoir 10 maximum\n");
					affichage.showJeton(null, joueur.ressources(), null);
					
					
					
					String jeton;
					int quantite;
					do{
						System.out.println("\nJeton : \n ");
	
						jeton = joueur.defausserNameJeton();
						
						System.out.println("Quantite : \n"); 
					
						quantite = joueur.defausseJetonQuantite();
						
						
						
					}while(Saisie.valideDefausse(joueur, jeton, quantite) == false || joueur.NbJetons_loose(quantite) == false);
					
					System.out.println("\n Défaussement réussie ! \n");
					joueur.ressources().put(jeton, joueur.defausseRessource(jeton, quantite, this.jetons_disponibles()));
					
					this.jetons_disponibles().put(jeton, this.jetons_disponibles().get(jeton) + quantite);
		}
	}
	
	/**
	 * This method check if the user as in the worst cast scenario 10 tokens
	 * If they possess more it force him to loss some
	 * 
	 * @param joueur
	 *       the player who his concerned by the checking
	 *  @param affichage
	 *       it shows the game
	 */
	public default void controleJeton(Joueur joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		if(!joueur.checkNbJetons()) {
			
			while(!joueur.checkNbJetons()) {
				
				System.out.println("\n/!\\ Vous possèdez trop de jetons veuillez en supprimer " + (joueur.NbJetons() - 10) + "  pour en avoir 10\n");
				affichage.showJeton(null, joueur.ressources(), null);
				
				String jeton;
				int quantite;
				do{
					System.out.println("\nJeton : ");
					
					jeton = Saisie.saisieJeton_name();
					
					System.out.println("Quantite :"); 
					
					quantite = Saisie.nb_jeton_defausse();
					
				}while(Saisie.valideDefausse(joueur, jeton, quantite) == false || joueur.NbJetons_loose(quantite));
				
				System.out.println("\n Suppression réussie ! \n");
				joueur.ressources().put(jeton, joueur.enleveRessource(jeton, quantite, this.jetons_disponibles()));
				
				this.jetons_disponibles().put(jeton, this.jetons_disponibles().get(jeton) + quantite);
			}
			
		}
	}
	
}
