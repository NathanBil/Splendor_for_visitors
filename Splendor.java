package fr.umlv.game;

import fr.umlv.affichage.Affichage;
import fr.umlv.saisie.Saisie;


/**
 * Class which represents the beginning of the Splendor Game 
 * 
 * @author dylandejesus
 */
public class Splendor {

	/**
	 * This makes the beginning of a Splendor Game by launching the dysplay mode.
	 * 
	 * @param args
	 * 		 Arguments on the console
	 * 	
	 */
	public static void main(String[] args) {
		
		int affichage_mode = Saisie.saisieAffichage();
		
		Affichage affichage = Partie.createAffichage(affichage_mode);
		
		affichage.launchAffichage();
		
	}
}

