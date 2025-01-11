package Hashing;
/**
 * @arthor Chuong Dang
 * This class repressent a student.
 * It's key is the student's ID
 * The value is the reeference to another hash table.
 * The second hash table with its key being names of assignment and the grades being the points.
 */
public class Student{

    //Name of student.
    String name;

    private ChainHashing<String, ChainHashing<String, Integer>> map;


    /**
     * Constructor
     * @param name is the name of the student.
     */
    Student(String name){
        this.name = name;
        map = new ChainHashing<>(new HashCodeString());
    }

    /**
     * This method adds the assigment and its grade as a HashItem and stores it within the table.
     * @param catagory is the catagory of the assignments
     * @param assignment is the name of the assigment.
     * @param grade the grade of the assignment.
     */
    public void addAssignment(String catagory, String assignment, int grade){
        ChainHashing<String, Integer> listOfAssignment = map.getValue(catagory);
        if(listOfAssignment == null){
            listOfAssignment = new ChainHashing<>(new HashCodeString());
            map.add(catagory, listOfAssignment);
        }
        listOfAssignment.add(assignment, grade);
    }

    /**
     * 
     * @return the hash table of students.
     */
    public ChainHashing<String, ChainHashing<String, Integer>> getMap(){
        return map;
    }


    /**
     * This method searches and return the grade of the assignment.
     * @param assignment is the name of the assigment.
     * @param catagory is the catagory of the assignment.
     * @return the grade of the assignment.
     */
    public Integer getGrade(String assignment, String catagory){
        ChainHashing<String, Integer> listOfAssignment = map.getValue(catagory);
        if(listOfAssignment ==null){
            return null;
        }
        if(listOfAssignment.contain(assignment)){
            return listOfAssignment.getValue(assignment);
        }
        return null;
    }


    /**
     * 
     * @param catagory is the catagory of the assignment to be removed.
     * @param assignment is the name of the assigment to be removed.
     */
    public void remove(String catagory, String assignment){
        ChainHashing<String, Integer> listOfAssignment = map.getValue(catagory);
        if(listOfAssignment == null){
            return;
        }
        listOfAssignment.remove(assignment);
    }

}