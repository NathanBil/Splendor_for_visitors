package fr.umlv.objects;

import java.util.Objects;

/**
 * Declaration of the type Jeton. It represents a color.
 * 
 * @author dylandejesus nathanbilingi
 */
public record Jeton(String couleur) {
	
	/**
	 * Constructor of the type Jeton. It is impossible to create a Jeton without giving any color.
	 * 
	 * @param couleur
	 *        Color given to the token created
	 */
	public Jeton{
		Objects.requireNonNull(couleur);
	}
	
	/**
	 * String representation of a token. I represents a gemstone.
	 */
	@Override
	public String toString() {
		
		if(this.couleur.equals("Vert")) {
			return "Emeraude";
		}
		if(this.couleur.equals("Rouge")) {
			return "Rubis";
		}
		if(this.couleur.equals("Bleu")) {
			return "Saphyr";
		}
		if(this.couleur.equals("Jaune")) {
			return "Or";
		}
		if(this.couleur.equals("Blanc")) {
			return "Diamant";
		}
		if(this.couleur.equals("Noir")) {
			return "Onyx";
		}

		return "Unknwon gem";
	}
}
