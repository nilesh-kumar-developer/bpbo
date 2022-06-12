package us.nilesh.bpbo.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import us.nilesh.bpbo.R;

public class SignUpFragment extends Fragment {
    View view;
//    private static final String TAG = "SignUpFragment";
    private ExtendedFloatingActionButton registerBtn, sigInPgeBtn;
    private EditText emailEditText,passEditText,confirmPassEditText,userEditText;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        registerBtn=view.findViewById(R.id.registerButton);
        sigInPgeBtn=view.findViewById(R.id.signInPageButton);
        emailEditText=view.findViewById(R.id.editTextEmail);
        passEditText=view.findViewById(R.id.editTextPassword);
        confirmPassEditText=view.findViewById(R.id.editTextConfirmPassword);
        userEditText=view.findViewById(R.id.editTextPersonName);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProfile();
            }
        });

        sigInPgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SignUpFragment.this)
                        .navigate(R.id.action_signUpFragment_to_loginFragment);

            }
        });
    }

    void updateUI(FirebaseUser getUser){
        if (getUser==null){
            Snackbar snackbar = Snackbar
                    .make(view, "Failed to register! try using different E-mail.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(view, "User registered, Successfully!", Snackbar.LENGTH_LONG);
            snackbar.show();
            NavHostFragment.findNavController(SignUpFragment.this)
                    .navigate(R.id.action_signUpFragment_to_loginFragment);
        }
    }

    void registerProfile(){
        String email, password, displayName, confirmPass;
        email = emailEditText.getText().toString();
        password = passEditText.getText().toString();
        displayName=userEditText.getText().toString();
        confirmPass=confirmPassEditText.getText().toString();

        if (TextUtils.isEmpty(displayName)) {
            userEditText.setError("Enter your name!");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("E-mail required!");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passEditText.setError("Password required!");
            return;
        }
        if (password.length()<8){
            passEditText.setError("Password should 8 digit long.");
            return;
        }
        if (!password.equals(confirmPass)) {
//            Log.d(TAG, "registerProfile: "+password+"  "+confirmPass);
            confirmPassEditText.setError("Password not same!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Log.d(TAG, "createUserWithEmail:success");
                            userId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            HashMap<String,String> student=new HashMap<>();
                            student.put("email",email);
                            student.put("name",displayName);
                            student.put("pass", password);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("USERS").child(userId).setValue(student);

                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }
}