package com.technerds.racelogger.dataModels.signupModel;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("country")
	private String country;

	@SerializedName("address")
	private String address;

	@SerializedName("gender")
	private String gender;

	@SerializedName("login_type")
	private String loginType;

	@SerializedName("city")
	private String city;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("weight")
	private String weight;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("time_zone")
	private String timeZone;

	@SerializedName("url")
	private String url;

	@SerializedName("zip_code")
	private String zipCode;

	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("weight_unit")
	private String weightUnit;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("start_running")
	private String startRunning;

	@SerializedName("dob")
	private String dob;

	@SerializedName("fcm_token")
	private String fcmToken;

	@SerializedName("state")
	private String state;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("longitude")
	private String longitude;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setLoginType(String loginType){
		this.loginType = loginType;
	}

	public String getLoginType(){
		return loginType;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setWeight(String weight){
		this.weight = weight;
	}

	public String getWeight(){
		return weight;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTimeZone(String timeZone){
		this.timeZone = timeZone;
	}

	public String getTimeZone(){
		return timeZone;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public String getZipCode(){
		return zipCode;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setWeightUnit(String weightUnit){
		this.weightUnit = weightUnit;
	}

	public String getWeightUnit(){
		return weightUnit;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setStartRunning(String startRunning){
		this.startRunning = startRunning;
	}

	public String getStartRunning(){
		return startRunning;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setFcmToken(String fcmToken){
		this.fcmToken = fcmToken;
	}

	public String getFcmToken(){
		return fcmToken;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"country = '" + country + '\'' + 
			",address = '" + address + '\'' + 
			",gender = '" + gender + '\'' + 
			",login_type = '" + loginType + '\'' + 
			",city = '" + city + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",weight = '" + weight + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",time_zone = '" + timeZone + '\'' + 
			",url = '" + url + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",profile_image = '" + profileImage + '\'' + 
			",weight_unit = '" + weightUnit + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",start_running = '" + startRunning + '\'' + 
			",dob = '" + dob + '\'' + 
			",fcm_token = '" + fcmToken + '\'' + 
			",state = '" + state + '\'' + 
			",id = '" + id + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}