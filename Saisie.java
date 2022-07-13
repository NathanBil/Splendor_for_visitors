package fr.umlv.saisie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.objects.*;
import fr.umlv.affichage.*;
import fr.umlv.players.*;
import fr.umlv.game.mode.*;

/**
 * Declaration of the class Saisie. It gathers all the functions which are in relation
 * with the user. It asks to his answers in the console
 * 
 * @author dylandejesus nathanbilingi
 */
public class Saisie {
	
	/**
	 * Performs the input of a token. Returns a token if the token has successfully been created.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return Token created with the informations given
	 */
	public static Jeton saisieJeton(){
		
		Scanner scanner = new Scanner(System.in);
		String text = scanner.next();  //User rentre "Violet"
 
		while(!isExistingColours(text)){  //La méthode renvoie true donc on entre dans la boucle

		    System.out.println("Valeur non recevable, veuillez entrer une nouvelle fois la couleur.\n");
		    text = scanner.next();  //User rentre "Vert"
		}
		
		var jeton = new Jeton(text);  // Jeton Vert

		return jeton;
	}
	
	/**
	 * Performs the input of a token name. Returns a the token's name if the token name exists.
	 * The user is guided by an exchange between him and the console.
	 * 
	 * @return String created with the informations given
	 */	
	public static String saisieJeton_name(){
			
		Scanner scanner = new Scanner(System.in);
		String text = scanner.next();  //User rentre "Violet"
	 
		while(!isExistingColours(text)){  //La méthode renvoie true donc on entre dans la boucle
	
			System.out.println("Valeur non recevable, veuillez entrer une nouvelle valeur.\n");
			text = scanner.next();  //User rentre "Vert"
		}
			
		return text;
	}
	
	/**
	 * Creates a player from the information the user provides. The user enters the name/nickname 
	 * and his age.
	 * 
	 * @return Player created
	 */
	public static Joueur saisieJoueur(){
		
		Scanner scanner = new Scanner(System.in);
		Joueur joueur = null;
		
		do{
				try {
					joueur = new Joueur(scanner.next(), Integer.parseInt(scanner.next()));
			}catch(Exception e) {
				System.out.println("Erreur : Entrez un nom avec des lettres et un âge avec des nombres, pas des lettres !");
			}
		}while(joueur == null);
		
		System.out.println("joueur enregistré avec succès !");
		return joueur;
	}
	
	/**
	 * Determines if the given String represnts an existing color in the game. If the color exists
	 * it returns true and false if it doesn't exist.
	 * 
	 * @param chaine
	 *        String thaht represents the colour
	 * 
	 * @return True if it exists or False
	 */
	public static Boolean isExistingColours(String chaine){
		
		Objects.requireNonNull(chaine);
		
		if(chaine.equals("Vert") != true && chaine.equals("Rouge") != true && chaine.equals("Bleu") != true 
				&& chaine.equals("Blanc") != true && chaine.equals("Noir") != true) {
			return false;
		}
		return true;
	}
	
	/**
	 * This can verify the fact leaving a certain quantity of tokens.
	 * 
	 * @param joueur
	 *        Player with the hand
	 *        
	 * @param jeton
	 *        Color of the token
	 * 
	 * @param nombre
	 *        Number of tokens to put to leave
	 * @return
	 * 		A boolean which indicates if the player can throw token
	 * 		true == yes
	 * 		false == no
	 */
	public static Boolean valideDefausse(Participant joueur, String jeton, int nombre){
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(jeton);
		
		if(nombre <= 0) {
			System.out.println("Cette action est inutile car vous vous êtes défaussés de  0 jeton. ");
			System.out.println("On recommence ! ");
			System.out.println("Couleur : ");
			return false;
		}
		
		else if(joueur.ressources().get(jeton) >= nombre){
			return true;	
		}
		
		System.out.println("Vous n'avez pas suffisamment de ressource de ce type, il faut réessayer.  ");
		return false;
	}
	
	/**
	 * Returns the amount of tokens to leave.
	 * 
	 * @return the amount
	 */
	public static int nb_jeton_defausse(){
		
		Scanner scan = new Scanner(System.in);
		
		int nb;
		
		do{
			try {
				nb = scan.nextInt();
				return nb;
				
			}catch(Exception e) {
				System.out.println("Erreur : Veuillez entrer un numero de jeton valide : ");
			}
		}while(true);
		
	}
	
