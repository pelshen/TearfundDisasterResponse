package hackathon.london.tearfunddisasterresponse;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 03/10/15.
 */
public class Questions {

    private HashMap<String, Question> questionsList = null;

    public Questions(Context context) {
        questionsFromXML(context);
    }


    public void questionsFromXML(Context context) {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = context.getAssets().open("questions.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException {
        int eventType = parser.getEventType();
        Question currentQuestion = null;
        String answerText = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    questionsList = new HashMap<String, Question>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Question")) {
                        currentQuestion = new Question();
                    } else if (currentQuestion != null) {
                        if (name.equalsIgnoreCase("QuestionIdentifier")) {
                            currentQuestion.setIdentifier(parser.nextText());
                        } else if (name.equalsIgnoreCase("QuestionText")) {
                            currentQuestion.setQuestionText(parser.nextText());
                        } else if (name.equalsIgnoreCase("AnswerText")) {
                            answerText = parser.nextText();
                        } else if (name.equalsIgnoreCase("FollowUpQuestionIdentifier")) {
                            String followupQuestionIdentifier = parser.nextText();
                            if(answerText != null) {
                                currentQuestion.addAnswer(answerText, followupQuestionIdentifier);
                            }
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("Question") && currentQuestion != null) {
                        questionsList.put(currentQuestion.getIdentifier(), currentQuestion);
                    }
            }
            eventType = parser.next();
        }
    }

    public Question getNextQuestion(String identifier) {
        return this.questionsList.get(identifier);
    }
}
