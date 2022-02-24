import newclasses.*;
import java.util.*;
import newclasses.Guess;

class Converter{
// public class Guesser{
//   String startingInstruction;
//   ArrayList<Question> questions;
//   HashSet<String> guesses;
  
  //NewGuesser(String startingInstruction,ArrayList<NewQuestion> questions, ArrayList<Guess> guessArray )
  NewGuesser convertGuesser(Guesser oldGuesser){

    //create guessArray;
    short i = 0;
    ArrayList<Guess> guessArray = new ArrayList<Guess>();
    HashMap<String, Short> nameLookup = new HashMap<>();
    ArrayList<String> sortedGuesses= new ArrayList<String>(oldGuesser.guesses);
    Collections.sort(sortedGuesses); 
    for (String guessName: sortedGuesses){
      newclasses.Guess guess = new newclasses.Guess(i,guessName);
      guess.verify();
      nameLookup.put(guessName,i);
      guessArray.add(guess);
      i++;
    }

    //create newQuestionArray
    // public NewQuestion (String question){
    //     this.question = question;

    //     yesGuesses = new HashSet<Short>();
    //     noGuesses = new HashSet<Short>();
    //     maybeGuesses = new HashSet<Short>();

    //     numYeses = new HashMap<Short, Integer>();
    //     numNos = new HashMap<Short, Integer>();
    //     numResponses = new HashMap<Short, Integer>();
    //     verified = false;
    // }

    ArrayList<NewQuestion> newQuestions = new ArrayList<NewQuestion>();
    for (Question question: oldGuesser.questions){
      NewQuestion newQuestion = new NewQuestion(question.question);
      for (String guessName: question.yesGuesses){
        newQuestion.yesGuesses.add(nameLookup.get(guessName));
      }
      for (String guessName: question.noGuesses){
        newQuestion.noGuesses.add(nameLookup.get(guessName));
      }
      for (String guessName: question.maybeGuesses){
        newQuestion.maybeGuesses.add(nameLookup.get(guessName));
      }

      for (String guessName: question.numYeses.keySet()){
        Integer count = question.numYeses.get(guessName);
        newQuestion.numYeses.put(nameLookup.get(guessName), count);
      }
      for (String guessName: question.numNos.keySet()){
        Integer count = question.numNos.get(guessName);
        newQuestion.numNos.put(nameLookup.get(guessName), count);
      }
      for (String guessName: question.numResponses.keySet()){
        Integer count = question.numResponses.get(guessName);
        newQuestion.numResponses.put(nameLookup.get(guessName), count);
      }
      newQuestion.verify();
    }
    return new NewGuesser(oldGuesser.startingInstruction, newQuestions, guessArray);
  }
}