package monthlyreport.dao;

public class NewCustItemDao2 {

    private String month;
    private String totalmale;
    private String totalfemale;
    
    
    
	public NewCustItemDao2(String month, String totalmale, String totalfemale) {
		this.month = month;
		this.totalmale = totalmale;
		this.totalfemale = totalfemale;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getTotalmale() {
		return totalmale;
	}
	public void setTotalmale(String totalmale) {
		this.totalmale = totalmale;
	}
	public String getTotalfemale() {
		return totalfemale;
	}
	public void setTotalfemale(String totalfemale) {
		this.totalfemale = totalfemale;
	}
  

}
