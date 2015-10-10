package hackathon.london.tearfunddisasterresponse;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 03/10/15.
 */
public class Question {

    private String question = null;
    private Bitmap illustration = null;
    private ArrayList<String> answers = new ArrayList<String>();

    private String identifier = null;
    private String questionText = null;
    private HashMap<String, String> answerAndNext = new HashMap<String, String>();

    public Question() {

    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void addAnswer(String answer, String nextQuestion) {
        answerAndNext.put(answer, nextQuestion);
    }

    public ArrayList<String> answers() {
        ArrayList<String> allAnswers = new ArrayList<String>(this.answerAndNext.keySet());
        return allAnswers;
    }

    public String nextQuestion(String answer) {
        return this.answerAndNext.get(answer);
    }

//    public Question(String question) {
//        this.question = question;
//    }
//
//    public Question(String question, ArrayList<String> answers) {
//        this.question = question;
//        this.answers = answers;
//    }
//
//    public Question(String question, Bitmap illustration, ArrayList<String> answers) {
//        this.question = question;
//        this.illustration = illustration;
//        this.answers = answers;
//    }
//
//    public String getQuestion() {
//        return this.question;
//    }
//
//    public ArrayList<String> getAnswers() {
//        return this.answers;
//    }
}
