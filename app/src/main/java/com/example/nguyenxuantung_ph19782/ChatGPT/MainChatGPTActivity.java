package com.example.nguyenxuantung_ph19782.ChatGPT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nguyenxuantung_ph19782.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainChatGPTActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText messageEDT;
    ImageButton btnsend;
    List<messageChatGPT> messageChatGPTList;
    MessageChatGPTAdapter messageChatGPTAdapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_gptactivity);
        messageChatGPTList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewChatGPT);
        messageEDT = findViewById(R.id.messageEdittexChatGPT);
        btnsend = findViewById(R.id.btnsendChatGPT);

        messageChatGPTAdapter = new MessageChatGPTAdapter(messageChatGPTList);
        recyclerView.setAdapter(messageChatGPTAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = messageEDT.getText().toString().trim();
                if (!question.isEmpty()) {
                    addToChat(question, messageChatGPT.SENT_BY_ME);
                    callAPI(question);
                    messageEDT.setText("");
                } else {
                    Toast.makeText(MainChatGPTActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageChatGPTList.add(new messageChatGPT(message, sentBy));
                messageChatGPTAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageChatGPTAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        addToChat(response, messageChatGPT.SENT_BY_BOT);
    }

    void callAPI(String question) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "davinci-002");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            addResponse("Failed to create JSON body: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer sk-MWKu4QKmX3yukv9SUGSnHX5t-Z_AwJ8RWqXrt03K8VT3BlbkFJGPhdDzP4kdXLUS05c4Ak-Ot8n03VkcVSX7RaQh3cQA") // Thay YOUR_API_KEY bằng API key của bạn
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        addResponse("Failed to parse response: " + e.getMessage() + "\nResponse body: " + responseBody);
                    }
                } else {
                    String errorBody = response.body().string();
                    Log.d("gpt","failed to load response" + response.message() + "\nError body: "+errorBody);
                    addResponse("Failed to load response: " + response.message() + "\nError body: " + errorBody);
                }
            }
        });
    }

}
