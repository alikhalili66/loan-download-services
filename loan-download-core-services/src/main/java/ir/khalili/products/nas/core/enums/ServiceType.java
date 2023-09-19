package ir.khalili.products.nas.core.enums;

public enum ServiceType {

    LOAN(12 , "وام")
    //
    ;

    private final Integer serviceId;
    private final String persianTitle;

    ServiceType(Integer serviceId , String persianTitle) {
        this.serviceId = serviceId;
        this.persianTitle = persianTitle;
    }

    public String getPersianTitle() {
        return persianTitle;
    }

    public Integer getServiceId() {
        return serviceId;
    }
}
