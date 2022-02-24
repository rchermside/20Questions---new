import java.util.*;

public class Admin{
  Guesser guesser;

  Admin(Guesser guesser){
    this.guesser = guesser;
  }

  public void run(Scanner in ){
    guesser.inflateCurrentEntries(50);

  }
}