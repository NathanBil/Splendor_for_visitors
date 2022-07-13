package fr.umlv.players;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import fr.umlv.objects.*;
import fr.umlv.saisie.*;
import fr.umlv.affichage.Affichage;
import fr.umlv.copie.Copie;
import fr.umlv.game.mode.Mode;
import fr.umlv.game.mode.ModeII;


/**
 * Declaration of the type Joueur. It is a player who belongs to a Splendor game.
 * 
 * @author dylandejesus nathanbilingi
 */
public class Joueur implements Participant {
	
	/**
	 * Number of cards earned by a player
	 */
	private int cartes;
	
	/**
	 * Name of the player during the game
	 */
	private final String pseudo;
	
	/**
	 * Age of the player
	 */
	private final int age;
	
	/**
	 * Number of points value
	 */
	private int points_prestiges;
	
	/**
	 * Player's resources (tokens he has)
	 */
	private HashMap<String, Integer> ressources;
	
	/**
	 * Player's bonus (when he pays developments cards)
	 */
	private HashMap<String, Integer> bonus;
	
	/**
	 * All the reserved cards of a player
	 */
	private ArrayList<CarteDev> reserve;

	/**
	 * Constructor of the type Joueur.
	 * 
	 * @param pseudo
	 * 		 Player name
	 * 
	 * @param age
	 * 		  Player age
	 * 
	 * @param points_prestiges
	 * 		  Points value
	 */
	public Joueur(String pseudo, int age, int points_prestiges) {
		
		Objects.requireNonNull(pseudo, "Pseudo hasn't been given");
	
		if(age <= 0) {
			throw new IllegalArgumentException();
		}
		
		if(points_prestiges < 0) {
			throw new IllegalArgumentException("Prestige points can't be under 0");
		}
		
		this.cartes = 0;
		this.pseudo = pseudo;
		this.age = age;
		this.points_prestiges = points_prestiges;
		this.reserve = new ArrayList<CarteDev>();
		this.bonus = new HashMap<>();
		this.ressources = new HashMap<>();
		this.initRessourcesMap();
		this.initBonus();
	}
	
	/**
	 * Constructor of the type Joueur. Is called if we create a player without giving any Points value.
	 *The points value are initialized with value 0.
	 * 
	 * @param pseudo
	 *        Player name
	 *        
	 * @param age
	 *        Age of the player
	 */
	public Joueur(String pseudo, int age) {
		this(pseudo, age, 0);
	}

	
	/**
	 * cartes field getter.
	 * 
	 * @return cartes field value
	 */
	public int cartes() {
		return this.cartes;
	}
	
	/**
	 * pseudo field getter.
	 * 
	 * @return pseudo field value
	 */
	public String pseudo() {
		return this.pseudo;
	}
	
	/**
	 * age field getter.
	 * 
	 * @return age field value
	 */
	public int age() {
		return this.age;
	}
	
	/**
	 * points_prestiges field getter.
	 * 
	 * @return points_prestiges field value
	 */
	public int points_prestiges() {
		return this.points_prestiges;
	}
	
	/**
	 * ressources field getter.
	 * 
	 * @return ressources field value
	 */
	public HashMap<String, Integer> ressources() {
		return this.ressources;
	}
	
	/**
	 * bonus field getter.
	 * 
	 * @return bonus field value
	 */
	public HashMap<String, Integer> bonus() {
		return this.bonus;
	}
	
	/**
	 * reserve field getter.
	 * 
	 * @return reserve field value
	 */
	public ArrayList<CarteDev> reserve(){
		return this.reserve;
	}
	
	/**
	 * Initialize all the coloured tokens bonus (earned by the player during the game). All the tokens start with the value 0. 
	 */
	private void initBonus() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc");
		
