package ir.khalili.products.nas.core.helper.external.services.kyc;

public class KycRequest {
    private String requestType;
    private String serviceId;
    private String userName;
    private Integer branchCode;

    public KycRequest(String serviceId, String requestType) {
        this.serviceId = serviceId;
        if (requestType == null) {
            this.requestType = "TRANSACTION";
        } else {
            this.requestType = requestType;
        }
        this.userName = "husseini";
        this.branchCode = 1002;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }
}
