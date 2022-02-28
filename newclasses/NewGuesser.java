package newclasses;
import newclasses.NewUnguessedComparator;

import java.util.*;


public class NewGuesser{
  public String startingInstruction;
  public ArrayList<NewQuestion> questions;
  //public HashSet<Short> guesses;
  public ArrayList<Guess> guessArray;
  static Random rndm = new Random();


public NewGuesser(String startingInstruction,ArrayList<NewQuestion> questions, ArrayList<Guess> guessArray ){
  this.startingInstruction = startingInstruction;
  this.questions = questions;
  this.guessArray = guessArray;
}
  
  NewGuesser (String question, ArrayList<Guess> guessArray, String startingInstruction){
    questions = new ArrayList<NewQuestion>();
    questions.add(new NewQuestion(question));
    this.guessArray = guessArray;
    this.startingInstruction = startingInstruction;
  }


  //retrieves the guessID for a given guess or returns -1 if no mathcing guess is found
  //note if guesses get large might want to make a hashmap
  short getGuessID(String guess){
    for (short i = 0; i < guessArray.size(); i++){
      if (guess.equals(guessArray.get(i).name)){
        return i;
      }
    }
    return -1;
  }

  //adds new guess to the guesser, assumes the guess has already been validated and checked to see if it already
  //exists
  public Guess addNewGuess(String guess){
      short guessId = (short)guessArray.size();
      Guess newGuess = new Guess(guessId, guess);
      return newGuess;
  }




  //sorts array by how many guesses have determined answers, the questions for with the fewest determined answers are at the beginning of the array
  public ArrayList<NewQuestion> sortByLeastGuessed(ArrayList<NewQuestion> questionPool){
    ArrayList<NewQuestion> leastGuessed = new ArrayList<NewQuestion>(questionPool);
    Collections.sort(leastGuessed, new NewUnguessedComparator());
    return leastGuessed;
  }
  




  public String guessesToString(HashSet<Short> guessSet){
    String guessesString = "";
    int i = 1;
    for (Short guess: guessSet){
      guessesString = guessesString + guessArray.get(guess);
      if (i< guessSet.size()-1){
        guessesString = guessesString + ", ";
      } else if (i == guessSet.size()-1){
        guessesString = guessesString + " or ";
      }
      i++;
    }
    return guessesString;
  }




  //picks the Question out of the question pool that will eliminate the most guesses if the worse answer is chosen and returns the index of the question -- if no question in the question pool will eliminate any answers returns -1
  public int chooseSmartQuestion(HashSet<Short> myGuesses, ArrayList<NewQuestion> questionPool, int random){
    if (random > 0){
      int rndNumber = rndm.nextInt(100);
      if (rndNumber < random){
        int chosen = rndm.nextInt(questionPool.size());
        return chosen;
      }
    }
    
    int chosen=-1;
    int bestElim =0;
    //chooses the question for which you get the most guesses eliminated for the weaker of 
    //the yes/no answers
    
    for (int i = 0; i< questionPool.size(); i++){
      HashSet<Short> noElims = new HashSet<Short>(myGuesses);
      noElims.retainAll(questionPool.get(i).noGuesses);
      HashSet<Short> yesElims = new HashSet<Short>(myGuesses);
      yesElims.retainAll(questionPool.get(i).yesGuesses);
      int minElim = Math.min(noElims.size(), yesElims.size());
      if (minElim > bestElim){
        bestElim = minElim;
        chosen = i;
      }
    }
    return (chosen);
  }





}
