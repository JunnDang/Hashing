package Hashing;
/*
 * @author Chuong Dang
 * 
 * This is the main file that takes in an array of strings of file names.
 * This file will then read the contents of the file and output the result to two different files.
 * One is a summary of students' scores.
 * The other one is providing the detail of their scores.
 */

public class PA2Main {
    
    public static void main(String args[]){
        TotalStudent school = new TotalStudent();
    
        for(String file : args){
            String[] token = file.split("_");
            String catagory = token[0];
            school.load(file, catagory);
        }
        school.outputData();
    }
	
}


