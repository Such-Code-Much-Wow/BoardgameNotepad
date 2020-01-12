package com.github.jan222ik.boardgamenotepad.server;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

    private Context context;

    public Helper(Context context) {
        this.context = context;
    }

    public String LoadText(int resourceId) {
        // The InputStream opens the resourceId and sends it to the buffer
        InputStream is = context.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        String total = "";
        try {
            // While the BufferedReader readLine is not null
            while ((readLine = br.readLine()) != null) {
               total += readLine;
            }

            // Close the InputStream and BufferedReader
            is.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    public byte[] LoadBytes(int resourceId) {
        // The InputStream opens the resourceId and sends it to the buffer
        InputStream is = context.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        byte[] buffer = new byte[0];
        try {
            buffer = new byte[is.available()];
            is.read(buffer);
            // Close the InputStream and BufferedReader
            is.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
