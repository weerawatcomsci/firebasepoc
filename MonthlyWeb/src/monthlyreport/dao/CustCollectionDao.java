package monthlyreport.dao;



import java.util.List;

/**
 * Created by HOTUM IT on 12/1/2561.
 */

public class CustCollectionDao {


	

/*    private List<CustItemDao> cust;
    public List<CustItemDao> getNewcus() {
        return cust;
    }

    public void setNewcus(List<CustItemDao> cust) {
        this.cust = cust;
    }
*/


    private List<CustItemDao> allcust;
    private List<NewCustItemDao2> newcuss;
  /*  private List<NewCustItemDao> newcuss;*/
   



	public List<NewCustItemDao2> getNewcuss() {
		return newcuss;
	}

	public void setNewcuss(List<NewCustItemDao2> newcuss) {
		this.newcuss = newcuss;
	}

	public List<CustItemDao> getAllcust() {
		return allcust;
	}

	public void setAllcust(List<CustItemDao> allcust) {
		this.allcust = allcust;
	}

	 
	   
	/*	public List<NewCustItemDao> getNewcuss() {
			return newcuss;
		}

		public void setNewcuss(List<NewCustItemDao> newcuss) {
			this.newcuss = newcuss;
		}
		*/
	

}
