package ch.test_viewpager.model.data;

import android.graphics.Bitmap;

/**
 * Created by CH on 2016-07-20.
 */

//미세먼지 예보 정보를 저장하기 위한 클래스
public class GetMinuDustFrcstDspth {
    private String resultCode; //결과코드
    private String resultMsg;       //결과메세지
    private String dataTime;        //데이터 발표시간
    private String informCode;      //데이터 타입
    private String informOverall;   //전국 미세먼지 예보정보
    private String informCause;     //전국 예보 자세히
    private String informGrade;     //시도별 예보

    private String[] imageURL;
    private Bitmap[] image;


    public GetMinuDustFrcstDspth(){
        imageURL = new String[6];
        image = new Bitmap[6];
    }


    private String informData;      //오늘 / 내일 / 모레 예보에 해당하는 날짜

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getInformCause() {
        return informCause;
    }

    public void setInformCause(String informCause) {
        this.informCause = informCause;
    }

    public String getInformCode() {
        return informCode;
    }

    public void setInformCode(String informCode) {
        this.informCode = informCode;
    }

    public String getInformData() {
        return informData;
    }

    public void setInformData(String informData) {
        this.informData = informData;
    }

    public String getInformGrade() {
        return informGrade;
    }

    public void setInformGrade(String informGrade) {
        this.informGrade = informGrade;
    }

    public String getInformOverall() {
        return informOverall;
    }

    public void setInformOverall(String informOverall) {
        this.informOverall = informOverall;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Bitmap[] getImage() {
        return image;
    }

    public void setImage(Bitmap[] image) {
        this.image = image;
    }

    public String[] getImageURL() {
        return imageURL;
    }

    public void setImageURL(String[] imageURL) {
        this.imageURL = imageURL;
    }

}
