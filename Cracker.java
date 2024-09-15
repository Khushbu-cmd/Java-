//CS645 Project1

// Khushbu Patel
// Esteban Lema
//Solomon Okine


package Part2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cracker {

    public static void main(String[] args) {
        
        Map<String, User> userMap = loadUsersFromShadowFile("shadow");
        Map<String, Boolean> commonPasswords = loadCommonPasswords("common-passwords.txt");
        MD5Shadow md5hasher = new MD5Shadow();

        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            User currentUser = entry.getValue();
            for (String commonPwd : commonPasswords.keySet()) {
                String output = md5hasher.crypt(commonPwd, currentUser.getSalt());

                if (currentUser.getHash().equals(output)) {
                    System.out.println(entry.getKey() + ":" + commonPwd);
                    break;
                }
            }
        }
    }

    private static Map<String, User> loadUsersFromShadowFile(String fileName) {
        Map<String, User> userMap = new HashMap<>();

        try (BufferedReader shadowBuffer = new BufferedReader(new FileReader(fileName))) {
            String sline;
            while ((sline = shadowBuffer.readLine()) != null) {
                String[] shadowSplit = sline.split(":");
                String user = shadowSplit[0];
                String saltHash = shadowSplit[1];
                String[] saltHashSplit = saltHash.split("\\$");
                String salt = saltHashSplit[2];
                String hash = saltHashSplit[3];

                userMap.put(user, new User(user, salt, hash));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userMap;
    }

    private static Map<String, Boolean> loadCommonPasswords(String fileName) {
        Map<String, Boolean> commonPasswords = new HashMap<>();

        try (BufferedReader commonPasswordsBuffer = new BufferedReader(new FileReader(fileName))) {
            String commonPwd;
            while ((commonPwd = commonPasswordsBuffer.readLine()) != null) {
                commonPasswords.put(commonPwd, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commonPasswords;
    }
}

class User {
    private String userName;
    private String salt;
    private String hash;

    User(String userName, String salt, String hash) {
        this.userName = userName;
        this.salt = salt;
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getHash() {
        return hash;
    }
}
