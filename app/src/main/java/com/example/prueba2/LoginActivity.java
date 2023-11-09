package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prueba2.ui.slideshow.SlideshowFragment;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    //request code Google
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    SignInButton signGoogle;
    private EditText uname, passwd;

    private Empleado[] employees = new Empleado[5];
    private Cliente[] mesas = new Cliente[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //herramientas Google.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        signGoogle = findViewById(R.id.btnGoogle);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //inicio de sesion con cuenta de bar
        uname = (EditText) findViewById(R.id.txt_user);
        passwd = (EditText) findViewById(R.id.txt_password);
        employees[0] = new Empleado("admin", "123", "Empleado", true);
        employees[1] = new Empleado("victor", "123", "Empleado", true);
        employees[2] = new Empleado("carlos", "123", "Empleado", true);
        employees[3] = new Empleado("diego", "123", "Empleado", true);
        employees[4] = SlideshowFragment.empleado; // TODO: solo un emplado por ahora
        for (int i=0; i<8; ++i) {
            mesas[i] = new Cliente("mesa" + i, "mesa", "Cliente", true);
        }
    }

    //metodos Google
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {
        user = mAuth.getCurrentUser();
        if (user != null){
            irHome();
        }
    }

    private void irHome() {
        Intent intent = new Intent(LoginActivity.this, MenuCliente.class);
        startActivity(intent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "ingreso con google rechazado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // [END onactivityresult]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            updateUI(null);
                        }
                    }
                });
    }

    //END

    public void login(View view) {
        String usr = uname.getText().toString();
        String pass = passwd.getText().toString();
        boolean success = false;
        for (int i=0; i<employees.length; ++i) {
            if (usr.equals(employees[i].getUsername()) && pass.equals(employees[i].getPassword())) {
                saveUser(employees[i]);
                Intent empl_intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(empl_intent);
                finish();
                success = true;
                break;
            } else if (usr.equals(mesas[i].getUsername()) && pass.equals(mesas[i].getPassword())) {
                // Cliente cliente = new Cliente(usr, pass, "Cliente", true);
                saveUser(mesas[i]);
                Intent usr_intent = new Intent(LoginActivity.this, MenuCliente.class);
                startActivity(usr_intent);
                finish();
                success = true;
                break;
            }
        }
        if (!success) {
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUser(Usuario usr) {
        SharedPreferences preferences;
        if (usr.getTipo().equals("Empleado")) {
            preferences = getSharedPreferences("employee.dat", MODE_PRIVATE);
        } else {
            preferences = getSharedPreferences("user.dat", MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", usr.getUsername());
        editor.putString("password", usr.getPassword());
        editor.putString("tipo", usr.getTipo());
        editor.putBoolean("exists", usr.isExists());
        editor.apply();
    }

    public void irRegistro(View view){
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

}