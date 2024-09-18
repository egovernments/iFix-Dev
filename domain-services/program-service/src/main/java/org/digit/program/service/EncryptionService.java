package org.digit.program.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.models.disburse.Disbursement;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class EncryptionService {

    private final ObjectMapper mapper;
    private final ProgramConfiguration configs;

    public EncryptionService(ObjectMapper mapper, ProgramConfiguration configs) {
        this.mapper = mapper;
        this.configs = configs;
    }

    public Disbursement getEncryptedDisbursement (Disbursement disbursement) {
        log.info("Encrypting PII data");
        Disbursement encryptedDisbursement;
        try {
            encryptedDisbursement = mapper.readValue(mapper.writeValueAsString(disbursement), Disbursement.class);
        } catch (JsonProcessingException e) {
            throw new CustomException("JSON_PROCESSING_ERROR", e.getMessage());
        }
        for (Disbursement childDisbursement : encryptedDisbursement.getDisbursements()) {

            String individualName = childDisbursement.getIndividual().getName();
            String individualPhone = childDisbursement.getIndividual().getPhone();
            String individualAddress = childDisbursement.getIndividual().getAddress();
            String accountNumber = childDisbursement.getAccountCode();
            try {
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                byte[] nameBytes = cipher.doFinal(individualName.getBytes());

                cipher = Cipher.getInstance("AES/GCM/NoPadding");
                gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                byte[] phoneBytes = cipher.doFinal(individualPhone.getBytes());

                cipher = Cipher.getInstance("AES/GCM/NoPadding");
                gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                byte[] addressBytes = cipher.doFinal(individualAddress.getBytes());

                cipher = Cipher.getInstance("AES/GCM/NoPadding");
                gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                byte[] accountNumberBytes = cipher.doFinal(accountNumber.getBytes());

                childDisbursement.getIndividual().setName(Base64.getEncoder().encodeToString(nameBytes));
                childDisbursement.getIndividual().setPhone(Base64.getEncoder().encodeToString(phoneBytes));
                childDisbursement.getIndividual().setAddress(Base64.getEncoder().encodeToString(addressBytes));
                childDisbursement.setAccountCode(Base64.getEncoder().encodeToString(accountNumberBytes));

            } catch (Exception e) {
                throw new CustomException("ENCRYPTION_ERROR", e.getMessage());
            }
        }
        log.info("Encryption of PII data completed");
        return encryptedDisbursement;
    }

    public void getDecryptedDisbursement (List<Disbursement> disbursements) {
        log.info("Decrypting PII data");
        for (Disbursement disbursement : disbursements) {
            for (Disbursement childDisbursement : disbursement.getDisbursements()) {
                String individualName = childDisbursement.getIndividual().getName();
                String individualPhone = childDisbursement.getIndividual().getPhone();
                String individualAddress = childDisbursement.getIndividual().getAddress();
                String accountNumber = childDisbursement.getAccountCode();
                try {
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                    cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                    byte[] nameBytes = cipher.doFinal(Base64.getDecoder().decode(individualName));

                    cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                    cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                    byte[] phoneBytes = cipher.doFinal(Base64.getDecoder().decode(individualPhone));

                    cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                    cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                    byte[] addressBytes = cipher.doFinal(Base64.getDecoder().decode(individualAddress));

                    cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(configs.getEncryptionVector()));
                    cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(configs.getEncryptionKey()), gcmParameterSpec);
                    byte[] accountNumberBytes = cipher.doFinal(Base64.getDecoder().decode(accountNumber));

                    childDisbursement.getIndividual().setName(new String(nameBytes));
                    childDisbursement.getIndividual().setPhone(new String(phoneBytes));
                    childDisbursement.getIndividual().setAddress(new String(addressBytes));
                    childDisbursement.setAccountCode(new String(accountNumberBytes));
                } catch (Exception e) {
                    throw new CustomException("DECRYPTION_ERROR", e.getMessage());
                }
            }
        }
        log.info("Decrypted PII data successfully");
    }

    public SecretKey getPrivateKey(String privateKeyString) {
        String privateKeyPEM = privateKeyString.replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "")
                .trim();

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        return new SecretKeySpec(privateKeyBytes, "AES");
    }
}
