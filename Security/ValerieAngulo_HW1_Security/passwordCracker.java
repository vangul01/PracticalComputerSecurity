/*
 * Name: Valerie Angulo
 * Description: Password cracking program
 * Note: Thought process and other notes located at bottom of code
*/
import java.io.*;
import java.util.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class passwordCracker {

    public static final int ROWS = 10;

    public static String encode(String value) {
        return  Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.US_ASCII));
    }

    public static String decode(String value) {
        byte[] decodedValue = Base64.getDecoder().decode(value);
        return new String(decodedValue,StandardCharsets.US_ASCII);
    }

    public static void decodeEncodeSalt (String[][] line) {
        String salt = null;
        for(int i=0; i<ROWS; i++){
            salt = line[i][1];
            String unsalted = decode(salt);
            System.out.println("Decoded salt for line" + (i+1) + ": " + unsalted);
            System.out.println("Encoded salt for line" + (i+1) + ": " + encode(unsalted));
        }
    }

    public static void print(String[][] line) {
        for(int i=0; i<ROWS; i++){
            System.out.println(line[i][0] + ":: Nope :(");// + line[i][3]);
        }
    }

	public static void main (String[] args) {

    String[][] information = {
        {"jgarcia", "1vGZ8kutWReG8oTOpX60dyRUHaI6hzjiS1fuD7ia+3w=", "1", "5HXchzDX2wqqGt9c/l1o3F1OH0Pd8aLPHQnN0ds4AK0="},
        {"tsato", "I9amxwcBZGedYerhFKLzJptk5HpH89nvLz99pTR8kWA=", "1", "BDbs2+2DOBs6I2DafuJYxsJZYMmi3MkuxtIL/hj3QoQ="},
        {"rwong", "FW0b0vJ72twGLxcWyWznnrC7ForkkUiCcP78yY2nXxM=", "1", "UQuR/p3YWmImeUP8qAukV43YolzLAiPXw2ohnLgsp1w="},
        {"kjackson", "uP0uwaYpAnlJuNP+IDamN+iXzBklCxjngui/TK9Pf/w=", "1", "7UBQA2IgzEdofrFMFIZ/cDGu4p8fqaHDp/W59FHacsI="},
        {"cbrown", "36fMDaD6guPJSaPFHv/jER6tCIqt4uPl9yd8FB/hm9I=", "1", "KYk7Ck088hMjYFtQvE9U0VhMW7sCQIX2T6Cx0KN7R5M="},
        {"kwilson", "SOxmsiQx80oIZ9hHmAOTj68kT6dkO9RNZMIuVfkz4QE=", "1" ,"jRFkzI/HxAYKMxHoE3H8sKcR2cpTu/v24M9PwShHT4U="},
        {"adavis", "JJ+smo6LgeDpt8qQWnmSzdpIiziQEfVEI7LLMt+e+nQ=", "1", "3TxZ6bsB/Rn87m7YiLLguT8culYptd9KYm/dDH3OR8o="},
        {"mzhang", "+cjc4tFN+Qa+EgwOCr/aE9qHrK/P+Z4/wtjKyp2BSNc=", "1", "7W+WSmHXLMUMHfZrOjx7zIzDTMTinA+5UXUg4IKw/io="},
        {"lzhao", "YtVdwHi5VGs7LU8hBDI2GpaaAjutk0BEfv1Lgaq+ciI=", "1", "dn0ZCRsfufR2aiwVkgwzqDms3RtAmYelnzmfSWRDjN4="},
        {"pmartinez", "5YLVoA7axW5vAR4RJH3HGqopohVEhr4vh0ZHId4U9IQ=", "1", "I0UFLxbDoYfK+f4yglCaKcLhEqD2LsPbceDDl1J3ne4="}
        };
        
        //The values obtained by decoding salt had unique characters, even when switching to UTF-8
        //decodeEncodeSalt(information);

        print(information);
    }
}

