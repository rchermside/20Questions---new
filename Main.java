import java.util.*;
import java.io.*;
import com.google.gson.Gson;


class Main {
  public static void main(String[] args) throws FileNotFoundException{
    System.out.println("Hello world!");


    Launcher launcher = new Launcher();

    //Scanner in = new Scanner(System.in);
    
    launcher.play();
    // //Guess

    // while (true){
    //   System.out.println ("Think of something in the D&D universe and I will guess it");
    //   System.out.println ("Do you want to have a smart guesser('s) or have a random guesser('r') or teach('t') the program?");
    //   String[] validInputs = {"r","s", "t"};
    //   String input = Guesser.getInput (in, validInputs);
    //   if (input.equals("s")){
    //     animalGuesser.smartGuess(in,0);
    //   } else if (input.equals("r")){
    //     animalGuesser.randomGuess(in);
    //   } else {
    //     animalGuesser.questionUtil(in);
    //   }
    //   System.out.println ("Do you want to play again (y/n)");
    //   input = Guesser.getInput(in);
    //   if (!(input.equals("yes") || input.equals("y"))){
    //     break;
    //   }
    // }

    // //Print out updated guesser to file 
    // launcher.save(guesserType);
    //Prune animalQuestions

    //animalGuesser.pruneQuestions(in);

    //Answer questions
    //animalGuesser.questionUtil(in);
 

  //   //Print out updated guesser to file 
  //   Gson ason = new Gson();
  //   String json = ason.toJson(animalGuesser); 
  //   //System.out.println (json);

  //   try {
  //     FileWriter myWriter = new FileWriter("dndGuesser.txt");
  //     myWriter.write(json);
  //     myWriter.close();
  //     System.out.println("Successfully wrote to the file.");
  //   } catch (IOException e) {
  //     System.out.println("An error occurred.");
  //     e.printStackTrace();
  //   } 
  }
}