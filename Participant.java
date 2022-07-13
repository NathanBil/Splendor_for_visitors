package fr.umlv.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.objects.*;
import fr.umlv.saisie.*;
import fr.umlv.affichage.Affichage;
import fr.umlv.game.mode.Mode;
import fr.umlv.game.mode.ModeII;

/**
 * Declaration of the type Participant. It is a generic type for the different players.
 * 
 * @author dylandejesus nathanbilingi
 */
public interface Participant {
	
	/**
	 * Cards field getter.
	 * 
	 * @return int
	 * 		 Cards field value
	 */
	default int cartes() {
		return this.cartes();
	}
	
	/**
	 * Pseudo field getter.
	 * 
	 * @return Pseudo field value
	 */
	default String pseudo() {
		return this.pseudo();
	}
	
	/**
	 * Age field getter.
	 * 
	 * @return Age field value
	 */
	default int age() {
		return this.age();
	}
	
	/**
	 * Points_prestiges field getter.
	 * 
	 * @return Points_prestiges field value
	 */
	default int points_prestiges() {
		return this.points_prestiges();
	}
	
	/**
	 * Ressources field getter.
	 * 
	 * @return Ressources field value
	 */
	default HashMap<String, Integer> ressources() {
		return this.ressources();
	}
	
	/**
	 * Bonus field getter.
	 * 
	 * @return Bonus field value
	 */
	default HashMap<String, Integer> bonus() {
		return this.bonus();
	}
	
	/**
	 * Reserve field getter.
	 * 
	 * @return Reserve field value
	 */
	default ArrayList<CarteDev> reserve(){
		return this.reserve();
	}
	
