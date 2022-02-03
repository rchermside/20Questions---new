import java.util.*;
import java.io.*;

public class Guesser{
  ArrayList<Question> questions;
  HashSet<String> guesses;

  public static String getInput(Scanner in, String[] inputs){
    while (true){
      String answer = in.nextLine();
      answer = answer.toLowerCase();
      for (int i =0; i< inputs.length; i++){
        if (answer.equals(inputs[i])){
          return answer;
        }
      }
      System.out.print("Invalid input, please enter ");
      System.out.print (inputs[0]);
      for (int i =1; i< inputs.length; i++){
        System.out.print(", "+ inputs[i] );
      }
    }
  }

  public static String getInput(Scanner in){
    String[] validInputs = {"y","yes","n","no","quit"};
    String input = getInput (in, validInputs);
    if (input.equals("yes")){
      input = "y";
    } else if (input.equals("no")){
      input = "n";
    }
    return input;
  }

  Guesser (String question, HashSet<String> guesses){
    questions = new ArrayList<Question>();
    questions.add(new Question(question));
    this.guesses = guesses;
  }

  public void serialGuess(Scanner in){
    HashSet<String> myGuesses = new HashSet<String>(guesses);
    boolean guessed = false;
    for (Question question: questions){
      System.out.println (question +" (y/n/quit)");
      String input = getInput(in);
      if (input.equals("n")){
        myGuesses.removeAll(question.yesGuesses);
      } else if (input == "y"  || input == "yes"){
        myGuesses.removeAll(question.noGuesses);
      }
      if (myGuesses.size() == 0){
        guessed = true;
        System.out.println ("I have no idea!!!");
        break;
      }
      if (myGuesses.size() == 1){
        guessed = true;
        String myGuess = myGuesses.iterator().next();
        System.out.println("Is your animal a " + myGuess + " ?");
        String correct = getInput(in);
        if (correct.equals("y")){
          System.out.println ("YAHOO!! I'm the best!!");
          return;
        } else if (correct.equals("quit")){
          return;
        } else {
          break;
        }
      }
    }
    //else didn't guess correctly
    if (!guessed){
      System.out.println ("I guess your animal is in " + myGuesses);
      System.out.println ("Am I right? (y/n/quit)");
      String correct = getInput(in);
      if (correct.equals("y")){
        //  NEED ASK FOR A NEW QUESTION HERE
        return;
      } else if (correct.equals("quit")){
        return;
      }
    }
    System.out.println ("What were you thinking of?");
    String guess = in.nextLine();
    guesses.add(guess);
    System.out.println ("New guesses:" + guesses);

  }

}
