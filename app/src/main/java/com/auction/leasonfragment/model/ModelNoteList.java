package com.auction.leasonfragment.model;

public class ModelNoteList {

    private String id;
    private String name;
    private String dateNote;
    private String text;

    public ModelNoteList(String id, String name, String dateNote, String text) {
        this.id = id;
        this.name = name;
        this.dateNote = dateNote;
        this.text = text;
    }

    public ModelNoteList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateNote() {
        return dateNote;
    }

    public void setDateNote(String dateNote) {
        this.dateNote = dateNote;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
