package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int addCredential(Credential credential) {
        credential = encryptPassword(credential);
        
        return credentialMapper.insertCredential(new Credential(null, 
                credential.getUrl(), credential.getUsername(), 
                credential.getKey(), credential.getPassword(), credential.getUserId()));
    }

    public boolean updateCredential(Credential credential) {
        credentialMapper.updateCredential(encryptPassword(credential));
        Credential updatedRow = credentialMapper.getCredential(credential);
        return credential.getUrl().equals(updatedRow.getUrl())
                && credential.getUsername().equals(updatedRow.getUsername())
                && credential.getPassword().equals(updatedRow.getPassword());
    }

    public List<Credential> getAllCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public boolean deleteCredential(Credential credential) {
        credentialMapper.deleteCredential(credential);
        return credentialMapper.getCredential(credential) == null;
    }

    private Credential encryptPassword(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        return credential;
    }

    public String decryptPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
    
}

