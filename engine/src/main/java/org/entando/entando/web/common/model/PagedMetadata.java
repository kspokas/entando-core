package org.entando.entando.web.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PagedMetadata<T> {

	private int page;
	private int size;
	private int last;

    @JsonIgnore
	private List<T> body;

    public PagedMetadata() {
		//
	}

    public PagedMetadata(int page, int size, int last) {
		this.page = page;
		this.size = size;
		this.last = last;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

    public List<T> getBody() {
        return body;
    }

    public void setBody(List<T> body) {
        this.body = body;
    }

}
