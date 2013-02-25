

import java.util.ArrayList;
import java.util.List;


public class Cards {

	private List<String> suits = new ArrayList<String>();
	private List<String> faces = new ArrayList<String>();

	/* The public deck will be used as a basis for populating a larger array
	 * to represent a stack of several decks  */
	public final String[] deck = new String[52];
	
	public Cards() {
		/* Set up a domain reference structure of all suits */
		suits.add("Diamonds");
		suits.add("Clubs");
		suits.add("Hearts");
		suits.add("Spades");

		/* Set up a domain reference structure of all faces */
		faces.add("Ace");
		faces.add("Two");
		faces.add("Three");
		faces.add("Four");
		faces.add("Five");
		faces.add("Six");
		faces.add("Seven");
		faces.add("Eight");
		faces.add("Nine");
		faces.add("Ten");
		faces.add("Jack");
		faces.add("Queen");
		faces.add("King");

		/* Populate the reference deck */
		int cardCount = 0;
		for (String someSuit : suits) {
			for (String someFace : faces) {
				/* generate cards */
				deck[cardCount++] = String.format("%s of %s", someFace, someSuit);
			}
		}

	}

	/* 
	 * Useful discussion of shuffling an array available here:
	 * http://adrianquark.blogspot.com/2008/09/how-to-shuffle-array-correctly.html
	 * 
	 * Basically, we can generate any possible arrangement of elements of an 
	 * array by iterating through all elements, exchanging each element with another
	 * following element, or no exchange at all (reflexive) to keep the element
	 * in place. This has an order of n!, which is the same number of possible
	 * permutations of arranging the elements. This allows us to infer that all
	 * arrangements of the elements have the same probability of occurring.
	 */
	public static void shuffler(String[] inDeck){
		
		/* Get random generator */
		MersenneTwisterFast randomizer = new MersenneTwisterFast();
		
		/* iterate through all elements of the array (stack of decks) */
		for (int i=0; i<inDeck.length; i++){
			/* Randomly select a position following the current iterated card */
			int randomPosition = randomizer.nextInt(inDeck.length - i);
			/* randomPosition can be any value from 0 to remaining length, 
			 * zero based, inclusive, and will be used to select a following card for exchange */
			if (randomPosition != 0){
				/* if the position selected is 0, no exchange is necessary, 
				 * zero is a reflexive exchange, card does not move */
				/* otherwise, exchange the cards */
				String exchangeTarget = inDeck[randomPosition + i];
				inDeck[randomPosition + i] = inDeck[i];
				inDeck[i] = exchangeTarget;
			}
		}
	}
	
	/* Alternate shuffler that avoids imported RNG code  */
	public static void shuffler_simple(String[] inDeck){
		
		/* iterate through all elements of the array (stack of decks) */
		for (int i=0; i<inDeck.length; i++){
			/* Randomly select a position following the current iterated card
			 * - to make a pseudo random selection, iterate forward looping until the clock changes
			 * This results in a select that should vary largely randomly */
			long startTime = System.currentTimeMillis();
			int psuedoRandomPosition = 0;
			long iterations = 0; // count total iterations before the clock changes
			while (System.currentTimeMillis()==startTime){
				psuedoRandomPosition = (++psuedoRandomPosition%(inDeck.length - i));
				iterations++;
			}
			System.out.print(iterations+", ");
			/* randomPosition can be any value from 0 to remaining length, 
			 * zero based, inclusive, and will be used to select a following card for exchange */
			if (psuedoRandomPosition != 0){
				/* if the position selected is 0, no exchange is necessary, 
				 * zero is a reflexive exchange, card does not move */
				/* otherwise, exchange the cards */
				String exchangeTarget = inDeck[psuedoRandomPosition + i];
				inDeck[psuedoRandomPosition + i] = inDeck[i];
				inDeck[i] = exchangeTarget;
			}
		}
	}

}
