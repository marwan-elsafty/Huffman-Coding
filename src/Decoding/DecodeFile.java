package Decoding;

import java.io.*;
import java.util.HashMap;

public class DecodeFile {

    // Method reads the translation table from the header of the compressed file

    public static HashMap<Integer, String> getDecodingTable(File compressedFile) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(compressedFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object newObject = ois.readObject();
        ois.close();
        fis.close();

        HashMap<Integer, String> headerMap = (HashMap<Integer, String>) newObject;
        return headerMap;
    }


    // Method reverses a given table's keys with its values

    public static HashMap<String, Character> reverseTable(HashMap<Integer, String> headerMap) {

        HashMap<String, Character> decodeMap = new HashMap<>();
        for (int key : headerMap.keySet()) {
            decodeMap.put(headerMap.get(key), (char) key);
        }
        return decodeMap;

    }


    // Method calculates the size of the header in bytes to skip it
    // when decompressing the files by rewriting the object to a Byte Array
    // Output Stream and getting its size

    public static int calculateHeaderSize(HashMap<Integer, String> headerMap) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(headerMap);
        oos.flush();
        oos.close();
        int size = baos.toByteArray().length;
        baos.close();

        return size;
    }


    // Method decompresses a given file and creates a new file with the original data
    //  by reading the bits in a byte per byte way to decode the values of those bits

    public static void generateDecompressedFile(File compressedFile, HashMap<String, Character> decodeMap, int size) throws IOException {

        // Create a new file to contain the decoded data (original data)
        File decompressedFile = new File("DecompressedFile.txt");
        FileInputStream reader = new FileInputStream(compressedFile);

        // StringBuilder to build the decoded string
        StringBuilder decodedString = new StringBuilder();

        // Skip the header
        reader.skip(size);

        // Read all encoded data in a bytes array
        byte[] data = reader.readAllBytes();
        FileWriter writer = new FileWriter(decompressedFile);

        byte current_bit = 0;
        StringBuilder code = new StringBuilder();

        // Loop over each byte and extract bit by bit using bitwise operations
        for (byte current_byte : data) {

            for (int i = 0; i < 8; i++) {

                // get the first bit of the byte
                current_bit = (byte) (current_byte & -128);

                // shift left the read byte to get the next bit
                current_byte <<= 1;

                // if the read bit is 1 append '1' to the code
                // if the read bit is 0 append '0' to the code
                if (current_bit == -128) {
                    code.append("1");
                } else {
                    code.append("0");
                }

                // check if the code exists in the translation table
                if (decodeMap.containsKey(code.toString())) {
                    decodedString.append(decodeMap.get(code.toString()).toString());
                    code = new StringBuilder();
                }
            }
        }

        // Write the decoded String to file
        writer.write(decodedString.toString());
        writer.flush();
        writer.close();
    }



}



