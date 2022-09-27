package org.bat2.vacationworker.service;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import lombok.Getter;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

public final class SecretService {
    private static final Logger logger = Logger.getLogger(SecretService.class.getName());

    private static SecretService INSTANCE;
    private final byte[] apiKeyBytes;
    private final String trelloToken;


    // Access the payload for the given secret version if one exists. The version
    // can be a version number as a string (e.g. "5") or an alias (e.g. "latest").
    private SecretService() {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            String projectId = "vacation-worker-project";
            String versionId = "latest";
            String apiKeySecretId = "api-key";
            apiKeyBytes = initBytesValue(projectId, apiKeySecretId, versionId, client);

            String trelloTokenSecretId = "trello-token";
            trelloToken = initStringValue(projectId, trelloTokenSecretId, versionId, client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static SecretService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SecretService();
        }

        return INSTANCE;
    }

    private byte[] initBytesValue(String projectId, String apiKeySecretId, String versionId, SecretManagerServiceClient client) {
        byte[] result;
        SecretVersionName apiKeySecretVersionName = SecretVersionName.of(projectId, apiKeySecretId, versionId);

        // Access the secret version.
        AccessSecretVersionResponse response = client.accessSecretVersion(apiKeySecretVersionName);

        // Verify checksum. The used library is available in Java 9+.
        result = response.getPayload().getData().toByteArray();
        Checksum checksum = new CRC32C();
        checksum.update(result, 0, result.length);
        if (response.getPayload().getDataCrc32C() != checksum.getValue()) {
            logger.severe("Secret payload data corrupted");
            result = null;
        }
        return result;
    }

    private String initStringValue(String projectId, String apiKeySecretId, String versionId, SecretManagerServiceClient client) {
        SecretVersionName apiKeySecretVersionName = SecretVersionName.of(projectId, apiKeySecretId, versionId);

        // Access the secret version.
        AccessSecretVersionResponse response = client.accessSecretVersion(apiKeySecretVersionName);

        // Verify checksum. The used library is available in Java 9+.
        byte[] data = response.getPayload().getData().toByteArray();
        Checksum checksum = new CRC32C();
        checksum.update(data, 0, data.length);
        if (response.getPayload().getDataCrc32C() != checksum.getValue()) {
            throw new RuntimeException("Secret payload data corrupted");
        }
        return response.getPayload().getData().toStringUtf8();
    }

    public byte[] getApiKeyBytes() {
        return apiKeyBytes;
    }

    public String getTrelloToken() {
        return trelloToken;
    }
}