	/**
	 *  This method allows the player to write a value if it is between two others (both are included)
	 * 
	 * @param v1 
	 *		 The lowest value that the user can write 
	 *
	 * @param v2
	 *       The highest value that the user can write
	 * @param mode
	 * 		the game mode
	 * @param affichage
	 * 		show the game.
	 *       
	 * @return int 
	 * 		the choosen value
	 */
	public static int choixIntervalle(int v1, int v2, Affichage affichage, int mode) {
		
		//Affichage can be null (at the beginning)
		
		if(mode != 1 && mode != 2 && mode != 0) {	//Au départ on a pas le mode, donc mode peut valoir 0 aussi
			throw new IllegalArgumentException();
		}
		
		Scanner scan = new Scanner(System.in);
		int choix;
		
		do{
				try {
					
					if(affichage == null) {
						choix = scan.nextInt();
					}else {
						if(mode == 1) {
							choix = affichage.recupMode();
						}
						
						else if(mode == 2) {
							choix = affichage.recupParticipants();
						}
						
						else {
							choix = affichage.recupTouche();
						}
					}
			
			}catch(Exception e) {
				
				if(affichage == null) {
					System.out.println("Erreur : Veuillez entrez une valeur entre " + v1 + " et " +v2);
					scan.next();
				}
				
						
				choix = -1;
			}
				
		}while(choix < v1 || choix > v2);
		
		return choix;
	}
	
	/**
	 * Check if the input is indeed a number.
	 * 
	 * @return the integer written by the user
	 */
	public static int captureInt() {
		
		Scanner scan = new Scanner(System.in);
		int choix;
		do{
			try {
				choix = scan.nextInt();
				return choix;
			}catch(Exception e) {
			System.out.println("Erreur : Veuillez entrer un numero de carte valide : ");
			}
		}while(true);
	
	}
	
	
	/**
	 * The card choice. This method enable the user to choose a valid card value. If they fail it resumes thanks to a loop.
	 * 
	 * @return a String[] which contains the choosen card informations : its number and its level.
	 */
	public static String[] choixCarte() {
		
		Scanner scan = new Scanner(System.in);
		int succes = -1;
		String[] tab = {"a"};
		
		do{
				try {
					tab = scan.next().split("-");
					/* test pour voir si les valeurs récupérées sont au bon format, en cas d'erreur on retry*/
					var choosen_card = Integer.parseInt(tab[1]) - 1;
					var ligne_choosen = Integer.parseInt(tab[0]);
					succes = 1;
			}catch(Exception e) {
				System.out.println("Erreur : veuillez écrire au format : Niveau - N° Carte ");
				/*scan.next();*/
				succes = -1;
			}
		}while(succes == -1);
		
		return tab;
	}
	
