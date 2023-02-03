package com.CodeJokeR.ChatGPT;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.IOException;

public class OpenAiClient {
	private static final MediaType JSON = MediaType.parse("application/json");
	private OkHttpClient client;
	private String apiKey = "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";
	
	public OpenAiClient() {
		this.client = new OkHttpClient();
	}
	
	public void generateResponse(String prompt, Callback callback) {
		
		JSONObject json = new JSONObject();
		try{
			json.put("prompt", prompt);
			json.put("max_tokens", 200);
			json.put("temperature", 0.9);
			// json.put("model", "text-davinci-003");
		}catch(Exception e){}
		
		Request request = new Request.Builder()
		.url("https://api.openai.com/v1/engines/text-davinci-003/completions")
		.header("Authorization", "Bearer " + apiKey)
		.header("Content-Type", "application/json")
		.post(RequestBody.create(JSON, json.toString()))
		.build();
		
		client.newCall(request).enqueue(callback);
	}
	
	public static String parseResponse(String responseBody){
		String text = responseBody;
		try {
			text = ((new JSONObject(responseBody)).getJSONArray("choices")).getJSONObject(0).getString("text");
		} catch (Exception e) {}
		
		//replaceAll is to cut some unnecessary longs
		return text.replaceAll("(?m)^[ \t]*\r?\n","");
		
	}
	
}
