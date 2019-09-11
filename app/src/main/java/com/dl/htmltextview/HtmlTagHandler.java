package com.dl.htmltextview;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A
 * B
 * @author zwl
 * @describe 自定义html tag解析器
 * @date on 2019-08-26
 */
public class HtmlTagHandler implements Html.TagHandler {

    private static final String TAG_CUSTOM_FONT = "tag_custom_font";
    private static final String TAG_FONT = "font";

    private int startIndex = 0;
    private int stopIndex = 0;
    final HashMap<String, String> attributes = new HashMap<String, String>();


    public static String getHtmlJson(String htmlJson) {
        return htmlJson.replaceAll(TAG_FONT, TAG_CUSTOM_FONT);
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        processAttributes(xmlReader);
        for (int j = 0; j < attributes.size(); j++) {
            if (tag.equalsIgnoreCase(TAG_CUSTOM_FONT)) {
                if (opening) {
                    startFont(tag, output, xmlReader);
                } else {
                    endFont(tag, output, xmlReader);
                }
            }

        }
    }


    public void startFont(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
    }


    public void endFont(String tag, Editable output, XMLReader xmlReader) {
        stopIndex = output.length();
        String color = attributes.get("color");
        String size = attributes.get("size");
        size = size.split("px")[0];

        if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(size)) {
            output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            output.setSpan(new AbsoluteSizeSpan(Integer.parseInt(size)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            output.setSpan(new ForegroundColorSpan(0xff000000), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private void processAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[]) dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer) lengthField.get(atts);

            for (int i = 0; i < len; i++) {
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
