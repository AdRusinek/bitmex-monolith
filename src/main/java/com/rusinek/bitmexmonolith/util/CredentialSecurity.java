package com.rusinek.bitmexmonolith.util;

import com.rusinek.bitmexmonolith.model.Account;
import org.springframework.stereotype.Component;

/**
 * Created by Adrian Rusinek on 17.05.2020
 **/
@Component
public class CredentialSecurity {

    public Account encodeCredentials(Account account) {
        account.setApiKey(encodeApiKey(account.getApiKey()));
        account.setApiKeySecret(encodeApiKeySecret(account.getApiKeySecret()));
        return account;
    }

    public Account decodeCredentials(Account account) {
        account.setApiKey(decodeApiKey(account.getApiKey()));
        account.setApiKeySecret(decodeApiKeySecret(account.getApiKeySecret()));
        return account;
    }

    private char[] getModifiedApiKey(String apiKeyToModified, boolean isEncoded) {
        char[] charApiKeyArray = apiKeyToModified.toCharArray();
        int ascii;

        ascii = (int) charApiKeyArray[1];
        charApiKeyArray[1] = (char) (isEncoded ? ascii + 2 : ascii - 2);
        ascii = (int) charApiKeyArray[4];
        charApiKeyArray[4] = (char) (isEncoded ? ascii - 1 : ascii + 1);
        ascii = (int) charApiKeyArray[13];
        charApiKeyArray[13] = (char) (isEncoded ? ascii + 1 : ascii - 1);
        ascii = (int) charApiKeyArray[18];
        charApiKeyArray[18] = (char) (isEncoded ? ascii - 2 : ascii + 2);
        ascii = (int) charApiKeyArray[23];
        charApiKeyArray[23] = (char) (isEncoded ? ascii - 3 : ascii + 3);

        return charApiKeyArray;
    }


    private String encodeApiKey(String apiKeyToEncode) {
        StringBuilder buildFalseApiKey = new StringBuilder(String.valueOf(getModifiedApiKey(apiKeyToEncode, false)));

        buildFalseApiKey
                .insert(3, "b")
                .insert(5, "t")
                .insert(9, "H")
                .insert(13, "G")
                .insert(16, "l")
                .insert(19, "a")
                .insert(22, "m")
                .insert(24, "P");


        return String.valueOf(buildFalseApiKey);
    }

    private String decodeApiKey(String apiKeyToDecode) {
        StringBuilder falseApiKey = new StringBuilder(apiKeyToDecode);

        falseApiKey
                .deleteCharAt(24)
                .deleteCharAt(22)
                .deleteCharAt(19)
                .deleteCharAt(16)
                .deleteCharAt(13)
                .deleteCharAt(9)
                .deleteCharAt(5)
                .deleteCharAt(3);

        return String.valueOf(getModifiedApiKey(String.valueOf(falseApiKey), true));
    }

    private String encodeApiKeySecret(String apiKeySecretToEncode) {
        StringBuilder falseApiKeySecret = new StringBuilder(apiKeySecretToEncode);

        falseApiKeySecret
                .insert(6, "r")
                .insert(17, "p")
                .insert(25, "Q")
                .insert(37, "M");

        return String.valueOf(falseApiKeySecret);
    }

    private String decodeApiKeySecret(String apiKeySecretToDecode) {
        StringBuilder falseApiKeySecret = new StringBuilder(apiKeySecretToDecode);

        falseApiKeySecret
                .deleteCharAt(37)
                .deleteCharAt(25)
                .deleteCharAt(17)
                .deleteCharAt(6);

        return String.valueOf(falseApiKeySecret);
    }
}
