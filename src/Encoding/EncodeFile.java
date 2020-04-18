package Encoding;

import Implementations.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class EncodeFile {


    // Method returns a HashMap with each character and its frequency

    public static HashMap<Integer, Integer> getFrequencies(File file) throws IOException {
        HashMap<Integer, Integer> hash = new HashMap<>();

        FileReader reader = new FileReader(file);
        int current_char;
        while ((current_char = reader.read()) != -1) {
            int value = hash.getOrDefault((int) current_char, 0);
            hash.put((int) current_char, value + 1);
        }
        return hash;
    }


    // Method constructs Huffman tree using a priority queue (Min Heap).
    // It returns the root of the tree

    public static Node constructHuffmanTree(HashMap<Integer, Integer> hash) {

        // Construct a min heap of nodes
        PriorityQueue<Node> minHeap = new PriorityQueue<Node>();

        // Add all the characters and their frequencies to the min heap
        for (int key : hash.keySet()) {
            minHeap.add(new Node(key, hash.get(key)));
        }


        // Construct the Huffman tree
        while (minHeap.size() > 1) {

            // Pop the top 2 nodes in the min heap
            Node firstNode = minHeap.poll();
            Node secondNode = minHeap.poll();

            // Construct a new node with the sum of frequencies of the recently popped nodes
            Node newNode = new Node(firstNode.getFrequency() + secondNode.getFrequency());

            // Construct the sub-tree of the recently constructed node
            newNode.setLeft(firstNode);
            newNode.setRight(secondNode);

            // Add the node to the min heap
            minHeap.add(newNode);
        }

        // Return the root
        return minHeap.poll();
    }


    // Method gets the root of huffman tree and an empty HashMap
    // And fills the map with the characters and their huffman code
    // Using DFS traversal

    public static void getCode(Node root, String str, HashMap<Integer, String> map) {

        // if leaf; insert the character and its code to the HashMap
        if (root.getRight() == null && root.getLeft() == null) {
            map.put(root.getCharacter(), str);
            return;
        }

        // Call the function recursively to get the code
        getCode(root.getLeft(), str + "0", map);
        getCode(root.getRight(), str + "1", map);
    }


    // Method compresses the file by replacing each character with its equivalent code
    // each code is written in a byte or more by using bitwise operations

    public static void generateCompressedFile(File inFile, HashMap<Integer, String> translationTable, File outFile) throws IOException {



        // Read the data of the file to be compressed in a bytes array
        byte[] inputData = Files.readAllBytes(inFile.toPath());
        // Created a string from bytes array
        String content = new String(inputData, StandardCharsets.UTF_8);
        // An ArrayList to contain the compressed data to be written to file
        ArrayList<Byte> outData = new ArrayList<Byte>();

        byte current_byte = 0;
        int count = 0;

        for (char current_char : content.toCharArray()) {

            // Get the code of the current character
            String code = translationTable.get((int) current_char);

            // Loop on every character in the code (either '1' or '0')
            for (char c : code.toCharArray()) {
                // if the character is '1' OR a 1 to the current byte
                // if the character is '0' OR a 0 to the current byte
                if (c == '1') {
                    current_byte |= 1;
                } else {
                    current_byte |= 0;
                }
                count++;

                // When the byte is fully written, add it to the byte array
                // and reset the counter and empty the current_byte to be reused
                if (count == 8) {
                    outData.add(current_byte);
                    current_byte = 0;
                    count = 0;
                }

                // Shift left to empty a bit for the next bit to be added
                current_byte <<= 1;
            }
        }

        // An array of bytes to be written to file
        byte[] writeData = new byte[outData.size()];
        for (int i = 0; i < outData.size(); i++) {
            writeData[i] = outData.get(i);
        }

        // Writing to file
        FileOutputStream fos = new FileOutputStream(outFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        // Write the header first that contains the translation table
        // using the writeObject method that writes serializable objects directly
        oos.writeObject(translationTable);
        oos.flush();

        // Write the compressed file after the header
        fos.write(writeData);
        fos.flush();
        oos.close();
        fos.close();
    }



    public static int calculateFileSize(File originalFile) throws IOException {

         FileInputStream reader =  new FileInputStream(originalFile);
         int size = reader.readAllBytes().length;
         reader.close();

        return size;
    }

}
