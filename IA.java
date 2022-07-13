package fr.umlv.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import fr.umlv.objects.*;
import fr.umlv.saisie.*;
import fr.umlv.affichage.Affichage;
import fr.umlv.copie.Copie;
import fr.umlv.game.Partie;
import fr.umlv.game.mode.Mode;
import fr.umlv.game.mode.ModeII;

/**
 * Declaration of the type IA. It is a player who belongs to a Splendor game.
 * It's automatically handled
 * 
 * @author dylandejesus nathanbilingi
 */

	public class IA implements Participant {
	
	
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
	 * The card that the player will buy for the next turn
	 * its value is null if he won't buy anything.
	 * Its value is filled during the test phase.
	 * The "value" value is -1 if the player want to buy a reserved card.
	 * The first integer is the level, the second is the number
	 */
	private HashMap<Integer,Integer> next_achat;
	
	/**
	 * this variable enable to know if the ai will buy a reserved card or a card on the board.
	 * Its value is 1 if it's a reserved card and 0 if it is from the board. -1 means no buying attempt.
	 */
	private int achat_type;
	
	/**
	 * 
	 * this variable contain the token that the ai will choose
	 */
	private String next_token;
	
	/**
	 * It creates the random aspect
	 */
	final Random ran;
	
	
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
	public IA(String pseudo, int age, int points_prestiges) {
		
		Objects.requireNonNull(pseudo, "Pseudo hasn't been given");
	
		if(age <= 0) {
			throw new IllegalArgumentException();
		}
		
		if(points_prestiges < 0) {
			throw new IllegalArgumentException("Prestige points can't be under 0");
		}
		ran = new Random(System.currentTimeMillis());
		this.cartes = 0;
		this.pseudo = "IA"+ran.nextInt(2000);
		this.age = age;
		this.points_prestiges = points_prestiges;
		this.reserve = new ArrayList<CarteDev>();
		this.bonus = new HashMap<>();
		this.ressources = new HashMap<>();
		this.initRessourcesMap();
		this.initBonus();
		this.next_achat = new HashMap<Integer,Integer>();
		this.achat_type = -1;

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
	public IA(String pseudo, int age) {
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
		return "IA [pseudo= "+ this.pseudo + ", age= " + this.age + ", prestige= " + points_prestiges + ", ressources=" + this.ressources +"]"; 
	}
	
	/** This function choose a random action that the AI will do.
	 * 
	 * @param achat_valide
		 *  the variable "achat valide" enables to be sure that the player can buy card
		 *  if its value is true they can
		 *  in the other case, they can't
		 *@param game_mode
		 *	No description found
		 *
		 *@param affichage
		 *		No description found
	 * @return The action choosen randomly.
	 */
	private int randomChoice(boolean achat_valide, int game_mode, Affichage affichage){
		
		Objects.requireNonNull(affichage);
		
		 var chaine = "(1) Acheter une carte\n(2) Prendre des ressources\n(3) Voir mes informations";
	     if(game_mode != 1) {
	           chaine = chaine + "\n(4) Réserver une carte";
	     }
	    affichage.affichageMessageInstructionsBox(chaine);
		int action = ran.nextInt(99);
		if(achat_valide == true) {
			/* on choisit de prendre des ressources avec 50% de chances*/
			if(action >= 0 && action <= 50) {
				System.out.println("2 \n");
				return 2;
			}
			/* on choisit d'acheter une carte avec 40% de chances */
			if(action >= 51 && action <=90) {
				System.out.println("1 \n");
				return 1;
			}
			/* on choisit de réserver une carte*/
			/* cas où l'ia peut réserver une carte*/
			if(this.reserve().size() < 3) {
				System.out.println("4\n");
				return 4; 
			}
			/* on choisit de prendre des ressources */
			else
				return 2;
		}
		/* cas où l'ia peut réserver une carte*/
		if(this.reserve().size() < 3) {
			/* on choisit de prendre des ressources avec 91% de chances*/
			if(action >= 0 && action <= 90) {
				System.out.println("2\n"); 
				return 2;
			}
			/* on choisit de réserver une carte avec 10% de chances*/
			System.out.println("4\n");  
			return 4;
		}
		System.out.println("2\n");  
		return 2;
	}
	
	
	/**
	 * This function enable to know which action the player wants to do.
	 * @param game
	 * 	The game state
	 * @param game_mode
	 * 	The game mode.
	 * @param affichage
	 * 		No description
	 * @return the action chosen by the player.
	 * 1 : buy a card
	 * 2 : take ressources
	 * 3 : reserve a card.
	 * 4 : look at its informations
	 */
	@Override
	public int action(Mode game, int game_mode, Affichage affichage){
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		var achat_valide = testBuyableBoardCard(game);
		if(achat_valide == false) {
			achat_valide = TestIaBuyableReservedCard(game);
			if(achat_valide == true)
				/* à supprimer*/
				System.out.println("l'ia va acheter dans sa réserve son achat_type est donc : "+this.achat_type);
		}
		if(achat_valide == true) {
			TestIaBuyableReservedCard(game);
		}
		return randomChoice(achat_valide, game_mode, affichage);
	}

	/**
	 * Check if the player has enough tokens in his resources to pay the card given.
	 *
	 * @param card
	 *        Card to pay
	 *  @param game
	 *  	Game state      
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
			if(this.ressources.get(name) + this.bonus.get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + this.ressources.get(name) + this.bonus.get(name) < val){
					return false;
				}
				/* on attribue au joker une nouvelle valeur en retirant ce qui a pu être payé avec les bonus et les ressources */
				val_joker = val_joker - ( val - (this.ressources.get(name)+ this.bonus.get(name)) );
			}
		}
		
		return true;
	}
	
	
	
	/** This create a card with a random number and a random level
	 * @param game
	 * The game state which is necessary to know which card would be acceptable.
	 * @return an HashMap which represents the random card that had been created.
	 */
	private HashMap<Integer, Integer> RandomCardChoosen(Mode game){
		
		Objects.requireNonNull(game);
		
		var carte = new HashMap<Integer,Integer>();
		/* on choisit un numero de carte entre 0 et 3*/
		var chosen_card =  ran.nextInt(3);
		/* on choisit un niveau de carte entre 1 et 3*/
		var ligne_chosen =  ran.nextInt(2)+1;
		var valide = 0;
		var nb_tentative = 0;
		/* garde les meilleures valeurs trouvées*/
	
		while(valide == 0 && nb_tentative < 50) {
			/* on choisit un numero de carte entre 0 et 3*/
			chosen_card =  ran.nextInt(3);
			/* on choisit un niveau de carte entre 1 et 3*/
			/* à modifier pour aller de (1 à 2) + 1*/
			ligne_chosen = ran.nextInt(2);
			valide = game.carteReserveValide2Arg(ligne_chosen, chosen_card);
		nb_tentative++;
		}
		carte.put(ligne_chosen, chosen_card);
		return carte;
	}


	/** This create a card with a random number and a random level
	 * 
	 * @param affichage
	 * 		No description found
    * @param game
    * the game state. It enables to know the card numbers which are acceptable.
    * @return an HashMap which represents the random card that had been created.
    */
	private HashMap<Integer, Integer> randomCardChoosenReservation(Affichage affichage, ModeII game){
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(game);
		
		int valide = 0;
		int nb_tentative = 0;
		var carte = new HashMap<Integer,Integer>();
		var chosen_card = 0;
		var ligne_chosen = 1;
		/* on retente le choix de la carte à réserver tant que celui-ci n'est pas valide*/
		/* normalement il n'est pas censé y avoir de problème et la valeur 50 n'est jamais atteinte car il y a statiquement tjrs des cartes de suffisamment de 
		 * niveau et avec le bon nombre  */
		while(valide == 0 && nb_tentative < 50) {
			/* on choisit un numero de carte entre 0 et 3*/
			chosen_card =  ran.nextInt(3);
			/* on choisit un niveau de carte entre 1 et 3*/
			/* à modifier pour aller de (1 à 2) + 1*/
			ligne_chosen = ran.nextInt(2)+1;
			valide = game.carteReserveValide2Arg(ligne_chosen, chosen_card);
			nb_tentative++;
		}
		
		affichage.affichageMessageInstructionsBox("Choisissez une carte du plateau\n(Niveau - N°Carte)\n");
		System.out.println(ligne_chosen +"    "+chosen_card);
		carte.put(ligne_chosen, chosen_card);
		return carte;
	}

	/**
	 *  This method check if an AI can buy a card. For this purpose it launch several attempts (5 times) with random card level and number.
	 * 
	 * @param game
	 * 		  The game state at this moment
	 * 
	 *        
	 *  @return boolean which indicates if the AI can buy the card.
	 *  True means "yes", false means "no".
	 *  
	 */
	private boolean testBuyableBoardCard(Mode game){
		
		Objects.requireNonNull(game);
		
		var copieGame = game.deepClone();
		var copieiA = (IA)this.clone();
		/* permet de revenir à l'état initial après les tests*/
		var copieiA2 = (IA)this.clone();
		/* ces variables récupèrent les meilleurs cartes possibles*/
		var best_chosen_card = 0;
		var best_ligne_chosen = 1;
	
		var best = false;
		var valide = false;
		/* on tente 30 fois d'acheter une carte avec des informations aléatoires mais possibles
		 * On récupère la meilleure parmi celles-ci*/
		for(var i = 0 ; i < 30;i++) {
			var carte = RandomCardChoosen(game);
			var chosen_card = carte.entrySet().stream().findFirst().get().getValue();
			var ligne_chosen = carte.entrySet().stream().findFirst().get().getKey();
			/* carte qui va être testée*/
			var realCard = copieGame.board().get(ligne_chosen).get(chosen_card);
			/* si l'ia peut acheter cette carte, il la renvoie*/
			if(copieiA.checkMoney(realCard,copieGame)) {
				if(next_achat != null && next_achat.size() >= 1) {
					this.next_achat.clear();
				}
				/* l'ia peut acheter une carte*/
				valide = true;
				/* cas où l'ia tombe sur une carte de niveau 3*/
				if(ligne_chosen >= 3)
					best = true;
				/* cas où il tombe sur une meilleure carte*/
				if(best_ligne_chosen <= ligne_chosen) {
					best_chosen_card = chosen_card;
					best_ligne_chosen = ligne_chosen;
				}
				/* cas où il y a déjà un élément*/
				if(this.next_achat.size() > 0)
					this.next_achat.clear();
				/* le user choisit d'acheter une carte qui n'est pas dans la réserve*/
				this.achat_type = 1;
				/* on enregistre la carte qu'il achètera*/
				this.next_achat.put(best_ligne_chosen, best_chosen_card);
				if(best == true)
					return true;
			}
		}
		return valide;
	}


	/**
	 *  This method check either an AI can buy a specific card which hadn't been reserved.
	 *  It only does it into the function not really in the party.
	 * 
	 * @param game
	 * 		  The game state at this moment
	 * 
	 * @param card
	 * 		The card that the AI try to buy
	 *        
	 *  @return a boolean which indicates either the operation succeed or failed
	 *  
	 *  true == success
	 *  false == fail
	 */
	private boolean testAiCheckMoney(CarteDev card, Mode game) {
		
		Objects.requireNonNull(card);
		Objects.requireNonNull(game);
		
		/* la deep copie n'est peut-être pas nécessaire, à vérifier*/
		var copieIa= (IA)this.clone();
		var copieGame = game.deepClone(); 
		var copieCard = card;
		var val_joker = copieIa.ressources().get("Jaune");
		
		for(var elem : copieCard.cout().entrySet()) {
			
			var name = elem.getKey();
			var val = elem.getValue();

			/* on vérifie que le user, grâce à ses ressources et/ou ses bonus puisses acheter la carte*/
			if(copieIa.ressources.get(name) + copieIa.bonus.get(name) < val) {
				
				if(val_joker <= 0) {
					return false;
				}
				if(val_joker + copieIa.ressources.get(name) + copieIa.bonus.get(name) < val){
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Adding of some points values.
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
	 * This function randomly choose the number of token that the ai wants.
	 
	 * @param game
	 * the game state
	 * @return int
	 * an int which represents the number of token that the ai wants.
	 * 1 means 2
	 * 2 means 3
	 */
	private int iaRandomTokensNum(Mode game) {
		
		Objects.requireNonNull(game);
		
		/* cas où le user peut prendre 2 jetons identiques*/
		if(this.iaTestTakeTwoTokens(game) == false)
			return 2;
		int action = (ran.nextInt(1));
		/*50% de chance de prendre 3 ressources*/
		if(action == 0)
			return 2;
		/* 50 %de chance de prendre deux ressources */
		return 1;
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
	public List<String> TakesThreeTokens(Mode game) {

		Objects.requireNonNull(game);
		
		var copie_game = game.deepClone();
		var already_chosen = new ArrayList<String>();
		/* cette variable dira si oui ou non on peut ajouter le token à la suite de token que le joueur veut*/
		/*boolean suite;*/
	    /* on récupère toutes les couleurs qui existent*/
	    var list1 = List.of("Rouge", "Vert","Bleu", "Noir", "Blanc");
				
			while(already_chosen.size() < 3){
					/* choisit un jeton au hasard*/
					/* cette variable contiendra le nom du token choisit*/
					var token_chosen = list1.get(ran.nextInt(5));
					/* cas où le token n'avait pas déjà été choisi*/
					if(!already_chosen.contains(token_chosen)) {
						/* cas où il n'y a plus assez de jetons de cette couleur*/
						if(!copie_game.enleveRessource(new Jeton(token_chosen), 1)) {
						}
						else {
								already_chosen.add(token_chosen);
						}
					}
					/* on doit recommencer si le token a déjà été choisi, le tour est invalide*/
					  
			}
			return already_chosen;
	}
	
	/**
	 * This function return precisely the number of token the AI should remove in order to quickly resume the party.
	 * 
	 * @param chosen_token
	 *  this variable contains the best token that the ai should remove. Here it's the first one with the highest 
		 * quantity which is equal or under max_remove.
	 * @return LinkedHashMap
	 * it represents the token the AI should loose. 
	 * String = name.
	 * Integer = the quantity.
	 */
	private LinkedHashMap<String, Integer>maximumJeton(String chosen_token){
		
		
		Objects.requireNonNull(chosen_token);
		
		/* this variable indicates how many tokens the player should remove in order to solve the problem*/
		var max_remove = (this.NbJetons()-10);
		var best_quantity = 0;
		var hash = new LinkedHashMap<String,Integer>();
		hash.put(chosen_token, best_quantity);
		var list1 = List.of("Rouge", "Vert","Bleu", "Noir", "Blanc");
		for(var i = 0;i < list1.size() ;i++) {
			if(this.ressources.get(list1.get(i)) >= this.ressources.get(chosen_token)) {
				/* one changes the best choice with the name of the token with which we can remove the highest number of tokens*/
				chosen_token = list1.get(i);
				/* one wipes the hashmap and prepare a place for the token with which we can remove the highest number of tokens*/
				if(hash.size() >= 1)
					hash.clear();
				best_quantity = this.ressources.get(list1.get(i));
				hash.put(chosen_token, best_quantity);
				if(best_quantity >= max_remove) {
					hash.clear();
					best_quantity = max_remove;
					hash.put(chosen_token, best_quantity);
					return hash;
				}
					
			}
				
		}	
		return hash;
	}
	
	/**
	 * This function handle the token's name that the player wants to remove.
	 * 
	 * @return String
	 * It is the name the token the ia wants to remove.
	 * 
	 */
	@Override
	public String defausserNameJeton() {
		var copie1 = (IA)this.clone();
		String chosen_token = "Rouge";
		/* on affiche la carte de choisie par l'ia comme si il écrviait*/
		var hash = copie1.maximumJeton(chosen_token);
		System.out.println(" "+hash.keySet().iterator().next());
		return hash.keySet().iterator().next();
	}
	
	/**
	 * This function handle the quantity of a token that the ai wants to remove.
	 * 
	 * @return int
	 * It is the quantity that the ia will remove.
	 * 
	 */
	@Override
	public int defausseJetonQuantite() {
		var copie1 = (IA) this.clone();
		String chosen_token = "Rouge";
		var hash = copie1.maximumJeton(chosen_token);
		var key = hash.keySet().iterator().next();
		/*System.out.println(" le nom de chosen_token après maximum de retour dans defausse: "+chosen_token);*/
		/* on affiche la quantité choisie par l'ia comme si il écrivait*/
		System.out.println(" "+hash.get(key));
		/* on renvoie la quantité du jeton choisit dont l'ia va se débarasser*/
		return hash.get(key);
	}
	
	
	/**
	 * This function test if an AI can take two tokens and it add it to the AI information.
	 
	 * @param game
	 * the game state
	 * @return boolean
	 * a boolean which represents if the transaction is possible properly;
	 * true == yes
	 * false == no
	 */
	private boolean iaTestTakeTwoTokens(Mode game) {
		
		Objects.requireNonNull(game);
		
		/*var copie_ia = (IA) this.clone();*/
		this.next_token = null;
		var copie_game = game.deepClone();
		boolean valid_choice = false;
		/* on récupère toutes les couleurs qui existent*/
		var list1 = List.of("Rouge", "Vert","Bleu", "Noir", "Blanc");
		for(var i = 0 ; (i < copie_game.jetons_disponibles().size()) && (valid_choice == false); i++) {
			String text = list1.get(i);
			
			var jeton = new Jeton(text);
			
			//Faire une fonction qui fait simultanément les deux actions
			if(copie_game.jetons_disponibles().get(jeton.couleur()) < 4) {
				/* pas assez de ressources pour en prendre 4*/	
			}else {
				/* suffisamment de ressources*/
				valid_choice = true;
				/* it registers the token that the ai will take*/
				this.next_token = text;
			}
			i++;
		}
		return valid_choice;
		
	}
	
	/**
	 * Check if the number of token that the player want to loose isn't too many.
	 * @param quantite
	 * 	 the number of token that the player wants to loose.
	 * @return boolean 
	 * 		it represents the possibility for the player to loose these tokens. If it's too much, it returns false.
	 * else it returns true.
	 */
	@Override
	public boolean NbJetons_loose(int quantite) {
		
		if(quantite < 0) {
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
			//System.out.println("coût de la ressource : "+val);
			int nouv_val;
			
			nouv_val = this.enleveRessource(name, val, game.jetons_disponibles());
			this.ressources.put(name, nouv_val);
			/*System.out.println("ressource récupérée : "+name);
			System.out.println("quantité récupérée : "+nouv_val);*/
			game.jetons_disponibles().put(name,  game.jetons_disponibles().get(name) + recover);	
		}
		
		this.addBonus(new Jeton(carte.couleur()));
		this.cartes += 1;
		
		return true;
	}
	
	/**
	 * Add the bonus token given ti the player.
	 * 
	 * @param jeton_bonus
	 *        Token considered like the bonus to add
	 */
	private void addBonus(Jeton jeton_bonus) {
		
		Objects.requireNonNull(jeton_bonus);
		
		this.bonus.put(jeton_bonus.couleur(), this.bonus.get(jeton_bonus.couleur()) + 1);
	}
	
	/**
	 *  This method allows the AI to choose a card
	 * 
	 * @param mode_jeu 
	 * the game mode choosen by the users 
	 * @param numero 
	 * number of the previous tested card
	 * @param niveau 
	 * level of the previous tested card
	*  @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	public static HashMap<Integer, Integer> choixAchatCarteNonReserveeIA(int mode_jeu, int numero, int niveau) {
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException("mode_jeu must be 1 or 2");
		}
		
		if(numero < 0 || niveau < 0) {
			throw new IllegalArgumentException();
		}
		/*
		 * création de la carte à tester
		 */
		var carte = new HashMap<Integer,Integer>();
		carte.put(numero, niveau);
		return carte;
		
	}
	
	/**
	 *  This method test if the user can validate or invalidate a iabuying attempt
	 * 
	 * @param ia
	 *        the player whom we will look at the reserve and the ressources
	 *        
	 * @param carte_numero
	 *        the number of the card that the user wants to buy. It allows to identify it.
	 * @param game
	 * 		the game state
	 *        
	 *  @return a boolean which the value indicates either the operation succeed or failed
	 *  
	 *  true = success
	 *  false = failure
	 */
	private boolean testIavalidationAchatReserve(IA ia, int carte_numero, Mode game) {
		
		Objects.requireNonNull(ia);
		Objects.requireNonNull(game);
		
		if(carte_numero < 0) {
			throw new IllegalArgumentException();
		}
		
		var game_copie = game.deepClone();
		if(carte_numero <= 0) {
			return false;
		}
		/* it tries to buy a card*/
		if(ia.acheteCarte(ia.reserve().get(carte_numero-1), game_copie)) {
			return true;
		}
		return false;
	}
	/**
	 * This function check if an ai can buy one of its reserved card. 
	 * If it can buy a reserved card it fill the filed next_achat with the buyable card and achat_type with 1.
	 * 
	 * @param game
	 * 	the game state
	 * @return If it can buy a reserved card, it returns true otherwise, it returns false.
	 */
	public boolean TestIaBuyableReservedCard(Mode game){
		
		Objects.requireNonNull(game);
		
		var game_copie = game.deepClone();
		var ia = (IA)this.clone();
		boolean valide;
		
		/* cas où la partie avec les cartes réservées n'est pas vide*/
		if(this.reserve().size() > 0) {
			for(var carte_num = 0; carte_num <= this.reserve().size();carte_num++) {
				/* cas où le numéro de la carte est valide*/
				if(testIavalidationAchatReserve(ia, carte_num, game_copie)) {
					/* on supprime les anciennes cartes à acheter*/
					if(next_achat.size() >= 1) {
						this.next_achat.clear();
					}
					/* le user choisit la carte qu'il va prendre*/
					this.next_achat.put(-1, carte_num);
					/* le user prend une carte réservée*/
					/* à supprimer*/
					System.out.println(" l'ia affecte le choix d'acheter dans la réserve \n");
					this.achat_type = 2;
					return true;
				}	
			}
		}
		/* échec, aucune carte n'est achetable*/
		return false;

	}

	/**This function do a deep copy of an AI.
	 * 
	 * @return the copy which has its own references.
	 */
	@Override
	protected Object clone(){
		
		var copie1 = new Copie();
		IA copie = new IA(this.pseudo(), this.age(),this.points_prestiges());
		copie.cartes = this.cartes;
		copie.ressources = copie1.copieHashmap(this.ressources);
		copie.bonus = copie1.copieHashmap(this.bonus);
		
		/* à finir la copie de la réserve n'a pas été faite*/
		/*copie.reserve = this.reserve.clone();*/
		/*on permet à la liste d'avoir la bonne taille car sinon la copie échoue*/
		for(var i = 0; i < this.reserve.size(); i++) {
			copie.reserve.add(this.reserve.get(0));
		}
		Collections.copy(copie.reserve, this.reserve);
		return (IA)copie;	
	}
	/**
	 * This function handle the use of
	 * @param affichage
	 * A variable which handles what should be display
	 * @param game
	 * the game state
	 * @return HashMap
	 * An HashMap which contains the card informations : number and level
	 */
	@Override
	public HashMap<Integer, Integer> reservationCartePlateau(Affichage affichage, ModeII game) {
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(game);
		
		return randomCardChoosenReservation(affichage, game);
	}
	
	/**
	 * This method enables to the user to choose the card's level he wants to reserve
	 * 
	 * 
	 * @return int
	 * an int which represents the card level between 1 and 3.
	 * 
	 */
	public int reservationCartePioche(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		/* renvoie un niveau de carte obtenu alétoirement dont le niveau est compris entre 1 et 3*/
		return ran.nextInt(2)+1;
	}
	
	
	/**
	 *  This method allows the player to buy a card that were placed in his reserve.
	 * 
	 * @param affichage
	 * a variable which is used to display information
	*  @return an int which 
	*  is the chronological order of appearance of the chosen card
	*  -1 : there is no card that had been reserved
	 */
	public int achatCarteReservee(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		/* cas où il n'y a pas de carte dans la réserve ou qu'on ne souhaitait pas acheter*/
		if(achat_type != 2)
			return -1;
		affichage.affichageMessageActions("\nChoisissez votre numero de carte parmi les suivantes \n");
		affichage.showReserved(this);
		System.out.println(" " +next_achat.get(-1));
		return next_achat.get(-1); 
	}
	
	/**
	 *  This method allows the player to choose a card
	 * 
	 * @param mode_jeu 
	 * the game mode choosen by the users 
	*  @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	public HashMap<Integer, Integer> achatCarteNonReservee(int mode_jeu, Affichage affichage){
		
		Objects.requireNonNull(affichage);
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException();
		}
		
		affichage.affichageMessageActions("\n\nChoisissez le numéro de la carte à acheter \n");
		affichage.affichageMessageActions("Niveau - N° Carte");	
		/* cas normal : l'ia achète pcq c'était possible*/
		if(this.next_achat != null) {
			String str = " "+ next_achat.entrySet().stream().findFirst().get().getKey() + "  -  "+next_achat.entrySet().stream().findFirst().get().getValue();
			String[] tab = {str};
			affichage.affichageMessageActions(tab[0]);
			return this.next_achat;
		}
			
		/* cas où il ne pouvait pas acheter : on met une carte random, le problème sera corrigé plus tard
		 * mais normalement ce cas n'arrive jamais
		 */
		
		var carte = new HashMap<Integer,Integer>();
		carte.put(1,1);
		return carte;
	}
	
	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * @param affichage
	 * 		NO DESCRIPTION
	 * @param game
	 *  	the game state
	*  @return int 
	*  an int which is the chronological order of appearance of the chosen card
	 */
	public int choixJeton(Affichage affichage, Mode game){
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(game);
		
		affichage.affichageMessageInstructionsBox("(1) Prendre 2 jetons de la même couleur\n(2) Prendre 3 jetons de couleurs différentes\n(3) Annuler votre action \n");
		var nb = iaRandomTokensNum(game);
		/* on affiche le choix de l'ia*/
		System.out.println(nb);
		return nb;
	}
	
	/**
	 * Performs the input of a token. Returns a token if the token has successfully been created.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return Token created with the informations given
	 */
	public Jeton saisieJeton() {
		/* on fait un contrôle, mais cette situation n'est pas censée arriver grâce aux contrôles précédents*/
		if(next_token == null) {
			var jeton = new Jeton("Rouge");
			System.out.println("Rouge");
			return jeton;
		}
		System.out.println("  "+next_token);
		/* cas normal, l'ia utilise un token qu'il avait réservé exprès pour ça et testé au préalable*/
		var jeton = new Jeton(next_token);
		return jeton;
	}
	
	/**
	 *  enables to the user to choose a noble among a list of nobles
	 *  @param mode
	 *  The game state
	 *  The player who will choose a noble
	 *  @param nobles_visiting
	 *  The nobles that the player can choose
	 *   @return Tuile
	 *  It represents the nobles that has been
	 * 
	 */
	public Tuile choixNoble(Mode mode, Affichage affichage, ArrayList<Tuile> nobles_visiting) {
		
		Objects.requireNonNull(mode);
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(nobles_visiting);
		
		var nobles_name = new StringBuilder();
		var separator = "";
		
		for(var elem : nobles_visiting) {
			
			nobles_name.append(separator).append(elem.name());
			separator = ", ";
		}
		affichage.affichageMessageActions("\n.....Un noble vient à votre visite\nIl s'agit de....." + nobles_name + " !\n\nChoisissez en un\n\n");
		Saisie.passer(affichage);
		affichage.affichageMessageActions("Nous vous rappelons que leurs cartes sont les suivantes : ");
		Saisie.passer(affichage);
		affichage.showTuiles(mode);
		int choix = 1+(ran.nextInt((nobles_visiting.size()-1))); /*Saisie.choixIntervalle(1,nobles_visiting.size(), affichage, 0) -1;*/
		return nobles_visiting.get(choix);
	}
	
	/**
	 *  This method allows the IA to choose from where he plans to reserve a card
	 * 
	*  @return int
	*  an int which represents the place from where the user wants to take its card.
	*  1 : from the board
	*  2 : from the pile
	 */
	public int choixReservation(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		/*Réservation de cartes*/
		affichage.affichageMessageInstructionsBox("\n");
		affichage.affichageMessageInstructionsBox("1) Réserver une carte du board\n2) Réserver une carte d'une des pioches\n\n");
		int val = ran.nextInt(1)+1;
		System.out.println(" "+val);
		return val;
	}
	
	/**
	 *  This method allows the player to buy a card
	 * 
	 * @param game 
	 * 		the game mode choosen by the users
	*	@param affichage
	 * 			No description
	*  @return int 
	*  		the new game state
	 */
	@Override
	public int choixAchatCarte(Mode game, Affichage affichage) {
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);
		
		System.out.println("\n1)  visible\n2) Acheter une carte réservée");
		System.out.println("" +achat_type);
		return achat_type;
	}
}