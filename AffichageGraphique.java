package fr.umlv.affichage;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.game.Partie;
import fr.umlv.game.mode.*;
import fr.umlv.objects.CarteDev;
import fr.umlv.objects.Tuile;
import fr.umlv.players.*;

/**
 * Declaration of the type Affichage. It is a class which is used to display graphically the game with a nice design.
 * 
 * @author dylandejesus nathanbilingi
 */

public class AffichageGraphique implements Affichage{
	
	
	/**
	 * Context, thaht represents the Application context of the type AffichageGraphique
	 */
	private ApplicationContext context; 
	
	/**
	 * It represents all the actions of play
	 */
	private ArrayList<String> listActions;
	
	/**
	 * Number of instructions shown in the Instruction box
	 */
	private int nb_instructions;
	
	/**
	 * Representation of the value of a click on the screen
	 */
	private int touche;
	
	/**
	 * Constructor of the type AffichageGraphique. It represents a type of display
	 * a graphic display using Zen5
	 */
	public AffichageGraphique() {
		
		this.context = null;
		this.listActions = new ArrayList<String>();
		this.nb_instructions = -1;
		this.touche = -1;
		
		for(int i =0; i < 3 ; i++) {
			this.listActions.add("");
		}
		
	};
	
	/**
	 * It displays the fact of cancelling an action
	 * 
	 * @param game
	 *        Game mode
	 */
	public void afficheAnnulation(Mode game) {
		
		Objects.requireNonNull(game);
		
		actualiseWindow(context, context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
		
		showPlateau(game, 0);
	}
	
	/**
	 * Returns the number of the mode choosen
	 * 
	 * @return Number of the mode (1, 2)
	 */
	public int recupMode() {
		
		context.renderFrame(graphics -> {
			
			this.touche = -1;
			
			float x_first = (context.getScreenInfo().getWidth() / 2) - 70;
			float y_first = ((context.getScreenInfo().getHeight() * 70) / 100);
			float x_second = (context.getScreenInfo().getWidth() / 2) + 20;
			float y_second = y_first;
			
			while(this.touche == -1) {
		
				Event event = context.pollOrWaitEvent(100);	//Attente de l'evenement
			
				while(event.getAction() != Action.POINTER_DOWN) {
					event = context.pollOrWaitEvent(100);	//Attente de l'evenement
				}
	        
				Point2D.Float location = event.getLocation();
				
				if(isInSquare(location.x, location.y, x_first, x_first + 50, y_first, y_first + 50)) {
					this.touche = 1;
					affichageMessage("Faîtes l'enregistement des deux joueurs dans le terminal", 0);
				}
				
				else if(isInSquare(location.x, location.y, x_second, x_second + 50, y_second, y_second + 50)) {
					this.touche = 2;
				}
			}
		});
		
		return this.touche;
	}

	
	/**
	 * Returns the number of players
	 * 
	 * @return Number of players(2,4)
	 */
	public int recupParticipants() {
		
		context.renderFrame(graphics -> {
			
			this.touche = -1;
			
			float x_first = (context.getScreenInfo().getWidth() / 2) - 70;
			float y_first = ((context.getScreenInfo().getHeight() * 70) / 100);
			
			float x_second = (context.getScreenInfo().getWidth() / 2) + 20;
			float y_second = y_first;
			
			float x_third = (context.getScreenInfo().getWidth() / 2) - 50 / 2;
			float y_third = y_first + 100;
			
			while(this.touche == -1) {
		
				Event event = context.pollOrWaitEvent(100);	//Attente de l'evenement
			
				while(event.getAction() != Action.POINTER_DOWN) {
					event = context.pollOrWaitEvent(100);	//Attente de l'evenement
				}
	        
				Point2D.Float location = event.getLocation();
				
				
				if(isInSquare(location.x, location.y, x_first, x_first + 50, y_first, y_first + 50)) {
					this.touche = 2;
				}
				
				else if(isInSquare(location.x, location.y, x_second, x_second + 50, y_second, y_second + 50)) {
					this.touche = 3;
				}
				
				else if(isInSquare(location.x, location.y, x_third, x_third + 50, y_third, y_third + 50)) {
					this.touche = 4;
				}	
			}
		});
		
		affichageMessage("Faîtes l'enregistement des joueurs dans le terminal", 0);
		
		return touche;
	}
	
	
	/**
	 * Returns the number of the instruction
	 * 
	 * @return Index of the Instruction
	 */
	public int recupTouche() {
		
		context.renderFrame(graphics -> {
			
			this.touche = -1;
			
			while(this.touche == -1) {
		
				Event event = context.pollOrWaitEvent(100);	//Attente de l'evenement
			
				while(event.getAction() != Action.POINTER_DOWN) {
					event = context.pollOrWaitEvent(100);	//Attente de l'evenement
				}
	        
				Point2D.Float location = event.getLocation();
				
				this.touche = this.giveTouche(context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight(), location);
			}
		});
		
		return this.touche;
	}
	
	
	
	/**
	 * Checks if the click given is in the square drawn with the values given.
	 * 
	 * @param clic_x
	 *        Click value on x
	 *        
	 * @param clic_y
	 *        Click value on y
	 *         
	 * @param x_0
	 *        Value on x (NO)       
	 * 
	 * @param x_n
	 * 		  Value on x (SE) 
	 * 
	 * @param y_0
	 *        Value on y (NO) 
	 * 
	 * @param y_n
	 *        Value on y (SE) 
	 * 
	 * @return True if the click is in the square then returns false
	 */
	private static boolean isInSquare(float clic_x, float clic_y, float x_0, float x_n, float y_0, float y_n) {

		if(clic_x > x_0 && clic_x < x_n && clic_y > y_0 && clic_y < y_n) {
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * This gives the number of the instruction that has been clicked on
	 * 
	 * @param x
	 *        Value on x
	 *        
	 * @param y
	 *        Value on y
	 * 
	 * @param location
	 *        Click location
	 * @return
	 * 		Not found
	 */
	private int giveTouche(float x, float y, Point2D.Float location) {
		
		Objects.requireNonNull(location);
		
		int touche_num = -1;
		
		float axe_x = x - ((25 * x) / 100) + ((2 * x) / 100);
		float axe_y = ((5 * y) / 100) + (((5 * y) / 100));
		
		for(int i = 0; i < this.nb_instructions; i++) {
			
			if(isInSquare(location.x, location.y, x - ((25 * x) / 100) + ((2 * x) / 100) - 2, x - ((25 * x) / 100) + ((2 * x) / 100) - 2 + 200, ((5 * y) / 100) + (((5 * y) / 100))*(i + 1) - 10, ((5 * y) / 100) + (((5 * y) / 100))*(i + 1) - 10 + 15)) {	//d = dimension en x de la box et i = place en y 
				
				return i + 1;
			}
		}
		
		return touche_num;
	}
	
	/**
	 * Launches the game with the display(Affichage)
	 */
	public void launchAffichage() {
		
		Application.run(Color.ORANGE, context -> {
			
			this.context = context;
			Partie.startGame(this);
			
			return;
		});
	}

	
	/**
	 * It shows the instructions with the message.
	 * 
	 * @param message
	 *        message to print
	 */
	public void affichageMessageInstructionsBox(String message) {
		
		Objects.requireNonNull(message);
		
		this.nb_instructions = message.split("\n").length;
		
		drawBoxInstructions(context, message);	
	}
	
	/**
	 * It shows the actions with the message.
	 * 
	 * @param message
	 *        message to print
	 */
	public void affichageMessageActions(String message) {
		
		Objects.requireNonNull(message);
		
		for(var i = 2 ; i > 0 ; i--) {
			
			if(i < this.listActions.size())
				this.listActions.set(i, this.listActions.get(i - 1));
		}
		
		if(this.listActions.size() > 0) {
			this.listActions.set(0, message);
		}
		
		
		drawBoxActions(context, this.listActions);
		
	}
	
	
	/**
	 * 
	 * It makes the window refreshing (graphically)
	 * 
	 * @param context
	 *        ApplicationContext to draw
	 *        
	 * @param largeur
	 *        Width of the screen
	 * 
	 * @param hauteur
	 *        Heigth of the screen
	 */
	private static void actualiseWindow(ApplicationContext context, float largeur, float hauteur) {
		
		Objects.requireNonNull(context);
		
		context.renderFrame(graphics -> {
			var rectangle = new Rectangle2D.Float(0, 0, largeur, hauteur);
			
			graphics.setColor(Color.ORANGE);
			
			graphics.fill(rectangle);
		});
		
	}
	
	/**
	 * It shows a message on the display.
	 * 
	 * @param message
	 *        message to print
	 */
	public void affichageMessage(String message, int nb_choices) {
		
		Objects.requireNonNull(message);
		
		if(nb_choices < 0) {
			throw new IllegalArgumentException();
		}
		
		context.renderFrame(graphics -> {
			
			var x = context.getScreenInfo().getWidth();
			var y = context.getScreenInfo().getHeight();
			
			actualiseWindow(context, x, y);
			
			var x_center = x / 2;
			var y_center = y / 2;
			
			graphics.setColor(Color.BLUE);
			graphics.setFont(new Font("SansSerif", Font.BOLD, 20));
			graphics.drawString(message, x_center - ((message.length() * 20) / 4) , y_center);
			
			if(nb_choices == 2) {
				//draw 2 carrés
				graphics.setColor(Color.BLUE);
				
				var rectangle2 = new Rectangle2D.Float(x_center - 70, ((y * 70) / 100), 50, 50);	//Pourcentages à mettre
				var rectangle3 = new Rectangle2D.Float(x_center + 20,  ((y * 70) / 100), 50, 50);	//Pourcentages à mettre
				
				graphics.fill(rectangle2);
				graphics.fill(rectangle3);
				
				graphics.setColor(Color.WHITE);
				
				graphics.drawString("1", x_center - 70 + 50 / 2, ((y * 70) / 100) + 50 / 2);
				graphics.drawString("2", x_center + 20 + 50 / 2,((y * 70) / 100) + 50 / 2);
			}
			
			if(nb_choices == 4) {

				graphics.setColor(Color.BLUE);
				
				var rectangle2 = new Rectangle2D.Float(x_center - 70, ((y * 70) / 100), 50, 50);	//Pourcentages à mettre
				var rectangle3 = new Rectangle2D.Float(x_center + 20,  ((y * 70) / 100), 50, 50);	//Pourcentages à mettre
				var rectangle4 = new Rectangle2D.Float(x_center - 50 / 2,  ((y * 70) / 100) + 100, 50, 50);	//Pourcentages à mettre
				
				graphics.fill(rectangle2);
				graphics.fill(rectangle3);
				graphics.fill(rectangle4);
				
				graphics.setColor(Color.WHITE);
				
				graphics.drawString("2", x_center - 70 + 50 / 2, ((y * 70) / 100) + 50 / 2);
				graphics.drawString("3", x_center + 20 + 50 / 2,((y * 70) / 100) + 50 / 2);
				graphics.drawString("4", x_center - 50 / 2 + 50 / 2,((y * 70) / 100) + 100 + 50 / 2);
			}
		});
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
	public void showPlateau(Mode game, int mode) {
		
		Objects.requireNonNull(game);
		
		if(mode != 1 && mode != 2 && mode != 0) { // Au départ il n'y a pas encore de choix de modes donc mode peut valoir 0
			throw new IllegalArgumentException();
		}
	
		actualiseWindow(context, context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
		
		float width = context.getScreenInfo().getWidth();
		float height = context.getScreenInfo().getHeight();
		
		showBoardGraph(context, game, Math.round(width) / 15, Math.round(height) / (5 + 1)); /*15 = nombres de cartes par lignes, 5 = nombre de piles*/
        showJetonMap(game.jetons_disponibles(), "", context);
        showTuiles(context, game, Math.round(width) / 15, Math.round(height) / (5 + 1));
        drawBoxInstructions(context, "1) Acheter une carte \n2) Prendre des ressources \n3) Réserver une carte\n ");
        drawBoxActions(context, listActions);   
	}

	/**
	 * 
	 * Print the available tokens on the console. It i représented as a board.
	 * 
	 * @param ressources
	 *        Resources of tokens
	 * @param message
	 * 		No description found
	 * @param context
	 * 		No description found
	 */
	public static void showJetonMap(HashMap<String, Integer> ressources, String message, ApplicationContext context) {
	
		Objects.requireNonNull(ressources);
		Objects.requireNonNull(message);
		Objects.requireNonNull(context);

		context.renderFrame(graphics -> {
			
			int i = 0;
			
			for(var elem : ressources.entrySet()) {
				drawGem(context, 1200, 300  + (i * 75), 35, elem.getValue(),  elem.getKey());
				
				i++;	
			}
	    });
	}

	
	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 */
	public void showBoard(Mode game) {
		
		Objects.requireNonNull(game);
		
		showPlateau(game, 0);
	}
	
	
	/**
	 * Display of the turn switch between two players
	 * 
	 * @param scan
	 *        Scanner to use to recup the new line
	 */
	public void turnChange(Scanner scan) {
		
		Objects.requireNonNull(scan);
		
		return;
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
	public void showJeton(HashMap<String, Integer> banque, HashMap<String, Integer> ressources, String message) {
		
		//message can be null
		Objects.requireNonNull(ressources);
		Objects.requireNonNull(context);
		
		context.renderFrame(graphics -> {
			
			var x = context.getScreenInfo().getWidth();
			var y = context.getScreenInfo().getHeight();
			
			
			actualiseWindow(context, x, y);
			
			float hauteur = context.getScreenInfo().getHeight();
		
			if(banque != null) {
				
				float pointeur = (x / banque.size()) + (25 * x / 100);
				
				
				graphics.setColor(Color.BLUE);
				
				graphics.drawString("Jetons disponibles :", pointeur, ((40 * hauteur) / 100));
				graphics.drawString("Vos jetons :", pointeur, ((60 * hauteur) / 100));
				
					
				for(var elem : banque.entrySet()) {
					 drawGem(context, pointeur, ((45 * hauteur) / 100), 30, elem.getValue() , elem.getKey());
					 
					 pointeur += 60;
				}
			}

			var pointeur = (x / ressources.size()) + (25 * x / 100);
			
			for(var elem : ressources.entrySet()) {
				 drawGem(context, pointeur, ((65 * hauteur) / 100), 30, elem.getValue() , elem.getKey());
				 
				 pointeur += 60;
			}
			
		});
	}
	

	/**
	 * Print the cards of the board of the game token.
	 * 
	 * @param game
	 *        Game token to print it board
	 *  @param largeur
	 *  	No description found
	 *  @param hauteur
	 *  	No description found
	 *  @param context
	 *  	No description found
	 */
	public static void showBoardGraph(ApplicationContext context, Mode game, int largeur, int hauteur) {
		
		Objects.requireNonNull(game);
		Objects.requireNonNull(context);
		
		if(largeur < 0 || hauteur < 0) {
			throw new IllegalArgumentException();
		}
		
		float x, y;
		y = 2;
		
		for(int i = 1 ; i < game.board().size() + 1 ; i++) {
			
			x = 4;
			var num_card = 1;
				
			
			if(game.board().get(i).get(0) != null) {
				
				drawCardPioche(context, largeur, (y * hauteur) + (10 * y), largeur, hauteur, Integer.toString(i));
				
				for(var carte : game.board().get(i)) {
					
					if(carte != null) {
						drawCard(context, (x * largeur) + (10 * x) , (y * hauteur) + (10 * y), largeur, hauteur, carte); // 10 = espace entre les cartes
					}
						
					
					num_card ++;
					
					x = x + 1;
				}
			}
			
			y = y + 1;
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
	 * @return int
	 * it indicates that the player successfully did its action
	 */
	public int showJoueur(Participant joueur, Mode game) {
		
		Objects.requireNonNull(joueur);
		Objects.requireNonNull(game);
		
		actualiseWindow(context, context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());

		showJeton(null, joueur.ressources(), null);
		
		drawBoxInstructions(context, "1) Retour au menu principal");
        drawBoxActions(context, listActions);
        
        context.renderFrame(graphics -> {
			
			graphics.setFont(new Font("SansSerif", Font.BOLD, 12));
			graphics.setColor(Color.BLUE);
			graphics.drawString("Joueur : " + joueur.pseudo(), context.getScreenInfo().getWidth() / 2 - 30, context.getScreenInfo().getHeight() / 2);
			graphics.drawString("Points de prestiges : " + joueur.points_prestiges(), context.getScreenInfo().getWidth() / 2 - 30, context.getScreenInfo().getHeight() / 2 + 20);
			graphics.drawString("Ressources : ", context.getScreenInfo().getWidth() / 2 - 30, context.getScreenInfo().getHeight() / 2 + 40);
			
		});
        
        var clic = 0;
        
        while(clic != 1) {
        	try{
        		clic = recupTouche();
        	}catch(Exception e) {
        		//On passe
        	}
        }
		
        
        showPlateau(game, 0);
        
		return 1;
	}
	
	/**
	 * Print the Nobles Cards on the board.
	 * 
	 * @param game
	 *        Game given to show its nobles card on the board
	 */
	public void showTuiles(Mode game){
		
		Objects.requireNonNull(game);
		
		showPlateau(game, 0);
	}
	
	/**
	 * This shows the Nobles cards on the board.
	 * 
	 * @param context
	 *        ApplicationContext to draw on
	 *        
	 * @param game
	 *        Game
	 * 
	 * @param largeur
	 *         Width of a card
	 * 
	 * @param hauteur
	 *        Heigth of a card
	 * 
	 */
	private void showTuiles(ApplicationContext context, Mode game, int largeur, int hauteur){
		
		Objects.requireNonNull(context);
		Objects.requireNonNull(game);
		
		if(largeur < 0 || hauteur < 0) {
			throw new IllegalArgumentException();
		}
		
		float x, y;
		
		x = 4;
		y = 1;
		
		for(int i = 0 ; i < game.tuiles_board().size(); i++) {
			
			drawCardNoble(context,(x * largeur) + (10 * x), (y * hauteur) - (30 * y), largeur, hauteur, game.tuiles_board().get(i));
			
			x = x + 1;
		}
	}
	
	
	/**
	 * Print the cards reserved by a player.
	 * 
	 * @param joueur
	 * 		  Player given.
	 */
	public void showReserved(Participant joueur) {
		
		Objects.requireNonNull(joueur);
		
		
		actualiseWindow(context, context.getScreenInfo().getWidth(), context.getScreenInfo().getHeight());
		
		drawBoxInstructions(context, "Entrez l'indice de carte correspondant");
		drawBoxActions(context, listActions);

		
		if(joueur.reserve().size() == 0) {
			return;
		}
		
		
		
		var x = 4;
		
		var y = 3;
		
		var largeur = context.getScreenInfo().getWidth();
		var hauteur = context.getScreenInfo().getHeight();
		
		
		for(var elem : joueur.reserve()) {
			drawCard(context, (x * (largeur / 15)) + (10 * x) , (y * (hauteur / (5 + 1))) + (10 * y), Math.round(largeur) / 15 , Math.round(hauteur) / (5 + 1), elem); // 10 = espace entre les cartes
			x++;
		}
	}
	
	/**
	 * Draws the box of the instructions givenin the game
	 * 
	 * @param context
	 *        ApplicationContext to draw on
	 *        
	 * @param instructions
	 *        instructions for the player
	 */
	private static void drawBoxInstructions(ApplicationContext context, String instructions){
    	
		Objects.requireNonNull(context);
		Objects.requireNonNull(instructions);
		
    	context.renderFrame(graphics -> {
    		
    		var x  = context.getScreenInfo().getWidth();
    		var y = context.getScreenInfo().getHeight();
    		
    		var rectangle = new Rectangle2D.Float(x - ((25 * x) / 100), ((5 * y) / 100), ((20 * x) / 100) , ((20 * y) / 100) + 35);
    		
    		graphics.setColor(Color.BLACK);
    		graphics.fill(rectangle);
    		graphics.setColor(Color.WHITE);
    		graphics.setFont(new Font("SansSerif", Font.BOLD, 10));

    		var y_offset = 1;
    		
    		for(var elem : instructions.split("\n")) {
    			graphics.drawString(elem, x - ((25 * x) / 100) + ((2 * x) / 100), ((5 * y) / 100) + (((5 * y) / 100) * y_offset));
    			
    			y_offset ++;
    		}
    	});	
    }
    
	/**
	 * Draws the box of the actions given in the game
	 * 
	 * @param context
	 *        ApplicationContext to draw on
	 *        
	 * @param actions
	 *        Actions of the player List
	 */
    private static void drawBoxActions(ApplicationContext context, ArrayList<String> actions){
    	
    	Objects.requireNonNull(context);
    	Objects.requireNonNull(actions);
    	
    	context.renderFrame(graphics -> {
    		
    		var x  = context.getScreenInfo().getWidth();
    		var y = context.getScreenInfo().getHeight();
    		
    		var rectangle = new Rectangle2D.Float(((3 * x) / 100), ((5 * y) / 100), ((25 * x) / 100) , ((20 * y) / 100));
    		
    		graphics.setColor(Color.BLACK);
    		
    		graphics.fill(rectangle);

    		var y_offset = 1;
    		
    		graphics.setColor(Color.RED);
    		graphics.setFont(new Font("SansSerif", Font.ITALIC, 12));
    		
    		for(var elem : actions) {
    			
    		
    			graphics.drawString(elem, ((5 * x) / 100) + ((1 * x) / 100), ((5 * y) / 100) + (((5 * y) / 100) * y_offset));
    			
    			graphics.setColor(Color.PINK);
	    		graphics.setFont(new Font("SansSerif",Font.ITALIC , 10));
    			
    			y_offset ++;
    		}
    	});
    }
    
    
    /**
     * Draw a noble Card on the screen.
     * 
     * @param context
     *        ApplicationContext to draw on
     *        
     * @param a
     *        Position in x of the card
     * 
     * @param b
     *        Position in y of the card
     * 
     * @param largeur
     *        Width of the card
     * 
     * @param hauteur
     *        Height of the card
     *        
     * @param card
     *        Card to draw
     */
    private void drawCardNoble(ApplicationContext context, float a, float b, int largeur, int hauteur, Tuile card) { // À changer avec l'interface Carte pour aussi faire les nobles
		
	    Objects.requireNonNull(context);
	    Objects.requireNonNull(card);
	    
	    if(a < 0 || b < 0 || largeur < 0 || hauteur < 0) {
	    	throw new IllegalArgumentException();
	    }
	    
	     context.renderFrame(graphics -> {
	    	
	    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
	    	 
	    	 // show a new ellipse at the position of the pointer
	    	 
	    	 graphics.setColor(Color.BLACK);
	    	 
	    	 rectangle = new Rectangle2D.Float(a - 1, b - 1 , largeur + 2 , hauteur + 2);
	    	 graphics.fill(rectangle);
	    	 
	    	 graphics.setColor(Color.PINK);

	    	 rectangle = new Rectangle2D.Float(a, b , largeur , hauteur);

	    	 graphics.fill(rectangle);
	        
	        
	    	 graphics.setColor(Color.BLACK);
	    	 rectangle = new Rectangle2D.Float(a + ((5 * largeur) / 100), b + ((2 * hauteur) / 100), 15, 15);
	    	 graphics.fill(rectangle);
	    	
	    	 /*PRICE*/
	        
	    	 var nb_carte = 0;
	    	 
	    	 for(var price : card.cout().entrySet()) {
	    		 drawGem(context, a + ((5 * largeur) / 100), b + ((45 * hauteur) / 100) + (nb_carte * 20), 15, price.getValue() , price.getKey());
	    		 
	    		 nb_carte ++;
	    	 }
	        
	       	 graphics.setColor(Color.WHITE);
	       	 
	    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
	    	 graphics.drawString(Integer.toString(card.points_prestiges()), a + ((5 * largeur) / 100)  + (15 / 2) - (15 / 6) - (15 / 8), b + ((2 * hauteur) / 100) + (15 / 2) + (15 / 6) + (15 / 6)); //15 = taille du carré

	    	 int fontsize = fontSize(card.name(), largeur);
		     graphics.setFont(new Font("SansSerif", Font.BOLD, fontsize));

		     graphics.drawString(card.name(), a + ((largeur / 2) - largeur / 4) + 1, b + ((30 * hauteur) / 100));
	        
	     });
	}  
    
    
    /**
     * Draw a Dev Card on the screen.
     * 
     * @param context
     *        ApplicationContext to draw on
     *        
     * @param a
     *        Position in x of the card
     * 
     * @param b
     *        Position in y of the card
     * 
     * @param largeur
     *        Width of the card
     * 
     * @param hauteur
     *        Height of the card
     *        
     * @param card
     *        Card to draw
     */
    private static void drawCard(ApplicationContext context, float a, float b, int largeur, int hauteur, CarteDev card) { // À changer avec l'interface Carte pour aussi faire les nobles
		
    	 Objects.requireNonNull(context);
 	     Objects.requireNonNull(card);
 	    
 	     if(a < 0 || b < 0 || largeur < 0 || hauteur < 0) {
 	    	throw new IllegalArgumentException();
 	     }
	    
	     context.renderFrame(graphics -> {
	    	 
	    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);
	    	 
	    	 // show a new ellipse at the position of the pointer
	    	 
	    	 graphics.setColor(Color.BLACK);
	    	 
	    	 rectangle = new Rectangle2D.Float(a - 1, b - 1 , largeur + 2 , hauteur + 2);
	    	 graphics.fill(rectangle);
	    	 
	    	 graphics.setColor(Color.WHITE);

	    	 rectangle = new Rectangle2D.Float(a, b , largeur , hauteur);

	    	 graphics.fill(rectangle);
	        
	        
	    	 graphics.setColor(Color.BLACK);
	    	 rectangle = new Rectangle2D.Float(a + ((5 * largeur) / 100), b + ((2 * hauteur) / 100), 15, 15);
	    	 graphics.fill(rectangle);
	    	 
	        
	    	 /*PRICE*/
	    	 
	    	 var nb_carte = 0;
	    	 for(var price : card.cout().entrySet()) {
	    		 drawGem(context, a + ((5 * largeur) / 100), b + ((45 * hauteur) / 100) + (nb_carte * 20), 15, price.getValue() , price.getKey());
	    		 
	    		 nb_carte ++;
	    	 }
	        
	       	 graphics.setColor(Color.WHITE);
	       	 
	    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
	    	 graphics.drawString(Integer.toString(card.points()), a + ((5 * largeur) / 100)  + (15 / 2) - (15 / 6) - (15 / 8), b + ((2 * hauteur) / 100) + (15 / 2) + (15 / 6) + (15 / 6)); //15 = taille du carré
	    	 
	    	 /*BONUS*/
	    	drawGem(context, a + (largeur - ((10 * largeur) / 100) - (13 / 2)), b + ((2 * hauteur) / 100), 13, -1 ,card.couleur()); // 13 = taille
	    	
	    	graphics.setColor(Color.BLACK);
	    	
	    	
	    	int fontsize = fontSize(card.object(), largeur);
	    	graphics.setFont(new Font("SansSerif", Font.BOLD, fontsize));

	    	graphics.drawString(card.object(), a + ((largeur / 2) - largeur / 4) + 1, b + ((30 * hauteur) / 100));
	        
	     });
	}  
    
    
    /**
     * It gives the best font size possible to enter in the card.
     * 
     * @param message
     *        Message to enter in the card
     *        
     * @param taille
     *        Width of a card
     *        
     * @return The size font which enters in the card
     */
    private static int fontSize(String message, int taille) {
    	
    	int font = taille / 10;
    	
    	while ((message.length() * font) > (taille + ((10 * taille) / 100))) {
    		font--;
    	}
   
    	
    	return font;
    }
    
    /**
     * Draw a reversed card (deck) on the screen.
     * 
     * @param context
     *        ApplicationContext to draw on
     *        
     * @param a
     *        Position in x of the card
     * 
     * @param b
     *        Position in y of the card
     * 
     * @param largeur
     *        Width of the card
     * 
     * @param hauteur
     *        Height of the card
     *        
     * @param val
     *        String written on the "back" 
     */
    private static void drawCardPioche(ApplicationContext context, float a, float b, int largeur, int hauteur, String val) { // À changer avec l'interface Carte pour aussi faire les nobles
		 
	     context.renderFrame(graphics -> {
	    	 
	    	 
	    	 Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, 0, 0);

	    	 // show a new ellipse at the position of the pointer
	    	 
	    	 graphics.setColor(Color.BLACK);
	    	 
	    	 rectangle = new Rectangle2D.Float(a - 1, b - 1 , largeur + 2 , hauteur + 2);
	    	 graphics.fill(rectangle);
	    	 
	    	 graphics.setColor(Color.CYAN);

	    	 rectangle = new Rectangle2D.Float(a, b , largeur , hauteur);

	    	 graphics.fill(rectangle);
	        
	       	 graphics.setColor(Color.BLACK);
	       	 
	    	 graphics.setFont(new Font("SansSerif", Font.BOLD, 10));
	    	 graphics.drawString(val, a + ((50 * largeur) / 100)  - 2, b + ((50 * hauteur) / 100)); //15 = taille du carré

	     });
	}
	
	
	/**
	 * Draw a a gem on the screen.
	 *
	 * @param context
	 *        ApplicationContext to draw on
	 *        
	 * @param a
	 *        Position on x of the gem
	 *        
	 * @param b
	 *         Position on x of the gem
	 * 
	 * @param taille
	 *        Diameter of the gem 
	 *         
	 * @param val
	 *        Value of the gem
	 *        
	 * @param color
	 *        Color of the gem
	 */
	private static void drawGem(ApplicationContext context, float a, float b, int taille, int val, String color){
		
		Objects.requireNonNull(context);
		Objects.requireNonNull(color);
		
		if(a < 0 || b < 0 || taille < 0) {
			throw new IllegalArgumentException();
		}
		
		context.renderFrame(graphics -> {
			Ellipse2D.Float cercle1 = new Ellipse2D.Float(a - 1, b - 1, taille + 2, taille + 2);
	    	Ellipse2D.Float cercle2 = new Ellipse2D.Float(a, b, taille, taille);
	    
	        
	    	graphics.setColor(Color.BLACK);
	    	graphics.fill(cercle1);
	    	// show a new ellipse at the position of the pointer
	    	graphics.setColor(Color.WHITE);

	    	if(color.equals("Rouge")) {
	    		graphics.setColor(Color.RED);
	    	}
	    	 
	    	if(color.equals("Bleu")) {
	    		graphics.setColor(Color.BLUE);
	    	 }
	    	 
	    	 if(color.equals("Blanc")) {
	    		 graphics.setColor(Color.WHITE);
	    	 }
	    	 
	    	 if(color.equals("Vert")) {
	    		 graphics.setColor(Color.GREEN);
	    	 }
	    	 
	    	 if(color.equals("Noir")) {
	    		 graphics.setColor(Color.BLACK);
	    	 }
	    	 
	    	 if(color.equals("Jaune")) {
	    		 graphics.setColor(Color.YELLOW);
	    	 }
	    	 
	    	 graphics.fill(cercle2);		
	    	 
	    	 if(color.equals("Jaune") || color.equals("Blanc")) {
	    		 graphics.setColor(Color.BLACK);
	    	 }else {
	    		 graphics.setColor(Color.WHITE);
	    	 }
	    	 
	    	 graphics.setFont(new Font("SansSerif", Font.BOLD, taille / 2));
	    	 
	    	 String chaine;
	    	 
	    	 if(val <= 0){
	    		 chaine = "";
	    	 }else {
	    		 chaine = Integer.toString(val);
	    	 }
	    	 
	    	 graphics.drawString(chaine, a + (taille / 2) - (taille / 6), b + (taille / 2) + (taille / 4));

	      });
	}
}

