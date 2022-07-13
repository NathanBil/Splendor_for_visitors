package fr.umlv.game;

import fr.umlv.players.*;
import fr.umlv.game.mode.*;

import java.util.Objects;

import fr.umlv.affichage.*;
import fr.umlv.saisie.*;


/**
 * Declaration of the type Partie. It represents a game of Splendor
 * 
 * @author dylandejesus nathanbilingi
 */
public class Partie {
	
	/**
	 * It generates the game mode choosen by the user thanks to the n° of the
	 * game mode he wants to play.
	 * 
	 * @param mode 
	 *        N° of the game mode the user want   
	 * 
	 * @return The Object Mode (ModeI or ModeII)
	 */
	private static  Mode createMode(int mode){
		
		if(mode == 1) {
			return new ModeI();
		}
		
		if(mode == 2) {
			return new ModeII();
		}
		
		return null;
	}
	
	/**
	 * It generates the game display mode. If type is 1, returns a console
	 * display mode, if type is 2 returns a graphic display mode.
	 * 
	 * @param type
	 *        N° of the display mode the user want   
	 * 
	 * @return The Object Affichage
	 */
	public static  Affichage createAffichage(int type){
		
		if(type != 1 && type != 2) {
			throw new IllegalArgumentException();
		}
		
		if(type == 1) {
			return new AffichageLigneCommande();
		}
		
		return new AffichageGraphique();
	}
	

	/**
	 * It makes the start of a Splendor game. It is how a game is played
	 * 
	 * @param affichage
	 * 		  Object Affichage taht represents the display
	 */
	public static void startGame(Affichage affichage) {
		
		Objects.requireNonNull(affichage);
		
		boolean endgame = false;
		int player_turn = 0;
		Participant player;
		int tour_valide = 1;
		
		int game_mode = Saisie.saisieMode(affichage);
		
		Mode game = createMode(game_mode);
		
		Saisie.saisieJoueurs(game, game_mode, game.choixNbJoueurs(affichage), affichage);
		
		game.initialisePartie();
		
		while(!endgame) {
			/* on affiche les infos des users et du plateau ssi le tour précédent s'est bien passé*/
			if(tour_valide == 1)
				affichage.showPlateau(game, game_mode);
			
			player = game.joueurs().get(player_turn % game.joueurs().size());
			
			affichage.affichageMessageActions("Your turn " + player.pseudo());
			
			var choice = player.action(game, game_mode, affichage);
			
			if(choice == 1){
				
				tour_valide = game.achatCarte(player, affichage);
				
				if(tour_valide == 1) {
					
					//affichage.showJoueur(player, game);
				}
			}
			
			else if(choice == 2){
				tour_valide = game.priseRessource(player, affichage);
			}
			
			else if(choice == 3) {
				affichage.showJoueur(player, game);
				tour_valide = 0;
			}
			else if(choice == 4){
				tour_valide = game.reservationCarte(player, affichage);
			}
			
			
			if(tour_valide == 1) {
				
				endgame = game.isEndgame(player, player_turn, endgame);
				game.endOfTurn(affichage, player);
				player_turn ++;
			}
		}
		
		affichage.affichageMessage("Félicitations : " + game.isWinner() + " !!", 0);
	}
}




  