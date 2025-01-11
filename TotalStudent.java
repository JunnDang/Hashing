package Hashing;
import java.io.*;
import java.util.*;
/**
 * @author Chuong Dang
 * 
 * This method reads in file, create student objects, and stores the assignments and its scores.
 * This class will then output the scores to two seperate files. One is the summary, the other one provide
 * details for students' grades.
 */

public class TotalStudent {
    private ChainHashing<String,ArrayList<String>> assignmentName; // List of assignments in numerical order.
    private ChainHashing<String, ChainHashing<String, Integer>> totalScore; // List of assigment and its total score
    private ChainHashing<Integer,Student> population; //Hash table of students with their ID
    

    /**
     * Constructor
     */
    public TotalStudent(){
        assignmentName = new ChainHashing<>(new HashCodeString());
        totalScore = new ChainHashing<>(new HashCodeString());
        population = new ChainHashing<>( new HashCodeInteger());
    }

    /**
     * This method add new assignements to the assignment list.
     * @param catagory is the catagory of assignments.
     * @param assignment is the name of the assignment.
     * @param grade is the grade of the assignment.
     * @param totalScore is the hash table with store the assignment as key, and the total grade as value.
     */
    public void addAssignment(String catagory, String assignment, int grade, ChainHashing<String, ChainHashing<String, Integer>> totalScore){
        ChainHashing<String, Integer> listOfAssignment = totalScore.getValue(catagory);
        if(listOfAssignment == null){
            listOfAssignment = new ChainHashing<>(new HashCodeString());
            totalScore.add(catagory, listOfAssignment);
        }
        listOfAssignment.add(assignment, grade);
    }


    /**
     * 
     * @param ID of student
     * @param name is name of student
     * @return if student is already in the system, return the the reference to the student.
     * Otherwise, create a new strudent object and return its reference.
     */
    private HashItem<Integer,Student> isItemExist(int ID, String name){
        HashItem<Integer,Student> item = population.getItem(ID);
        if(item != null){
            return item;
        }

        return new HashItem<Integer,Student>(ID, new Student(name));
    }

    /**
     * This method load up the assigment scores from the file .
     * @param file the name of the file.
     * @param category is the catagory of the assignments of that file.
     */
    public void load(String file, String category){
        BufferedReader reader = null;
        String line = "";
        try {  
            
            String line2 ="";
            reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            line2 = reader.readLine();
            String [] row = line.split(",");
            String[] row2 = line2.split(",");
            ArrayList<String> assigment = new ArrayList<>();
            for(int i = 2; i < row.length;i++){
                addAssignment(category, row[i], Integer.parseInt(row2[i]), totalScore);
                assigment.add(row[i]);
                
            }
            assignmentName.add(category,assigment);
            
            while ((line = reader.readLine() )!= null){
                row2 = line.split(",");
                String name = row2[1]+ " " + row2[2];
                name = name.substring(1,name.length() -1);
                HashItem<Integer, Student> item = isItemExist(Integer.parseInt(row2[0]), name);
                
                for(int i = 2; i < row.length;i++){
                    item.value.addAssignment(category,row[i], Integer.parseInt(row2[i+1]));
                }
                population.add(item);
            }    
            reader.close();
        } catch (Exception e){
        } finally{
        }
    }

    /**
     * This method print the information of the student with their assignments and score.
     * @param studentName is the student oject.
     */
    public void print(Student studentName){
        for(LinkedList<HashItem<String, ArrayList<String>>> list : assignmentName.getMap()){
            for(HashItem<String, ArrayList<String>> item :list){
                for(String assignment : item.value){
                    System.out.print(assignment+": "+studentName.getGrade(assignment,item.key) + "  ");
                }
                System.out.println();
            }
        }
    }

    /**
     * This method print the score of the entire number of students
     */

    public void print(){
        for(LinkedList<HashItem<Integer, Student>> list : population.getMap()){
            for(HashItem<Integer, Student> student : list){
                System.out.println(student.key + ": " + student.value.name);
                Student studentName  = student.value;
                print( studentName);
            }
        }
    }

    /**
     * This method return the catagory score of student.
     * @param catagory is the catagory of assignments
     * @param ID of student
     * @return the student's percent score that catagory.
     */
    public Float getCatagoryPercent(String catagory, int ID){
        Student student = population.getValue(ID);
        if(student == null){return null;}
        ChainHashing<String, Integer> listOfAssigment = totalScore.getValue(catagory);
        if(listOfAssigment==null){return null;}
        int cumulativeScore = student.getGrade("Total", catagory);
        return (float)(cumulativeScore) / (float)totalScore.getValue(catagory).getValue("Total") * (float)100;
    }

