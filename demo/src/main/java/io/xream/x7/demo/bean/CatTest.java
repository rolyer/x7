package io.xream.x7.demo.bean;

import x7.core.repository.X;

import java.sql.Timestamp;
import java.util.Date;

public class CatTest {

	@X.Key
	private long id;
	private long dogId;
	private String catFriendName;
	private Date time;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDogId() {
		return dogId;
	}
	public void setDogId(long dogId) {
		this.dogId = dogId;
	}
	public String getCatFriendName() {
		return catFriendName;
	}
	public void setCatFriendName(String catFriendName) {
		this.catFriendName = catFriendName;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "CatTest [id=" + id + ", dogId=" + dogId + ", catFriendName=" + catFriendName + ", time=" + time + "]";
	}

}
