package sk.miroc.whitebikes.data.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StandBikes {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("content")
    @Expose
    private List<String> content = null;
    @SerializedName("notes")
    @Expose
    private List<String> notes = null;
    @SerializedName("stacktopbike")
    @Expose
    private Boolean stacktopbike;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public Boolean getStacktopbike() {
        return stacktopbike;
    }

    public void setStacktopbike(Boolean stacktopbike) {
        this.stacktopbike = stacktopbike;
    }

    @Override
    public String toString() {
        return "StandBikes{" +
                "error=" + error +
                ", content=" + content +
                ", notes=" + notes +
                ", stacktopbike=" + stacktopbike +
                '}';
    }
}
