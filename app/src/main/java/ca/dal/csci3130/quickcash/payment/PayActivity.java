package ca.dal.csci3130.quickcash.payment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import ca.dal.csci3130.quickcash.BuildConfig;
import ca.dal.csci3130.quickcash.R;
import ca.dal.csci3130.quickcash.joblisting.ViewJobActivity;

public class PayActivity extends AppCompatActivity {

    private boolean isPaid;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPConfig;
    EditText enterAmount;
    Button makePaymentBtn;
    Button backBtn;
    TextView paymentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_employee);
        init();
        configPP();
        initActivityLauncher();
        setListeners();

    }

    private void init() {
        enterAmount = findViewById(R.id.enterAmount);
        makePaymentBtn = findViewById(R.id.makePaymentBtn);
        paymentTV = findViewById(R.id.idTVStatus);
        backBtn = findViewById(R.id.backBtn);
    }

    private void configPP() {
        payPConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(BuildConfig.PAYPAL_CLIENT_ID);
    }

    private void setListeners() {
        makePaymentBtn.setOnClickListener(v ->
                processPayment());

        backBtn.setOnClickListener(view ->
                redirectViewJobs());
    }

    private void processPayment() {
        final String amount = enterAmount.getText().toString();

        if (validAmount(Integer.parseInt(amount))) {
            final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount)
                    , "CAD", "Compensate Employee", PayPalPayment.PAYMENT_INTENT_SALE);
            final Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPConfig);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            activityResultLauncher.launch(intent);
        } else {
            Toast.makeText(PayActivity.this, "Invalid amount, try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void initActivityLauncher() {
        isPaid = false;

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {

                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        paymentTV.setText("Payment " + state + "\n  Your payment id is " + payID);
                        Toast.makeText(PayActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                        isPaid = true;
                    } catch (JSONException e) {

                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }

            } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.d(TAG, "Launcher Result Invalid");
            } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Launcher Result Cancelled");
            }
        });
    }

    private void redirectViewJobs() {
        Intent viewJobIntent = new Intent(this, ViewJobActivity.class);
        viewJobIntent.putExtra("PAYMENT_STATUS", isPaid);
        this.startActivity(viewJobIntent);
    }
    private boolean validAmount(int amount) {
        return amount > 13.35 && (amount <= (getIntent().getIntExtra("JOBSALARY", 0)
                * getIntent().getIntExtra("JOBDURATION", 0)));
    }
}
