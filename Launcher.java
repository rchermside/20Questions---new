import java.util.*;
import java.io.*;
import com.google.gson.Gson;

class Launcher {
  static  final Scanner in = new Scanner(System.in);
  private HashMap <String, Guesser> guessers;
  

Launcher(){
  guessers = new HashMap<String, Guesser>();
}

  
  public void play(String type){

  }

  //if a guesser has been previously read it returns that, if not
  //checks to see if there is an existing file for the guesser and
  //if not creates a new guesser
  public Guesser getGuesser(String guesserType){
    //get guesser if it one has already been created
    Guesser guesser = guessers.get(guesserType);

    //if guesser hasn't already been created read it in from a file 
    //or if no file exists create a brand new guesser
    if (guesser == null){//read in file
      File file = new File(guesserType + "Guesser.txt");
      try {
        guesser = readFile(file);
      } catch(FileNotFoundException err) {
          //create a guesser
          HashSet<String> guesses;
          String startingInstruction;
          if (guesserType.equals("animal")){
            guesses = new HashSet<>(Arrays.asList("cat", "bat", "crocodile","coyote","dog"));
            startingInstruction = "Think of an animal and I will guess it";
          }
          else if (guesserType.equals("dnd"))  {
            guesses = new HashSet<>(Arrays.asList("cleric", "dragon", "wyvern", "druid", "your character"));
            startingInstruction = "Think of something related to d&d and I will guess it";
          }    
          else if (guesserType.equals("food")){
            guesses = new HashSet<>(Arrays.asList("onion","taco","apple"));
            startingInstruction = "Think of a food and I will guess it";
          } else {
            guesses = new HashSet<String>();
            startingInstruction = "Think of a " + guesserType;
          }
          guesser = new Guesser(null, guesses, startingInstruction);      

      }


    }
    return guesser;
  }

  //reads the guesser file 
  public Guesser readFile(File file) throws FileNotFoundException{
    Guesser guesser;
    Scanner scan = new Scanner(file);
    String json = scan.nextLine();
    Gson gson = new Gson();
    guesser = gson.fromJson(json, Guesser.class);  
    return guesser;
  }
}