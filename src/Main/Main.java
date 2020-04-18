package Main;

import Decoding.DecodeFile;
import Encoding.EncodeFile;
import Implementations.Node;

import javax.swing.JFileChooser;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import static Decoding.DecodeFile.*;


public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        while (true) {

            Scanner scan = new Scanner(System.in);

            System.out.println("Please Select the mode of Execution:");
            System.out.println("COMPRESSION --------->Type (1)");
            System.out.println("DECOMPRESSION ------->Type (2)");
            System.out.println("EXIT ---------------->Type (3)");

            System.out.print("Choice is : ");
            int choice = scan.nextInt();

            if (choice == 1) {

                // ENCODING


                System.out.println("You chose Compression!");
                // JFileChooser to select the file to be compressed
                JFileChooser compChooser = new JFileChooser("g:");
                compChooser.showOpenDialog(null);
                File originalFile = compChooser.getSelectedFile();


                // Get starting time of execution
                long startTime = System.nanoTime();

                // A HashMap that contains the frequency of each character in the file
                HashMap<Integer, Integer> freqTable = EncodeFile.getFrequencies(originalFile);

                // Huffman Tree
                // Each Leaf is a character
                Node treeNode = EncodeFile.constructHuffmanTree(freqTable);

                // A HashMap that contains each character and its equivalent code
                HashMap<Integer, String> translationTable = new HashMap<>();
                EncodeFile.getCode(treeNode, "", translationTable);


                // Create a new file to contain the compressed data
                File compressedFile = new File("CompressedFile.txt");
                compressedFile.createNewFile();

                // Encode the previously selected file
                EncodeFile.generateCompressedFile(originalFile, translationTable,compressedFile);

                // Get finish time of execution
                long stopTime = System.nanoTime();
                double elapsedTime = (double)(stopTime - startTime)/1000000000;


                // Print each character with its original code and its new code
                ArrayList<Integer> charArray = new ArrayList<>();
                for (int key : translationTable.keySet()) {
                    charArray.add(key);
                }

                Collections.sort(charArray);

                System.out.println("************************************************CODING TABLE*************************************************************");
                System.out.format("\n%30s%30s%30s\n", "Character", "Original Code", "New Code");
                for (int key : charArray) {
                    System.out.format("%30d%30s%30s\n", key, Integer.toBinaryString(key), translationTable.get(key));
                }

                System.out.println("Execution time of compressing is: " + elapsedTime);
                System.out.println("Original File size is: " + EncodeFile.calculateFileSize(originalFile) + " bytes");
                System.out.println("Compressed File size is: " + EncodeFile.calculateFileSize(compressedFile) + " bytes");
                System.out.println("Compression ratio: " + EncodeFile.calculateFileSize(compressedFile)/(double)EncodeFile.calculateFileSize(originalFile));

                System.out.println("Compression Done!");
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();


            } else if (choice == 2) {

                // DECODING

                System.out.println("You chose Decompression!");
                // JFileChooser to select the file to be decompressed
                JFileChooser decChooser = new JFileChooser("g:");
                decChooser.showOpenDialog(null);
                File compressedFile = decChooser.getSelectedFile();

                // Get start time of execution
                long startTime = System.nanoTime();

                // HashMap that contains the translation table, uses a method that reads the header
                HashMap<Integer, String> headerMap = getDecodingTable(compressedFile);

                // Calculate the header size to skip it when decompressing the file
                int size = calculateHeaderSize(headerMap);

                // HashMap that contains the translation table reversed for use in decoding
                HashMap<String, Character> decodeMap = reverseTable(headerMap);

                // Decompress the compressed file
                generateDecompressedFile(compressedFile, decodeMap, size);

                // Get end time of execution
                long endTime = System.nanoTime();
                double elapsedTime = (double)(endTime - startTime)/1000000000;

                System.out.println("Execution time of decompression is: " + elapsedTime);
                System.out.println("Decompression Done!");
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();

            } else if (choice == 3) {
                System.exit(0);

            } else {
                System.out.println("PLEASE ENTER A VALID CHOICE!!");
                continue;
            }
        }
    }
}








