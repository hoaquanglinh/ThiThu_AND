package com.example.thithu_and;

public class XeMay {
    private String _id;
    private String ten_ph43159;
    private String mau_ph43159;
    private double gia_ph43159;
    private String mota_ph43159;
    private String anh_ph43159;

    public XeMay(String _id, String ten_ph43159, String mau_ph43159, double gia_ph43159, String mota_ph43159, String anh_ph43159) {
        this._id = _id;
        this.ten_ph43159 = ten_ph43159;
        this.mau_ph43159 = mau_ph43159;
        this.gia_ph43159 = gia_ph43159;
        this.mota_ph43159 = mota_ph43159;
        this.anh_ph43159 = anh_ph43159;
    }

    public XeMay() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen_ph43159() {
        return ten_ph43159;
    }

    public void setTen_ph43159(String ten_ph43159) {
        this.ten_ph43159 = ten_ph43159;
    }

    public String getMau_ph43159() {
        return mau_ph43159;
    }

    public void setMau_ph43159(String mau_ph43159) {
        this.mau_ph43159 = mau_ph43159;
    }

    public double getGia_ph43159() {
        return gia_ph43159;
    }

    public void setGia_ph43159(double gia_ph43159) {
        this.gia_ph43159 = gia_ph43159;
    }

    public String getMota_ph43159() {
        return mota_ph43159;
    }

    public void setMota_ph43159(String mota_ph43159) {
        this.mota_ph43159 = mota_ph43159;
    }

    public String getAnh_ph43159() {
        return anh_ph43159;
    }

    public void setAnh_ph43159(String anh_ph43159) {
        this.anh_ph43159 = anh_ph43159;
    }
}
