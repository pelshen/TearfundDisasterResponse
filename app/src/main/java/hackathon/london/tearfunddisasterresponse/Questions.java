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

    private HashMap<String, Question[]> categoryToQuestions = new HashMap<String, Question[]>();
    private ArrayList<Question> queuedQuestions = new ArrayList<Question>();

    private HashMap<String, Question> questionsList = null;

    public Questions(Context context) {
//        createQuestions();
        questionsFromXML(context);
    }

//    public Question getNextQuestion(String category) {
//        Question[] newQuestions = categoryToQuestions.get(category);
//        if(newQuestions != null) {
//            for (int i = 0; i < newQuestions.length; i++) {
//                queuedQuestions.add(newQuestions[i]);
//            }
//            Question returnedQuestion = null;
//            if (queuedQuestions.size() > 0) {
//                returnedQuestion = queuedQuestions.get(0);
//                queuedQuestions.remove(0);
//                return returnedQuestion;
//            }
//        }
//        return null;
//    }

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


//    public void createQuestions() {
//
//        String[] categories = {"building", "collapsed", "partially collapsed", "standing but needs repairs",
//                "standing with slight cracks", ">5","5-10","11-20","21+", ">15cm",">50cm", ">150cm", "150cm+",
//                ">0.5cm", ">1cm", ">5cm", "5cm+"};
//        ArrayList<String[]> questions = new ArrayList<String[]>();
//        String[] questionList1 = {"What is the current status of the building?"};
//        String[] questionList2 = {"How many people lived in the house?"};
//        String[] questionList3 = {"Approximately how many visible cracks are there?"};
//        String[] questionList4 = {"Length of longest crack?"};
//        String[] questionList5 = {"Width of longest crack?"};
//        questions.add(questionList1);
//        questions.add(questionList2);
//        questions.add(questionList2);
//        questions.add(questionList3);
//        questions.add(questionList3);
//        questions.add(questionList4);
//        questions.add(questionList4);
//        questions.add(questionList4);
//        questions.add(questionList4);
//        questions.add(questionList5);
//        questions.add(questionList5);
//        questions.add(questionList5);
//        questions.add(questionList5);
//        questions.add(questionList2);
//        questions.add(questionList2);
//        questions.add(questionList2);
//        questions.add(questionList2);
//
//        ArrayList<String> answersPeople = new ArrayList<String>();
//        answersPeople.add(">3");
//        answersPeople.add("3-5");
//        answersPeople.add("5-7");
//        answersPeople.add("8+");
//
//        ArrayList<String> answersStatus = new ArrayList<String>();
//        answersStatus.add("collapsed");
//        answersStatus.add("partially collapsed");
//        answersStatus.add("standing but needs repairs");
//        answersStatus.add("standing with slight cracks");
//
//
//        ArrayList<String> answersCracksNumber = new ArrayList<String>();
//        answersCracksNumber.add(">5");
//        answersCracksNumber.add("5-10");
//        answersCracksNumber.add("11-20");
//        answersCracksNumber.add("21+");
//
//        ArrayList<String> answersCracksLength = new ArrayList<String>();
//        answersCracksLength.add(">15cm");
//        answersCracksLength.add(">50cm");
//        answersCracksLength.add(">150cm");
//        answersCracksLength.add("150cm+");
//
//        ArrayList<String> answersCracksWidth = new ArrayList<String>();
//        answersCracksWidth.add(">0.5cm");
//        answersCracksWidth.add(">1cm");
//        answersCracksWidth.add(">5cm");
//        answersCracksWidth.add("5cm+");
//
//        for(int i = 0; i < categories.length; i++) {
//            String category = categories[i];
//            ArrayList<Question> questionsForCategory = new ArrayList<Question>();
//            String[] stringQuestionsForCategory = questions.get(i);
//            for(int j = 0; j < stringQuestionsForCategory.length; j++) {
//                String question = stringQuestionsForCategory[j];
//                Question temp = null;
//                if(question.contains("status")) {
//                    temp = new Question(question, answersStatus);
//                } else if(question.contains("people")) {
//                    temp = new Question(question, answersPeople);
//                } else if(question.contains("visible")) {
//                    temp = new Question(question, answersCracksNumber);
//                } else if(question.contains("Length")) {
//                    temp = new Question(question, answersCracksLength);
//                } else if(question.contains("Width")) {
//                    temp = new Question(question, answersCracksWidth);
//                }
//                questionsForCategory.add(temp);
//            }
//            Question[] questionArray = new Question[questionsForCategory.size()];
//            for(int n = 0; n < questionsForCategory.size(); n++) {
//                questionArray[n] = questionsForCategory.get(n);
//            }
//            categoryToQuestions.put(category, questionArray);
//        }
//    }
}
