package fr.umlv.affichage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.awt.Color;
import java.lang.StringBuilder;

import fr.umlv.game.Partie;
import fr.umlv.game.mode.*;
import fr.umlv.players.*;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;


/**
 * Declaration of the class AffichageLigneCommande. It gathers all the functions thah print objets on
 * the console
 * 
 * @author dylandejesus nathanbilingi
 */
public class AffichageLigneCommande implements Affichage{
	
	/**
	 * Launches the game with the display(Affichage)
	 */
	public void launchAffichage() {
		Partie.startGame(this);
	}
	
	
	/**
	 * Print the game board of a game.
	 * 
	 * @param game
	 *        Game to print its board
	 * 
	 * @param mode
	 *        Game mode
	 */
	@Override
	public void showPlateau(Mode game, int mode) {
		
		Objects.requireNonNull(game);
		
		if(mode != 1 && mode != 2) {
			throw new IllegalArgumentException();
		}
		
		if(mode != 1) {
			showTuiles(game);
		}
		
		showBoard(game);
		showJeton(game.jetons_disponibles(), null, "JETON");
		
		System.out.println("\n\n========================================== JOUEURS ==========================================\n\n");
		
		for(var joueur : game.joueurs()) {
			showJoueur(joueur, game);
			System.out.println("\n");
		}	
	}
	
	/**
	 * It displays the fact of cancelling an action
	 * 
	 * @param game
	 *        Game mode
	 */
	@Override
	public void afficheAnnulation(Mode game) {
		
		Objects.requireNonNull(game);
		return;
	}
	
	/**
	 * Returns the number of the mode choosen
	 * 
	 * @return Number of the mode (1, 2)
	 */
	@Override
	public int recupMode() {
		return recupTouche();
	}

	/**
	 * Returns the number of players
	 * 
	 * @return Number of players(2,4)
	 */
	@Override
	public int recupParticipants() {
		return recupTouche();
	}
	
	/**
	 * Returns the number of the instruction
	 * 
	 * @return Index of the Instruction
	 */
	@Override
	public int recupTouche() throws IllegalArgumentException{	/*On fera plutôt passer un scanner*/
		
		Scanner scanner = new Scanner(System.in);
		
		var scan = new Scanner(System.in);
		
		
		return scan.nextInt();
	}
	
	/**
	 * It shows the instructions with the message.
	 * 
	 * @param message
	 *        message to print
	 */
	@Override
	public void affichageMessageInstructionsBox(String message) {
		
		Objects.requireNonNull(message);
		
		affichageMessage(message, 0);
	}
	
	/**
	 * It shows the actions with the message.
	 * 
	 * @param message
	 *        message to print
	 */
	@Override
	public void affichageMessageActions(String message) {
		
		Objects.requireNonNull(message);
		
		affichageMessage(message, 0);
		
	}
	
	/**
	 * It shows a message on the display.
	 * 
	 * @param message
	 *        message to print
	 */
	@Override
	public void affichageMessage(String message, int nb_choices) {
		
		Objects.requireNonNull(message);
		
		System.out.println(message);
	}
	
