package knowledgeswap2.model;

public class Student extends User implements Displayable {
    private static final long serialVersionUID = 1L;

    private String majorCode; // BIT, BCSSE, ...

    public Student(int id, String name, String majorCode) {
        super(id, name);
        setMajorCode(majorCode);
    }

    public String getMajorCode() {
        return majorCode == null ? "" : majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode == null ? "" : majorCode.trim();
    }

    @Override
    public String toDisplayString() {
        String code = getMajorCode().trim();
        if (code.isEmpty()) return getId() + " - " + getName();
        return getId() + " - " + getName() + " (" + code + ")";
    }

    @Override
    public String toString() {
        return toDisplayString();
    }
}

