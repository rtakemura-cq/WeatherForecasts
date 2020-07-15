package com.example.weatherforecasts;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WeatherApi {

    private static final String USER_AGENT = "WeatherForecasts Sample";
    private static final String URL = "http://weather.livedoor.com/forecast/webservice/json/v1?city=";

//    WeatherApiにgetWeatherメソッドを追加する。getWeatherメソッドは、引数pointIdの示す天気情報をサーバーから取得する。
//    また、getWeatherメソッドは、静的メソッドなので、WeatherApiクラスをインスタンス化しなくても呼び出すことができる。
    public static String getWeather(Context context, String pointId)
            throws IOException {

        AndroidHttpClient client = AndroidHttpClient.newInstance(USER_AGENT, context);
        HttpGet get = new HttpGet(URL + pointId);

        StringBuilder sb = new StringBuilder();
        try {
            HttpResponse response = client.execute(get);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            client.close();
        }

        return sb.toString();

    }
}
