package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {
    private CredentialsMapper credentialsMapper;
    private EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public void updateCredentials(Credentials credentials, Integer userId) {
        Credentials storedCredentials = credentialsMapper.getCredentialsbyId(credentials.getCredentialId(), userId);

        credentials.setKey(storedCredentials.getKey());
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), credentials.getKey());
        credentials.setPassword(encryptedPassword);
        credentialsMapper.updateCredentials(credentials);
    }

    public void deleteCredentials(Integer credentialId, Integer userId) {
        credentialsMapper.deleteCredentials(credentialId, userId);
    }

    public List<Credentials> getAllCredentials(Integer userId) {
        return credentialsMapper.getAllCredentials(userId);
    }

    public void addCredentials(Credentials credentials, Integer userId) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedKey);

        credentials.setUserId(userId);
        credentials.setKey(encodedKey);
        credentials.setPassword(encryptedPassword);

        credentialsMapper.insertCredentials(credentials);
    }
}
