package br.com.voxali.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private static final String URL_SYNC = "https://voxali.com.br/api/sync";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView status;

    private final Runnable sync = new Runnable() {
        @Override public void run() {
            new Thread(() -> {
                boolean ok = checkServer();
                runOnUiThread(() -> status.setText(ok
                    ? "Voxali\n\nSincronizado ✓\n\nAtualização: 5 segundos"
                    : "Voxali\n\nServidor aguardando...\n\nNova tentativa em 5 segundos"));
            }).start();
            handler.postDelayed(this, 5000);
        }
    };

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root = new LinearLayout(this);
        root.setGravity(Gravity.CENTER);
        root.setPadding(40,40,40,40);
        root.setBackgroundColor(Color.rgb(11,11,18));
        status = new TextView(this);
        status.setTextColor(Color.WHITE);
        status.setTextSize(20);
        status.setGravity(Gravity.CENTER);
        status.setText("Voxali\n\nConectando...");
        root.addView(status, new LinearLayout.LayoutParams(-1,-2));
        setContentView(root);
        handler.post(sync);
    }

    private boolean checkServer() {
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) new URL(URL_SYNC).openConnection();
            c.setRequestMethod("GET");
            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);
            return c.getResponseCode() >= 200 && c.getResponseCode() < 300;
        } catch (Exception e) { return false; }
        finally { if (c != null) c.disconnect(); }
    }

    @Override protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
