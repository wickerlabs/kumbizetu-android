package or.buni.bukit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import or.buni.bukit.util.Constants;
import or.buni.ventz.R;

public class PaymentActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();

        String bid = intent.getStringExtra("bid");
        String uid = Constants.DEFAULT_UID;
        String postData = "";

        try {
            postData = "uid=" + URLEncoder.encode(uid, "UTF-8") + "&bookingId=" + URLEncoder.encode(bid, "UTF-8");
            webView = (WebView) findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient());

            webView.postUrl(Constants.PAYMENT, postData.getBytes());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
