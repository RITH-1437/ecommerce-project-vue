package com.demo.backend.service.impl;

import com.demo.backend.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.api.key:sk-placeholder-key-for-development}")
    private String apiKey;

    // Increased timeout
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    public String ask(String prompt) {
        try {
            JSONObject json = new JSONObject()
                    .put("model", "gpt-4o-mini")
                    .put("messages", new JSONArray()
                            .put(new JSONObject()
                                    .put("role", "user")
                                    .put("content", prompt)));

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return "OpenAI API error: " + response.code() + " - " + response.message();
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) return "Empty response from OpenAI";

            String result = responseBody.string();
            JSONObject res = new JSONObject(result);

            if (res.has("error")) {
                return "OpenAI error: " + res.getJSONObject("error").getString("message");
            }

            return res.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

        } catch (Exception e) {
            return "Error calling AI service: " + e.getMessage();
        }
    }
}
