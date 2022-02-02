import java.util.*;
import java.io.*;
import com.google.gson.Gson;

class Balancer{

  balance(QuestionTree tree, int percent, Scanner in){
    int ySize = tree.nextNo.guesses.size();
    int nSize = tree.nextYes.guesses.size();
    dif = Math.abs(ySize-nSize);
    if ((dif/(ySize+nSize)> percent  && ((ySize + xSize)> 4))){
      HashSet<String> guesses = new HashSet<String>();
      guesses.addAll(tree.nextNo.guesses);
      guesses.addAll(tree.nextYes.guesses);
      System.out.println ("I currently need to  split as evenly as possible:");
      System.out.println (guesses);
      System.out.println ("Enter question or 'skip'");
      String question = in.nextLine();
      if (question != "skip"){

        //get answers to the question
        HashSet<String> noGuesses = new HashSet<String>();
        HashSet<String> yesGuesses = new HashSet<String>();
        System.out.println ("Please answer your question for the following characters");
        for (String guess: guesses){
          System.out.print (guess + " (y/n/quit):");
          answer = getInput(in);
          if (answer.equals("quit")){
            return;
          }
          if (answer.equals("y") || answer.equals("yes")){
            yesGuesses.add(guess);
          } else if (answer.equals("n") || answer.equals("no")){
            noGuesses.add(guess);
          }
        }

        QuestionTree newNode = new QuestionTree(question,guesses,tree.prev);
        //insert the new node above the unbalanced node and copy unbalanced node
        QuestionTree copy = copyNode(tree);
        newNode.nextNo = tree;
        newNode.nextYes = copy;



      }

    }
  }

}


class Question{
  String question;
  HashSet<String> yesGuesses;
  HashSet<String> noGuesses;
  HashSet<String> maybeGuesse;

  public String toString(){
    return question;
  }

  public boolean equals(Question question){
    if (id == question.id){
      return true;
    } else {
      return false;
    }
  }

  public int hashCode(){
    return id;
  }


}

class Guess{
  int id;
  String guess;
  ArrayList <Boolean> questions;
  
  public boolean equals(Guess guess){
    if (id == guess.id){
      return true;
    } else {
      return false;
    }
  }

  public String toString(){
    return guess;
  }

  public int hashCode(){
    return id;
  }
}

class Buckets{
  HashMap<Question,HashSet<Guess>> buckets;
}

class QuestionTree{
  public String question;
  public HashSet<String> guesses;
  public QuestionTree nextYes;
  public QuestionTree nextNo;
  transient QuestionTree prev;

  QuestionTree(){

  }

  QuestionTree(String question, HashSet<String> guesses, QuestionTree prev){
    this.question = question;
    this.guesses = guesses;
    nextYes = null;
    nextNo = null;
    prev = null;
  }

  QuestionTree copyNode(){
    HashSet<String> copyGuesses = new HashSet<String>(guesses);
    QuestionTree copy = new QuestionTree(question, copyGuesses, prev);
    copy.nextYes = nextYes;
    copy.nextNo = nextNo;
    return copy;
  }
  //navigates the tree filling in the parent nodes -- this is useful after reading in from json
  public void fillInPrev(QuestionTree parent){
    prev = parent;
    parent = this;
    if (nextYes != null){
      nextYes.fillInPrev(parent);
    }
    if (nextNo != null){
      nextNo.fillInPrev(parent);
    }
    return;
  }

  public static String getInput(Scanner in){
    String[] validInputs = {"y","yes","n","no","quit"};
    return getInput (in, validInputs);
  }

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

  public void getNewQuestion(Scanner in){
    System.out.println ("Can you enter a question that distinguishes between "+ guesses+ " (y/n)");
    String answer = getInput(in);
    if (answer.equals("y") || answer.equals("yes")){
      System.out.println ("Please enter the question");
      String newQuestion = in.nextLine();
      HashSet<String> noGuesses = new HashSet<String>();
      HashSet<String> yesGuesses = new HashSet<String>();
      System.out.println ("Please answer your question for the following characters");
      for (String guess: guesses){
        System.out.print (guess + " (y/n/quit):");

        answer = getInput(in);
        if (answer.equals("quit")){
          return;
        }
        if (answer.equals("y") || answer.equals("yes")){
          yesGuesses.add(guess);
        } else if (answer.equals("n") || answer.equals("no")){
          noGuesses.add(guess);
        }
      }

      QuestionTree noQuestionNode = new QuestionTree(null, noGuesses, this);
      QuestionTree yesQuestionNode = new QuestionTree(null, yesGuesses, this);

      question = newQuestion;
      nextYes = yesQuestionNode;
      nextNo = noQuestionNode;
    }
  }

  public void addGuess(String guess){
    guesses.add(guess);
    QuestionTree node = this;
    while (this.prev != null){
      node = this.prev;
      node.guesses.add(guess);
    }
    return;
  }

  public void askQuestion(Scanner in){

    //if we've reached the end of the tree
    if (question == null){
      String input = "n";
      if (guesses.size() > 1){
        System.out.println ("I guess:" + guesses);
        System.out.println ("Is your animal in the list?");
        input = getInput(in);
        if (input.equals("yes") || input.equals("y")){
          getNewQuestion(in);
          return;
        }
      } else if (guesses.size() == 1){
        for (String guess:guesses){
          System.out.println ("I guess: " + guess);
        }
        System.out.println ("Am I right?");
        input = getInput(in);
        if (input.equals("yes") || input.equals("y")){
          return;
        }
      } else { // if list is empty
        System.out.println ("I have no guesses");
      }
      if (input.equals("quit")){
        return;
      }
    // place holder for entering an animal
      System.out.println ("What were you thinking of?");
      String guess = in.nextLine();
      addGuess(guess);
      getNewQuestion(in);
    return;
    }

    System.out.println (question);

    String answer = getInput(in);
    System.out.println("answer "+ answer);

    if (answer.equals("q") || answer.equals("quit")){
      return;
    }

    if (answer.equals("y") || answer.equals("yes") ){
      if (nextYes == null){
        System.out.println ("Error: next yes is null");
        return;
      } else {
        nextYes.askQuestion(in);
      }
    } else if (answer.equals("n") || answer.equals("no") ){
      if (nextNo == null){
        System.out.println ("Error: next No is null");
        return;
      } else {
        nextNo.askQuestion(in);
      }
    }
  }
}


class Main {
  public static void main(String[] args) throws FileNotFoundException{
    System.out.println("Hello world!");

    //create tree

    File file = new File("animalQuestions.txt");
    QuestionTree animalTree;

    //if file doesn't exist create a brand new tree
    if (!file.exists()){
      HashSet<String> animals = new HashSet<>(Arrays.asList("cat", "bat", "crocodile","coyote","dog"));
      animalTree = new QuestionTree(null, animals, null);
    } else { // read in tree from a file 
      Scanner scan = new Scanner(file);
      String json = scan.nextLine();
      System.out.println (json);

      Gson gson = new Gson();
      animalTree = gson.fromJson(json, QuestionTree.class);  
      animalTree.fillInPrev(null);
    }

    //Guess
    Scanner in = new Scanner(System.in);
    while (true){
      System.out.println ("Do you want to guess an animal? (y/n)");
      String input = QuestionTree.getInput(in);
      if (!(input.equals("yes") || input.equals("y"))){
        break;
      }
      animalTree.askQuestion(in);

    }
    
    //Print out updated tree to file 
    Gson ason = new Gson();
    String json = ason.toJson(animalTree); 
    System.out.println(ason);
    try {
      FileWriter myWriter = new FileWriter("animalQuestions.txt");
      myWriter.write(json);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}