	/**
	 * It checks thes arguments for the reservation of the cards by the player.
	 * 
	 * @param game
	 *        Game mode needed
	 * 
	 * @param niveau_carte
	 *        Card level
	 * 
	 * @param num_carte
	 *        Index of the card
	 * 
	 * @return 0 if it is not allowed then returns 1
	 */
	public static int carte_reserve_valide_2arg(Mode game, int niveau_carte, int num_carte) {
		
		Objects.requireNonNull(game);
		
		try {
			game.board().get(niveau_carte).get(num_carte);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	
	}

	/**
	 * Check if the card is pickable.
	 * 
	 * @param game
	 *        Game mode needed
	 *        
	 * @param niveau_carte
	 *        Index of the card
	 *        
	 * @return int
	 * 		the card level is correct
	 */
	public static int carte_reserve_valide_1arg(Mode game, int niveau_carte) {
		
		Objects.requireNonNull(game);
		
		try {
			game.pioche().get(niveau_carte).get(game.pioche().size() - 1);
		}catch(Exception e) {
			return 0;
		}
		return 1;
	}
	
	/**
	 * Enables the user to choose the party type he wants.
	 * 
	 * @param affichage
	 *         This variable is used to show the game.
	 *         
	 * @return int
	 * 		The value of the mode choosen (1 or 2)
	 */
	public static int saisieMode(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		int mode_jeu;

		affichage.affichageMessage("Quel mode de jeu (1 ou 2) choisissez-vous ?", 2);
		
		mode_jeu = choixIntervalle(1,2, affichage, 1);
		
		return mode_jeu;
	}
	
	
	/**
	 * Enables the user to choose the party type he wants.
	 * 
	 *        
	 * @return int
	 * 		the display type.
	 */
	public static int saisieAffichage() {
		
		int mode_affichage;
		System.out.println("Quel affichage souhaitez vous ? (1) Affichage en ligne de commande, (2) Affichage Graphique  =>  ");
		mode_affichage = choixIntervalle(1,2, null, 0);
		
		return mode_affichage;
	}
	
	/**
	 * Allows the users to choose the number of player they wants.
	 * 
	 * @param max
	 *        This variable gives information regarding the maximum number of player for the current party
	 *        
	 * @param affichage
	 *       this variable is used to show the game state
	 *        
	 * @return int
	 * 		the number of players for the game
	 */
	public static int choixNbJoueurs(int max, Affichage affichage) {
			
		Objects.requireNonNull(affichage);
		
		affichage.affichageMessage("Combien de joueurs participent à la partie (choisissez un nombre entre 2 et 4) ?", 4);
		var nb_joueurs = Saisie.choixIntervalle(2, max, affichage, 2);

		return nb_joueurs;	
	}
	
	/**
     * Allows the users to write their names, and their age
     * 
     * @param game
     *        This variable gives information regarding the current party
     *        
     * @param mode_jeu
     *        This variable gives information regarding the game mode (1 or 2)
     *  @param nb_joueurs
     *  		the number of player for this current party
     *        
     * @param affichage
     *        this variable is used to show the game graphically
     *        
     * @return the game with the players
     */
    public static Mode saisieJoueurs(Mode game, int mode_jeu, int nb_joueurs, Affichage affichage) {
    	
        Objects.requireNonNull(game);
        Objects.requireNonNull(affichage);
        
        if(mode_jeu != 1 && mode_jeu != 2) {
            throw new IllegalArgumentException("mode_jeu !=1 && mode_jeu != 2");
        }
        
        if(nb_joueurs < 2 || nb_joueurs > 4) {
            throw new IllegalArgumentException("nb_joueurs > 4 or nb_joueurs < 2");
        }
        
        for(int i = 1; i <= nb_joueurs ;i++) {
            var choix = 1;
            if(i == 1 && mode_jeu != 2) {
                System.out.println("Veuillez entrer le nom et l'âge du joueur " + i);
                Participant joueur1 = Saisie.saisieJoueur();    
                game.addPlayer(joueur1);
                i++;
                
            }
            if(mode_jeu == 2) {
                System.out.println("Voulez-vous créer un humain (1) ou une IA (2) pour le joueur numéro "+i);
                choix = choixIntervalle(1, 2, null, 0);
            }
            /* on crée un user*/
            if(choix == 1) {
                System.out.println("Veuillez entrer le nom et l'âge du joueur " + i);
                Participant joueur1 = Saisie.saisieJoueur();    
                game.addPlayer(joueur1);
            }
            /* on crée une IA*/
            else if(choix == 2) {
                IA joueur1 = new IA("IA", 100 ,0);    
                game.addPlayer(joueur1);
            }
            
        }
        
        
        return game;
    }

	
	/**
	* This method allows to write the player's answers for each game option.
	*
	* @param mode_jeu 
	*        It refers to the gameplay choosen by the users
	* @param affichage
	* 		it is used to show the game
	*
	* @return it returns an int which allow to know what actions the player wants to do 
	*/
	public static int menuSaisie(int mode_jeu, Affichage affichage) {
		
			Objects.requireNonNull(affichage);
			
			if(mode_jeu != 1 && mode_jeu != 2) {
				throw new IllegalArgumentException();
			}
		
	        int answer;
	        String chaine;

	        if(mode_jeu != 1 && mode_jeu != 2) {
	            throw new IllegalArgumentException(" 'mode_jeu' must be a value between 1 and 2");
	        }
	        
	        chaine = "(1) Acheter une carte\n(2) Prendre des ressources\n(3) Voir mes informations";
	    
	        if(mode_jeu != 1) {
	            chaine = chaine + "\n(4) Réserver une carte";
	        }
	        
	        affichage.affichageMessageInstructionsBox(chaine);
	        
	        answer = choixIntervalle(1, 4, affichage, 0);

	        return answer;        
	}
	
	/**
	 *  This method allows the player to buy a card.
	 * 
	 * @param game
	 *        The game state
	 *        
	 * @param affichage
	 *        This variable is used to show the game graphically.
	 *  
	 * @return 
	 * 		the new game state
	 */
	public static int choixAchatCarte(Mode game, Affichage affichage) {
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(affichage);

		affichage.showBoard(game);
		
		affichage.affichageMessageInstructionsBox("1) Acheter une carte face visible\n2) Acheter une carte réservée");
		
		return Saisie.choixIntervalle(1, 2, affichage, 0);
	}
	
	/**
	 *  This method allows the player to choose a card
	 * 
	 * @param mode_jeu 
	 *        The game mode choosen by the users 
	 *  @param affichage
	 *  		it is used to show the game
	 * @return an Hashmap which contains the card data : its number and its level. key = level, value = number
	 */
	public static HashMap<Integer, Integer> achatCarteNonReservee(int mode_jeu, Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		if(mode_jeu != 1 && mode_jeu != 2) {
			throw new IllegalArgumentException("mode_jeu must be 1 or 2");
		}
		
		int choosen_card;
		
		/* on attribue à ligne_choosen la valeur un par défaut, elle sera toujours utilisée telle quelle si on est dans le mode de jeu 1*/
		int ligne_choosen = 1;
		affichage.affichageMessageActions("\n\nChoisissez le numéro de la carte à acheter \n");
		
		if(mode_jeu != 1) {
			
			affichage.affichageMessageActions("Niveau - N° Carte");	
			var tab = Saisie.choixCarte();
			
			affichage.affichageMessageActions(tab[1]);
			
			choosen_card = Integer.parseInt(tab[1]) - 1;
			ligne_choosen = Integer.parseInt(tab[0]);
	
		}else {
			choosen_card = Saisie.captureInt()- 1;
		}	
		
		var carte = new HashMap<Integer,Integer>();
		carte.put(ligne_choosen, choosen_card);
		
		return carte;	
	}
	

	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * 
	 * @param joueur
	 *        The player whom we will look at the reserve
	 * @param affichage
	 * 		it is used to show the game
	 *       
	 * @return 
	 * 		an integer which is the chronological order of appearance of the chosen card
	 * 
	 *  -2 : le numéro de carte n'existe pas
	 *  -1 : il n'y a pas de carte dans la réserve.
	 *  1 : transaction succeed 
	 *  another number means : the card position +1 
	 */
	public static int achatCarteReservee(Joueur joueur, Affichage affichage) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(affichage);
		
		/* cas où la partie avec les cartes réservées n'est pas vide*/
		if(joueur.reserve().size() > 0) {
			
			affichage.affichageMessageActions("\nChoisissez votre numero de carte parmi les suivantes \n");
			affichage.showReserved(joueur);
			
			var carte_numero = Saisie.captureInt();
			
			/* cas où le numéro de la carte est valide*/
			if(carte_numero <= joueur.reserve().size() && carte_numero > 0) { 
				
				return carte_numero;
			}
			
			/* cas où le numéro de la carte n'est pas valide*/
			else {
				affichage.affichageMessageActions("\nCe numéro de carte n'existe pas !\n");
				return -2;
			}
		}
		/* cas où la partie avec les cartes réservées est vide*/
		else {
			/* échec, la partie avec les cartes réservées est vide*/
			affichage.affichageMessageActions("\nVous n'avez pas réservé de carte ! Cette action est donc impossible ! \n");
			affichage.affichageMessageInstructionsBox("\nVeuillez réserver une carte avant de vouloir acheter une carte réservée ! \n");
			return -1;
		}
	}
	

