package ch.test_viewpager.model.data;

/**
 * Created by CH on 2016-07-16.
 */
public class URLName {
    private String mainURL;
    private String apiKey;
    private String serviceName;
    private String operationName;

    private String x_val;
    private String y_val;
    private String pageNo;
    private String numOfRows;
    private String returnType;

    private String version;
    private String dataTerm;
    private String stationName;

    private String informCode;
    private String searchDate;
    private String searchCondition;


    public URLName() {
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getMainURL() {
        return mainURL;
    }

    public void setMainURL(String mainURL) {
        this.mainURL = mainURL;
    }

    public String getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getX_val() {
        return x_val;
    }

    public void setX_val(String x_val) {
        this.x_val = x_val;
    }

    public String getY_val() {
        return y_val;
    }

    public void setY_val(String y_vale) {
        this.y_val = y_vale;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getDataTerm() {
        return dataTerm;
    }

    public void setDataTerm(String dataTerm) {
        this.dataTerm = dataTerm;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getInformCode() {
        return informCode;
    }

    public void setInformCode(String informCode) {
        this.informCode = informCode;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }
}
