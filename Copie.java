package fr.umlv.copie;

import java.util.HashMap;
import java.util.Objects;


/**
 * It makes the copy of some game states. List copies...
 * 
 * @author dylandejesus
 */
public class Copie {
	
	/**This function do a deep copy of an HashMap.
	 * @param <T>
	 * 	type key
	 * @param <U>
	 * 	type value
	 * @param hash
	 * 	      The hashmap that one wants to copy.
	 * @return hashMap
	 * 		  A	copy which has its own references.
	 */
	public <T,U>HashMap<T,U> copieHashmap(HashMap<T,U> hash) {
		
		Objects.requireNonNull(hash, "the hashmap argument can't be null");
		
		HashMap<T,U> copie = new HashMap<T,U>();
		
		if(hash.size() == 0)
			return copie;
		
		/* copie les éléments*/
		for(HashMap.Entry<T,U> entry : hash.entrySet()){
			
			var cle = entry.getKey();
			var value = entry.getValue();
			
			/* si on tombe sur une chaine on fait une copie profonde de chaque char*/
			if(cle instanceof String cle3) {
				
				/* ça semble inévitable ici de faire un cast. Nous savons que c'est un string mais le compilateur non.
				 * Mais c'est à éviter normalement*/
				cle =  (T) copieChaine(cle3);
			}
			
			/* ça semble inévitable ici de faire un cast. Nous savons que c'est un string mais le compilateur non.
			 * Mais c'est à éviter normalement*/
			if(value instanceof String value3) {
				value =  (U) copieChaine(value3);
			}
			copie.put(cle, value);
		}
		/* renvoie la copie qui a ses propres références pour les valeurs*/
		return copie;
	}
	
	/**This function do a deep copy of a String.
	 * 
	 * @param src
	 *        The String that one wants to copy.
	 * 
	 * @return the copy which has its own references.
	 */
	public String copieChaine(String src) {
		
		Objects.requireNonNull(src);
		
		var dest = new String();
		var builder = new StringBuilder();
		
		for(var i =0; i < src.length(); i++) {
			builder.append(src.charAt(i));
		}
		
		dest = builder.toString();
		return dest;
	}
}