	/**
	 *  This method allows the player to buy a card that were placed in his reserve
	 * 
	 * @param affichage
	 *        it is used to show the game
	 *        
	 * @return int
	 * 		an int which is the chronological order of appearance of the chosen token
	 */
	public static int choixJeton(Affichage affichage){
		
		Objects.requireNonNull(affichage);
		
		affichage.affichageMessageInstructionsBox("(1) Prendre 2 jetons de la même couleur\n(2) Prendre 3 jetons de couleurs différentes\n(3) Annuler votre action");
		
		return Saisie.choixIntervalle(1,3, affichage, 0);
	}
	
	/**
	 * This method allows the player to choose from where he plans to reserve a card
	 *  
	 * @param affichage
	 *         Display needed
	 * 
	 * @return an int which represents the place from where the user wants to take its card.
	 */
	public static int choixReservation(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		affichage.affichageMessageInstructionsBox("1) Réserver une carte du board\n2) Réserver une carte d'une des pioches\n\n");
		
		var choix_scanner = Saisie.choixIntervalle(1, 2, affichage, 0);
		return choix_scanner;
	}
	
	/**
	 *  This method allows the player to choose a card from the board and reserve it
	 * @param affichage
	 * 		it is used to show the game.
	 *  @return an hashMap that represents the card that the user has chosen : key : its level and value : its number.
	 */
	public static HashMap<Integer, Integer>reservationCartePlateau(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		affichage.affichageMessageActions("Choisissez une carte du plateau\n(Niveau - N°Carte)\n");
		
		var tab = Saisie.choixCarte();
		var niveau_carte = Integer.parseInt(tab[0]);
		var num_carte = Integer.parseInt(tab[1]) - 1;
		var card = new HashMap<Integer, Integer>();
		
		card.put(niveau_carte, num_carte);
		return card;
	}
	
