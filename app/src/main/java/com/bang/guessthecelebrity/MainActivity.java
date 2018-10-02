package com.bang.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebUrl = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int chosenCeleb = 0;
    Bitmap celebImage;

    int location = 0;
    String[] answers = new String[4];

    ImageView imageView;

    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public void celebChosen(View view) {
        if (view.getTag().toString().equals(Integer.toString(location))) {

            Toast.makeText(getApplicationContext(), "correct folks ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "its wrong man, its " + celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();
        }

        createNewQuestion();// it will create new question

    }


    public class imageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream IS = connection.getInputStream();
                connection.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(IS);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
            // return "failed image ";// we can not do because its a string and return type is Bitmap
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... str) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(str[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream IN = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(IN);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "failed folks";
            }


            // return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView2);
        button0 = findViewById(R.id.button);
        button1 = findViewById(R.id.button2);
        button2 = findViewById(R.id.button3);
        button3 = findViewById(R.id.button4);

        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            result = task.execute("http://www.posh24.se/kandisar").get();

            // splitting the string means now we are filtering the required parts
            String[] splitString = result.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitString[0]);

            while (m.find()) {
                celebUrl.add(m.group(1));

                // System.out.println(m.group(1));// display the image url
            }
            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitString[0]);

            while (m.find()) {
                celebNames.add(m.group(1));

                //System.out.println(m.group(1));//display the celebrity names
            }

            // Now creating random image to display
            Random random = new Random();
            chosenCeleb = random.nextInt(celebUrl.size());
            //random.nextInt() gives the random number between 0 and 1 less than size given by celebUrl.size()

            imageDownloader imageTask = new imageDownloader();


            celebImage = imageTask.execute(celebUrl.get(chosenCeleb)).get();

            imageView.setImageBitmap(celebImage);

            location = random.nextInt(4);

            int incorrectAnswer;

            for (int i = 0; i < 4; i++) {
                if (i == location) {
                    answers[i] = celebNames.get(chosenCeleb);
                } else {
                    incorrectAnswer = random.nextInt(celebUrl.size());

                    while (incorrectAnswer == chosenCeleb) {
                        incorrectAnswer = random.nextInt(celebUrl.size());
                    }

                    answers[i] = celebNames.get(incorrectAnswer);
                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);

            //Log.i("content of Url",result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewQuestion() {
        // Now creating random image to display
        Random random = new Random();
        chosenCeleb = random.nextInt(celebUrl.size());
        //random.nextInt() gives the random number between 0 and 1 less than size given by celebUrl.size()

        imageDownloader imageTask = new imageDownloader();


        try {
            celebImage = imageTask.execute(celebUrl.get(chosenCeleb)).get();


            imageView.setImageBitmap(celebImage);
            location = random.nextInt(4);

            int incorrectAnswer;

            for (int i = 0; i < 4; i++) {
                if (i == location) {
                    answers[i] = celebNames.get(chosenCeleb);
                } else {
                    incorrectAnswer = random.nextInt(celebUrl.size());

                    while (incorrectAnswer == chosenCeleb) {
                        incorrectAnswer = random.nextInt(celebUrl.size());
                    }

                    answers[i] = celebNames.get(incorrectAnswer);
                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);

        } catch (Exception e) {

        }

        imageView.setImageBitmap(celebImage);
        location = random.nextInt(4);

        int incorrectAnswer;

        for (int i = 0; i < 4; i++) {
            if (i == location) {
                answers[i] = celebNames.get(chosenCeleb);
            } else {
                incorrectAnswer = random.nextInt(celebUrl.size());

                while (incorrectAnswer == chosenCeleb) {
                    incorrectAnswer = random.nextInt(celebUrl.size());
                }

                answers[i] = celebNames.get(incorrectAnswer);
            }
        }

        button0.setText(answers[0]);
        button1.setText(answers[1]);
        button2.setText(answers[2]);
        button3.setText(answers[3]);
    }
}
