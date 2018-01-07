package com.example.easy.login.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.example.easy.commons.model.IBaseDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//@XmlRootElement
//@ApiModel
public class LoginResponseDTO implements IBaseDTO {

	private String firstName;
	private String lastName;
	private String screenName;
	private String email;
	private String userUId;
	private String contactNumber;

	@ApiModelProperty(value = "Account Id in which user logged-in")
	private String accountUId;

	@ApiModelProperty(value = "Account Name in which user logged-in")
	private String accountName;

	@ApiModelProperty(value = "Account Type in which user logged-in")
	private String accountType;

	@ApiModelProperty(value = "Account Status in which user logged-in")
	private String accountStatus;

	@ApiModelProperty(value = "User Role in account")
	private Integer roleId;

	@ApiModelProperty(value = "Authentication Token")
	private String authToken;

	@ApiModelProperty(value = "Authentication Token expiry")
	private Long authTokenExpiry;

	@ApiModelProperty(value = "Refrsh-Auth Token for getting new Authentication token")
	private String refreshToken;

	boolean trial;
	Date planEndDate;
	int daysLeft;

	private Integer totalBookings;
	private Integer totalAmount;
	private Integer totalTax;
	private Integer baalanceAmount;

	private Integer monthTotalBookings;
	private Integer monthTotalAmount;
	private Integer monthTotalTax;
	private Integer monthBaalanceAmount;

	@Override
	public String toString() {
		return "LoginResponseDTO [firstName=" + firstName + ", lastName=" + lastName + ", screenName=" + screenName
				+ ", email=" + email + ", userUId=" + userUId + ", contactNumber=" + contactNumber + ", accountUId="
				+ accountUId + ", accountName=" + accountName + ", accountType=" + accountType + ", roleId=" + roleId
				+ ", authToken=" + authToken + ", authTokenExpiry=" + authTokenExpiry + ", refreshToken=" + refreshToken
				+ ", totalBookings=" + totalBookings + ", totalAmount=" + totalAmount + ", totalTax=" + totalTax
				+ ", baalanceAmount=" + baalanceAmount + ", monthTotalBookings=" + monthTotalBookings
				+ ", monthTotalAmount=" + monthTotalAmount + ", monthTotalTax=" + monthTotalTax
				+ ", monthBaalanceAmount=" + monthBaalanceAmount + "]";
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@ApiModelProperty(name = "firstName", value = "First Name of logged-in user")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@ApiModelProperty(value = "Last Name of logged-in user")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@ApiModelProperty(value = "Display Name of logged-in user")
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@ApiModelProperty(value = "Email of logged-in user")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ApiModelProperty(value = "User Id of logged-in user")
	public String getUserUId() {
		return userUId;
	}

	public void setUserUId(String userUId) {
		this.userUId = userUId;
	}

	@ApiModelProperty(value = "Contact Number of logged-in user")
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAccountUId() {
		return accountUId;
	}

	public void setAccountUId(String accountUId) {
		this.accountUId = accountUId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Long getAuthTokenExpiry() {
		return authTokenExpiry;
	}

	public void setAuthTokenExpiry(Long authTokenExpiry) {
		this.authTokenExpiry = authTokenExpiry;
	}

	public Integer getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(Integer totalBookings) {
		this.totalBookings = totalBookings;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Integer totalTax) {
		this.totalTax = totalTax;
	}

	public Integer getBaalanceAmount() {
		return baalanceAmount;
	}

	public void setBaalanceAmount(Integer baalanceAmount) {
		this.baalanceAmount = baalanceAmount;
	}

	public Integer getMonthTotalBookings() {
		return monthTotalBookings;
	}

	public void setMonthTotalBookings(Integer monthTotalBookings) {
		this.monthTotalBookings = monthTotalBookings;
	}

	public Integer getMonthTotalAmount() {
		return monthTotalAmount;
	}

	public void setMonthTotalAmount(Integer monthTotalAmount) {
		this.monthTotalAmount = monthTotalAmount;
	}

	public Integer getMonthTotalTax() {
		return monthTotalTax;
	}

	public void setMonthTotalTax(Integer monthTotalTax) {
		this.monthTotalTax = monthTotalTax;
	}

	public Integer getMonthBaalanceAmount() {
		return monthBaalanceAmount;
	}

	public void setMonthBaalanceAmount(Integer monthBaalanceAmount) {
		this.monthBaalanceAmount = monthBaalanceAmount;
	}

	public boolean isTrial() {
		return trial;
	}

	public void setTrial(boolean trial) {
		this.trial = trial;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public int getDaysLeft() {
		return daysLeft;
	}

	public void setDaysLeft(int daysLeft) {
		this.daysLeft = daysLeft;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

}