	/**
	 * This method enables to the user to choose the card's level he wants to reserve
	 * 
	 * @param affichage
	 * 		it is used to show the game
	 * 
	 * @return 
	 * 		an integer which represents the card level between 1 and 3.
	 * 
	 */
	public static int reservationCartePioche(Affichage affichage){
		
		Objects.requireNonNull(affichage);
		
		affichage.affichageMessageInstructionsBox("Donnez le niveau de carte à piocher\n");
		
		var niveau_carte = Saisie.choixIntervalle(1,3, null, 0);
		
		return niveau_carte;
	}
	
	/**
	 * Enables to the user to end its turn
	 * 
	 * @param affichage
	 *        Type of display used
	 * 
	 * @param player
	 *        Player who plays
	 */
	public static void saisieFinTour(Affichage affichage, Participant player){
		
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(player);
		
		affichage.affichageMessageInstructionsBox("Veuillez taper sur entrée pour confirmer la fin du tour de : "+ player.pseudo() +"\n");
		Scanner scan = new Scanner(System.in);
		
		String choix = "a";
		do{
			try {
				affichage.turnChange(scan);
				
			}catch(Exception e) {
				
				choix = null;
				affichage.affichageMessageActions("Erreur...");
				affichage.affichageMessageInstructionsBox("Veuillez taper sur entrée ");
			}
		}while(choix == null);
	}
	
	/**
	 *  Enables to the user to choose a noble among a list of nobles
	 *  
	 *  @param mode
	 *         The game state
	 *  @param affichage
	 *  	it is used to display the game 
	 *  @param joueur
	 *         The player who will choose a noble
	 *         
	 *  @param nobles_visiting
	 *         The nobles that the player can choose
	 *  
	 *  @return the noble choosen by the player
	 * 
	 */
	public static Tuile choixNoble(Mode mode, Affichage affichage, Joueur joueur, ArrayList<Tuile> nobles_visiting) {
		
		Objects.requireNonNull(mode);
		Objects.requireNonNull(affichage);
		Objects.requireNonNull(joueur);
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
		var choix = Saisie.choixIntervalle(1,nobles_visiting.size(), affichage, 0) -1;
		
		return nobles_visiting.get(choix);
	}
	
	/**
	 * This function enables to be sure that the user had had time enough to read everything happened. 
	 * It's just a transition function.
	 * 
	 * @param affichage
	 *        Display type 
	 * 
	 */
	public static void passer(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		affichage.affichageMessageInstructionsBox("Appuyez sur entrée pour continuer : ");
		Scanner scan = new Scanner(System.in);
		String choix = "a";
		do{
			try {
				choix = scan.nextLine();
			}catch(Exception e) {
			choix = null;
			
			affichage.affichageMessageActions("Erreur...");
			affichage.affichageMessageInstructionsBox("Veuillez taper sur entrée");
			
			}
		}while(choix == null);
	}
}