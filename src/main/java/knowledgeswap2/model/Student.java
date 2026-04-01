package knowledgeswap2.model;

public class Student extends User implements Displayable {
    private static final long serialVersionUID = 1L; //save/L

    private String majorCode; // BIT, BCSSE, ...variables for students only

    public Student(int id, String name, String majorCode) { //when we create, students must give it
        super(id, name); //send it to parent
        setMajorCode(majorCode);
    }

    public String getMajorCode() {//method returns empty to avoid null
        return majorCode == null ? "" : majorCode;
    }
    public void setMajorCode(String majorCode) { //void = method doesn't return just for show
        this.majorCode = majorCode == null ? "" : majorCode.trim(); // short if major = null "" else trim spaces
    }

    @Override //  custom method for Student from Displayable
    public String toDisplayString() { //no void mean it will return string "student information"
        String code = getMajorCode().trim(); //example: BIT
        if (code.isEmpty()) return getId() + " - " + getName(); //if major Empty show id and name only
        return getId() + " - " + getName() + " (" + code + ")"; //else return them all
    }

    @Override // rewrite method
    public String toString() { //toString (knowable method to java) = return text ...
        return toDisplayString(); //from here toDisplayString
    }//method = method (both strings)
}

