package hackathon.london.tearfunddisasterresponse;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Sam on 03/10/15.
 */
public class Question {

    private String question = null;
    private Bitmap illustration = null;
    private ArrayList<String> answers = new ArrayList<String>();

    public Question(String question) {
        this.question = question;
    }

    public Question(String question, ArrayList<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    public Question(String question, Bitmap illustration, ArrayList<String> answers) {
        this.question = question;
        this.illustration = illustration;
        this.answers = answers;
    }

    public String getQuestion() {
        return this.question;
    }

    public ArrayList<String> getAnswers() {
        return this.answers;
    }
}
