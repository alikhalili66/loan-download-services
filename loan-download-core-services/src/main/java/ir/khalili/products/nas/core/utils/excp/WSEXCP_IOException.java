package ir.khalili.products.nas.core.utils.excp;

import ir.khalili.products.nas.core.dao.excp.DAOException;

public class WSEXCP_IOException extends DAOException {

    private static final long serialVersionUID = 9141432548554161363L;

    public WSEXCP_IOException() {
        super();
    }

    public WSEXCP_IOException(Throwable e , String daoMessage) {
        super(e , daoMessage);
    }

    public WSEXCP_IOException(String resMessage) {
        super(resMessage);
    }

    public WSEXCP_IOException(int resCode , String resMessage) {
        super(resCode , resMessage);
    }


}
