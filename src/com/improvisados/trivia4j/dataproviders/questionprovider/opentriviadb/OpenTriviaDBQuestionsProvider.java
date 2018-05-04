/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.dataproviders.questionprovider.opentriviadb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.improvisados.trivia4j.logic.Question;
import com.improvisados.trivia4j.logic.QuestionDifficulty;
import com.improvisados.trivia4j.dataproviders.questionprovider.QuestionProvider;
import com.improvisados.trivia4j.logic.QuestionType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaquin
 */
public class OpenTriviaDBQuestionsProvider extends QuestionProvider
{

    private int amount;
    private int categoty;
    private QuestionDifficulty difficulty;
    private QuestionType type;
    private Gson gson;

    private static OpenTriviaDBQuestionsProvider instance;

    private static String API_BASE_URL = "https://opentdb.com/api.php";

    private OpenTriviaDBQuestionsProvider()
    {
        amount = 0;
        categoty = 0;
        difficulty = null;
        type = null;
        gson = new GsonBuilder().registerTypeAdapter(Question.class, new QuestionModelDeserializer()).setPrettyPrinting().create();
    }

    public void withAmmount(int amount)
    {
        if (amount > -1)
        {
            this.amount = amount;
        }
    }

    public void withCategory(int categoty)
    {
        this.categoty = categoty;
    }

    public void withDifficulty(QuestionDifficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    public void withType(QuestionType type)
    {
        this.type = type;
    }

    @Override
    public List<Question> getQuestions()
    {
        URL url = null;
        HttpURLConnection request;
         List<Question> output=null;
        try
        {
            url = buildURL();
            request = (HttpURLConnection) url.openConnection();
            request.setDoOutput(true);
            request.setRequestMethod("GET");
            request.connect();
            
            Reader reader = new InputStreamReader((InputStream) request.getContent());
            
            OpentDBResponse answer = gson.fromJson(reader, OpentDBResponse.class);
            
            if(answer.getResponseCode()==0)
            {
                output=answer.getQuestions();
            }
            
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(OpenTriviaDBQuestionsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ProtocolException ex)
        {
            Logger.getLogger(OpenTriviaDBQuestionsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(OpenTriviaDBQuestionsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static OpenTriviaDBQuestionsProvider getInstance()
    {
        if (instance == null)
        {
            instance = new OpenTriviaDBQuestionsProvider();
        }

        return instance;
    }

    private URL buildURL() throws MalformedURLException
    {

        Map<String, String> map = new HashMap<String, String>();

        if (this.amount > 0)
        {
            map.put("amount", this.amount + "");
        }

        if (categoty > 0)
        {
            map.put("category", this.categoty + "");
        }

        if (difficulty != null)
        {
            map.put("dificulty", difficulty.toString());
        }

        if (type != null)
        {
            map.put("type", type.toString());
        }

        URL url = new URL(API_BASE_URL + urlEncodeUTF8(map));

        return url;
    }

    private static String urlEncodeUTF8(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (sb.length() == 0)
            {
                sb.append("?");
            }
            if (sb.length() > 1)
            {
                sb.append("&");
            }
            sb.append(String.format("%s=%s", entry.getKey(), entry.getValue().toString()));
        }
        return sb.toString();
    }

}
