package com.example.cheffy.data.auth.social;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.example.cheffy.R;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executors;

public class GoogleAuthHelper {

    private final Context context;
    private final CredentialManager credentialManager;
    private final SocialAuthCallback callback;

    public GoogleAuthHelper(Context context, SocialAuthCallback callback) {
        this.context = context;
        this.callback = callback;
        this.credentialManager = CredentialManager.create(context);
    }

    public void launchSignIn() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(true)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                context,
                request,
                null,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignInResult(result);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        if (!e.getClass().getName().contains("CancellationException")) {
                            callback.onError("Google Sign-In failed: " + e.getMessage());
                        }
                    }
                }
        );
    }

    private void handleSignInResult(GetCredentialResponse result) {
        Credential credential = result.getCredential();
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (customCredential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                try {
                    GoogleIdTokenCredential googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(customCredential.getData());

                    callback.onSuccess(googleIdTokenCredential.getIdToken());
                } catch (Exception e) {
                    callback.onError("Invalid Google credentials");
                }
            } else {
                callback.onError("Unexpected credential type");
            }
        } else {
            callback.onError("Unexpected credential type");
        }
    }
}
