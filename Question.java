import java.util.*;
import java.io.*;


class Question{
  String question;
  int yesses;  // the number of guesses for which a s high percentage of yes
  int nos;    // the number of guesses for which you have a strong no
  int maybes;  // the number of guesses that are unsure

  HashSet<String> yesGuesses;
  HashSet<String> noGuesses;
  HashSet<String> maybeGuesses;

  HashMap <String, Integer> numYesses;  //for each guess how many times a user has entered yes for this question
  HashMap <String, Integer> numNos;  //for each guess how many times a user has entered no for this question
  HashMap<String, Integer> numResponses;  //total number of times a user has entered a response to this question either yes, no, or unclear

  Question (String question){
    this.question = question;
    yesses = 0;
    nos =0;
    maybes =0;

    yesGuesses = new HashSet<String>();
    noGuesses = new HashSet<String>();
    maybeGuesses = new HashSet<String>();
    
    numYesses = new HashMap <String, Integer>();
    numNos = new HashMap <String, Integer>();
    numResponses = new HashMap<String, Integer>();
  }

  public String toString(){
    return question;
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

}
