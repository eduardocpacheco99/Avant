
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        /* path to the directory containing the identifier.txt, encryption.key
        *  and decryption.key downloaded from the WebApp.
        * */
        String keysDirectory = "/path/to/keys/directory/"; // change this to the appropriate location

        /* initialize the KeyManager */
        KeyManager keyManager = new KeyManager(keysDirectory);

        /* read the keys from the specified directory */
        try {
            keyManager.loadKeys();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* read the identifier to use in the login method
         *
         * Sample request to the login method:
         * {
         *   'identifier': '07c7667ead8751e9aa09f06f6f536ac0e9db1e0ecb5ccec279dc832a768fbe89'
         * }
         * */
        String identifier = keyManager.getIdentifier();

        /* Both the tokens are returned as a response of the login method.
        *
        *  Sample response from the login method:
        *  {
        *    'authToken': 'Q2293V5HaFDtdVwLadw4VA==\n',
        *    'reqToken': '2q3PYKMv8owXFFaAmFHFxiGNr/0wkV5VgAez8rmB4ygtA9Mdki5dhQYwJBO6XpMZZFwOu5V/ffwK\n8QeBbVagBIiUBwnaJvmFkqNbnAHPf7s=\n',
        *    'status': 1,
        *    'statusMessage': 'Success'
        *  }
        * */
        String authToken = "Q2293V5HaFDtdVwLadw4VA==\n";
        String reqToken = "2q3PYKMv8owXFFaAmFHFxiGNr/0wkV5VgAez8rmB4ygtA9Mdki5dhQYwJBO6XpMZZFwOu5V/ffwK\n8QeBbVagBIiUBwnaJvmFkqNbnAHPf7s=\n";

        try {
            keyManager.setIV(authToken);
            String encryptedReqToken = keyManager.reencodeToken(reqToken);
            System.out.println(encryptedReqToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* With the received authToken and encryptedReqToken you may query the other methods
        *  of our API
        *
        *  Sample request to the dateQuery method:
        *  {
        *    'dateQuery': '2015-05-01 00:00:00',
        *    // the same as received from the login method
        *    'authToken': 'Q2293V5HaFDtdVwLadw4VA==\n',
        *    // the encryptedReqToken from the previous step
        *    'reqToken': 'N0MeyNXGk+Wh5TXL3kPDq2mM1BMzy5Jhwm6+FGXTq5AD5awFVetP9fWUBpoYb8XYG8l2sG/pV1QN\nw/jcke+H4eZDnMqFksN5KJF5atzeZsk=\n'
        *  }
        * */
    }
}