		for(var elem : couleurs) {
			this.bonus.put(elem, 0);
		}	
	}
	
	/**
	 * Initialize all the couloured tokens (those which represent the ressources of the player). All the tokens start with value 0.
	 */
	private void initRessourcesMap() {
		var couleurs = List.<String>of("Rouge", "Vert", "Noir", "Bleu", "Blanc", "Jaune");
		
		for(var elem : couleurs) {
			this.ressources.put(elem, 0);
		}	
	}
	
	/**
	 * String representation of a Player.
	 */
	@Override
	public String toString() {
		return "Joueur [pseudo= "+ this.pseudo + ", age= " + this.age + ", prestige= " + points_prestiges + ", ressources=" + this.ressources +"]"; 
	}

	/**
	 * Remove a player ressources. It removes the ressources thanks to the value given and give it to the bank of tokens. 
	 * 
	 * @param type_ressource
	 *        Name of one of the colors represented in the game
	 *        
	 * @param val
	 *        Number of tokens to remove
	 * 
	 * @return New value of tokens the player have
	 */
	public int enleveRessource(String type_ressource, int val, HashMap<String, Integer> ressources_banque) {
		
		Objects.requireNonNull(type_ressource);
		Objects.requireNonNull(ressources_banque);
		
		if(val < 0) {
			throw new IllegalArgumentException();
		}
		
		int prix;
		int old_val = this.ressources.get(type_ressource);	//Ressources of the player
		
		//New val taking in count the bonus
		if(val < this.bonus.get(type_ressource)) {
			val  = 0;
		}
		else {
			val = val - this.bonus.get(type_ressource);
		}
		
		if(old_val < val) {
			
			ressources_banque.put("Jaune", ressources_banque.get("Jaune") + ((-1)*(old_val - val)));	//Jokers rendus à la banque
			this.ressources.put("Jaune", this.ressources.get("Jaune") - ((-1)*(old_val - val)));	//Joker retirés au joueur
			return 0;
		}
		
		return old_val - val;
	}
	
	/**
	 * This function do a deep copy of a player.
	 * 
	 * @return the copy which has its own references.
	 */
	@Override
	protected Object clone(){
		
		Objects.requireNonNull(this);
		
		var copie1 = new Copie();
		Joueur copie = new Joueur(this.pseudo(), this.age(),this.points_prestiges());
		
		copie.cartes = this.cartes;
		copie.ressources = copie1.copieHashmap(this.ressources);
		copie.bonus = copie1.copieHashmap(this.bonus);

		Collections.copy(copie.reserve, this.reserve);
		
		return copie;	
	}
	
	/**
	 * Rewrite of the equals function, allows to know if two Players are equal or not. Returns
	 * true if two players are equal and false otherwise.
	 * 
	 * @param player
	 *        Second player to compare to the other.
	 *        
	 * @return True if the two players are equals and false otherwise
	 */
	@Override
	public boolean equals(Object player) {
		
		Objects.requireNonNull(player);
		
		return player instanceof Joueur joueur
				&& age == joueur.age
				&&pseudo.equals(pseudo);
	}
	
	/**
	 * Add the bonus token given to the player.
	 * 
	 * @param jeton_bonus
	 *        Token considered like the bonus to add
	 */
	private void addBonus(Jeton jeton_bonus) {
		
		Objects.requireNonNull(jeton_bonus);
		
		this.bonus.put(jeton_bonus.couleur(), this.bonus.get(jeton_bonus.couleur()) + 1);
	}
	
	/**
	 * This function enables the developer to choose by a cheat code the number of bonus he wants to begin the part
	 */
	public void cheatBonus() {
		
		System.out.println("Vous êtes entré dans une partie réservée au développeur. Vous pouvez vous octroyer des bonus : \n");
		System.out.println("Couleur : \n");
		
		var jeton = Saisie.saisieJeton();
		System.out.println("Nombre : \n");
		
		var quantite = Saisie.choixIntervalle(1,9, null, 0);
		this.bonus.put(jeton.couleur(), quantite);
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
	public int ajouteRessource(String type_ressource, int val) {
		
		Objects.requireNonNull(type_ressource);
		
		if(val < 0) {
			throw new IllegalArgumentException();
		}
		
		return this.ressources.get(type_ressource) + val;
	}
	
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
	public boolean acheteCarte(CarteDev carte, Mode game) {
		
		Objects.requireNonNull(carte);
		Objects.requireNonNull(game);
		
		if(!this.checkMoney(carte, game)) {
			return false;
		}
		
		this.addPrestige(carte.points());
		
		for(var elem : carte.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();
			
			/* ressources récupérées par la banque*/
			var recover = val;
			
			/* cas où le user n'a pas assez de ressources du type recherché sauf quand il utilise des ressources en or*/
			if(this.ressources.get(name) < recover)
				recover = this.ressources.get(name);
			
			int nouv_val;
			
			nouv_val = this.enleveRessource(name, val, game.jetons_disponibles());
			this.ressources.put(name, nouv_val);

			game.jetons_disponibles().put(name,  game.jetons_disponibles().get(name) + recover);	
		}
		
		this.addBonus(new Jeton(carte.couleur()));
		this.cartes += 1;
		
		return true;
	}
	
	/**
	 * This function enable to know which action the player wants to do.
	 * 
	 * @param game
	 *        The game state
	 *        
	 *  @param game_mode
	 *        The game mode
	 *             
	 * @param affichage
	 *        This variable handle what will be displayed to the player.
	 *        
	 *
	 *        
	 * @return int 
	 * 		the action chosen by the player.
	 * 
	 * 1 : buy a card
	 * 2 : take ressources
	 * 3 : reserve a card.
	 * 4 : look at its informations
	 */
	@Override
	public int action(Mode game, int game_mode, Affichage affichage){
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		return Saisie.menuSaisie(game_mode, affichage);
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
	public boolean isNobleVisiting(ArrayList<Tuile> nobles_visiting, ArrayList<Tuile> tuiles_board){

		Objects.requireNonNull(nobles_visiting);
		Objects.requireNonNull(tuiles_board);
		
		for(var noble : tuiles_board) {
			
			var is_visiting = true;
			
			for(var couleur_price : noble.cout().entrySet()) {
				var name_price = couleur_price.getKey();
				var val_price = couleur_price.getValue();
				
				if(!(this.bonus.get(name_price) >= val_price)) {
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
	 * Check if the player has enough tokens in his resources to pay the card given.
	 *
	 * @param card
	 *        Card to pay
	 * @param game
	 * 		game state
	 *        
	 * @return boolean
	 * 		Returns true if the card can be earned or false.
	 */
	
	private boolean checkMoney(CarteDev card, Mode game) {
		
		Objects.requireNonNull(card);
		Objects.requireNonNull(game);
		
		var val_joker = this.ressources().get("Jaune");
		
		for(var elem : card.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();

			/* on vérifie que le user, grâce à ses ressources et/ou ses bonus puisses acheter la carte*/
			if(this.ressources.get(name) + this.bonus.get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + this.ressources.get(name) + this.bonus.get(name) < val){
					return false;
				}
				/* on attribue au joker une nouvelle valeujr en retirant ce qui a pu être payé avec les bonus et les ressources */
				val_joker = val_joker - ( val - (this.ressources.get(name)+ this.bonus.get(name)) );
			}
		}
		
		return true;
	}
	
	
	/**
	 * Addin of some points values.
	 * 
	 * @param val
	 *        Points value to add
	 */
	public void addPrestige(int val) {
		
		if(val < 0) {
			throw new IllegalArgumentException();
		}
		
		int result = this.points_prestiges + val;
		
		if(result < 0) {
			result = 0;
		}
		
		this.points_prestiges = result;
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
	public void addRessource(Jeton jeton, int quantite) {
		
		Objects.requireNonNull(jeton);
		
		if(quantite < 0) {
			throw new IllegalArgumentException();
		}
		
		int quantite_total = this.ressources.get(jeton.couleur()) + quantite;
		
		this.ressources.put(jeton.couleur(), quantite_total);
	}
	
	/**
	 * This function handle the choice for a player to loose a toke.
	 * 
	
	 * 
	 * @return String
	 * 		the type of the token.
	 */
	@Override
	public String defausserNameJeton() {
		return Saisie.saisieJeton_name();
	}
	
	/**
	 * This function handle the quantity of a token that the player wants to remove.
	 * 
	 * @return the quantity that the player will remove.
	 * 
	 */
	@Override
	public int defausseJetonQuantite(){
		return Saisie.nb_jeton_defausse();
	}
	
	/**
	 * Check if the number of tokens of a player is under 10.
	 * 
	 * @return True if the user has 10 tokens or under and false otherwise.
	 */
	public boolean checkNbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources.values()) {
			count = count + nb_jet;
		}
		
		if(count > 10) { /*10 => Nombre de jetons max*/
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if the number of token that the player want to loose isn't too many.
	 * 
	 * @param quantite
	 *       The number of token that the player wants to loose.
	 * 
	 * @return boolean that represents the possibility for the player to loose these tokens. If it's too much, it returns false.
	 * else it returns true.
	 */
	@Override
	public boolean NbJetons_loose(int quantite) {
		
		if (quantite < 0) {
			throw new IllegalArgumentException();
		}
		
		/* cette variable enregistre le nombre de token du joueur*/
		int count = 0;
		
		for(var nb_jet : this.ressources.values()) {
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
	 * return the number of token possessed by a player
	 * 
	 * @return the number of token that the player possess
	 */
	public int NbJetons() {
		
		int count = 0;
		
		for(var nb_jet : this.ressources.values()) {
			count = count + nb_jet;
		}
		
		return count;
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
	public boolean reserveCarte(CarteDev carte, HashMap<String, Integer> ressources_jeu) {
		
		Objects.requireNonNull(carte);
		Objects.requireNonNull(ressources_jeu);
		
		if(this.reserve.size() == 3) {
			System.out.println("Désolé il n'est pas possible de réserver plus de 3 cartes !");
			return false;
		}
		
		this.reserve.add(carte);
		return true;
	}
	

	/**
	 * This function handle the use of
	 * 
	 * @param affichage
	 *        A variable which handles what should be display
	 *        
	 * @param game
	 *        The game state (ModeII always)
	 *        
	 * @return an HashMap which contains the card informations : number and level
	 */
	@Override
	public HashMap<Integer, Integer> reservationCartePlateau(Affichage affichage, ModeII game) {
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		return Saisie.reservationCartePlateau(affichage);
	}
	
	/**
	 * This method enables to the user to choose the card's level he wants to reserve
	 * @param affichage
	 * 		it is used to display messages
	 * @return int
	 * an int which represents the card level between 1 and 3.
	 * 
	 */
	@Override
	public int reservationCartePioche(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		return Saisie.reservationCartePioche(affichage);
	}
	
	/**
	 * This method handles the whole process necessary for a transaction
	 * 
	 * @param affichage
	 *     it is used to display messages or show the game.
	 *       
	 * @param game
	 *       the game mode which had been chosen and the game state.
	 *       
	 * @return int
	 * 		an int which represents either the transaction occured properly;
	 * 
	 * 1 = transaction has worked
	 * 0 = transaction canceled by the user
	 * -1 = transaction failed
	 * 
	 */
	public int achatCarte(Affichage affichage, Mode game) {
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		return game.achatCarte(this, affichage);
	}
	
	/**
	 *  This method allows the player to buy a card that were placed in his reserve.
	 * 
	 * @param affichage
	 *        it is used the to show the fame state
	 * 
	 * @return int 
	 * 		An int which is the chronological order of appearance of the chosen card
	 * 
	 *  -2 : le numéro de carte n'existe pas
	 *  -1 : il n'y a pas de carte dans la réserve.
	 *  1 : transaction succeed 
	 *  another number means : the card position +1 
	 */
	@Override
	public int achatCarteReservee(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		return Saisie.achatCarteReservee(this,affichage);
	}
	
	/**
	 *  This method allows the player to choose a card
	 * 
	 * @param mode_jeu 
	 *        The game mode choosen by the users 
	 *  @param affichage
	 *  	it is used to display the game state.
	 * 
	 * @return Hashmap
	 * 		an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	@Override
	public HashMap<Integer, Integer> achatCarteNonReservee(int mode_jeu, Affichage affichage){

		Objects.requireNonNull(affichage);
		
		if(mode_jeu < 0) {
			throw new IllegalArgumentException();
		}
		return Saisie.achatCarteNonReservee(mode_jeu, affichage);
	}
	
	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * 
	 * @param affichage
	 *        it is used to display the game
	 * @param game
	 * 		the game state
	 *        
	*  @return an int which is the chronological order of appearance of the chosen card
	 */
	@Override
	public int choixJeton(Affichage affichage, Mode game){
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		return Saisie.choixJeton(affichage);
	}
	
	/**
	 * Performs the input of a token. Returns a token if the token has successfully been created.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return Jeton 
	 * a Token created with the informations given
	 */
	@Override
	public Jeton saisieJeton() {
		return Saisie.saisieJeton();
	}
	
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
	@Override
	public List<String> TakesThreeTokens(Mode game){
		
		Objects.requireNonNull(game);
		
		return null;
	}
	
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
	 *  @return It represents the nobles that has been choosen
	 * 
	 */
	@Override
	public Tuile choixNoble(Mode mode, Affichage affichage, ArrayList<Tuile> nobles_visiting) {
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(nobles_visiting);
		Objects.requireNonNull(mode);
		
		return Saisie.choixNoble(mode, affichage, this, nobles_visiting);
	}
	
	/**
	 * This method allows the player to choose from where he plans to reserve a card
	 * 
	 * @return an int which represents the place from where the user wants to take its card.
	 */
	@Override
	public int choixReservation(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		/*Réservation de cartes*/
		return Saisie.choixReservation(affichage);
	}
	
	/**
	 *  This method allows the player to buy a card
	 * 
	 * @param game
	 *        The game mode choosen by the users and the game state
	 *        
	 * @param affichage
	 *        show the game
	 *        
	 * @return the new game state
	 */
	@Override
	public int choixAchatCarte(Mode game, Affichage affichage) {
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		return Saisie.choixAchatCarte(game, affichage);
	}
}





