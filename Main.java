import java.util.*;
import java.io.*;
import com.google.gson.Gson;


class Main {
  public static void main(String[] args) throws FileNotFoundException{
    System.out.println("Hello world!");

    File file = new File("animalQuestions.txt");
    Guesser animalGuesser;

    //if file doesn't exist create a brand new tree
    if (!file.exists()){
      //create a guesser
      HashSet<String> animals = new HashSet<>(Arrays.asList("cat", "bat", "crocodile","coyote","dog"));
      Scanner in = new Scanner(System.in);
      animalGuesser = new Guesser("Are you thinking of an animal?", animals);      
    // read in tree from a file 
    } else { 
      Scanner scan = new Scanner(file);
      String json = scan.nextLine();
      //System.out.println (json);
      Gson gson = new Gson();
      animalGuesser = gson.fromJson(json, Guesser.class);  
    }

    //Guess
    Scanner in = new Scanner(System.in);
    while (true){
      animalGuesser.randomGuess(in);
      System.out.println ("Do you want to play again (y/n)");
      String input = Guesser.getInput(in);
      if (!(input.equals("yes") || input.equals("y"))){
        break;
      }


    }
    
    //Print out updated guesser to file 
    Gson ason = new Gson();
    String json = ason.toJson(animalGuesser); 
    //System.out.println (json);

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