package com.Schoolio.api;

import com.Schoolio.helpers.BearerToken;
import com.Schoolio.helpers.ConfigFile;
import com.Schoolio.helpers.GsonMultipleTimeFormats;
import com.Schoolio.helpers.TokenHandler;
import com.Schoolio.objects.BearerObject;
import com.Schoolio.objects.LessonScheduleObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class LessonScheduleApi {
    private final String apiUrl = new ConfigFile().config().getProperty("API_URL");
    private final BearerObject bearer;
    private final HashMap<String,HashMap<String, String>> idTokenObj;

    public LessonScheduleApi() {
        Optional<String> bearerRaw = new BearerToken().getBearerToken();
        bearer = bearerRaw.map(BearerObject::new).orElseGet(BearerObject::new);

        idTokenObj = new TokenHandler().decodeJWT(bearer.getIdToken());
    }

    public LessonScheduleObject[] index() {
        HttpClient client = HttpClientBuilder.create().build();

        String sub = idTokenObj.get("payload").get("sub");
        HttpGet request = new HttpGet(apiUrl + "/secured/lesson-schedule/get/" + sub);
        request.addHeader("Authorization", bearer.getAccessToken());

        HttpResponse response;
        try {
            response = client.execute(request);
            String responseContent = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Content empty/ failed to get. " + responseContent);
            }
            return processTimetableData(responseContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LessonScheduleObject[] processTimetableData(String data) {
        Type type = new TypeToken<LessonScheduleObject[]>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Time.class, new GsonMultipleTimeFormats.TimeDeserializer())
                .registerTypeAdapter(Timestamp.class, new GsonMultipleTimeFormats.TimestampDeserializer())
                .create();
        return gson.fromJson(data, type);

    }
}
