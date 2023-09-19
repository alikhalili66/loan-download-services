package ir.khalili.products.nas.core.helper.external.services.kyc.customerExistenceInquiryMsg;

import java.io.Serializable;

/**
 * Created by gheibi on 26/09/2017.
 */
public class ChCustomerExistenceInquiryResponseDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4249081540619303904L;
	//share
    private String customerNo;
    private String name;
    private String nationalNo;
    private String nationality;
    private String foreignName;
    private String foreignerNo;
    private String email;
    private String mobileNo;
    private String country;
    private String province;
    private String city;
    private String cityName;
    private String postalCode;
    private String phoneNo;
    private String address;
    //individual
    private String family;
    private String fatherName;
    private String referralDate;
    private String identificationNo;
    private String birthDate;
    private String identificationCharacter;
    private String identificationLongSerial;
    private String identificationShortSerial;
    private String birthLocation;
    private String identificationIssuingLocationCode;
    private String authenticationDocuments;
    private String gender;
    private String passportNo;
    private String passportExpireDate;
    private String maritalStatus;
    private String healthStatus;
    private String foreignFamily;
    private String foreignFatherName;
    //corporate
    private String registrationCode;
    private String shahabCode;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getNationalNo() {
        return nationalNo;
    }

    public void setNationalNo(String nationalNo) {
        this.nationalNo = nationalNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(String referralDate) {
        this.referralDate = referralDate;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdentificationCharacter() {
        return identificationCharacter;
    }

    public void setIdentificationCharacter(String identificationCharacter) {
        this.identificationCharacter = identificationCharacter;
    }

    public String getIdentificationLongSerial() {
        return identificationLongSerial;
    }

    public void setIdentificationLongSerial(String identificationLongSerial) {
        this.identificationLongSerial = identificationLongSerial;
    }

    public String getIdentificationShortSerial() {
        return identificationShortSerial;
    }

    public void setIdentificationShortSerial(String identificationShortSerial) {
        this.identificationShortSerial = identificationShortSerial;
    }

    public String getBirthLocation() {
        return birthLocation;
    }

    public void setBirthLocation(String birthLocation) {
        this.birthLocation = birthLocation;
    }

    public String getIdentificationIssuingLocationCode() {
        return identificationIssuingLocationCode;
    }

    public void setIdentificationIssuingLocationCode(String identificationIssuingLocationCode) {
        this.identificationIssuingLocationCode = identificationIssuingLocationCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAuthenticationDocuments() {
        return authenticationDocuments;
    }

    public void setAuthenticationDocuments(String authenticationDocuments) {
        this.authenticationDocuments = authenticationDocuments;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportExpireDate() {
        return passportExpireDate;
    }

    public void setPassportExpireDate(String passportExpireDate) {
        this.passportExpireDate = passportExpireDate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getForeignName() {
        return foreignName;
    }

    public void setForeignName(String foreignName) {
        this.foreignName = foreignName;
    }

    public String getForeignFamily() {
        return foreignFamily;
    }

    public void setForeignFamily(String foreignFamily) {
        this.foreignFamily = foreignFamily;
    }

    public String getForeignFatherName() {
        return foreignFatherName;
    }

    public void setForeignFatherName(String foreignFatherName) {
        this.foreignFatherName = foreignFatherName;
    }

    public String getForeignerNo() {
        return foreignerNo;
    }

    public void setForeignerNo(String foreignerNo) {
        this.foreignerNo = foreignerNo;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShahabCode() {
        return shahabCode;
    }

    public void setShahabCode(String shahabCode) {
        this.shahabCode = shahabCode;
    }
}