//------------------------------------------------------------
/*------------------------------------------------------------
                        THOUGHT PROCESS
//------------------------------------------------------------
Pre-processes:
1. input psw.txt file to program
2. save usernames in string variable array
2. save salt
3. save password

Then:

1. decode salt with Base64 Java decode function -- This is not working, random unicode characters come up
2. re-encode decoded salt with Base64 Java encode function to make sure the values are equal
If I was able to get the salt...
I know that the hashed password in pswd.txt is obtained by appending or prepending unhashed salt to encoded(password)
Therefore, I could use brute force methods such as testing out users names or very simple passwords
Methods to create hashed passwords could be...
    encode(encoded(password) + unhashedSalt)
    encode(unhashedSalt + plaintextPassword)
    encode(ptPassword + unsalted hash)    

Since all passwords have = at the end, this means the last character of the password
has a length of 2 and the last four characters in the hash will only decode to 2 bytes, 
rather than three

I tested the example given (pw: computer) with this site: https://www.base64decode.org/
to decode the salt value. I chose the ASCII charset because there were 2 digits remaining 
rather than 1 with the UTF-8 example.

I tried changing the StandardCharsets in my code but to no avail.


3. make sure salt decoded = salt encoded with boolean method

4. If returned true... 
    Password hash is obtained by methods such as 
    PWH = [salt] + HMAC-SHA-256([key], [salt] + [credential]);
        (salt + encodingAlgorithm(salt+pw))

    return [salt] + pbkdf2([salt], [credential], c=10000);

    [protected form] = [salt] + protect([protection func], [salt] + [credential]);

    And the examples I mentioned above
    So I would next attempt to decode password in Base 64 

5. append or prepend salt to password and encode it, hopefully until 
    it matches up with the hashed password provided

Something I was curious about but that didn't work when I tried implementing it:
    Key factories are bi-directional, i.e., they allow to build an opaque key 
    object from a given key specification (key material), or to retrieve the 
    underlying key material of a key object in a suitable format.

______________________Brute Force to find correct decoding of hashed salt_________________________________________
1st decoded salt: KYΥ~w$T:8KW| --> KY~w$T:8KW| (the strange characters are from JSON script on website)
2nd decoded salt: #dga&dzG/?}4|`
using https://www.base64decode.org/

Brute force using website to decode along with my decoder method, 
trying to find what symbols actually match up: 

m{/l瞰Hpɍ_ --> FW0b ewYv FxZs 556w Fkhw yY1f Ew==	-->UTF8 
m{/lHp_ --> FW0b ewYv FxZs Fkhw XxM=   -->ASCII

from my decoder: m����/�l瞰���H�p��ɍ �_

trying to get: 	FW0b 	0vJ7	2twG LxcW 	yWzn nrC7 Fork kUiC cP78 yY2n XxM=
				m 			 /							  _
				FW0b				 LxcW								  XxM=

From my decoder	m 		��{  	��	 /		 �l�  ���  ��  �H�  p��  ɍ�   _
				m��{��/�l�������H�p��ɍ�_

				NAK=µ 
ASCII			Alt+230
11 char: m{/l瞰Hpɍ_

HURDLES ENCOUNTERED AND OTHER ATTEMPTS:

/*------- biggest hurdle was decoding salt, getting special characters in result, encoding again
         and not getting decoded salt, even after trying to deal with special characters--------

        System.out.println("Salt: " + saltString);
        System.out.println(saltString + " ---plainText---> " + decode(saltString));
        System.out.println(decoded + " ---Base64----> " + decodedSaltString);

//----  Attempts at hashing with computer example, could not obtain hashed password, 
        also attempted to use method utilizing secretKeyFactory on web page 
        https://www.owasp.org/index.php/Hashing_Java ------//

        char[] password = new char[] {'c', 'o', 'm', 'p', 'u', 't', 'e', 'r'};
        byte[] salt = saltString.getBytes();
        int iterations = 1;
        int keyLength = 44;
        byte[] res = hashPassword(password, salt, iterations, keyLength);
        System.out.println("Secret PW: " + res);

//----  Attempts to figure out how to make decoded salt from https://www.base64decode.org/ add to "computer" 
        to get the password in "computer" example:
            salt is: B9OGLTbJNATU+ZJdnaUUGnMe4hOeK9qRW/6zG+Lkn0E=
            password is: cTrpsypRsEoi0Sotz1r0jvkTjTSfA60yxO3RzBRNF3o=
            plain text password is: computer  --------//

        String password = "computer"; //encode, add salt then encode both
        String salted = encode(encode(password) + "-64]s+[A"); //cTrpsypRsEoi0Sotz1r0jvkTjTSfA60yxO3RzBRNF3o=
        System.out.println("salty: " + salted);
        String result1 = "-64]s+[A" + "Y29tcHV0ZXI=";
        String result = encode(password); //***
        String result2 = encode(result1); //
        System.out.println("Encoded password w program: Y29tcHV0ZXI=: " + result);
        System.out.println("encoded obtained salt and pw from site: " + result2);

        System.out.println("Oh man...:" + decode(("cTrpsypRsEoi0Sotz1r0jvkTjTSfA60yxO3RzBRNF3o=")));    
*/
/*------Attempt at taking whole file in, ideally would have put all info in a 2D array-------------      
        String fileName = "pswd.txt";
        String listDelimiter = ", ";
        String line = null;
        List allLines = null;
        String allLinesString = null;

        try {
            FileReader fileReader = new FileReader(fileName); // FileReader reads text files in the default encoding.
            BufferedReader bufferedReader = new BufferedReader(fileReader); // Always wrap FileReader in BufferedReader.

            while((line = bufferedReader.readLine()) != null) {
                allLines = java.nio.file.Files.readAllLines(new File(fileName).toPath(), StandardCharsets.UTF_8);
            }

            allLinesString = String.join(", ", allLines);
            
            String[] lineSplit = allLinesString.split(listDelimiter);
            //System.out.print(" " + lineSplit[1]);

            String[] myfirstLine= getUsername(lineSplit, 1);
            System.out.println("This is my line split up!: " + myfirstLine);

        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
*///----------------------------------------------------------------------------------------
