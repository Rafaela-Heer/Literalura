package com.project.literalura.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexResponse {
    private int count;
    private String next;
    private String previous;
    private List<GutenBook> results;

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getNext() { return next; }
    public void setNext(String next) { this.next = next; }

    public String getPrevious() { return previous; }
    public void setPrevious(String previous) { this.previous = previous; }

    public List<GutenBook> getResults() { return results; }
    public void setResults(List<GutenBook> results) { this.results = results; }

    @Override
    public String toString() {
        return "count=" + count + ", results=" + (results != null ? results.size() : 0);
    }
}
