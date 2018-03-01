package org.onetimepad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class OneTimePad {
    /**
     * <h1>One Time Pad Implementation</h1>
     * Current only encodes / decodes alphabetic and space characters (A-Z + ' ')
     * If any non-alphabetic characters are present it will overwrite it with a space
     * Lazy native java random which implements linear congruential generator.
     * @author Martin Kennelly
     * @version 1.0
     * @since 20-01-2018
     *
     */

    public void Start() {
        /**<h1>OneTimePad.Start Method</h1>
         * Displays user menu with three options - Encode - Decode - Quit         *
         *  */
        menu(new Scanner(System.in));
    }

    private void menu(Scanner scanner) {
        System.out.println("----------------------------------------");
        System.out.println("\t\t\tEncoder & Decoder");
        System.out.println("\t\t\tUsing One-Time Pads");
        System.out.println("\t\t\tBy Martin Kennelly");
        System.out.println("\t\t\t\t\tv0.1");
        System.out.println("----------------------------------------");
        int selection = 0;
        while (selection == 0) {
            System.out.println("Menu:");
            System.out.println("1) Encode");
            System.out.println("2) Decode");
            System.out.println("3) Quit");
            System.out.println("Example: Entering the integer '2' will help you decode your message");
            System.out.print(": ");
            int iResponse = Integer.parseInt(scanner.nextLine());
            if (!(iResponse > 0 && iResponse <= 3)) {                   //Check input boundaries
                continue;
            }
            selection = iResponse;
        }
        switch (selection) {
            case 1: encodePreStage(scanner);
                break;
            case 2: decodePreStage(scanner);
                break;
            case 3:
                System.out.println("Thank you for using my software, goodbye.");
                break;
            default:
                System.out.println("Unknown input, exiting");
        }
    }

    private void decodePreStage(Scanner scanner) {
        String key = null;
        while (key == null) {
            System.out.print("Enter a key for decryption: ");
            String temp = scanner.nextLine();
            if (!temp.isEmpty()) {
                key = temp;
            }
        }
        String filePath = null;
        do {
            System.out.print("Enter the link of a text file to decode (ext. txt): ");
            filePath = scanner.nextLine();
        } while (!checkFile(filePath));

        File fileEncrypted = new File(filePath);
        int lastDotIndex = fileEncrypted.getName().lastIndexOf('.');
        File fileDecrypted = new File(fileEncrypted.getParent() + "\\" + fileEncrypted.getName().substring(0,lastDotIndex) + "_Decrypted" + Long.toString(System.currentTimeMillis()) + ".txt");
        FileWriter writer;
        try {
            fileDecrypted.createNewFile();
            writer = new FileWriter(fileDecrypted);
            scanner = new Scanner(fileEncrypted);
            while (scanner.hasNext()) {
                writer.write(decryptMessage(scanner.nextLine(),key) + "\n");
            }
            writer.close();
            scanner.close();
            System.out.println("Finished - Decrypted file saved at " + fileDecrypted.getPath());
        } catch (IOException iox) {
            iox.printStackTrace();
            System.out.println(iox.getMessage());
            System.exit(1);
        }
    }

    private void encodePreStage(Scanner scanner) {
        int keyLength = 0;
        while (keyLength == 0) {
            System.out.println("Please enter a key length between 1 and 1000 inclusive or 'q' to quit");
            System.out.print("Length of key: ");
            String sResponse = scanner.nextLine();
            if (sResponse.equalsIgnoreCase("q")) {           //Check for if user wants to quit
                scanner.close();
                System.out.println("Exiting..");
                return;
            }
            int iResponse = Integer.parseInt(sResponse);
            if (!(iResponse >= 1 && iResponse <= 1000)) {                 //Check input bounds
                System.out.println("Input not correct!\nPossibly not within correct input bounds\nplease try again");
                continue;
            }
            keyLength = iResponse;
        }
        System.out.println("----------------------------------------");
        System.out.println("\t\t\tKEY - Please write down");
        System.out.println("----------------------------------------");
        String key = generateKey(keyLength);
        System.out.println("KEY: " + key);
        String filePath = null;
        do {
            System.out.println("Enter TEXT file full path (ext: txt): ");
            filePath = scanner.nextLine();
        } while (!checkFile(filePath));

        File filePlainText = new File(filePath);
        int indexOfDot = filePlainText.getName().lastIndexOf('.');
        File fileEncrypted = new File(filePlainText.getParent() + "\\" +  filePlainText.getName().substring(0,indexOfDot) + "_Encrypted" + Long.toString(System.currentTimeMillis()) + ".txt");
        FileWriter writer;
        try {
            fileEncrypted.createNewFile();
            writer = new FileWriter(fileEncrypted);
            scanner = new Scanner(filePlainText);
            while (scanner.hasNext()) {
                writer.write(encryptMessage(scanner.nextLine(),key) + "\n");
            }
            writer.close();
            scanner.close();
            System.out.println("Finished - Encrypted file saved at " + fileEncrypted.getPath());
            System.out.println("Your file is only recoverable with your key\nso make sure you have it safely stored somewhere");
        } catch (IOException iox) {
            System.out.println(iox.getMessage());
            iox.printStackTrace();
            System.out.println("Possible privilege error, could not create file");
            System.exit(1);
        }
    }

    private String decryptMessage(String message, String key) {
        message = message.toUpperCase();
        key = key.toUpperCase();
        String decryptedMessage = "";
        for (int i = 0; i < message.length(); i += key.length()) {
            for (int j = i; j < Math.min(i+key.length(),message.length()); j++) {
                int messageInt = message.charAt(j) - 65;
                int keyInt = key.charAt(j-i) - 65;
                if (messageInt < 0 || messageInt > 25) {            //Check for non-alphabetic characters and apply a space.
                    decryptedMessage = decryptedMessage + " ";
                }
                else {
                    decryptedMessage = decryptedMessage + (char) ((messageInt - keyInt + 26) % 26 + 65);
                }
            }
        }
        return decryptedMessage;
    }

    private String encryptMessage(String message, String key) {
        message = message.toUpperCase();
        String encryptedMessage = "";

        for (int i = 0; i < message.length(); i += key.length()) {
            for (int j = i; j < Math.min(i + key.length(),message.length()); j++) {

                int messageInt = message.charAt(j) - 65;
                int keyInt = key.charAt(j - i) - 65;
                if (messageInt < 0 || messageInt > 25) {            //Check for non-alphabetic characters and apply a space.
                    encryptedMessage = encryptedMessage + " ";
                } else {
                    encryptedMessage = encryptedMessage + (char) ((messageInt + keyInt) % 26 + 65);

                }
            }
        }
        return encryptedMessage;
    }

    private String generateKey(int length) {
        //java.util.Random class implements what is called a linear congruential generator
        //number [i+1] = (a * number [i] + c) mod m
        //Please do not rely on this software for advanced encryption
        Random rand = new Random();
        String key = "";
        for (int i = 0 ; i < length ; i++) {
            key = key + (char)(rand.nextInt(26) + 65);      //A-Z
        }
        return key;
    }

    public boolean checkFile(String filePath) {
        //Checks if text file exists
        //Checks if file is a text file with extention .txt
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        if (!file.exists()) {       //if files doesnt exist
            return false;
        }
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && fileName.length() > (dotIndex + 1)) {
            String fileExt = fileName.substring(dotIndex + 1);
            if (fileExt.equalsIgnoreCase("txt")) {
                return true;
            }
        }
        return false;
    }
}
