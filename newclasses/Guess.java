package newclasses;

public class Guess {
    public short guessId;
    public String name;
    public boolean v; //whether the guess has been verified

    public Guess(short guessId, String name){
        this.guessId = guessId;
        this.name = name;
        v = false;
    }

    public void verify(){
        v = true;
    }

    public String toString(){
        return name;
    }

}