	/**
	 * It shows (in the console) the board of tokens. (color and Value).
	 * 
	 * @param banque
	 *        Map of tokens to print
	 *        
	 * @param chaine_tab
	 * 		  String which represents the board
	 * 
	 * @param chaine_noms
	 * 		  String which represents all the names of color
	 * 
	 * @param chaine_quantite
	 * 		  String which represents the board the number of tokens
	 * 
	 */
	private void showBanqueOrRessources(HashMap<String, Integer> banque,StringBuilder chaine_tab, StringBuilder chaine_noms, StringBuilder chaine_quantite) {

		Objects.requireNonNull(chaine_tab);
		Objects.requireNonNull(chaine_noms);
		Objects.requireNonNull(chaine_quantite);
		
        String separator = "|    ";
        String separator_quantite = "|      ";
        
        if(banque != null) {
        	
            for(var entry : banque.entrySet()) {
                
                var jeton_name = entry.getKey();
                
                if(jeton_name.equals("Vert") || jeton_name.equals("Noir") || jeton_name.equals("Bleu")) {
                    jeton_name = jeton_name + " ";
                }
                var jeton_quantite = entry.getValue();
                
            
                 chaine_noms.append(separator).append(jeton_name);
                 chaine_quantite.append(separator_quantite).append(jeton_quantite);
                 
                 separator = "    |    ";
                 separator_quantite = "      |      ";
            }
        }
        System.out.println(chaine_tab);
        System.out.println(chaine_noms);
        System.out.println(chaine_tab);
        System.out.println(chaine_quantite);
        System.out.println(chaine_tab);
    }
	
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
	@Override
    public void showJeton(HashMap<String, Integer> banque, HashMap<String, Integer> ressources, String message) {
        
        StringBuilder chaine_tab = new StringBuilder();
        StringBuilder chaine_noms = new StringBuilder();
        StringBuilder chaine_quantite = new StringBuilder();
        
        
        if(message != null) {
            System.out.println("\n\n========================================== "+ message + " ==========================================\n\n");
        }
        if(banque != null) {
            showBanqueOrRessources(banque,chaine_tab,chaine_noms, chaine_quantite);
            
        }
        
        if(ressources != null) {
            StringBuilder chaine_tab2 = new StringBuilder();
            StringBuilder chaine_noms2 = new StringBuilder();
            StringBuilder chaine_quantite2 = new StringBuilder();
            showBanqueOrRessources(ressources,chaine_tab2,chaine_noms2, chaine_quantite2);
        }
    }

	
	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	@Override
	public void showBoard(Mode game) {
		
		Objects.requireNonNull(game);
		
		for(int i = 1 ; i < game.board().size() + 1 ; i++) {
			
			var num_card = 1;
			
			if(game.board().get(i).get(0) != null) {
				
				System.out.println("\n\n    -- NIVEAU " + i + " --\n\n");
				
				for(var carte : game.board().get(i)) {
					
					System.out.println("\n      Carte n° " + num_card + "\n");
					
					if(carte != null)
						System.out.println(carte);
					num_card ++;	
				}
			}
		}
	}
	
	/**
	 * Print the player informations and other informations of the game to complete player's one.
	 * 
	 * @param joueur
	 * 	      Player given to show its game infomations 
	 * 
	 * @param game
	 *        Game needed to complete informations
	 */
	public int showJoueur(Participant joueur, Mode game) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(game);
		
		System.out.println("Joueur : " + joueur.pseudo() + "\n\nPoints de prestiges : " + joueur.points_prestiges() + "\n\nRessources : \n");
		System.out.println("Joueur :  " + "\n");
		showJeton(joueur.ressources(), null, "JETON");
		showJeton(joueur.bonus(), null, "BONUS");
		this.showReserved(joueur);
		return 0;
	}
	
	/**
	 * Print the Nobles Cards on the board.
	 * 
	 * @param game
	 *        Game given to show its nobles card on the board
	 */
	@Override
	public void showTuiles(Mode game){
		
		Objects.requireNonNull(game);
		
		System.out.println("    -- NOBLES --   \n\n");
		
		for(int i = 0 ; i < game.tuiles_board().size(); i++) {
			
			System.out.println(game.tuiles_board().get(i));
		}
	}
	
	/**
	 * Print the cards reserved by a player.
	 * 
	 * @param joueur
	 * 		  Player given.
	 */
	@Override
	public void showReserved(Participant joueur) {
		
		Objects.requireNonNull(joueur);
		
		if(joueur.reserve().size() == 0) {
			System.out.println("Vous ne possèdez aucune carte réservée \n\n");
			return;
		}
		
		System.out.println("Cartes que vous avez réservé \n\n");
		
		for(var elem : joueur.reserve()) {
			System.out.println(elem + "\n");
		}
	}
	
	/**
	 * Display of the turn switch between two players
	 * 
	 * @param scan
	 *        Scanner to use to recup the new line
	 */
	@Override
	public void turnChange(Scanner scan) {
		
		Objects.requireNonNull(scan);
		
		scan.nextLine();
	}
}
