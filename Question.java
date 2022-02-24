
import java.util.*;
import java.io.*;


class Question{

  String question;
  public final static float GOOD_PERCENT = 80;  // if more than GOOD_PERCENT percent of the responses are a yes or no it goes in the yes or no set else it goes in the maybeGuesses

  public HashSet<String> yesGuesses;
  public HashSet<String> noGuesses;
  public HashSet<String> maybeGuesses;

  HashMap <String, Integer> numYeses;  //for each guess how many times a user has entered yes for this question
  HashMap <String, Integer> numNos;  //for each guess how many times a user has entered no for this question
  HashMap<String, Integer> numResponses;  //total number of times a user has entered a response to this question either yes, no, or unclear

  boolean verified;  // whether this is a admin verified question

  Question (String question){
    this.question = question;

    yesGuesses = new HashSet<String>();
    noGuesses = new HashSet<String>();
    maybeGuesses = new HashSet<String>();
    
    numYeses = new HashMap <String, Integer>();
    numNos = new HashMap <String, Integer>();
    numResponses = new HashMap<String, Integer>();
    verified = false;
  }


  Question (String question, HashSet<String> yesGuesses, HashSet<String> noGuesses, HashSet<String> maybeGuesses){
    this(question);
    this.yesGuesses.addAll(yesGuesses);
    this.noGuesses.addAll(noGuesses);
    this.maybeGuesses.addAll(maybeGuesses);
    addToCount(yesGuesses,numYeses);
    addToCount(noGuesses,numNos);
    addToCount(yesGuesses,numResponses);
    addToCount(noGuesses, numResponses);
    addToCount(maybeGuesses, numResponses);
    verified = false;
    
  }

  public int numGuessesKnown(){
    return (yesGuesses.size()+noGuesses.size()+maybeGuesses.size());
  }
  public void addToCount(HashSet<String> keys, HashMap<String, Integer> counts){
    for (String key: keys){ 
      Integer count = counts.getOrDefault(key, 0);
      count++;
      counts.put(key,count);
    }
  }

  public String toString(){
    return ("Question: " + question + " \\n yesGuesses:" + yesGuesses + "//n noGuesses:" +noGuesses +maybeGuesses);
  }


  public boolean equals(Question q){
    if (this.question.equals(q.question) ){
      return true;
    } else {
      return false;
    }

  }

  public int hashCode(){
    return question.hashCode();
  }

  //takes a guess and and an answer, and updates counts
  //answer must be "y","n","m"
  public void add(String guess, String answer){
    // System.out.println ("In add numYeses:" +numYeses);
    // System.out.println ("guess " + guess);
    // System.out.println ("answer " + answer);
    // System.out.println ("question:" + question);
    Integer yeses = numYeses.getOrDefault(guess, 0);
    Integer nos = numNos.getOrDefault(guess,0);
    Integer total = numResponses.getOrDefault(guess,0);

    if (answer.equals("y")){
      yeses++;
      numYeses.put(guess,yeses);     
    }  else if (answer.equals("n")){
      nos++;
      numNos.put(guess,nos);
    } else if (!answer.equals("m")){
      System.out.println ("Invalid input to Question.add");
      return;
    }
    total++;
    numResponses.put(guess,total);

    float yesPercent = yeses/total * 100;
    float noPercent = nos/total * 100;

    if (yesPercent > GOOD_PERCENT){
      yesGuesses.add(guess);
      noGuesses.remove(guess);
      maybeGuesses.remove(guess);
    } else if (noPercent > GOOD_PERCENT){
      noGuesses.add(guess);
      yesGuesses.remove(guess);
      maybeGuesses.remove(guess);
    } else {
      maybeGuesses.add(guess);
      noGuesses.remove(guess);
      yesGuesses.remove(guess);
    }

  }

  public void inflate(int multiplier){

        inflate(numYeses, multiplier);
        inflate(numNos, multiplier);
        inflate(numResponses, multiplier);

  }

  public void inflate (HashMap<String, Integer> data, int multiplier){
    for (String key: data.keySet()){
      int num = data.get(key);
      num = num * multiplier;
      data.put (key, num);
    }
  }
}
