package com.lgvalle.photosnearby.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lgvalle on 21/07/14.
 */
public class Photo500pxUser {

	@Expose
	private Integer id;
	@Expose
	private String username;
	@Expose
	private String firstname;
	@Expose
	private String lastname;
	@Expose
	private String city;
	@Expose
	private String country;
	@Expose
	private Integer usertype;
	@Expose
	private String fullname;
	@SerializedName("userpic_url")
	@Expose
	private String userpicUrl;
	@SerializedName("userpic_https_url")
	@Expose
	private String userpicHttpsUrl;
	@SerializedName("upgrade_status")
	@Expose
	private Integer upgradeStatus;
	@Expose
	private Integer affection;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getUsertype() {
		return usertype;
	}

	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getUserpicUrl() {
		return userpicUrl;
	}

	public void setUserpicUrl(String userpicUrl) {
		this.userpicUrl = userpicUrl;
	}

	public String getUserpicHttpsUrl() {
		return userpicHttpsUrl;
	}

	public void setUserpicHttpsUrl(String userpicHttpsUrl) {
		this.userpicHttpsUrl = userpicHttpsUrl;
	}

	public Integer getUpgradeStatus() {
		return upgradeStatus;
	}

	public void setUpgradeStatus(Integer upgradeStatus) {
		this.upgradeStatus = upgradeStatus;
	}

	public Integer getAffection() {
		return affection;
	}

	public void setAffection(Integer affection) {
		this.affection = affection;
	}
}
