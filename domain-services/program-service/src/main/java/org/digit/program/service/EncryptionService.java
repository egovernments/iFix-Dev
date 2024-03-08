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
import java.security.*;
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
        log.info("getEncryptedDisbursement");
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

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return encryptedDisbursement;
    }

    public void getDecryptedDisbursement (List<Disbursement> disbursements) {
        log.info("getDecryptedDisbursement");
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
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public SecretKey getPrivateKey(String privateKeyString) throws Exception {
        String privateKeyPEM = privateKeyString.replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "")
                .trim();

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        return new SecretKeySpec(privateKeyBytes, "AES");
    }
}
