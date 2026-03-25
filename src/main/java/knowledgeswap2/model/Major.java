package knowledgeswap2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Major implements Serializable, Displayable {
    private static final long serialVersionUID = 1L;

    private final String fullName;
    private final String code;

    public Major(String fullName, String code) {
        this.fullName = fullName == null ? "" : fullName.trim();
        this.code = code == null ? "" : code.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toDisplayString() {
        return fullName + " \u2192 " + code;
    }

    @Override
    public String toString() {
        return toDisplayString();
    }

    public static List<Major> allMajors() {
        ArrayList<Major> list = new ArrayList<>();
        list.add(new Major("Bachelor of Information Technology", "BIT"));
        list.add(new Major("Bachelor in Software Engineering", "BCSSE"));
        list.add(new Major("Bachelor of Accountancy", "BAC"));
        list.add(new Major("Bachelor of Business Administration", "BBA"));
        list.add(new Major("Bachelor of Civil Engineering", "BCE"));
        list.add(new Major("Bachelor of Mechanical Engineering", "BME"));
        list.add(new Major("Bachelor of Nursing", "BN"));
        list.add(new Major("Bachelor in Graphic Design", "BGD"));
        return Collections.unmodifiableList(list);
    }
}

