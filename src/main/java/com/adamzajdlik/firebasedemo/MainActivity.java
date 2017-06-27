package com.adamzajdlik.firebasedemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.adamzajdlik.firebasedemo.adapters.MessageListAdapter;
import com.adamzajdlik.firebasedemo.content.Message;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private ImageButton addAttachmentButton;
    private ImageButton sendButton;
    private ListView listView;
    private ProgressBar progressBar;
    private MessageListAdapter messageListAdapter;
    private static final int RC_SIGN_IN = 1;  // RC = request code

    private String username;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference messagesDatabaseReference;
    private ChildEventListener childEventListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        messagesDatabaseReference = firebaseDatabase.getReference().child(getString(R.string.db_node_messages));

        username = "anonymous";

        addAttachmentButton = (ImageButton) findViewById(R.id.button_add_image);
        listView = (ListView) findViewById(R.id.lv_messages);
        messageListAdapter = new MessageListAdapter(this, R.layout.item_message, new ArrayList<Message>());
        listView.setAdapter(messageListAdapter);
        progressBar = (ProgressBar) findViewById(R.id.pb_main);

        editText = (EditText) findViewById(R.id.et_message);
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });

        sendButton = (ImageButton) findViewById(R.id.button_send_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Message message = new Message(editText.getText().toString(), username, null);
                messagesDatabaseReference.push().setValue(message);
                editText.setText("");
            }
        });

        childEventListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                messageListAdapter.add(newMessage);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        };
        messagesDatabaseReference.addChildEventListener(childEventListener);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    Snackbar.make(null, getString(R.string.snackbar_signed_in), Snackbar.LENGTH_SHORT).show();
                } else {
                    // user is not signed in
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                            .build(), RC_SIGN_IN);
                }
            }
        };
    }

    @Override protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override protected void onPause() {
        firebaseAuth.removeAuthStateListener(authStateListener);
        super.onPause();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_sign_out:

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }

    }
}