    /**
     * 
     * @param ID of student
     * @return the cumulative score of the student.
     */
    public Float getCumulativePercent(int ID){
        Student student = population.getValue(ID);
        if(student == null){return null;}
        int totalPossiblePoints = 0;
        int cumulativePoints = 0;
        for(LinkedList<HashItem<String,ChainHashing<String,Integer>>> catagory : totalScore.getMap()){
            for(HashItem<String,ChainHashing<String,Integer>> item: catagory ){
                ChainHashing<String,Integer> assignment = item.value;
                totalPossiblePoints+=assignment.getValue("Total");
                cumulativePoints+=student.getGrade("Total", item.key);
            }
        }
        return (float)(cumulativePoints) / (float)(totalPossiblePoints ) * (float)100;
    }

    /**
     * 
     * @return a string that summarize the grades of student
     */
    public String outputSummay(){
        String summary = "";
        String line2 = "";
        summary +="\"#ID" +"\",\"" + "Name" +"\",\"" + "Final Grade" ;
        
        line2 += "\"" +"\",\"" + "Overall" +"\",\"" ;


        for(LinkedList<HashItem<String,ChainHashing<String,Integer>>> catagory : totalScore.getMap()){
            for(HashItem<String,ChainHashing<String,Integer>> item: catagory ){
                ChainHashing<String,Integer> assignment = item.value;
                summary+= "\",\"" + item.key;
                line2+= "\",\"" + assignment.getValue("Total").toString();
            }
            
        }
        summary+= "\"" + "\n"+ line2 +"\"" + "\n";
        for(LinkedList<HashItem<Integer, Student>> list : population.getMap()){
            for (HashItem<Integer, Student> student : list){
                String name = student.value.name;
                String[] token = name.split("  ");
                name = token[0] +", " + token[1];
                summary += "\"" + student.key +"\",\"" + name+"\",\"" + String.format("%.6f",getCumulativePercent(student.key));
                for(LinkedList<HashItem<String,ChainHashing<String,Integer>>> catagory : totalScore.getMap()){
                    for(HashItem<String,ChainHashing<String,Integer>> item: catagory ){
                        summary+= "\",\"" + String.format("%.6f", (float)student.value.getGrade("Total", item.key));
                    }
                    
                }
                summary+="\"\n";
            }
        }
        return summary;
    }

    /**
     * 
     * @return a summary with details of student's scores for each assignment.
     */
    public String outputDetail(){
        String summary = "";
        summary +="\"#ID" +"\",\"" + "Name";
        String overall = "\"" +"\",\"" + "Overall";

        for(LinkedList<HashItem<String,ArrayList<String>>> listOfAssigment: assignmentName.getMap()){
            for(HashItem<String ,ArrayList<String>> catagory: listOfAssigment){
                for(String assignment : catagory.value){
                    if(assignment.compareTo("Total")!=0){
                        ChainHashing<String, Integer> getAssignmentScore = totalScore.getValue(catagory.key);
                        summary+= "\",\"" + assignment;
                        overall += "\",\""+ getAssignmentScore.getValue(assignment);
                    }
                }
            }
        }
        summary+="\"\n" + overall+"\"\n";
        for(LinkedList<HashItem<Integer, Student>> list : population.getMap()){
            for (HashItem<Integer, Student> student : list){
                String name = student.value.name;
                String[] token = name.split("  ");
                name = token[0] +", " + token[1];
                summary += "\"" + student.key +"\",\"" + name;
                for(LinkedList<HashItem<String,ArrayList<String>>> listOfAssigment: assignmentName.getMap()){
                    for(HashItem<String ,ArrayList<String>> catagory: listOfAssigment){
                        for(String assignment : catagory.value){
                            if(assignment.compareTo("Total")!=0){
                                summary+="\",\"" + String.format("%.6f", (float)student.value.getGrade(assignment, catagory.key));
                            }
                        }
                    }
                    
                }
                summary+="\"\n";
            }
        }
        return summary;
    }

    /**
     * This method output the scores to two files.
     * One is a summary.
     * The other one provide details with score of eacxh assignment.
     */
    public void outputData(){
        try {  
            BufferedWriter  writer = new BufferedWriter(new FileWriter("Output-summary.csv"));
            String summary = outputSummay();
            writer.write(summary);
            writer.close();
            writer = new BufferedWriter(new FileWriter("Output-details.csv"));
            String detail = outputDetail();
            writer.write(detail);
           writer.close();
        } catch (Exception e){
        } finally{
        }
    }
}
