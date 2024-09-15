//CS645 Project1

// Khushbu Patel
// Esteban Lema
//Solomon Okine


package Part1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SimpleCracker {

    public static void main(String[] args) {
        // Load common passwords into a set for efficient lookup
        Map<String, Boolean> commonPassword = loadCommonPasswords("common-passwords.txt");

        // Read and process the shadow file
        processShadowFile("shadow-simple", commonPassword);
    }

    private static Map<String, Boolean> loadCommonPasswords(String filename) {
        Map<String, Boolean> commonPasswords = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String password;
            while ((password = reader.readLine()) != null) {
                commonPasswords.put(password, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonPasswords;
    }

    private static void processShadowFile(String shadowFilename, Map<String, Boolean> commonPasswords) {
        try (BufferedReader reader = new BufferedReader(new FileReader(shadowFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String username = parts[0];
                    String salt = parts[1];
                    String hash = parts[2];

                    // Generate hash for each common password with the given salt
                    for (String commonPassword : commonPasswords.keySet()) {
                        String CHash = generateHash(commonPassword, salt);

                        // Check if the generated hash matches the hash in the shadow file
                        if (CHash.equals(hash)) {
                            System.out.println(username + ":" + commonPassword);
                            break; // Break the loop once a match is found
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateHash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((salt + password).getBytes());
            byte[] byteData = md.digest();
            return toHex(byteData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
