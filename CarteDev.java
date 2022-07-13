
package fr.umlv.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.umlv.copie.Copie;


/**
 * Declaration of the type CarteDev. A development card is an objet with a level, a bonus color, some points values 
 * (it can be 0 too), it name , a cost (number of tokens needed to pay the card)
 * 
 * @author dylandejesus nathanbilingi
 */
public record CarteDev(int niveau, String couleur, int points , String object, HashMap<String, Integer> cout) implements Carte {
	
	/**
	 * Constructor of the type  CarteDev.
	 * 
	 * @param couleur
	 * 		  Color of the card
	 *
	 * @param object
	 *        Name of the card (Ex : Francis I Of France), could be null
	 * 
	 * @param cout
	 * 		  Cost to earn the card, could be null
	 * 
	 * @param points
	 * 		  Points value it can give
	 * @param niveau
	 * 		Card level
	 */
	public CarteDev{
		
		//Le champs object peut-être nul le coût l'est au départ
		Objects.requireNonNull(couleur, "Carte de couleur null");
		
		if(points < 0) {
			throw new IllegalArgumentException("Points de prestiges négatifs de la carte");
		}
		
	}
	
	/**
	 * Create a Development Card from the information taken on a String line. The informations of the 
	 * line must be separated by a character : " - ", then with these informations it creates a card.
	 * 
	 * @param line 
	 *        Formatted String line
	 *        
	 * @return Development card created
	 */
	public static CarteDev fromText(String line) {
		
		Objects.requireNonNull(line);
		
		var couleurs = List.of("Blanc", "Bleu", "Vert", "Rouge", "Noir");
		var tab = line.split(" - ");
		var cout = new HashMap<String, Integer>();
		
		int cout_couleur;
		
		for(int i = 4; i < tab.length ;i++) {
			
			cout_couleur = Integer.parseInt(tab[i]);
			
			if(cout_couleur != 0) {
				cout.put(couleurs.get(i - 4), cout_couleur);
			}
		}
		
		return new CarteDev(Integer.parseInt(tab[0]), tab[1], Integer.parseInt(tab[2]), tab[3], cout);
	}
	
	/**
	 * Returns a formatted String of a String given. It creates a new String as it can write properly
	 * the name of a Developement card.
	 * 
	 * @param name_card
	 *        Name of the card that must be formatted
	 * 
	 * @return The new formatted name
	 */
	private static String chaineFormatte(String name_card) {			
		
		if(name_card == null) {
			return "|        mine        |\n";
		}
		int nb_espaces = (20 - name_card.length()) / 2;
		var chaine = new StringBuilder();
		var espaces = new StringBuilder();
		
		chaine.append("|");
		
		
		for(var i = 0; i < nb_espaces ;i++) {
			espaces.append(" ");
		}
		
		chaine.append(espaces).append(name_card).append(espaces);
		
		if(name_card.length() % 2 == 1) {
			chaine.append(" ");
		}
		
		chaine.append("|\n");
		
		return chaine.toString();
	}
	
	/**
	 * Do a deep copy of a development card.
	 * 
	 * @return the deep copy
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException{
		
		String objet2;
		var copie1 = new Copie();
		
		/* on fait une copie profonde des chaines de caractère*/
		objet2 = copie1.copieChaine(object);
		
		var couleur2 = copie1.copieChaine(couleur);
		var cout = copie1.copieHashmap(this.cout);
		
		CarteDev copie = new CarteDev(this.niveau(), couleur2,this.points(), objet2, cout);
		
		return copie;	
	}
	
	/**
	 * String representation of a CarteDev.  It's a card representation drawn on the console.
	 */
	@Override
	public String toString() {

		var ligne = " ―――――――――――――――――――― \n";
		var cout = new StringBuilder();
		var separator = "| ";
		
		var fin_first_line = "";
		
		if(this.couleur.length() == 5) {
			fin_first_line = "  |\n";
		}else {
			fin_first_line = "   |\n";
		}
		
		var first_line = "|  " + this.points + "          " + this.couleur + fin_first_line;
		var vide = "|                    |\n";
		var name = chaineFormatte(this.object);
		
		var fin_ligne = "";
		
		for(var elem : this.cout().entrySet()) {
			
			var couleur_name = elem.getKey();
			var couleur_val = elem.getValue();
			
			if(couleur_name.length() == 5) {
				fin_ligne = "           |\n";
			}else {
				fin_ligne = "            |\n";
			}
			cout.append(separator).append(couleur_name).append(": ").append(couleur_val).append(fin_ligne).append(vide);
		}
		
		return ligne + vide + first_line + vide + vide + name + vide + vide + cout + vide + ligne;
	}
}
