package ir.zelzele.general.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import ir.zelzele.R;

public class RefrencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrences);
        getSupportActionBar().setTitle(getString(R.string.refs));

    }
}
