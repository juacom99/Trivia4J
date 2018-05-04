/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j;

import com.improvisados.trivia4j.dataproviders.questionprovider.opentriviadb.OpenTriviaDBQuestionsProvider;
import com.improvisados.trivia4j.logic.Trivia;
import com.improvisados.trivia4j.events.ITriviaEventListener;
import com.improvisados.trivia4j.logic.Question;
import com.improvisados.trivia4j.logic.QuestionCategory;
import com.improvisados.trivia4j.logic.QuestionDifficulty;
import com.improvisados.trivia4j.logic.QuestionType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joaquin
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException
    {
        Trivia t=new Trivia();
        
        OpenTriviaDBQuestionsProvider p=OpenTriviaDBQuestionsProvider.getInstance();
        
        p.withAmmount(15);
        p.withCategory(15);
        
        List<Question> questions=p.getQuestions();
       
        for(Question q: questions)
        {
            t.addQuestion(q);
        }
        
        
        t.addEventListener(new ITriviaEventListener()
        {
            @Override
            public void onTriviaStart()
            {
                System.out.println("Trivia is about to start");
            }

            @Override
            public void onNewQuestion(Question q,int index)
            {
                System.out.println("Question :"+index+"/"+t.size());
                System.out.println("Category: "+q.getCategory());
                System.out.println("Dificulty: "+q.getDifficulty());
                System.out.println(q.getQuestion());
                System.out.println("");
                int itemChar=97;
                for(String anwsers:q.getAnwsers())
                {
                    System.out.println(((char)itemChar)+" "+anwsers);
                    itemChar++;
                }
                
            }

            @Override
            public void onQuestionTimeOut(Question q,int index)
            {
                System.out.println("");
                System.out.println("Correct Anwser: "+q.getCorrectAnwser());
            }

            @Override
            public void onTriviaFinish(boolean wasCanceled)
            {
                System.out.println("Trivia has finshed "+wasCanceled);
            }
        });
        
        t.start();
        
        Thread.sleep(90000);
        
    }
    
}
