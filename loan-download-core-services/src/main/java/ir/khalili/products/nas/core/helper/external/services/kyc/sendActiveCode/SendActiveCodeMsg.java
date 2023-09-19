package ir.khalili.products.nas.core.helper.external.services.kyc.sendActiveCode;


import java.io.Serializable;


public class SendActiveCodeMsg implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 58846635035284490L;

	public static class Inbound implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 3265942541524247621L;
		private String cardNumber;
        public String getCardNumber() {
            return cardNumber;
        }
        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    public static class Outbound implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = -5263176447117069171L;
		private Boolean result;
        public Boolean getResult() {
            return result;
        }
        public void setResult(Boolean result) {
            this.result = result;
        }
    }
}
