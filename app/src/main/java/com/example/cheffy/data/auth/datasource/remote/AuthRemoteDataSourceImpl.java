package com.example.cheffy.data.auth.datasource.remote;

import com.example.cheffy.data.auth.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

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
    public Single<User> login(String email, String password) {
        return Single.create(emitter -> 
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    User user = mapFirebaseUserToUser(firebaseUser);
                    if (user != null) {
                        emitter.onSuccess(user);
                    } else {
                        emitter.onError(new RuntimeException("Login failed: User is null"));
                    }
                })
                .addOnFailureListener(emitter::onError)
        );
    }

    @Override
    public Single<User> loginWithGoogle(String idToken) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    User user = mapFirebaseUserToUser(firebaseUser);
                    if (user != null) {
                        emitter.onSuccess(user);
                    } else {
                        emitter.onError(new RuntimeException("Google Sign-In failed: User is null"));
                    }
                })
                .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Single<User> register(String email, String password, String displayName) {
        return Single.create(emitter -> 
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();

                            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    emitter.onSuccess(new User(displayName, email));
                                } else {
                                    emitter.onError(new RuntimeException("User registered, but failed to save name."));
                                }
                            });
                        } else {
                            emitter.onError(new RuntimeException("Registration failed: User is null"));
                        }
                    } else {
                        String errorMsg = task.getException() != null 
                            ? task.getException().getMessage() 
                            : "Registration failed";
                        emitter.onError(new RuntimeException(errorMsg));
                    }
                })
        );
    }

    @Override
    public Completable sendPasswordResetEmail(String email) {
        return Completable.create(emitter -> 
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        emitter.onComplete();
                    } else {
                        String errorMsg = task.getException() != null 
                            ? task.getException().getMessage() 
                            : "Failed to send reset email";
                        emitter.onError(new RuntimeException(errorMsg));
                    }
                })
        );
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
