package newclasses;

import java.util.*;


public class NewGuesser{
  public String startingInstruction;
  public ArrayList<NewQuestion> questions;
  public ArrayList<Guess> guessArray;
  static Random rndm = new Random();


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

  public NewGuesser (String question, ArrayList<Guess> guessArray, String startingInstruction){
    questions = new ArrayList<NewQuestion>();
    questions.add(new NewQuestion(question));
    this.guessArray = guessArray;
    this.startingInstruction = startingInstruction;
  }

public NewGuesser(String startingInstruction,ArrayList<NewQuestion> questions, ArrayList<Guess> guessArray ){
  this.startingInstruction = startingInstruction;
  this.questions = questions;
  this.guessArray = guessArray;
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

  public static String getQInput(Scanner in){
    String[] validInputs = {"y","yes","n","no","maybe","m","quit"};
    String input = getInput (in, validInputs);
    if (input.equals("yes")){
      input = "y";
    } else if (input.equals("no")){
      input = "n";
    } else if (input.equals("maybe")){
      input = "m";
    }
    return input;
  }

  public void pruneQuestions(Scanner in){
    int pageSize = 25;
    int i = 0;

    while (i< questions.size()){
      System.out.println (i+". "+ questions.get(i).question);
      if ((i%pageSize == pageSize -1)|| (i == questions.size()-1)){
        System.out.println ("Enter integer to delete or return");
        String input = in.nextLine();
        if (!input.equals("")){
          int index = Integer.parseInt(input);
          System.out.println ("Delete:" + questions.get(index).question + "? (y/n/quit)");
          input = getInput(in);
          if (input.equals("y")){
            questions.remove(index);
            i--;
          }
        }
      }
      i++;
    }
  }

  //sorts array by how many guesses have determined answers, the questions for with the fewest determined answers are at the beginning of the array
  public ArrayList<NewQuestion> sortByLeastGuessed(ArrayList<NewQuestion> questionPool){
    ArrayList<NewQuestion> leastGuessed = new ArrayList<NewQuestion>(questionPool);
    Collections.sort(leastGuessed, new NewUnguessedComparator());
    return leastGuessed;
  }
  
  public void getNewQuestion(Scanner in, HashSet<Short> myGuesses){
  
    System.out.println ("Do you want to enter a question that distinguishes between "+ myGuesses+ " (y/n)");
    String answer = getInput(in);
    if (answer.equals("y")){
      System.out.println ("Please enter the question");
      String newQuestion = in.nextLine();
      HashSet<Short> noGuesses = new HashSet<Short>();
      HashSet<Short> yesGuesses = new HashSet<Short>();
      HashSet<Short> maybeGuesses = new HashSet<Short>();

      
      HashSet<Short> asks = new HashSet<Short>(myGuesses);
      final int NUM_ASKS = 9;
      int needed = NUM_ASKS - asks.size();
      int pool = guessArray.size();
      for (Guess guess: guessArray){
        if (pool <= needed){
          asks.add(guess.guessId);
          needed--;
        } else {
          int rndNumber = rndm.nextInt(pool);
          if (rndNumber < needed){
            asks.add(guess.guessId);
            needed--;
          }
          if (needed == 0){
            break;
          }
        }
        pool--;
      }
      
      System.out.println ("Please answer your question for the following characters");

      for (Short guess: asks){
        System.out.print (guessArray.get(guess) + " (y/n/maybe/quit):");

        answer = getQInput(in);
        if (answer.equals("quit")){
          return;
        }
        if (answer.equals("y") || answer.equals("yes")){
          yesGuesses.add(guess);
        } else if (answer.equals("n") || answer.equals("no")){
          noGuesses.add(guess);
        } else if (answer.equals("m")){
          maybeGuesses.add(guess);
        }
      }
      NewQuestion q = new NewQuestion(newQuestion, yesGuesses, noGuesses, maybeGuesses);
      questions.add(q);
      System.out.println ("Thank you for making me a better game!");
    }
  }

  //asks the passed in question to the user and returns a response
  public NewResponse askQuestion (Scanner in, NewQuestion question, HashSet<Short> myGuesses){
      System.out.println (question.question +" (y/n/maybe/quit)");
      String input = getQInput(in);
      if (input.equals("n")){
        myGuesses.removeAll(question.yesGuesses);
      } else if (input.equals("y")){
        myGuesses.removeAll(question.noGuesses);
      }
      NewResponse response = new NewResponse (question, input);
      return response;

  }


  //makes a guess to the user and returns whether the guess was correct(true)
  public boolean makeGuess(Scanner in, HashSet<Short> myGuesses){
      if (myGuesses.size() == 0){
        System.out.println ("I have no idea!!!");
        return false;
      }

      if (myGuesses.size() == 1){
        Short myGuess = myGuesses.iterator().next();
        System.out.println("I guess a  " + guessArray.get(myGuess).name + ". Am I right? (y/n)");
        String correct = getInput(in);
        if (correct.equals("y")){
          System.out.println ("YAHOO!! I'm the best!!");
          return true;
        } 
        return false;
      }

      //else there are multiple guesses in myGuesses

      System.out.println ("I guess its one of " + guessesToString(myGuesses));
      System.out.println ("Am I right? (y/n)");
      String correct = getInput(in);
      return false;
  }

  public String guessesToString(HashSet<Short> guessSet){
    String guessesString = "";
    for (Short guess: guessSet){
      guessesString = guessesString + ", " + guessArray.get(guess);
    }
    guessesString = guessesString.substring(1);
    return guessesString;
  }


  public void randomGuess(Scanner in ){

    HashSet<Short> myGuesses = new HashSet<Short>();
    for (Guess guess: guessArray){
      myGuesses.add(guess.guessId);
    }
    ArrayList<NewQuestion> questionPool = new ArrayList<NewQuestion>(questions);
    //nSystem.out.println ("questionPool:" +questionPool);
    boolean guessed = false;
    ArrayList<NewResponse> responseHistory = new ArrayList<NewResponse>();
    while (!questionPool.isEmpty()){
      //randomly choose a question out of the question pool
      //check to see if this question will narrow the guesses

      // this will generate a random number between 0 and
      // HashSet.size - 1
      int rndmNumber = rndm.nextInt(questionPool.size());
      NewQuestion question = questionPool.get(rndmNumber);
      questionPool.remove(rndmNumber);

      HashSet<Short> all = new HashSet<Short>(question.yesGuesses);
      all.addAll(question.noGuesses);
      HashSet<Short> clone =new HashSet<Short>(myGuesses);
      clone.retainAll(all);

      if (!(question.yesGuesses.containsAll(clone) || question.noGuesses.containsAll(clone))){
        //ask the question
        NewResponse response = askQuestion (in, question, myGuesses);
        //System.out.println ("My guesses are: " + myGuesses);
        if (response.answer.equals("quit")){
          return;
        }
        responseHistory.add(response);
        if (myGuesses.size()<2) {
          break;
        }
      }
    }
    boolean correct = makeGuess(in, myGuesses);
    Short guessId;
    if (!correct){
      //else didn't guess correctly or had multiple guesses
      System.out.println ("What were you thinking of?");
      String guessName = in.nextLine();
      guessName = guessName.toLowerCase();
      Guess guess = new Guess((short)guessArray.size(), guessName);
      guessArray.add(guess);
      myGuesses.add(guess.guessId);
      getNewQuestion(in, myGuesses);
      guessId = guess.guessId;
    } else {
      guessId = myGuesses.iterator().next();
    }
    for (NewResponse response: responseHistory){
      response.question.add(guessId, response.answer);
    }

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


  //random - percentage of times to stick a random question in 
  public void smartGuess(Scanner in, int randomPercent){
    HashSet<Short> myGuesses = new HashSet<Short>();
    for (Guess guess:guessArray){
      myGuesses.add(guess.guessId);
    }
    ArrayList<NewQuestion> questionPool = new ArrayList<NewQuestion>(questions);
    //nSystem.out.println ("questionPool:" +questionPool);
    boolean guessed = false;
    ArrayList<NewResponse> responseHistory = new ArrayList<NewResponse>();
    while (!questionPool.isEmpty()){
      int i = chooseSmartQuestion(myGuesses, questionPool, randomPercent);
      if (i == -1){// there were no questions that eliminated poss
        break;
      }
      NewQuestion question = questionPool.get(i);
      questionPool.remove(i);
      //ask the question
      NewResponse response = askQuestion (in, question, myGuesses);
      //System.out.println ("My guesses are: " + myGuesses);
      if (response.answer.equals("quit")){
        return;
      }
      responseHistory.add(response);
      if (myGuesses.size()<2) {
        break;
      }
    
    }

    //Ask a few more neglected questions
    for (int i =0; i<3; i++){
      if (questionPool.size() ==0){
        break;
      }
      questionPool = sortByLeastGuessed(questionPool);
      int rndmNumber = rndm.nextInt(Math.min(questionPool.size(),5));
      NewQuestion question = questionPool.get(rndmNumber);
      questionPool.remove(rndmNumber);
      //ask the question
      NewResponse response = askQuestion (in, question, myGuesses);
      //System.out.println ("My guesses are: " + myGuesses);
      if (response.answer.equals("quit")){
        return;
      }
      responseHistory.add(response);

    }
    boolean correct = makeGuess(in, myGuesses);
    Short guessId;
    if (!correct){
      //else didn't guess correctly or had multiple guesses
      System.out.println ("What were you thinking of?");
      String guessName = in.nextLine();
      guessName = guessName.toLowerCase();
      guessId = (short)guessArray.size();
      Guess guess = new Guess(guessId,guessName);
      guessArray.add(guess);
      myGuesses.add(guessId);
      getNewQuestion(in, myGuesses);
    } else {
      guessId = myGuesses.iterator().next();
    }
    for (NewResponse response: responseHistory){
      response.question.add(guessId, response.answer);
    }


  }

//  public void serialGuess(Scanner in){
//
//    HashSet<Short> myGuesses = new HashSet<Short>(guesses);
//    boolean guessed = false;
//    ArrayList<NewResponse> responseHistory = new ArrayList<NewResponse>();
//    for (Question question: questions){
//      //check to see if this question will narrow the guesses
//
//      HashSet<Short> all = new HashSet<Short>(question.yesGuesses);
//      all.addAll(question.noGuesses);
//      HashSet<Short> clone =new HashSet<Short>(myGuesses);
//      clone.retainAll(all);
//
//      if (!(question.yesGuesses.containsAll(clone) || question.noGuesses.containsAll(clone))){
//        //ask the question
//        NewResponse response = askQuestion (in, question, myGuesses);
//        //System.out.println ("My guesses are: " + myGuesses);
//        if (response.answer.equals("quit")){
//          return;
//        }
//        responseHistory.add(response);
//        if (myGuesses.size()<2) {
//          break;
//        }
//      }
//    }
//    boolean correct = makeGuess(in, myGuesses);
//    String guess;
//    if (!correct){
//      //else didn't guess correctly or had multiple guesses
//      System.out.println ("What were you thinking of?");
//      guess = in.nextLine();
//      guess = guess.toLowerCase();
//      guesses.add(guess);
//      myGuesses.add(guess);
//      getNewQuestion(in, myGuesses);
//    } else {
//      guess = myGuesses.iterator().next();
//    }
//    for (NewResponse response: responseHistory){
//      response.question.add(guess, response.answer);
//    }
//  }
//

  //just a utility function to answer questions with incomplete answers
  public void questionUtil(Scanner in){
    questions = sortByLeastGuessed(questions);
    ArrayList<NewQuestion> sorted = new ArrayList<NewQuestion>(questions);
    for (int i =0; i< sorted.size(); i++){
      System.out.println (i + ". " + sorted.get(i).question + "(" + sorted.get(i).numGuessesKnown());
    }

    boolean valid  = false;

    int i =0;
    while (!valid){
      System.out.println ("Enter number of question to fill in answers or 'quit'");
      String input = in.nextLine();
      if (input.equals("quit") || input.equals("q")){
        return;
      }

      try {
        i = Integer.parseInt(input);
        if (i >= 0 && i < sorted.size()){
          valid = true;
        }
      } catch (NumberFormatException nfe) {
          valid = false;
      }
    }
    NewQuestion question = sorted.get(i);
    HashSet<Short> unGuessed = new HashSet<Short>();
    for (Guess guess: guessArray){
      unGuessed.add(guess.guessId);
    }
    unGuessed.removeAll(question.yesGuesses);
    unGuessed.removeAll(question.noGuesses);
    unGuessed.removeAll(question.maybeGuesses);

    
    System.out.println ("Please answer your question for the following characters");
    System.out.println (question.question);
    for (Short guess: unGuessed){
      System.out.print (guess + " (y/n/maybe/quit):");

      String answer = getQInput(in);
      if (answer.equals("quit")){
        return;
      }
      question.add(guess, answer);
    }
  }
}
