package org.entando.entando.web.common.model;

public class PageMetadata {

	private int page;
	private int size;
	private int last;

	public PageMetadata() {
		//
	}

	public PageMetadata(int page, int size, int last) {
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

}
