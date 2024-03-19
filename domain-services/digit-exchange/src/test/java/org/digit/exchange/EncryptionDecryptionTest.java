package org.digit.exchange;

import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.service.SecurityService;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
public class EncryptionDecryptionTest {

    public static final String endpointOnePrivateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDrD9z2aCCj1FPs\n" +
            "p0c2NbijCcXWFXBu6G+VBHAZLXZjW7uf72xGNgQAgigvJZkz7wqj+B8NIv9vrAAJ\n" +
            "EnPjUXsATBAgf3GIkKJY+lflT0Cz0TVO8nfzKSXWYKFhyzgA8+185vvFllediuQU\n" +
            "CSBnaVy+bYHlup4dZOLuqx/l2NW+kqVoQXDpwigdfAD6gpFvytSrhS10Dcep79Z9\n" +
            "n5ajMHRVy83B+YnF5rQWElJIcAdaqFdwE/4xig0r7eueGNvCzEK23R9TvFITPO7H\n" +
            "VzbpQ+l4jBM1YiYS5XlgjyHOzE+BW11yyhGp51Jiu0d4ILn6/owHWrvCXDqJK9L0\n" +
            "96XNm+bHAgMBAAECggEABIDLy16oTWsqZmy39QZncJwl0Zmci0tho9mvahQQYgvm\n" +
            "VsSH7vpm7jmQGoMeYbvRGN4Ofjpu6U1CvOnBFZhAgntyjaTTYHng2lKFb4uqoic+\n" +
            "XbJQcPSXWHmGbfCuNHp30L4+EYV1TTvVbbrVB5YhpDZF2EhQciC2JjtaA5W5VorA\n" +
            "QNt6vcNZ2+CotRRdxwN9ksQfAsVRM17SzeZYyB8ZhObYS8sEndliuMmzdKaIHoAq\n" +
            "QsyExE41fY3MuV+G7vOLzzk5mz6hoAoY22u3owwdoJZvRXfGh3yFY7syLdO1ejEu\n" +
            "ctnqsH5aPlePNq2ZEkrf4Q/aYNzt3IXERrfi6l5ZCQKBgQD6Ok0icgSdBRyqSRjX\n" +
            "kzI9RVQ1vXc2kwYqOjwMQ0HDVRzj7SXuG/xr7wAqoYHgdO52lA8pneJ2Rsz+GCJv\n" +
            "2t6oZocsaYlAepD1jEoutqhOgsbCqu8n8M2RiOwhhY6DhwfHUbI3mqhmJDqUa27c\n" +
            "xaRlnRZ0lzf0UtYLaAhUWe+NyQKBgQDwfABrKyf0q5Yr0yTWBldtPa1KuUYiBrKV\n" +
            "YW8GJnS6lffEsWKqEOK5FFPdJ+bBpywCtvQY0XEwYVkl+BjRjlEn+KaGcdkslCP9\n" +
            "0Jk0FbQ98FrN2+YYsGhExUScxt5O51TH+hK+9XGLWcORdYoNWhDP3w5BhGNwOeCT\n" +
            "jvDR9enYDwKBgQDPV7oMvc/G0LKVA+b60rTBgo4pzapX5XIpUYHqY1y93+wFjb9t\n" +
            "U3FReoR3zsaQ2DO0vDDoOjSp7zbzocn/R6xSfEqr6XNao3U0kp/xn1dNXx4VtpBf\n" +
            "VzDv1DZd/I4/vIcciTyUyKmboY+M5ozBkWAM9yLhT2CFKpLEdtrYEoHmyQKBgEGd\n" +
            "A5w53R/zhPiWFlHSzx2+Gz7tCAaWnzkEqX8bFPQnoL9oyouuKb3nnu4TSfGCRA7F\n" +
            "jhoLYv+ZP/KNG3BpjRkDzaOXAnMYRC2nMXW1ikTwBEZwffo0f25sCEliNRuAIHCj\n" +
            "eWPiocUsi6BvWmhEL5wXttMHZP4kvHJmKKyQlOnfAoGBAKuBIACao1N5UGL78HTg\n" +
            "rG7bDPq+DFpvmtrnJdhi+BkOdfb8ykvsiyt0/XdXzMjHPzHnhQPgDSenT/kK4f1J\n" +
            "elJEw7wya0KF2fum3aM6HgbIqpzni1iOKWwDTfmyiPY3wWg3yFMBvOHejAHkP0Xb\n" +
            "VL4KVa0q9j+xWyeXbShuyLYj\n" +
            "-----END PRIVATE KEY-----";
    public static final String endpointOnePublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6w/c9mggo9RT7KdHNjW4\n" +
            "ownF1hVwbuhvlQRwGS12Y1u7n+9sRjYEAIIoLyWZM+8Ko/gfDSL/b6wACRJz41F7\n" +
            "AEwQIH9xiJCiWPpX5U9As9E1TvJ38ykl1mChYcs4APPtfOb7xZZXnYrkFAkgZ2lc\n" +
            "vm2B5bqeHWTi7qsf5djVvpKlaEFw6cIoHXwA+oKRb8rUq4UtdA3Hqe/WfZ+WozB0\n" +
            "VcvNwfmJxea0FhJSSHAHWqhXcBP+MYoNK+3rnhjbwsxCtt0fU7xSEzzux1c26UPp\n" +
            "eIwTNWImEuV5YI8hzsxPgVtdcsoRqedSYrtHeCC5+v6MB1q7wlw6iSvS9PelzZvm\n" +
            "xwIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    public static final String endpointTwoPrivateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC3c2rfkAFPnpeu\n" +
            "0YdTsR1A04zHPWCkwGR6Cwb6gJBVAOPL3h8VqWhAbuUhfqFFfQY2dqzY9vXUgKsG\n" +
            "C/SWAc4YEx/YG2R1z4yu8kt5rFDJsWvRLfQidKsIPoh51HUYl+kTLSvOhhqjEoWg\n" +
            "SAsrZrtEMxl+Dl+rUeRJ1YWYFS9sKMuNVKhC5whQmx3v1zkKDi3Etp3fXyxv0nkw\n" +
            "Bx89aMIEPgZ/G70GEqRnPJY4Ec/U2281te4c0loJsdU9smYtS73Oa+pw34TFB2xt\n" +
            "QB2Lx1Fb3oCapm+UJ0hQppywxU0k4xV6aI++8m2zNs/VT0hnP0BBOe2a6Pefgcyq\n" +
            "+t3NPuwFAgMBAAECggEAHWBLkGIwe43zTyi5SWMWwp89r1f6RleSn2JtqyZ0NlBu\n" +
            "qeI3SxI2If9ADMWI0KZNHOKTGiktw2oZON4oO+8tD/jcBBCa3SyvpAIjqOe5hhq1\n" +
            "n6kCq5PQIYFV/GBToe0NgyccGTWrRUq9gCWZOEl6mliub9ugYUA7VMFu46Qc1G5M\n" +
            "V0LJ3q22y9xjQjDYQjQyWB97OBVahDTHhthcg5pRP8kBqXf+ab+qkAc4gerOg2iv\n" +
            "ISl6Sm6yiZUQ62woTfoZvrX4nE7gtA+8zU6k1Yaej71r7KcwG3yaLQwbGx83jtUd\n" +
            "Sp0Xx7tBPjx6B5ok2pvcswizTaitOZZ0a5zm6neVzwKBgQDpFi6zIDv5YW8oPeK0\n" +
            "haG7rM6HkikRY2ms0iwAIKuvn/4pxF2mblrROBC2CRmWyuu9qUWi3i3QjOA9KWtW\n" +
            "nzLCPt634bPSZC0Lv9hoLBKE5/v154IP96DuGBbSSWV78spdc089m4yCkOazP7C9\n" +
            "xe4hpRP1E/X7vPBmdOaNY0RIywKBgQDJfBv3E1WuTxI4sMs+o+eiAc9kbuKXXqIn\n" +
            "jB8S3hM/DdQUbuLs+JVo9P9kk7uCLZt5J28NDhl4GafbOSWWR0KwxjAwRYnbNwTs\n" +
            "c8z2ophRG/VIzFzKMhDCK9I/Y/zlbdhEgXLP1Mz4bcgib+DCZnYz5WF4mQBBx2Bz\n" +
            "+tfwXeqUbwKBgQC0EaUGUVLD+Bft2NF5cPtfh8MOTUZY3ppXHw9LEfJQ/aNXFRLc\n" +
            "bsjOdSfD2gfgzOeOYGUhy89NHGhoF8WK5/ZIxlU9baqEUUvTleRTpiiwcJ/ge3ep\n" +
            "aj7zcby/vGAgf5MC2wBOsgq9G8B+niemlHDBoUxnu4H/dxmprYonlJa+PQKBgQCf\n" +
            "7oDwNYu8ag1nrIYYOdhik5dA/HemebqzM5lyy45xbZObiI1eVs+aWWbkOhQL+BHC\n" +
            "MJgIv9ldqNfBvvHkALqiQR79IiWUOW8SFbVrTJMlRtEDsRwyb104E7q3ELIsl22k\n" +
            "qzAsYpk01UeYdj/tb5GCDiC5rpEypYUdas6/gHz0xwKBgHfLURs5XYYPXPnA6ARW\n" +
            "9Wo/gPERIHi2vAkJ6xtj6PBldg4ZPeIyylFCtH7WJ5Pjyioqow8ItUh3C2KQX62A\n" +
            "bMFKmYeYl0n1hc4Oc0DsfBAc7bR8V/BiMTpJc2o8/OnICV6S5xXjMC9WgodAtPwu\n" +
            "j6dpMIIJA3zpO+AXaeHkolzn\n" +
            "-----END PRIVATE KEY-----";
    public static final String endpointTwoPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt3Nq35ABT56XrtGHU7Ed\n" +
            "QNOMxz1gpMBkegsG+oCQVQDjy94fFaloQG7lIX6hRX0GNnas2Pb11ICrBgv0lgHO\n" +
            "GBMf2Btkdc+MrvJLeaxQybFr0S30InSrCD6IedR1GJfpEy0rzoYaoxKFoEgLK2a7\n" +
            "RDMZfg5fq1HkSdWFmBUvbCjLjVSoQucIUJsd79c5Cg4txLad318sb9J5MAcfPWjC\n" +
            "BD4Gfxu9BhKkZzyWOBHP1NtvNbXuHNJaCbHVPbJmLUu9zmvqcN+ExQdsbUAdi8dR\n" +
            "W96AmqZvlCdIUKacsMVNJOMVemiPvvJtszbP1U9IZz9AQTntmuj3n4HMqvrdzT7s\n" +
            "BQIDAQAB\n" +
            "-----END PUBLIC KEY-----";


