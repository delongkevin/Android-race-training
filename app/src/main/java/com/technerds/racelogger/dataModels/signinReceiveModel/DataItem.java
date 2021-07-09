package com.technerds.racelogger.dataModels.signinReceiveModel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("country")
	private String country;

	@SerializedName("gender")
	private String gender;

	@SerializedName("city")
	private String city;

	@SerializedName("login_type")
	private String loginType;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("zip_code")
	private String zipCode;

	@SerializedName("ft")
	private double ft;

	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("role_id")
	private String roleId;

	@SerializedName("fcm_token")
	private String fcmToken;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("height")
	private double height;

	@SerializedName("address")
	private String address;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("weight")
	private double weight;

	@SerializedName("email_verified_at")
	private String emailVerifiedAt;

	@SerializedName("cm")
	private double cm;

	@SerializedName("forget_token")
	private String forgetToken;

	@SerializedName("time_zone")
	private String timeZone;

	@SerializedName("url")
	private String url;

	@SerializedName("token")
	private String token;

	@SerializedName("social_token")
	private Object socialToken;

	@SerializedName("weight_unit")
	private String weightUnit;

	@SerializedName("dob")
	private String dob;

	@SerializedName("inch")
	private double inch;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLoginType(String loginType){
		this.loginType = loginType;
	}

	public String getLoginType(){
		return loginType;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public String getZipCode(){
		return zipCode;
	}

	public void setFt(double ft){
		this.ft = ft;
	}

	public double getFt(){
		return ft;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleId(){
		return roleId;
	}

	public void setFcmToken(String fcmToken){
		this.fcmToken = fcmToken;
	}

	public String getFcmToken(){
		return fcmToken;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
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

	public void setHeight(double height){
		this.height = height;
	}

	public double getHeight(){
		return height;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setWeight(double weight){
		this.weight = weight;
	}

	public double getWeight(){
		return weight;
	}

	public void setEmailVerifiedAt(String emailVerifiedAt){
		this.emailVerifiedAt = emailVerifiedAt;
	}

	public String getEmailVerifiedAt(){
		return emailVerifiedAt;
	}

	public void setCm(double cm){
		this.cm = cm;
	}

	public double getCm(){
		return cm;
	}

	public void setForgetToken(String forgetToken){
		this.forgetToken = forgetToken;
	}

	public String getForgetToken(){
		return forgetToken;
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

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setSocialToken(Object socialToken){
		this.socialToken = socialToken;
	}

	public Object getSocialToken(){
		return socialToken;
	}

	public void setWeightUnit(String weightUnit){
		this.weightUnit = weightUnit;
	}

	public String getWeightUnit(){
		return weightUnit;
	}

	public void setDob(String dob){
		this.dob = dob;
	}

	public String getDob(){
		return dob;
	}

	public void setInch(double inch){
		this.inch = inch;
	}

	public double getInch(){
		return inch;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"country = '" + country + '\'' + 
			",gender = '" + gender + '\'' + 
			",city = '" + city + '\'' + 
			",login_type = '" + loginType + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",ft = '" + ft + '\'' + 
			",profile_image = '" + profileImage + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",role_id = '" + roleId + '\'' + 
			",fcm_token = '" + fcmToken + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",height = '" + height + '\'' + 
			",address = '" + address + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",weight = '" + weight + '\'' + 
			",email_verified_at = '" + emailVerifiedAt + '\'' + 
			",cm = '" + cm + '\'' + 
			",forget_token = '" + forgetToken + '\'' + 
			",time_zone = '" + timeZone + '\'' + 
			",url = '" + url + '\'' + 
			",token = '" + token + '\'' + 
			",social_token = '" + socialToken + '\'' + 
			",weight_unit = '" + weightUnit + '\'' + 
			",dob = '" + dob + '\'' + 
			",inch = '" + inch + '\'' + 
			"}";
		}
}