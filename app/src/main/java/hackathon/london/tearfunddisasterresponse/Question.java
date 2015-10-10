package hackathon.london.tearfunddisasterresponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 03/10/15.
 */
public class Question {

    private String identifier = null;
    private String questionText = null;
    private HashMap<String, String> answerAndNext = new HashMap<String, String>();
    private ArrayList<String> answers = new ArrayList<String>();

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
        answers.add(answer);
    }

    public ArrayList<String> answers() {
        return this.answers;
    }

    public String nextQuestion(String answer) {
        return this.answerAndNext.get(answer);
    }
}
