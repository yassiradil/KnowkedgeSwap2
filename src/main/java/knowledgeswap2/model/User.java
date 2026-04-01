package knowledgeswap2.model; //File belongs to the model package (folder).

import java.io.Serializable; //save and load objects.

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
//creates "User" class. / public =others can use this class. (like students) /abstract = cant create objects

    //which, who user//
    private final int id;
    private String name;

    public User(int id, String name) {//constructor: create object --> 1st values.
        this.id = id; //take parameter from out n save inside this.obj
        this.name = name == null ? "" : name.trim(); //check short if/ T:same F:cut spaces
    }

    public int getId() { //getterMth:read id value.
        return id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) { //SetterMth:update and update name (new value)
        this.name = name == null ? "" : name.trim();
    }
}
//Inheritance student from user
