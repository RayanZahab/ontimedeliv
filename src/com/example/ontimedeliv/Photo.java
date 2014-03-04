package com.example.ontimedeliv;

public class Photo {
	private int id;
	private String url, thumb;
	Photo(int id,String url,String thumb)
	{
		this.setId(id);
		this.url=url;
		this.thumb=thumb;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
