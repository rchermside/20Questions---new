import java.util.*;
import java.io.*;
import com.google.gson.Gson;
import newclasses.NewGuesser;


class Main {
  public static void main(String[] args) throws FileNotFoundException{
    System.out.println("Click on screen to get focus before playing");



    // String guesserType="dnd";
    // Guesser guesser = launcher.getGuesser(guesserType);
    // guesser.inflateCurrentEntries(50);
    // launcher.save(guesserType);
    


    //Scanner in = new Scanner(System.in);
    String guesserType = "animal";
    Launcher launcher = new Launcher();
    Guesser oldGuesser = launcher.getGuesser(guesserType);
    Converter conv = new Converter();
    NewGuesser newGuesser = conv.convertGuesser(oldGuesser);


    Gson ason = new Gson();
    String json = ason.toJson(newGuesser); 
    //System.out.println (json);

    try {
      FileWriter myWriter = new FileWriter("data/" + "new" + guesserType + "Guesser.txt");
      myWriter.write(json);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    // launcher.play();
    
  }
}