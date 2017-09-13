package or.buni.bukit;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import or.buni.bukit.interfaces.PaymentListener;
import or.buni.bukit.networking.Backend;
import or.buni.bukit.util.Constants;
import or.buni.ventz.R;

public class PaymentActivity extends BaseActivity {

    String message = "";
    String pTitle = "";
    String postData;
    private WebView webView;
    private ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        Intent intent = getIntent();

        String bid = intent.getStringExtra("bid");
        String vid = intent.getStringExtra("vid");

        Log.d("VenueID", vid);
        String uid = Constants.DEFAULT_UID;
        postData = Constants.PAYMENT + "?u=" + uid + "&v=" + vid + "&b=" + bid;

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        webView = (WebView) findViewById(R.id.webView);

        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("tracking_id") && url.contains("merchant_reference")) {
                    String urls[] = url.split("\\?");

                    String other = urls[1];

                    String[] parameters = other.split("&");

                    String merchantRef = parameters[1].split("=")[1];

                    progressBar.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);

                    Backend.getInstance().checkPayment(merchantRef, new PaymentListener() {
                        @Override
                        public void onCheckComplete(String status, Exception e) {

                            if (e == null) {

                                if (status.equals(Constants.COMPLETE)) {
                                    pTitle = "Payment complete";
                                    message = "Your payment was successful. " +
                                            "Our team will be in touch with you soon";
                                }

                                if (status.equals(Constants.PENDING)) {
                                    pTitle = "Processing";
                                    message = "Your payment is now being processed. Our team will contact you when its received.";
                                }

                                if (status.equals(Constants.FAILED) || status.equals(Constants.INVALID)) {
                                    pTitle = "Payment failed";
                                    message = "There was a problem with your payment. " +
                                            "If this is wrong, contact us bukitapp@gmail.com";
                                }

                                Log.d("PaymentStatus", status);


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        new AlertDialog.Builder(PaymentActivity.this)
                                                .setMessage(message)
                                                .setTitle(pTitle)
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        PaymentActivity.this.finish();
                                                    }
                                                }).show();
                                    }
                                });
                            } else {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        new AlertDialog.Builder(PaymentActivity.this)
                                                .setMessage(message)
                                                .setTitle(pTitle)
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                        PaymentActivity.this.finish();
                                                    }
                                                }).show();
                                    }
                                });
                            }
                        }
                    });

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);
                }

            }
        };

        webView.setWebViewClient(client);
        webView.canGoBackOrForward(5);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(postData);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payments, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_refresh) {
            //getSupportActionBar().hide();
            webView.loadUrl(webView.getUrl());
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
