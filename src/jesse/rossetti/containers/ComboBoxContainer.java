package jesse.rossetti.containers;

public class ComboBoxContainer {

    public ComboBoxContainer(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int id;
    public String title;

    @Override
    public String toString() {
        return id + ", " + title;
    }


}
