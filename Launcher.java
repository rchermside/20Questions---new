import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    


class Launcher {
  static  final Scanner in = new Scanner(System.in);
  static final String[] guesserTypes = {"dnd", "animal"};
  private HashMap <String, Guesser> guessers;
  

Launcher(){
  guessers = new HashMap<String, Guesser>();
}

  
  public void play(){

    System.out.println ("What kind of guesser do you want to guess?");
    String[] choices = new String[guesserTypes.length];
    int i = 1;
    for (String guesserType: guesserTypes){
      System.out.println (i + "." + guesserType);
      choices[i-1]=Integer.toString(i);
      i++;
    }
    System.out.println ("Enter choice:");
    String input = Guesser.getInput (in, choices);
    int choice = Integer.parseInt(input)-1;


    String guesserType = guesserTypes[choice];
    Guesser guesser = getGuesser(guesserType);

    while (true){

      System.out.println (guesser.startingInstruction);
      System.out.println ("Do you want to have a smart guesser('s) or have a random guesser('r') or teach('t') the program?");
      String[] validInputs = {"r","s", "t","runtheadmin1234"};
      input = Guesser.getInput (in, validInputs);
      if (input.equals("s")){
        guesser.smartGuess(in,0);
      } else if (input.equals("r")){
        guesser.randomGuess(in);
      } else {
        guesser.questionUtil(in);
      } 
      System.out.println ("Do you want to play again (y/n)");
      input = Guesser.getInput(in);
      if (!(input.equals("yes") || input.equals("y"))){
        break;
      }
    }

    save(guesserType);

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
      File file = new File("data/"+ guesserType + "Guesser.txt");
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
          guesser = new Guesser("Are you thinking of a monster?", guesses, startingInstruction);      

      }
    guessers.put(guesserType, guesser);
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

  public void save(String guesserType){
    
    Guesser guesser = guessers.get(guesserType);
    if (guesser == null){
      System.out.println ("Guesser type " + guesserType + " has not been created.  Can not write to file.");
      return;
    }
    
    Gson ason = new Gson();
    String json = ason.toJson(guesser); 
    //System.out.println (json);

    try {
      //Print out updated guesser to file 
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
      LocalDateTime now = LocalDateTime.now();  
      System.out.println(dtf.format(now));  

      File renameTo = new File ("data/"+guesserType+ "Guesser" +dtf.format(now) + ".txt");

      // File (or directory) with old name
      File file = new File("data/" + guesserType+ "Guesser.txt");

      if (renameTo.exists())
        renameTo.delete();

      // Rename file (or directory)
      boolean success = file.renameTo(renameTo);

      if (!success) {
        System.out.println ("file was not renamed");
      }
      FileWriter myWriter = new FileWriter("data/" + guesserType + "Guesser.txt");
      myWriter.write(json);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}