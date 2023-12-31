package mz.org.csaude.mentoring.adapter.recyclerview.listable;

public interface Listble {

    int getId();

    default int getListPosition() {
        return 0;
    }

    String getDescription();

    void setListPosition(int listPosition);

    int getDrawable();

    String getCode();
}