	/**
	 * Initialize all the coloured tokens bonus (earned by the player during the game). All the tokens start with the value 0. 
	 */
	private void initBonus() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		for(var elem : couleurs) {
			this.bonus().put(elem, 0);
		}	
	}
	
	/**
	 * Initialize all the couloured tokens (those which represent the ressources of the player). All the tokens start with value 0.
	 */
	private void initRessourcesMap() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc", "Jaune");
		
		for(var elem : couleurs) {
			this.ressources().put(elem, 0);
		}	
	}
	
	/**
	 * Remove a player ressources. It removes the ressources thanks to the value given and give it to the bank of tokens. 
	 * 
	 * @param type_ressource
	 *        Name of one of the colors represented in the game
	 *        
	 * @param val
	 *        Number of tokens to remove
	 * @param ressources_banque
	 * 		the bank's tokens
	 * @return New value of tokens the player have
	 */
	default int enleveRessource(String type_ressource, int val, HashMap<String, Integer> ressources_banque) {
		
		Objects.requireNonNull(type_ressource);
		Objects.requireNonNull(ressources_banque);
		
		int prix;
		int old_val = this.ressources().get(type_ressource);	//Ressources of the player
		
		//New val taking in count the bonus
		if(val < this.bonus().get(type_ressource)) {
			val  = 0;
		}
		else {
			val = val - this.bonus().get(type_ressource);
		}
		
		if(old_val < val) {
			
			ressources_banque.put("Jaune", ressources_banque.get("Jaune") + ((-1)*(old_val - val)));	//Jokers rendus à la banque
			this.ressources().put("Jaune", this.ressources().get("Jaune") - ((-1)*(old_val - val)));	//Joker retirés au joueur
			return 0;
		}
		
		return old_val - val;
	}
	
	/**
	 * Remove a player ressources. It removes the ressources thanks to the value given and give it to the bank of tokens. 
	 * It doesn't depends on the user's golden tokens or bonus.
	 * 
	 * @param type_ressource
	 *        Name of one of the colors represented in the game
	 *        
	 * @param val
	 *        Number of tokens to remove
	 * @param ressources_banque
	 * 		 No description found
	 * @return New value of tokens the player have
	 */
	default int defausseRessource(String type_ressource, int val, HashMap<String, Integer> ressources_banque) {
		
		Objects.requireNonNull(type_ressource);
		Objects.requireNonNull(ressources_banque);
		
		int prix;
		int old_val = this.ressources().get(type_ressource);	//Ressources of the player
		return old_val - val;
	}
	
	/**
	 * Add the bonus token given ti the player.
	 * 
	 * @param jeton_bonus
	 *        Token considered like the bonus to add
	 */
	private void addBonus(Jeton jeton_bonus) {
		
		Objects.requireNonNull(jeton_bonus);
		
		this.bonus().put(jeton_bonus.couleur(), this.bonus().get(jeton_bonus.couleur()) + 1);
	}
	
	/**
	 * This function enables the developer to choose by a cheat code the number of bonus he wants to begin the part
	 */
	default void cheatBonus() {
		
		System.out.println("Vous êtes entré dans une partie réservée au développeur. Vous pouvez vous octroyer des bonus : \n");
		System.out.println("Couleur : \n");
		
		var jeton = Saisie.saisieJeton();
		
		System.out.println("Nombre : \n");
		
		var quantite = Saisie.choixIntervalle(1,9, null, 0);
		this.bonus().put(jeton.couleur(), quantite);
	}
	
	/**
	 * Adding a defined amount of a player's resource.
	 * 
	 * @param type_ressource
	 *        Name of one of the colors représenting a resource of the game.
	 *        
	 * @param val
	 * 		  Number of tokens to remove
	 * 
	 * @return New number of tokens the player has
	 */
	default int ajouteRessource(String type_ressource, int val) {
		
		Objects.requireNonNull(type_ressource);
		
		if(val < 0) {
			throw new IllegalArgumentException();
		}
		
		return this.ressources().get(type_ressource) + val;
	}
	
	/**
	 * Returns true if a Noble is visiting the player and false otherwise. A Noble is visiting a player if
	 * he possesses the same bonus as the Noble cost. Many nobles can visit a player
	 * 
	 * @param nobles_visiting
	 *        List of nobles who visit the player
	 *        
	 * @param tuiles_board
	 *        Nobles on the board
	 * 
	 * @return Returns true if a Noble is visiting the player and false otherwise
	 */
	default boolean isNobleVisiting(ArrayList<Tuile> nobles_visiting, ArrayList<Tuile> tuiles_board){

		Objects.requireNonNull(tuiles_board);
		Objects.requireNonNull(nobles_visiting);
		
		for(var noble : tuiles_board) {
			
			var is_visiting = true;
			
			for(var couleur_price : noble.cout().entrySet()) {
				
				var name_price = couleur_price.getKey();
				var val_price = couleur_price.getValue();
				
				if(!(this.bonus().get(name_price) >= val_price)) {
					is_visiting = false;
				}
			}
			
			if(is_visiting) {
				nobles_visiting.add(noble);
			}	
		}
		
		if(nobles_visiting.size() != 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * This function enable to know which action the player wants to do.
	 * 
	 * 
	 * @param game
	 *        The game mode.
	 * @param game_mode
	 * 		 No description found
	 * @param affichage
	 *         No description found
	 * @return int
	 * 		the action chosen by the player.
	 * 
	 *
	 * 1 : buy a card
	 * 2 : take ressources
	 * 3 : reserve a card.
	 * 4 : look at its informations
	 */
	public int action(Mode game, int game_mode, Affichage affichage);
	
	/**
	 * Check if the player has enough tokens in his resources to pay the card given.
	 *
	 * @param card
	 *        Card to pay
	 * @param game
	 * 		The game state
	 *        
	 * @return Returns ture if the card can be earned or false.
	 */
	private boolean checkMoney(CarteDev card, Mode game) {
		
		Objects.requireNonNull(card);
		Objects.requireNonNull(game);
		
		var val_joker = this.ressources().get("Jaune");
		
		for(var elem : card.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();

			/* on vérifie que le user, grâce à ses ressources et/ou ses bonus puisses acheter la carte*/
			if(this.ressources().get(name) + this.bonus().get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + this.ressources().get(name) + this.bonus().get(name) < val){
					return false;
				}
				/* on attribue au joker une nouvelle valeujr en retirant ce qui a pu être payé avec les bonus et les ressources */
				val_joker = val_joker - ( val - (this.ressources().get(name)+ this.bonus().get(name)) );
			}
		}
		
		return true;
	}
	
	/**
	 * Adding of an amount of a token.
	 * 
	 * @param jeton
	 *        Token type to add
	 *        
	 * @param quantite
	 *        	Number of tokens to add
	 */
	default void addRessource(Jeton jeton, int quantite) {
		
		Objects.requireNonNull(jeton);
		
		if(quantite < 0) {
			throw new IllegalArgumentException();
		}
		
		int quantite_total = this.ressources().get(jeton.couleur()) + quantite;
		
		this.ressources().put(jeton.couleur(), quantite_total);
	}
	
	/**
	 * Check if the number of tokens of a player is under 10.
	 * 
	 * @return True if the user has 10 tokens or under and false otherwise.
	 */
	default boolean checkNbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources().values()) {
			count = count + nb_jet;
		}
		
		if(count > 10) {	// mettre une constate pour 10
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the number of token possessed by a player
	 * 
	 * @return the number of token that the player possess
	 */
	default int NbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources().values()) {
			count = count + nb_jet;
		}
		
		return count;
	}
	
	/**
	 * Check if the number of token that the player want to loose isn't too many.
	 * 
	 * @param quantite
	 *        The number of token that the player wants to loose.
	 *        
	 * @return a boolean that represents the possibility for the player to loose these tokens. If it's too much, it returns false.
	 * else it returns true.
	 */
	default boolean NbJetons_loose(int quantite) {
		
		if(quantite < 0) {
			throw new IllegalArgumentException();
		}
		
		/* cette variable enregistre le nombre de token du joueur*/
		int count = 0;
		
		for(var nb_jet : this.ressources().values()) {
			count = count + nb_jet;
		}
		/* le joueur souhaite retirer trop de token, action annulée*/
		if(count - quantite < 10) {
			System.out.println("Vous avez essayé de retirer trop de jetons... On recommence \n ");
			return false;
		}
		/*le joueur retire un nombre de token acceptable*/
		return true;
	}
	
	/**
	 * Makes a card reservation for the player.
	 * 
	 * @param carte
	 *        card to reserve
	 *        
	 * @param ressources_jeu
	 *        Tokens available in the game (bank)
	 *        
	 * @return True if the reservation is possible (under 3 reserved cards) and false otherwise.
	 */
	default boolean reserveCarte(CarteDev carte, HashMap<String, Integer> ressources_jeu) {
		
		Objects.requireNonNull(carte);
		Objects.requireNonNull(ressources_jeu);
		
		if(this.reserve().size() == 3) {
			System.out.println("Désolé il n'est pas possible de réserver plus de 3 cartes !");
			return false;
		}
		
		this.reserve().add(carte);

		return true;
	}
	
	/**
	 * This method enables to the user to choose the card's level he wants to reserve
	 * 
	 * @param affichage
	 *        Type of display used
	 *  
	 * @return An int which represents the card level between 1 and 3.
	 */
	public int reservationCartePioche(Affichage affichage);
	
	/**
	 * This function handle the use of 
	 * 
	 * @param affichage
	 *        A variable which handles what should be display
	 *        
	 * @param game
	 *   	  The game state
	 *   
	 * @return HashMap
	 * 		An HashMap which contains the card informations : number and level
	 */
	public HashMap<Integer, Integer> reservationCartePlateau(Affichage affichage, ModeII game);
	
	/**
	 * This function handle the choice for a player to loose a toke.
	 *        
	 * @return String
	 *         It is the type of the type of the token.
	 * 
	 */
	public String defausserNameJeton();
	
	/**
	 * This function handle the quantity of a token that the player wants to remove.
	 * 
	 * @return int
	 * It is the quantity that the player will remove.
	 * 
	 */
	public int defausseJetonQuantite();
	
	/**
	 * The player buys a card from the game. The card is bought if the resource
	 * requested is possessed in sufficient quantity by the player.
	 * 
	 * @param carte
	 *        Card to earn
	 *        
	 * @param game
	 *        Game in which buy the card
	 * 
	 * @return True if the card has successfully been earned and false otherwise.
	 */
	public boolean acheteCarte(CarteDev carte, Mode game);
	
	/**
	 *  This method allows the player to buy a card that were placed in his reserve.
	 * 
	 * @param affichage
	 * 		 No description found
	 *        
	*  @return int 
	*  		an int which is the chronological order of appearance of the chosen card
	*  
	*  -2 : le numéro de carte n'existe pas
	*  -1 : il n'y a pas de carte dans la réserve.
	*  1 : transaction succeed 
	*  another number means : the card position +1 
	 */
	public int achatCarteReservee(Affichage affichage);
	
	/**
	 *  This method allows the player to choose a card
	 * 
	 * @param mode_jeu 
	 *        The game mode choosen by the users 
	 *        
	 * @param affichage
	 *        Type of display used
	 * 
	 * @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	public HashMap<Integer, Integer> achatCarteNonReservee(int mode_jeu, Affichage affichage);
	
	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * 
	 * @param affichage
	 *        Type of display used
	 * 
	 * @param game
	 *        Current game
	 * 
	 * @return an int which is the chronological order of appearance of the chosen card
	 */
	public int choixJeton(Affichage affichage, Mode game);
	
	/**
	 * Performs the input of a token. Returns a token if the token has successfully been created.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return a Token created with the informations given
	 */
	public Jeton saisieJeton();
	
	/**
	 * This method handles the whole process necessary to acquire 3 tokens
	 * 
	 * @param game
	 *       the game state
	 *       
	 * @return 
	 * an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user : it never happens normally
	 * -1 = transaction failed : it never happens normally
	 */
	public List<String> TakesThreeTokens(Mode game);
	
	/**
	 *  Enables to the user to choose a noble among a list of nobles
	 *  
	 *  @param mode
	 *         The game state
	 *         
	 *  @param affichage
	 *         The display function
	 *         
	 *  @param nobles_visiting
	 *         The nobles that the player can choose
	 *         
	 *  @return Tuile
	 *          It represents the nobles that has been
	 */
	public Tuile choixNoble(Mode mode, Affichage affichage, ArrayList<Tuile> nobles_visiting);
	
	/**
	 * Adding of some points values.
	 * 
	 * @param val
	 *        Points value to add
	 */
	public void addPrestige(int val);
	
	/**
	 *  This method allows the IA to choose from where he plans to reserve a card
	 * 
	 * @param affichage
	 *        Type of display used
	 * 
	 * @return an int which represents the place from where the user wants to take its card.
	 *  
	 *  1 : from the board
	 *  2 : from the pile
	 */
	public int choixReservation(Affichage affichage) ;
	
	/**
	 *  This method allows the player to buy a card
	 *     The game mode choosen by the users
	 *     
	 * @param game
	 *        The game state 
	 *        
	 * @param affichage
	 *        Type of display used
	 *        
	 * @return int
	 * 		the new game state
	 */
	public int choixAchatCarte(Mode game, Affichage affichage);
}