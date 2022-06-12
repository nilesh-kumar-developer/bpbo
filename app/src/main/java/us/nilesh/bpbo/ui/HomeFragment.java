package us.nilesh.bpbo.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import us.nilesh.bpbo.R;
import us.nilesh.bpbo.adapter.HomeAdapter;
import us.nilesh.bpbo.interfaces.EnquiryInterface;

public class HomeFragment extends Fragment implements EnquiryInterface {

    View view;

    private static final String TAG = "HomeFragment";
    private FirebaseAuth mAuth;
    private TextView email,name;
    private RecyclerView homeList;
    private HomeAdapter homeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        name=view.findViewById(R.id.nameTextV);
        email=view.findViewById(R.id.emailTextV);
        homeList =view.findViewById(R.id.homeListRv);

        userDetails();
        listLoader();

        return view;
    }

    private void userDetails(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("USERS").child(currentUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                name.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void listLoader(){
        ArrayList<String> Name = new ArrayList<>();
        ArrayList<String> Organ = new ArrayList<>();
        Name.add("Horse");
        Name.add("Cow");
        Organ.add("Camel");
        Organ.add("Sheep");
        homeList.setLayoutManager(new LinearLayoutManager(getContext()));
        homeAdapter =new HomeAdapter(getContext(),Name,Organ,this);
        homeList.setAdapter(homeAdapter);
    }

    @Override
    public void onClickEnquiry(String LINK) {
        Log.d(TAG, "onClickEnquiry: ");
    }
}