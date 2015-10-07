package hackathon.london.tearfunddisasterresponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sam on 03/10/15.
 */
public class ItemReport implements Serializable {

    private String deviceId = null;
    private String phoneNumber = null;
    private String category = null;
    private String location = null;
    private List<String> pictures = null;
    private HashMap<String, String> answers = new HashMap<>();
    private String notes = null;
    private Date timestamp = null;

    private final static String DEVICE_ID_NAME = "deviceId";
    private final static String PHONE_NUMBER_NAME = "phoneNumber";
    private final static String CATEGORY_NAME = "category";
    private final static String LOCATION_NAME = "location";
    private final static String TIME_NAME = "date/time";
    private final static String PICTURES_NAME = "pictureLinkList";
    private final static String NOTES_NAME = "notes";
    private final static String ANSWERS_NAME = "answers";

    private static final String PICTURE_URL_PREFIX = "https://s3-eu-west-1.amazonaws.com/tearfunddr/";

    public ItemReport() {

    }

    public ItemReport(String phoneNumber) {
        super();
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAnswer(String question) {
        return this.answers.get(question);
    }

    private Map<String, String> getAnswers() {
        return answers;
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

    public JSONObject generateJSON() {
        JSONObject reportJSON = new JSONObject();
        try {
            reportJSON.put(DEVICE_ID_NAME, getDeviceId());
            reportJSON.put(CATEGORY_NAME, getCategory());
            reportJSON.put(LOCATION_NAME, getLocation());
            reportJSON.put(TIME_NAME, getTimestamp().toString());
            reportJSON.put(PHONE_NUMBER_NAME, getPhoneNumber());
            reportJSON.put(PICTURES_NAME, createPictureList());
            reportJSON.put(NOTES_NAME, getNotes());
            reportJSON.put(ANSWERS_NAME, new JSONObject(getAnswers()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reportJSON;
    }

    private String createPictureList() {
        String pictureLinks = "";
        for (String pictureLink : getPictures()) {
            pictureLinks += pictureLink + " ";
        }
        return pictureLinks;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public void addPicture(String name) {
        String link = PICTURE_URL_PREFIX + name;
        pictures.add(link);
    }
}
