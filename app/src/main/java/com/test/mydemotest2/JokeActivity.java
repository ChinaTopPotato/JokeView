package com.test.mydemotest2;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class JokeActivity extends BaseActivity {
    private TextView jokeContent;
    private Toolbar jokeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        jokeContent = (TextView)findViewById(R.id.joke_content);
        jokeTitle = (Toolbar) findViewById(R.id.joke_title);

        Intent intent = getIntent();
        Joke joke = (Joke)intent.getSerializableExtra("joke");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jokeContent.setText(Html.fromHtml(joke.getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            jokeContent.setText(Html.fromHtml(joke.getContent()));
        }

        jokeTitle.setTitle(joke.getTitle());
    }
}
