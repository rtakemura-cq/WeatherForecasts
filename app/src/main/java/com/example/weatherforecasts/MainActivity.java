package com.example.weatherforecasts;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Handler handler; // Handlerをクラスのフィールドに追加する

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        setContentView(R.layout.activity_main);

        // activity_main.xmlで設定した、id:tv_mainを使って、TextViewのオブジェクトを取得する
        textView = (TextView) findViewById(R.id.tv_main);

        // tryブロック：「例外が発生するかもしれない処理」を記述
        // catchブロック：「例外を捕まえたときの処理」を記述
//        try {
//            // WeatherApiのgetWeatherメソッドで天気情報を取得して、結果をTextViewに表示する
//            String data = WeatherApi.getWeather(this, "400040");
//            textView.setText(data);
//        } catch (IOException e) { // 例外クラス名：通信やデータ保存領域への入出力処理が失敗した時に発生、e:変数名
//            // Toastクラス：短時間だけメッセージを画面に表示する機能
//            // makeText()メソッド：任意のテキストを保持するToastオブジェクトを生成し、show()メソッドで表示する
//            // 定数Toast.LENGTH_SHORT：表示時間は短時間
//            Toast.makeText(this, "IOException is occurred", Toast.LENGTH_SHORT).show();
//        }

        // Androidには、ネットワーク通信はメインスレッドから実行してはいけないルールがあるため、下記にて変更
        // ネットワーク通信を、threadのrunメソッドの中で実行する
        // Threadインスタンスを生成
        Thread thread = new Thread() {
            @Override
            // ThreadクラスはRunnableインターフェースを実装したクラス
            // そのスレッドを開始すると、独立して実行されるスレッド内で、オブジェクトの run メソッドが呼び出される
            public void run() {
                try {
                    // WeatherApiのgetWeatherメソッドで天気情報を取得して、結果をTextViewに表示する
                    final String data = WeatherApi.getWeather(MainActivity.this, "400040");

                    // handlerのpostメソッドに、実行する処理を含めたRunnableオブジェクトを渡す。
                    // ネットワーク接続の戻り値であるString dataと、例外のIOException eにfinalがついているのは、Runnableの中からアクセスするため。
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(data);
                        }
                    });
                } catch (final IOException e) { // 例外クラス名：通信やデータ保存領域への入出力処理が失敗した時に発生、e:変数名
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Toastクラス：短時間だけメッセージを画面に表示する機能
                            // makeText()メソッド：任意のテキストを保持するToastオブジェクトを生成し、show()メソッドで表示する
                            // 定数Toast.LENGTH_SHORT：表示時間は短時間
                            // e.getMessage()：メッセージを短くすることができる
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        thread.start();
    }
}
