package co.meyasuba.android.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hatenablog.shoma2da.android.timer.R;

import java.util.ArrayList;

public class DialogActivity extends Activity {

    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private RadioButton selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        final int launch = getIntent().getIntExtra("launch", 0);
        final View sendButton = findViewById(R.id.send_button);
        sendButton.setEnabled(false);

        radioButtons.add((RadioButton) findViewById(R.id.value_0));
        radioButtons.add((RadioButton) findViewById(R.id.value_1));
        radioButtons.add((RadioButton) findViewById(R.id.value_2));
        radioButtons.add((RadioButton) findViewById(R.id.value_3));
        radioButtons.add((RadioButton) findViewById(R.id.value_4));
        radioButtons.add((RadioButton) findViewById(R.id.value_5));
        radioButtons.add((RadioButton) findViewById(R.id.value_6));
        radioButtons.add((RadioButton) findViewById(R.id.value_7));
        radioButtons.add((RadioButton) findViewById(R.id.value_8));
        radioButtons.add((RadioButton) findViewById(R.id.value_9));
        radioButtons.add((RadioButton) findViewById(R.id.value_10));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (RadioButton b : radioButtons) {
                    if (b != v) {
                        b.setChecked(false);
                    }
                }
                selected = (RadioButton) v;
                sendButton.setEnabled(true);
            }
        };
        for (RadioButton b : radioButtons) {
            b.setOnClickListener(listener);
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(selected.getTag().toString());
                String comment = ((EditText) findViewById(R.id.comment)).getText().toString();
                Meyasubaco.getInstance(getApplicationContext()).sendNps(value, comment, launch);
                Toast.makeText(DialogActivity.this, R.string.request_thanks, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
