package ir.khalili.products.nas.core.helper.external.services.kyc.dto;

import io.vertx.ext.sql.SQLConnection;

public class LogRequestDto {
  private long customerId;
  private long agentId;
  private long customerServiceId;
  private SQLConnection sqlConnection;

  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

  public long getAgentId() {
    return agentId;
  }

  public void setAgentId(long agentId) {
    this.agentId = agentId;
  }

  public long getCustomerServiceId() {
    return customerServiceId;
  }

  public void setCustomerServiceId(long customerServiceId) {
    this.customerServiceId = customerServiceId;
  }

  public SQLConnection getSqlConnection() {
    return sqlConnection;
  }

  public void setSqlConnection(SQLConnection sqlConnection) {
    this.sqlConnection = sqlConnection;
  }
}
