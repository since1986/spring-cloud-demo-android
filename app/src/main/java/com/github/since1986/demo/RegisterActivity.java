package com.github.since1986.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageViewClose;
    private TextView textViewLogin;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPhone;
    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPhone;

    private void doRegister(final String username, final String password, String email, String phone) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageViewClose = findViewById(R.id.image_view_close_activity_register);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewLogin = findViewById(R.id.text_view_login_activity_register);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(RegisterActivity.this, LoginActivity.class)
                );
            }
        });

        textInputLayoutUsername = findViewById(R.id.text_input_layout_username_activity_register);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password_activity_register);
        textInputLayoutEmail = findViewById(R.id.text_input_layout_email_activity_register);
        textInputLayoutPhone = findViewById(R.id.text_input_layout_phone_activity_register);

        textInputEditTextUsername = findViewById(R.id.text_input_edit_text_username_activity_register);
        textInputEditTextPassword = findViewById(R.id.text_input_edit_text_password_activity_register);
        textInputEditTextEmail = findViewById(R.id.text_input_edit_text_email_activity_register);
        textInputEditTextPhone = findViewById(R.id.text_input_edit_text_phone_activity_register);

        Button buttonRegister = findViewById(R.id.button_register_activity_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = textInputEditTextUsername.getText().toString();
                final String password = textInputEditTextPassword.getText().toString();
                String email = textInputEditTextEmail.getText().toString();
                String phone = textInputEditTextPhone.getText().toString();

                if (username.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    textInputLayoutUsername.setErrorEnabled(false);
                    textInputLayoutPassword.setErrorEnabled(false);
                    textInputLayoutEmail.setErrorEnabled(false);
                    textInputLayoutPhone.setErrorEnabled(false);
                    doRegister(username, password, email, phone);
                }
            }
        });
    }
}
