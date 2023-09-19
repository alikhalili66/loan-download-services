package ir.khalili.products.nas.core.helper.external.services.kyc.customerExistenceInquiryMsg;

import java.io.Serializable;

/**
 * Created by gheibi on 26/09/2017.
 */
public class ChCustomerExistenceInquiryRequestDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1368836393338459032L;
	private String customerNo;
    private String individualNationalCode;
    private String corporateNationalCode;
    private String individualForeignerNo;
    private String corporateForeignerNo;

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getIndividualNationalCode() {
        return individualNationalCode;
    }

    public void setIndividualNationalCode(String individualNationalCode) {
        this.individualNationalCode = individualNationalCode;
    }

    public String getCorporateNationalCode() {
        return corporateNationalCode;
    }

    public void setCorporateNationalCode(String corporateNationalCode) {
        this.corporateNationalCode = corporateNationalCode;
    }

    public String getIndividualForeignerNo() {
        return individualForeignerNo;
    }

    public void setIndividualForeignerNo(String individualForeignerNo) {
        this.individualForeignerNo = individualForeignerNo;
    }

    public String getCorporateForeignerNo() {
        return corporateForeignerNo;
    }

    public void setCorporateForeignerNo(String corporateForeignerNo) {
        this.corporateForeignerNo = corporateForeignerNo;
    }
}
