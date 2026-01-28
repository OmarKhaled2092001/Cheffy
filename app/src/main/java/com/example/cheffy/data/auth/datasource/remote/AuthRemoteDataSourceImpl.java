package com.example.cheffy.data.auth.datasource.remote;

import com.example.cheffy.data.auth.models.User;
import com.example.cheffy.data.auth.repository.AuthResultCallback;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthRemoteDataSourceImpl implements IAuthRemoteDataSource {

    private final FirebaseAuth firebaseAuth;
    private static AuthRemoteDataSourceImpl instance;

    private AuthRemoteDataSourceImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized AuthRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new AuthRemoteDataSourceImpl();
        }
        return instance;
    }

    @Override
    public void login(String email, String password, AuthResultCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    User user = mapFirebaseUserToUser(firebaseUser);
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage());
                });
    }

    @Override
    public void loginWithGoogle(String idToken, AuthResultCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    User user = mapFirebaseUserToUser(firebaseUser);
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage());
                });
    }

    @Override
    public void register(String email, String password, String displayName, AuthResultCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();

                            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    callback.onSuccess(new User(displayName, email));
                                } else {
                                    callback.onError("User registered, but failed to save name.");
                                }
                            });
                        } else {
                            callback.onError("Registration failed: User is null");
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                        callback.onError(errorMsg);
                    }
                });
    }

    @Override
    public void sendPasswordResetEmail(String email, AuthResultCallback callback) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Failed to send reset email";
                        callback.onError(errorMsg);
                    }
                });
    }

    @Override
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        return mapFirebaseUserToUser(firebaseUser);
    }

    private User mapFirebaseUserToUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return null;
        return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
    }
}
