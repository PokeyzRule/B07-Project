package com.b07.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.b07.salesapplication.R;
import com.b07.services.AdminInterface;
import java.util.ArrayList;
import java.util.List;

public class AdminViewAcctsActivity extends AppCompatActivity {

  private TextView txtLayoutTitle;
  private EditText inputCustId;
  private Button btnEnter;

  @Override
  public void onCreate(Bundle savedBundleInstance) {
    super.onCreate(savedBundleInstance);
    setContentView(R.layout.emp_new_user_layout);

    txtLayoutTitle = findViewById(R.id.newAccountTitle);
    inputCustId = findViewById(R.id.customerId);
    btnEnter = findViewById(R.id.enterButton);
    setText();
    setupListeners();
  }

  private void setText() {
    txtLayoutTitle.setText(R.string.viewAcctsTitle);
  }

  private void setupListeners() {
    btnEnter.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String custId = inputCustId.getText().toString();
        if (!custId.isEmpty()) {
          AdminInterface adminInterface = new AdminInterface(getApplicationContext());
          List<Integer> activeAccts = adminInterface.getActiveAccounts(Integer.parseInt(custId));
          List<Integer> inactiveAccts = adminInterface
              .getInactiveAccounts(Integer.parseInt(custId));

          Intent intent = new Intent(AdminViewAcctsActivity.this, AdminListAcctsActivity.class);
          intent.putExtra("activeAccounts", (ArrayList<Integer>) activeAccts);
          intent.putExtra("inactiveAccounts", (ArrayList<Integer>) inactiveAccts);
          intent.putExtra("custId", custId);
          startActivity(intent);
        }
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if(id == android.R.id.home){
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
