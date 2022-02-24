package newclasses;

public class NewResponse{
  NewQuestion question;
  String answer;

  public NewResponse(NewQuestion q, String answer){
    this.question = q;
    this.answer = answer;
  }
}