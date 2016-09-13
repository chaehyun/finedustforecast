package ch.test_viewpager.model.data;

/**
 * Created by CH on 2016. 7. 17..
 */
public class GetMsrstnAcctoRltmMesureDnsty {

    private String dataTime; //측정일
    //private String mangName; //측정망정보 with ver1.2
    private String so2Value; //아황산가스 (단위 : ppm)
    private String coValue;
    private String o3Value;
    private String no2Value;

    private String pm10Value;   //미세먼지(단위 : ㎍/㎥)
    private String pm10Value24; //24시간 예측이동농도 with ver1.1
    private String pm25Value;   //초미세먼지 with ver1.0
    private String pm25Value24; // with ver 1.0

    private String khaiValue;   //통합대기환경수치
    private String khaiGrade;   //통합대기환경지수

    private String so2Grade;    //아황산가스지수
    private String coGrade;     //일산화탄소 지수
    private String o3Grade;     //오존 지수
    private String no2Grade;    //이산화질수 지수

    private String pm10Grade;   //미세먼지 PM10 지수
    private String pm25Grade;   //미세먼지 PM25 지수

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(String so2Value) {
        this.so2Value = so2Value;
    }

    public String getCoValue() {
        return coValue;
    }

    public void setCoValue(String coValue) {
        this.coValue = coValue;
    }

    public String getO3Value() {
        return o3Value;
    }

    public void setO3Value(String o3Value) {
        this.o3Value = o3Value;
    }

    public String getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(String no2Value) {
        this.no2Value = no2Value;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm10Value24() {
        return pm10Value24;
    }

    public void setPm10Value24(String pm10Value24) {
        this.pm10Value24 = pm10Value24;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getPm25Value24() {
        return pm25Value24;
    }

    public void setPm25Value24(String pm25Value24) {
        this.pm25Value24 = pm25Value24;
    }

    public String getKhaiValue() {
        return khaiValue;
    }

    public void setKhaiValue(String khaiValue) {
        this.khaiValue = khaiValue;
    }

    public String getKhaiGrade() {
        return khaiGrade;
    }

    public void setKhaiGrade(String khaiGrade) {
        this.khaiGrade = khaiGrade;
    }

    public String getSo2Grade() {
        return so2Grade;
    }

    public void setSo2Grade(String so2Grade) {
        this.so2Grade = so2Grade;
    }

    public String getCoGrade() {
        return coGrade;
    }

    public void setCoGrade(String coGrade) {
        this.coGrade = coGrade;
    }

    public String getO3Grade() {
        return o3Grade;
    }

    public void setO3Grade(String o3Grade) {
        this.o3Grade = o3Grade;
    }

    public String getNo2Grade() {
        return no2Grade;
    }

    public void setNo2Grade(String no2Grade) {
        this.no2Grade = no2Grade;
    }

    public String getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(String pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public String getPm25Grade() {
        return pm25Grade;
    }

    public void setPm25Grade(String pm25Grade) {
        this.pm25Grade = pm25Grade;
    }
}
