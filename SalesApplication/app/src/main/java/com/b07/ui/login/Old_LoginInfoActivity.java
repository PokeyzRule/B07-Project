package com.b07.ui.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Old_LoginInfoActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle intent = getIntent().getExtras();
    String userId;
    String password;

    if (intent.getString("passwordMessage") != null) {
      userId = intent.getString("userMessage");
      password = intent.getString("passwordMessage");
    }

    // start new activity based on login credentials

  }

}
