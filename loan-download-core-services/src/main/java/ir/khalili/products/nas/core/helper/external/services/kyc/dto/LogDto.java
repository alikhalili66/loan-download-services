package ir.khalili.products.nas.core.helper.external.services.kyc.dto;

public class LogDto {

  private long customerId;
  private long customerServiceId;
  private long agentId;
  private String ServiceName;
  private String InputJson;
  private String OutputJson;
  private String requestDate;

  public static LogDto of(long customerId, long customerServiceId, long agentId, String serviceName,
      String inputJson, String outputJson, String requestDate) {
    LogDto log = new LogDto();
    log.setCustomerId(customerId);
    log.setCustomerServiceId(customerServiceId);
    log.setAgentId(agentId);
    log.setServiceName(serviceName);
    log.setInputJson(inputJson);
    log.setOutputJson(outputJson);
    log.setRequestDate(requestDate);

    return log;

  }

  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

  public long getCustomerServiceId() {
    return customerServiceId;
  }

  public void setCustomerServiceId(long customerServiceId) {
    this.customerServiceId = customerServiceId;
  }

  public long getAgentId() {
    return agentId;
  }

  public void setAgentId(long agentId) {
    this.agentId = agentId;
  }

  public String getServiceName() {
    return ServiceName;
  }

  public void setServiceName(String serviceName) {
    ServiceName = serviceName;
  }

  public String getInputJson() {
    return InputJson;
  }

  public void setInputJson(String inputJson) {
    InputJson = inputJson;
  }

  public String getOutputJson() {
    return OutputJson;
  }

  public void setOutputJson(String outputJson) {
    OutputJson = outputJson;
  }

  public String getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
  }
}
