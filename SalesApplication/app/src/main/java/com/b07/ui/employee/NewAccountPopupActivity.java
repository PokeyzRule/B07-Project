package com.b07.ui.employee;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.salesapplication.R;

public class NewAccountPopupActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_account_popup);

    Bundle intent = getIntent().getExtras();
    int id = intent.getInt("customerId");

    boolean valid = true; // change this

    TextView userText = findViewById(R.id.userText);
    if (valid) {
      int userId = 5; // change this
      userText.setText("New user ID is " + userId);
    } else {
      userText.setText("Invalid customer ID; please try again.");
    }

  }
}
