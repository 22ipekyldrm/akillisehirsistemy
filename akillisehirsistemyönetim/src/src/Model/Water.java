// src/model/Water.java
package src.Model;

public class Water {
    private int id;
    private String location;
    private double level; // Su seviyesi, metre cinsinden
    private String date; // YYYY-MM-DD formatında tarih

    public Water(int id, String location, double level, String date) {
        this.id = id;
        this.location = location;
        this.level = level;
        this.date = date;
    }

    // Getter ve Setter Metodları
    public int getId() { return id; }
    public String getLocation() { 
    	return location; }
    public double getLevel() { 
    	return level; }
    public String getDate() { 
    	return date; }

    public void setId(int id) { 
    	this.id = id; }
    public void setLocation(String location) { 
    	this.location = location; }
    public void setLevel(double level) { 
    	this.level = level; }
    public void setDate(String date) { 
    	this.date = date; }
}
