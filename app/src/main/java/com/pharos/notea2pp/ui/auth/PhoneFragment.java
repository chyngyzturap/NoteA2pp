package com.pharos.notea2pp.ui.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pharos.notea2pp.Prefs;
import com.pharos.notea2pp.R;

import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {
    private EditText editPhone, addCode, editCode;
    private Button btnNext, btnSign;
    private TextView textRed, timer;
    private String verificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
               return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.editPhone);
        btnNext = view.findViewById(R.id.btnNext);
        editPhone.setVisibility(View.VISIBLE);
        btnSign = view.findViewById(R.id.btnSign);
        btnSign.setVisibility(View.GONE);
        addCode = view.findViewById(R.id.addCode);
        addCode.setVisibility(View.GONE);
        textRed = view.findViewById(R.id.textRed);
        textRed.setVisibility(View.GONE);
        timer = view.findViewById(R.id.timer);
        timer.setVisibility(View.GONE);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSms();
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
                requireActivity().getOnBackPressedDispatcher().addCallback(
                        getViewLifecycleOwner(),
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                requireActivity().finish();
                            }
                        }
                );
            }
        });


        setCallbacks();
    }

    private void confirm() {
        String code = addCode.getText().toString();
        if (code.length() == 6 & TextUtils.isDigitsOnly(code));
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signIn(credential);
    }


    private void setCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG1", "onVerificationCompleted" + phoneAuthCredential.getSmsCode());
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w("TAG2", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                Log.d("TAG3", "onVerificationCompleted");
                editPhone.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                addCode.setVisibility(View.VISIBLE);
                btnSign.setVisibility(View.VISIBLE);
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setVisibility(View.VISIBLE);
                        timer.setText("Осталось: "
                                + millisUntilFinished / 1000);

                    }

                    public void onFinish() {
                        timer.setVisibility(View.GONE);
                        textRed.setVisibility(View.VISIBLE);
                    }

                }.start();

            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance()
                .signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            close();
                        } else {
                            Toast.makeText(requireContext(),
                                    "Error: " + task
                                            .getException()
                                            .getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    private void requestSms() {
        String phoneNumber = editPhone.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void close() {
        NavController navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();

    }
}