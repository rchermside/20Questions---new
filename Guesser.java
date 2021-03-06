import java.util.*;
import java.io.*;


public class Guesser{
  String startingInstruction;
  ArrayList<Question> questions;
  HashSet<String> guesses;
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

  Guesser (String question, HashSet<String> guesses, String startingInstruction){
    questions = new ArrayList<Question>();
    questions.add(new Question(question));
    this.guesses = guesses;
    this.startingInstruction = startingInstruction;
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
  public ArrayList<Question> sortByLeastGuessed(ArrayList<Question> questionPool){
    ArrayList<Question> leastGuessed = new ArrayList<Question>(questionPool);
    Collections.sort(leastGuessed, new UnguessedComparator());
    return leastGuessed;
  }
  
  public void getNewQuestion(Scanner in, HashSet<String> myGuesses){
  
    System.out.println ("Do you want to enter a question that distinguishes between "+ myGuesses+ " (y/n)");
    String answer = getInput(in);
    if (answer.equals("y")){
      System.out.println ("Please enter the question");
      String newQuestion = in.nextLine();
      HashSet<String> noGuesses = new HashSet<String>();
      HashSet<String> yesGuesses = new HashSet<String>();
      HashSet<String> maybeGuesses = new HashSet<String>();

      
      HashSet<String> asks = new HashSet<String>(myGuesses);
      final int NUM_ASKS = 9;
      int needed = NUM_ASKS - asks.size();
      int pool = guesses.size();
      for (String guess: guesses){
        if (pool <= needed){
          asks.add(guess);
          needed--;
        } else {
          int rndNumber = rndm.nextInt(pool);
          if (rndNumber < needed){
            asks.add(guess);
            needed--;
          }
          if (needed == 0){
            break;
          }
        }
        pool--;
      }
      
      System.out.println ("Please answer your question for the following characters");

      for (String guess: asks){
        System.out.print (guess + " (y/n/maybe/quit):");

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
      Question q = new Question(newQuestion, yesGuesses, noGuesses, maybeGuesses);
      questions.add(q);
      System.out.println ("Thank you for making me a better game!");
    }
  }

  //asks the passed in question to the user and returns a response
  public Response askQuestion (Scanner in, Question question, HashSet<String> myGuesses){
      System.out.println (question.question +" (y/n/maybe/quit)");
      String input = getQInput(in);
      if (input.equals("n")){
        myGuesses.removeAll(question.yesGuesses);
      } else if (input.equals("y")){
        myGuesses.removeAll(question.noGuesses);
      }
      Response response = new Response (question, input);
      return response;

  }


  //makes a guess to the user and returns whether the guess was correct(true)
  public boolean makeGuess(Scanner in, HashSet<String> myGuesses){
      if (myGuesses.size() == 0){
        System.out.println ("I have no idea!!!");
        return false;
      }

      if (myGuesses.size() == 1){
        String myGuess = myGuesses.iterator().next();
        System.out.println("I guess a  " + myGuess + ". Am I right? (y/n)");
        String correct = getInput(in);
        if (correct.equals("y")){
          System.out.println ("YAHOO!! I'm the best!!");
          return true;
        } 
        return false;
      }

      //else there are multiple guesses in myGuesses
      System.out.println ("I guess its one of" + myGuesses);
      System.out.println ("Am I right? (y/n)");
      String correct = getInput(in);
      return false;
  }


  public void randomGuess(Scanner in ){

    HashSet<String> myGuesses = new HashSet<String>(guesses);
    ArrayList<Question> questionPool = new ArrayList<Question>(questions);
    //nSystem.out.println ("questionPool:" +questionPool);
    boolean guessed = false;
    ArrayList<Response> responses = new ArrayList<Response>();
    while (!questionPool.isEmpty()){
      //randomly choose a question out of the question pool
      //check to see if this question will narrow the guesses

      // this will generate a random number between 0 and
      // HashSet.size - 1
      int rndmNumber = rndm.nextInt(questionPool.size());
      Question question = questionPool.get(rndmNumber);
      questionPool.remove(rndmNumber);

      HashSet<String> all = new HashSet<String>(question.yesGuesses);
      all.addAll(question.noGuesses);
      HashSet<String> clone =new HashSet<String>(myGuesses);
      clone.retainAll(all);

      if (!(question.yesGuesses.containsAll(clone) || question.noGuesses.containsAll(clone))){
        //ask the question
        Response response = askQuestion (in, question, myGuesses);
        //System.out.println ("My guesses are: " + myGuesses);
        if (response.answer.equals("quit")){
          return;
        }
        responses.add(response);
        if (myGuesses.size()<2) {
          break;
        }
      }
    }
    boolean correct = makeGuess(in, myGuesses);
    String guess;
    if (!correct){
      //else didn't guess correctly or had multiple guesses
      System.out.println ("What were you thinking of?");
      guess = in.nextLine();
      guess = guess.toLowerCase();
      guesses.add(guess);
      myGuesses.add(guess);
      getNewQuestion(in, myGuesses);
    } else {
      guess = myGuesses.iterator().next();
    }
    for (Response response: responses){
      response.question.add(guess, response.answer);
    }

  }


  //picks the Question out of the question pool that will eliminate the most guesses if the worse answer is chosen and returns the index of the question -- if no question in the question pool will eliminate any answers returns -1
  public int chooseSmartQuestion(HashSet<String> myGuesses, ArrayList<Question> questionPool, int random){
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
      HashSet<String> noElims = new HashSet<String>(myGuesses);
      noElims.retainAll(questionPool.get(i).noGuesses);
      HashSet<String> yesElims = new HashSet<String>(myGuesses);
      yesElims.retainAll(questionPool.get(i).yesGuesses);
      int minElim = Math.min(noElims.size(), yesElims.size());
      if (minElim > bestElim){
        bestElim = minElim;
        chosen = i;
      }
    }
    System.out.println ("bestelim is:" + bestElim);
    return (chosen);
  }


  //random - percentage of times to stick a random question in 
  public void smartGuess(Scanner in, int randomPercent){
    HashSet<String> myGuesses = new HashSet<String>(guesses);
    ArrayList<Question> questionPool = new ArrayList<Question>(questions);
    //nSystem.out.println ("questionPool:" +questionPool);
    boolean guessed = false;
    ArrayList<Response> responses = new ArrayList<Response>();
    while (!questionPool.isEmpty()){
      //System.out.println ("myGuesses:" + myGuesses);
      int i = chooseSmartQuestion(myGuesses, questionPool, randomPercent);
      if (i == -1){// there were no questions that eliminated poss
        break;
      }
      Question question = questionPool.get(i);
      questionPool.remove(i);
      //ask the question
      Response response = askQuestion (in, question, myGuesses);
      //System.out.println ("My guesses are: " + myGuesses);
      if (response.answer.equals("quit")){
        return;
      }
      responses.add(response);
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
      Question question = questionPool.get(rndmNumber);
      questionPool.remove(rndmNumber);
      //ask the question
      Response response = askQuestion (in, question, myGuesses);
      //System.out.println ("My guesses are: " + myGuesses);
      if (response.answer.equals("quit")){
        return;
      }
      responses.add(response);

    }
    boolean correct = makeGuess(in, myGuesses);
    String guess;
    if (!correct){
      //else didn't guess correctly or had multiple guesses
      System.out.println ("What were you thinking of?");
      guess = in.nextLine();
      guess = guess.toLowerCase();
      guesses.add(guess);
      myGuesses.add(guess);
      getNewQuestion(in, myGuesses);
    } else {
      guess = myGuesses.iterator().next();
    }
    for (Response response: responses){
      response.question.add(guess, response.answer);
    }


  }

  public void serialGuess(Scanner in){

    HashSet<String> myGuesses = new HashSet<String>(guesses);
    boolean guessed = false;
    ArrayList<Response> responses = new ArrayList<Response>();
    for (Question question: questions){
      //check to see if this question will narrow the guesses
      
      HashSet<String> all = new HashSet<String>(question.yesGuesses);
      all.addAll(question.noGuesses);
      HashSet<String> clone =new HashSet<String>(myGuesses);
      clone.retainAll(all);

      if (!(question.yesGuesses.containsAll(clone) || question.noGuesses.containsAll(clone))){
        //ask the question
        Response response = askQuestion (in, question, myGuesses);
        //System.out.println ("My guesses are: " + myGuesses);
        if (response.answer.equals("quit")){
          return;
        }
        responses.add(response);
        if (myGuesses.size()<2) {
          break;
        }
      }
    }
    boolean correct = makeGuess(in, myGuesses);
    String guess;
    if (!correct){
      //else didn't guess correctly or had multiple guesses
      System.out.println ("What were you thinking of?");
      guess = in.nextLine();
      guess = guess.toLowerCase();
      guesses.add(guess);
      myGuesses.add(guess);
      getNewQuestion(in, myGuesses);
    } else {
      guess = myGuesses.iterator().next();
    }
    for (Response response: responses){
      response.question.add(guess, response.answer);
    }
  }


  //just a utility function to answer questions with incomplete answers
  public void questionUtil(Scanner in){
    questions = sortByLeastGuessed(questions);
    ArrayList<Question> sorted = new ArrayList<Question>(questions);
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
    Question question = sorted.get(i);
    HashSet<String> unGuessed = new HashSet<String>(guesses);
    unGuessed.removeAll(question.yesGuesses);
    unGuessed.removeAll(question.noGuesses);
    unGuessed.removeAll(question.maybeGuesses);

    
    System.out.println ("Please answer your question for the following characters");
    System.out.println (question.question);
    for (String guess: unGuessed){
      System.out.print (guess + " (y/n/maybe/quit):");

      String answer = getQInput(in);
      if (answer.equals("quit")){
        return;
      }
      question.add(guess, answer);
    }
  }

  public void inflateCurrentEntries(int multiplier){
    for (Question question: questions){
      question.inflate (multiplier);
    }
  }
}
