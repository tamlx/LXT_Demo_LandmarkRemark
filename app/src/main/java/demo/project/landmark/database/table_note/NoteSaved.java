package demo.project.landmark.database.table_note;

import b.laixuantam.myaarlibrary.database.orm.SugarRecord;
import demo.project.landmark.model.NotesModel;

public class NoteSaved extends SugarRecord {

    String title;
    String description;
    double lat;
    double lng;

    public NoteSaved() {
    }

    public NoteSaved(String title, String des, long lat, long lng) {
        this.title = title;
        this.description = des;
        this.lat = lat;
        this.lng = lng;
    }

    public NoteSaved(NotesModel model, double lat, double lng) {
        this.title = model.getTitle();
        this.description = model.getDescription();
        this.lat = lat;
        this.lng = lng;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
