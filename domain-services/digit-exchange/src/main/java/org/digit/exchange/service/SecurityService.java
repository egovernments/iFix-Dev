package org.digit.exchange.service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.digit.exchange.config.AppConfig;
import org.digit.exchange.model.ExchangeServer;
import org.digit.exchange.model.ExchangeServerSearch;
import org.digit.exchange.model.RequestMessage;
import org.digit.exchange.repository.ExchangeServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;


@Service
public class SecurityService {

    @Autowired
    private AppConfig config;

    @Autowired
    private ExchangeServerRepository serverRepository;

    public PublicKey getPublicKey(String publicKeyString) throws Exception {
        String publicKey = publicKeyString
                .replaceAll("\\s+", "")
                .replace("-----BEGINPUBLICKEY-----", "")
                .replace("-----ENDPUBLICKEY-----", "");

        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        String privateKeyPEM = privateKeyString.replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "")
                .trim();

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        try {
            // get RSA private key
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    public String signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    public boolean verifySignature(String message, String signedMessage, PublicKey publicKey) throws Exception {
        byte[] signatureBytes = Base64.getDecoder().decode(signedMessage);
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(message.getBytes());
        return verifier.verify(signatureBytes);
    }

    public static String encrypt(String plaintext, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String ciphertext, PublicKey publicKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public void validateSignatureOfRequest(RequestMessage exchangeRequest) {
        try {
            String senderHost = extractHostUrlFromURL(exchangeRequest.getHeader().getSenderId());
            ExchangeServerSearch exchangeServerSearch = ExchangeServerSearch.builder()
                    .hostUrl(senderHost)
                    .build();
            List<ExchangeServer> exchangeServers = serverRepository.searchServers(exchangeServerSearch);
            if (exchangeServers.size() == 0) {
                throw new RuntimeException("Sender host not registered.");
            }
            boolean signatureVerified = verifySignature(exchangeRequest.getMessage(), exchangeRequest.getSignature(), getPublicKey(exchangeServers.get(0).getPublicKey()));
            if (!signatureVerified) {
                throw new RuntimeException("Signature verification failed.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RequestMessage signRequestMessage(RequestMessage exchangeRequest) {
        try {
            String signedMessage = signMessage(exchangeRequest.getMessage(), getPrivateKey(config.getServerPrivateKey()));
            exchangeRequest.setSignature(signedMessage);
            return exchangeRequest;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSameDomain(String url1, String url2) {
        boolean isValid = false;
        String domain1 = extractHostUrlFromURL(url1);
        String domain2 = extractHostUrlFromURL(url2);
        if (domain1 != null && domain2 != null) {
            isValid = domain1.equals(domain2);
        }
        return isValid;
    }
    public static String extractHostUrlFromURL(String input) {
        // Split and validate regex for domain
        // String regex = "(https?://[a-zA-Z0-9.-]+(:\\d+)?)";

        // Regex pattern to match the domain with http/https
        String regex = "\\bhttps?://\\S+\\b";

        // Regular expression pattern to match the domain with http/https
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(); // Returns the matched domain with http/https
        } else {
            return null; // No match found
        }
    }

}
