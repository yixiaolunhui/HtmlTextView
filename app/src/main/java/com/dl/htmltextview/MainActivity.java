package com.dl.htmltextview;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.text);
        String json = "<span style=\"color:0xFFFFFF00;font-size:100px;\"><font color='#D81B60' size='48px'>红色的字</font><strong><font color='#0000CD' size='60px'>加粗蓝色的字</font>默认的字</span>";

        textView.setText(Html.fromHtml(HtmlTagHandler.getHtmlJson(json), null, new HtmlTagHandler()));
    }
}
