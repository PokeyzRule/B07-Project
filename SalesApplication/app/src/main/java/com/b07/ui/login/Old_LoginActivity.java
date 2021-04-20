package com.b07.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.salesapplication.R;

public class Old_LoginActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_main);

    final EditText userEditText = findViewById(R.id.idUserText);

    final EditText passwordEditText = findViewById(R.id.idPasswordText);

    Button enterButton = findViewById(R.id.enterButton);
    enterButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Old_LoginActivity.this, Old_LoginInfoActivity.class);

        EditText userEditText = findViewById(R.id.idUserText);
        String userId;
        userId = userEditText.getText().toString();
        intent.putExtra("userMessage", userId);

        EditText passwordEditText = findViewById(R.id.idPasswordText);
        String password;
        password = passwordEditText.getText().toString();
        intent.putExtra("passwordMessage", password);
        startActivity(intent);
      }
    });
  }

}





