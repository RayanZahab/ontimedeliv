package com.mobilife.delivery.admin.model;

import java.io.File;
import java.io.FileInputStream;

import com.mobilife.delivery.admin.ValidationError;

import android.net.Uri;

public class Photo {
	private int id;
	private String url, thumb, name;

	public Photo(int id, String url, String thumb) {
		this.setId(id);
		this.url = url;
		this.thumb = thumb;
	}

	public Photo(String url, String name) {
		this.url = url;
		this.name = name;
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

	public String getName() {
		Uri u = Uri.parse(this.name);

		File f = new File("" + u);

		return f.getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public ValidationError validate() {
		boolean valid = false;
		String msg = null;
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(url);
			int bytesAvailable = fileInputStream.available();
			if (bytesAvailable <= (200 * 8 * 1024)) {
				valid = true;

			} else
				msg = "Image too big";
			fileInputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ValidationError(valid, msg);
	}

}
