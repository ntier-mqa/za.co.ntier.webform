package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class DiscretionaryGrantsApplication {

    // EMPLOYER INFO
    private String employerName;
    private String sdlNumber;
    private String siteSdlNumber;
    private String registrationNumber;

    // PHYSICAL ADDRESS
    private String streetName;
    private String suburb;
    private String city;
    private String postalCode;
    private String province;
    private String district;
    private String localMunicipality;
    private String ruralOrUrban;

    // POSTAL ADDRESS
    private String postAddress;
    private String postSuburb;
    private String postCity;
    private String postCode;
    private String postProvince;

    // ORGANISATION SIZE AND STATUS
    private String size;
    private Boolean submittedWsp;
    private Boolean submittedPivotal;
    private Boolean approvedWorkplace;

    @Init
    @NotifyChange("*")
    public void init() {
        // Preloaded sample data
        employerName = "ABC Holdings Ltd.";
        sdlNumber = "123456789";
        siteSdlNumber = "987654321";
        registrationNumber = "REG-001234";

        streetName = "12 Green Avenue";
        suburb = "Sunnyside";
        city = "Cape Town";
        postalCode = "8000";
        province = "Western Cape";
        district = "Cape Winelands";
        localMunicipality = "Stellenbosch";
        ruralOrUrban = "Urban";

        postAddress = "PO Box 12345";
        postSuburb = "Sunnyside";
        postCity = "Cape Town";
        postCode = "8001";
        postProvince = "Western Cape";

        size = "Medium";
        submittedWsp = true;
        submittedPivotal = false;
        approvedWorkplace = false;
    }

    // Getters and Setters

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public String getSdlNumber() { return sdlNumber; }
    public void setSdlNumber(String sdlNumber) { this.sdlNumber = sdlNumber; }

    public String getSiteSdlNumber() { return siteSdlNumber; }
    public void setSiteSdlNumber(String siteSdlNumber) { this.siteSdlNumber = siteSdlNumber; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getStreetName() { return streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public String getSuburb() { return suburb; }
    public void setSuburb(String suburb) { this.suburb = suburb; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getLocalMunicipality() { return localMunicipality; }
    public void setLocalMunicipality(String localMunicipality) { this.localMunicipality = localMunicipality; }

    public String getRuralOrUrban() { return ruralOrUrban; }
    public void setRuralOrUrban(String ruralOrUrban) { this.ruralOrUrban = ruralOrUrban; }

    public String getPostAddress() { return postAddress; }
    public void setPostAddress(String postAddress) { this.postAddress = postAddress; }

    public String getPostSuburb() { return postSuburb; }
    public void setPostSuburb(String postSuburb) { this.postSuburb = postSuburb; }

    public String getPostCity() { return postCity; }
    public void setPostCity(String postCity) { this.postCity = postCity; }

    public String getPostCode() { return postCode; }
    public void setPostCode(String postCode) { this.postCode = postCode; }

    public String getPostProvince() { return postProvince; }
    public void setPostProvince(String postProvince) { this.postProvince = postProvince; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public Boolean getSubmittedWsp() { return submittedWsp; }
    public void setSubmittedWsp(Boolean submittedWsp) { this.submittedWsp = submittedWsp; }

    public Boolean getSubmittedPivotal() { return submittedPivotal; }
    public void setSubmittedPivotal(Boolean submittedPivotal) { this.submittedPivotal = submittedPivotal; }

    public Boolean getApprovedWorkplace() { return approvedWorkplace; }
    public void setApprovedWorkplace(Boolean approvedWorkplace) { this.approvedWorkplace = approvedWorkplace; }
}