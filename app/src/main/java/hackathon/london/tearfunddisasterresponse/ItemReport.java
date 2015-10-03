package hackathon.london.tearfunddisasterresponse;

import android.graphics.Bitmap;

import java.util.HashMap;

/**
 * Created by Sam on 03/10/15.
 */
public class ItemReport {

    private String phoneNumber = null;
    private String category = null;
    private String location = null;
    private Bitmap picture = null;
    private HashMap<String, String> answers = new HashMap<String, String>();
    private String notes = null;

    public ItemReport() {

    }

    public ItemReport(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getPicture() {
        return this.picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getAnswer(String question) {
        return this.answers.get(question);
    }

    public void answerQuestion(String question, String answer) {
        this.answers.put(question, answer);
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