    @Test
    public void encryptMessage() {
        log.info("Test Sign Message");
        SecurityService encService = new SecurityService();
        String message = "Hello World1";
        String signature = null;
        try {
            PrivateKey privateKey = encService.getPrivateKey(endpointOnePrivateKey);
            signature = encService.encrypt(message, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Signature: " + signature);
    }

    @Test
    public void decryptMessage() {
        log.info("Test decrypt message");
        SecurityService encService = new SecurityService();
        String message = "Hello World1";
        String signature = "e4NwqUECBSjRblGRoI1taBCw6Bw+cTLKAzVPlL9KelkAJCJ3VWLXb7KzRg8lXXjOUgRgnXZcC+mNVI9+vl4cXP9eAuynudWNMm2HAtUZNKsteDHOptNJNIWYLTGes79nwGbGPDetZD/vU4KJl13ZuFKoHJoYnt3qYKK6RI7YKgBNStZ3ctvnyvHgHHiMUEZxcGFl/i7OrrNZerrRazdu993SXxykBlOgSLp0ViU1LdRnHbOOawqVBNkOkDvgQ1M2CUtqB4Z+36vFWfI/0VV/GF+ZBRl7EU+bz/YG+cmIEjuNvRxkvdrS1PL3BD0SVCEEhF6GlNhDmuExAYO8rDiYqw==";
        String decryptedMessage = null;
        try {
            PublicKey publicKey = encService.getPublicKey(endpointOnePublicKey);
            log.info("Public Key: " + publicKey);
            decryptedMessage = encService.decrypt(signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("decryptedMessage: " + decryptedMessage);
        if (message.equals(decryptedMessage)) {
            log.info("Both messages are same!" );
        } else {
            log.info("Both messages are not same!" );
        }
    }

    @Test
    public void signMessage() {
        log.info("Test Sign Message");
        SecurityService encService = new SecurityService();
        String message = "{\"id\":\"0d2cc98d-b528-4fb3-993e-5efda3f61f60\",\"function_code\":null,\"administration_code\":null,\"recipient_segment_code\":null,\"economic_segment_code\":null,\"source_of_fund_code\":null,\"target_segment_code\":null,\"currency_code\":null,\"locale_code\":null,\"status\":{\"status_code\":\"INITIATED\",\"status_message\":\"INITIATED\"},\"location_code\":\"pg.citya\",\"program_code\":\"PG/2023-24/000310\",\"target_id\":\"EP/1013/2023-24/02/29/000090\",\"parent_id\":null,\"sanction_id\":\"7b6bb0f8-109d-4b94-bc97-c3220509c632\",\"transaction_id\":null,\"account_code\":null,\"net_amount\":100,\"gross_amount\":100,\"individual\":null,\"additional_details\":{\"billNumbers\":[\"PB/2023-24/000558\"],\"referenceIds\":[\"WO/2023-24/000783_PR_000491\"]},\"audit_details\":{\"createdBy\":\"ee3379e9-7f25-4be8-9cc1-dc599e1668c9\",\"lastModifiedBy\":\"ee3379e9-7f25-4be8-9cc1-dc599e1668c9\",\"createdTime\":1709188644862,\"lastModifiedTime\":1709188644862},\"children\":[{\"id\":\"bb14c451-634a-44b2-aa0c-78b069d6cd64\",\"function_code\":null,\"administration_code\":null,\"recipient_segment_code\":null,\"economic_segment_code\":null,\"source_of_fund_code\":null,\"target_segment_code\":null,\"currency_code\":\"INR\",\"locale_code\":\"en_IN\",\"status\":{\"status_code\":\"INITIATED\",\"status_message\":\"INITIATED\"},\"location_code\":\"pg.citya\",\"program_code\":\"PG/2023-24/000310\",\"target_id\":\"62d30121-261e-442b-b23c-5049c2141131\",\"parent_id\":null,\"sanction_id\":null,\"transaction_id\":null,\"account_code\":\"XYZ1230987@SBI000007\",\"net_amount\":12,\"gross_amount\":12,\"individual\":{\"name\":\"GSTTDS\",\"email\":null,\"phone\":\"9999999999\",\"pin\":null,\"address\":\"pg.citya\"},\"additional_details\":null,\"audit_details\":{\"createdBy\":\"ee3379e9-7f25-4be8-9cc1-dc599e1668c9\",\"lastModifiedBy\":\"ee3379e9-7f25-4be8-9cc1-dc599e1668c9\",\"createdTime\":1709188644862,\"lastModifiedTime\":1709188644862},\"children\":null},{\"id\":\"bc5db982-b9e6-47b3-aeac-c37f135b76ee\",\"function_code\":null,\"administration_code\":null,\"recipient_segment_code\":null,\"economic_segment_code\":null,\"source_of_fund_code\":null,\"target_segment_code\":null,\"currency_code\":\"INR\",\"locale_code\":\"en_IN\",\"status\":{\"status_code\":\"INITIATED\",\"status_message\":\"INITIATED\"},\"location_code\":\"pg.citya\",\"program_code\":\"PG/2023-24/000310\",\"target_id\":\"91c4d866-7ec1-49b6-9d02-492e42cbb0da\",\"parent_id\":null,\"sanction_id\":null,\"transaction_id\":null,\"account_code\":\"333367679911@SBIN0000063\",\"net_amount\":88,\"gross_amount\":88,\"individual\":{\"name\":\"roso\",\"email\":null,\"phone\":\"9876678790\",\"pin\":null,\"address\":\"pg.citya\"},\"additional_details\":null,\"audit_details\":{\"createdBy\":\"ee3379e9-7f25-4be8-9cc1-dc599e1668c9\",\"lastModifiedBy\":\"ee3379e9-7f25-4be8-9cc1-dc599e1668c9\",\"createdTime\":1709188644862,\"lastModifiedTime\":1709188644862},\"children\":null}]}";
        String signature = null;
        try {
            PrivateKey privateKey = encService.getPrivateKey(endpointTwoPrivateKey);
            signature = encService.signMessage(message, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Signature: " + signature);
    }

    @Test
    public void validateMessage() {
        log.info("Test Sign Message");
        SecurityService encService = new SecurityService();
        String message = "{\"location_code\":\"pg.citya\",\"name\":\"Test 1\",\"start_date\":0,\"end_date\":0,\"client_host_url\":\"https://unified-dev.digit.org\",\"function_code\":\"string\",\"administration_code\":\"string\",\"recipient_segment_code\":\"string\",\"economic_segment_code\":\"string\",\"source_of_fund_code\":\"string\",\"target_segment_code\":\"string\",\"currency_code\":\"string\",\"locale_code\":\"string\",\"status\":{\"status_code\":\"RECEIVED\",\"status_message\":\"string\"}}";
        String signature = "TgE1hcA2E+YPMdPGz4vveKQpR0x+pgzRTlet52qh63Kekr71vWWScXqaRFtQW64uRFZGBUhHYYZQ2y6LffwnNOOQhhssaThhqVBhXNEwX9i75SNYXi5XSJVDYzSyHrhF20HW6RE9mAVWdc80i7d+FXlh+b/U+fnj+SrZ2s6Xd0WUZvU29LgqeUpyznlWLu1mDdJxNZavsDLWmxjTnknqBjDvwSc35WhFDhXDA2lWmm8YpZ1Y6TBmvvtVS7mAOTnhFy9sdCbrLcfXk5QWIsdzlvPqlkvdwEf30OZ6ewb680Aj3hO2OT5LCv7iLyz7C7srnB9lJT5gXiw+eSnktPXlDA==";
        boolean signatureVerified = false;
        try {
            PublicKey publicKey = encService.getPublicKey(endpointOnePublicKey);
            signatureVerified = encService.verifySignature(message, signature, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Signature Verified: " + signatureVerified);
    }

    @Test
    public void signMessage1() {
        log.info("Test Sign Message");
        SecurityService encService = new SecurityService();
        String message = "{\"location_code\":\"pg.citya\",\"name\":\"Test 1\",\"start_date\":0,\"end_date\":0,\"client_host_url\":\"https://unified-dev.digit.org\",\"function_code\":\"string\",\"administration_code\":\"string\",\"recipient_segment_code\":\"string\",\"economic_segment_code\":\"string\",\"source_of_fund_code\":\"string\",\"target_segment_code\":\"string\",\"currency_code\":\"string\",\"locale_code\":\"string\",\"status\":{\"status_code\":\"RECEIVED\",\"status_message\":\"string\"}}{\"location_code\":\"pg.citya\",\"name\":\"Test 1\",\"start_date\":0,\"end_date\":0,\"client_host_url\":\"https://unified-dev.digit.org\",\"function_code\":\"string\",\"administration_code\":\"string\",\"recipient_segment_code\":\"string\",\"economic_segment_code\":\"string\",\"source_of_fund_code\":\"string\",\"target_segment_code\":\"string\",\"currency_code\":\"string\",\"locale_code\":\"string\",\"status\":{\"status_code\":\"RECEIVED\",\"status_message\":\"string\"}}";
        String signature = null;
        try {
            PrivateKey privateKey = encService.getPrivateKey(endpointOnePrivateKey);
            signature = encService.signMessage(message, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Signature: " + signature);
    }

}
