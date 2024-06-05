package ir.FiveMFive.FiveMFive.Java;

public class CategoryItem {
    private String title;
    private int position;
    public CategoryItem(String title, int position) {
        this.title = title;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
