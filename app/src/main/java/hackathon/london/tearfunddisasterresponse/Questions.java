package hackathon.london.tearfunddisasterresponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 03/10/15.
 */
public class Questions {

    private HashMap<String, Question[]> categoryToQuestions = new HashMap<String, Question[]>();
    private ArrayList<Question> queuedQuestions = new ArrayList<Question>();

    public Questions() {
        createQuestions();
    }

    public Question getNextQuestion(String category) {
        Question[] newQuestions = categoryToQuestions.get(category);
        if(newQuestions != null) {
            for (int i = 0; i < newQuestions.length; i++) {
                queuedQuestions.add(newQuestions[i]);
            }
            Question returnedQuestion = null;
            if (queuedQuestions.size() > 0) {
                returnedQuestion = queuedQuestions.get(0);
                queuedQuestions.remove(0);
                return returnedQuestion;
            }
        }
        return null;
    }

    public void createQuestions() {

        String[] categories = {"building", "collapsed", "partially collapsed", "standing but needs repairs", "standing with slight cracks"};
        ArrayList<String[]> questions = new ArrayList<String[]>();
        String[] questionList1 = {"What is the current status of the building?"};
        String[] questionList2 = {"How many people lived in the house?"};
        String[] questionList3 = {"Approximately how many visible cracks are there?", "Length of longest crack?", "Width of longest crack?",
            "How many people lived in the house?"};
        questions.add(questionList1);
        questions.add(questionList2);
        questions.add(questionList2);
        questions.add(questionList3);
        questions.add(questionList3);

        ArrayList<String> answersPeople = new ArrayList<String>();
        answersPeople.add(">3");
        answersPeople.add("3-5");
        answersPeople.add("5-7");
        answersPeople.add("8+");

        ArrayList<String> answersStatus = new ArrayList<String>();
        answersStatus.add("collapsed");
        answersStatus.add("partially collapsed");
        answersStatus.add("standing but needs repairs");
        answersStatus.add("standing with slight cracks");


        ArrayList<String> answersCracksNumber = new ArrayList<String>();
        answersCracksNumber.add(">3");
        answersCracksNumber.add("3-6");
        answersCracksNumber.add("7-10");
        answersCracksNumber.add("11+");

        ArrayList<String> answersCracksLength = new ArrayList<String>();
        answersCracksNumber.add(">15cm");
        answersCracksNumber.add(">50cm");
        answersCracksNumber.add(">150cm");
        answersCracksNumber.add("150cm+");

        ArrayList<String> answersCracksWidth = new ArrayList<String>();
        answersCracksNumber.add(">0.5cm");
        answersCracksNumber.add(">1cm");
        answersCracksNumber.add(">5cm");
        answersCracksNumber.add("5cm+");

        for(int i = 0; i < categories.length; i++) {
            String category = categories[i];
            ArrayList<Question> questionsForCategory = new ArrayList<Question>();
            String[] stringQuestionsForCategory = questions.get(i);
            for(int j = 0; j < stringQuestionsForCategory.length; j++) {
                String question = stringQuestionsForCategory[j];
                Question temp = null;
                if(question.contains("status")) {
                    temp = new Question(question, answersStatus);
                } else if(question.contains("people")) {
                    temp = new Question(question, answersPeople);
                } else if(question.contains("visible")) {
                    temp = new Question(question, answersCracksNumber);
                } else if(question.contains("Length")) {
                    temp = new Question(question, answersCracksLength);
                } else if(question.contains("Width")) {
                    temp = new Question(question, answersCracksWidth);
                }
                questionsForCategory.add(temp);
            }
            Question[] questionArray = new Question[questionsForCategory.size()];
            for(int n = 0; n < questionsForCategory.size(); n++) {
                questionArray[n] = questionsForCategory.get(n);
            }
            categoryToQuestions.put(category, questionArray);
        }
    }
}
