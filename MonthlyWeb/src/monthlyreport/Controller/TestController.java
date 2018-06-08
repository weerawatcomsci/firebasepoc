package monthlyreport.Controller;


import monthlyreport.dao.*;
import monthlyreport.util.DBUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import javax.annotation.Resource;
import javax.mail.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


@Controller
public class TestController {

    private String startdata, enddate;
    String excelFilePath =  "C:\\test\\SeperateIncome.xls";
    String excelFilePath1 = "C:\\test\\SumIncome.xls";
    String excelFilePath2 = "C:\\test\\cust\\allcust.xls";
    private String response;
    static String emailToRecipient, emailSubject, emailMessage;

    String totalofmale,month;
    String totaloffemale;

    @Autowired
    private MailSenderAsync mailSenderAsync;

    @Resource(mappedName = "mail/hotummail")
    private Session mail;


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/SeperateIncome", method = RequestMethod.GET)
    @ResponseBody
    public SeperateCollectionDao listProvince(HttpServletRequest httpReq, HttpServletResponse httpResp,
                                              @RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate,
                                              @RequestParam("email") String email) {
      
        SeperateCollectionDao resp = new SeperateCollectionDao();
        try {

            HashMap<String, Object> inputParam = new HashMap<String, Object>();
            inputParam.put("startdate", startdate);
            inputParam.put("enddate", enddate);
            List<SeparateIncome> mydata = DBUtil.selectList("addr.getSomething", inputParam);
            resp.setSeparateIncomes(mydata);
        	writeExcel(mydata, excelFilePath);
            preparemail(email);	

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resp;
    }

  




	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/SumIncome", method = RequestMethod.GET)
    @ResponseBody
    public MerSumCollectionDao getMerSumIncome(HttpServletRequest httpReq, HttpServletResponse httpResp, 
    		@RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate) {
        MerSumCollectionDao resp = new MerSumCollectionDao();
        try {

            HashMap<String, Object> inputParam = new HashMap<String, Object>();
            inputParam.put("startdate", startdate);
            inputParam.put("enddate", enddate);
            List<MerSumIncome> mydata = DBUtil.selectList("addr.getSumIncome", inputParam);
            resp.setSumIncome(mydata);
            writeExcelSum(mydata,excelFilePath1);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
    }

	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/getCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CustCollectionDao getNewCus(HttpServletRequest httpReq, HttpServletResponse httpResp, 
    		@RequestParam("date") String date,@RequestParam("edate") String edate) {
		CustCollectionDao resp = new CustCollectionDao();
	ArrayList<NewCustItemDao2> listA = new ArrayList<>();
		
		try {
			 
	  
			  HashMap<String, Object> inputParam = new HashMap<String, Object>();
	          inputParam.put("date", date);
	          inputParam.put("edate", edate);
	          List<NewCustItemDao> cus = DBUtil.selectList("addr.getNewCustByMonth", inputParam);
	          
	        
	          for(int i=0;i<cus.size();i++){
	     
	        	  		month = cus.get(i).getMonth();
	        	  if(cus.get(i).getGender().equals("M")){
	        		    totalofmale = cus.get(i).getNewregister();
	        		  
			        		
	        	  }else if(cus.get(i).getGender().equals("F")){
	        		    totaloffemale = cus.get(i).getNewregister();
	        	  
		        				
	        	  }
	        	  NewCustItemDao2 idA3 = new NewCustItemDao2(month,totalofmale,totaloffemale);
		          listA.add(idA3);
	          }
	          
	          for(int i=0;i<listA.size();i++){
	        	  System.out.println("Inloop2"+listA.remove(i).getMonth()); 
	        	 
	          }
	         
	         
	          resp.setNewcuss(listA);
	       

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
     
    }
	
	
	
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/getAllCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CustCollectionDao getAllCus() {
		   
		CustCollectionDao resp = new CustCollectionDao();
		try {
			 
	   
	          List<CustItemDao> cus = DBUtil.selectList("addr.getAllCust");
	          System.out.println("allcust"+cus);
	          resp.setAllcust(cus);;
	          writeAllCusExcel(cus,excelFilePath2);
	      

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;
     
    }
	
	
	
	
	
	

    private void writeAllCusExcel(List<CustItemDao> cus, String excelFilePath) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(workbook.getSheetIndex(sheet), "Allcust");
        createHeaderRowCust(sheet);
        int rowCount = 0;

        for (CustItemDao aBook : cus) {
            Row row = sheet.createRow(++rowCount);
            writeRowAllCust(aBook, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        }
		
	}






	private void createHeaderRowCust(Sheet sheet) {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("cust_id");
		headers.add("cust_frst_name");
		headers.add("cust_last_name");
		headers.add("cust_frst_name_en");
		headers.add("cust_last_name_en");
		headers.add("cust_img_url");
		headers.add("cust_email");
		headers.add("cust_mobl_no");
		headers.add("cust_auth_id");
		headers.add("cust_auth_type");
		headers.add("cust_gend");
		headers.add("cust_brth_dttm");
		headers.add("cust_nation");
		headers.add("cust_username");
		headers.add("cust_social_ntwk");
		headers.add("cust_stts");
		headers.add("crtd_by");
		headers.add("crtd_dttm");
		headers.add("updt_by");
		headers.add("updt_dttm");
		for (int i=0; i < headers.size(); i++) {
			

		    Row header = sheet.createRow(0);
		    header.createCell(0).setCellValue(headers.get(0));
		    header.createCell(1).setCellValue(headers.get(1));
		    header.createCell(2).setCellValue(headers.get(2));
		    header.createCell(3).setCellValue(headers.get(3));
		    header.createCell(4).setCellValue(headers.get(4));
		    header.createCell(5).setCellValue(headers.get(5));
		    header.createCell(6).setCellValue(headers.get(6));
		    header.createCell(7).setCellValue(headers.get(7));
		    header.createCell(8).setCellValue(headers.get(8));
		    header.createCell(9).setCellValue(headers.get(9));
		    header.createCell(10).setCellValue(headers.get(10));
		    header.createCell(11).setCellValue(headers.get(11));
		    header.createCell(12).setCellValue(headers.get(12));
		    header.createCell(13).setCellValue(headers.get(13));
		    header.createCell(14).setCellValue(headers.get(14));
		    header.createCell(15).setCellValue(headers.get(15));
		    header.createCell(16).setCellValue(headers.get(16));
		    header.createCell(17).setCellValue(headers.get(17));
		    header.createCell(18).setCellValue(headers.get(18));
		    header.createCell(19).setCellValue(headers.get(19));
	
		}
	}






	private void writeRowAllCust(CustItemDao Allcust, Row row) {
		
		for(int i=0;i<26;i++){
			 row.createCell(0).setCellValue(Allcust.getCust_id());
			 row.createCell(1).setCellValue(Allcust.getCust_frst_name());
			 row.createCell(2).setCellValue(Allcust.getCust_last_name());
			 row.createCell(3).setCellValue(Allcust.getCust_frst_name_en());
			 row.createCell(4).setCellValue(Allcust.getCust_last_name_en());
			 row.createCell(5).setCellValue(Allcust.getCust_img_url());
			 row.createCell(6).setCellValue(Allcust.getCust_email());
			 row.createCell(7).setCellValue(Allcust.getCust_mobl_no());
			 row.createCell(8).setCellValue(Allcust.getCust_auth_id());
			 row.createCell(9).setCellValue(Allcust.getCust_auth_type());
			 row.createCell(10).setCellValue(Allcust.getCust_gend());
			 row.createCell(11).setCellValue(Allcust.getCust_brth_dttm());
			 row.createCell(12).setCellValue(Allcust.getCust_nation());
			 row.createCell(13).setCellValue(Allcust.getCust_username());
			 row.createCell(14).setCellValue(Allcust.getCust_social_ntwk());
			 row.createCell(15).setCellValue(Allcust.getCust_stts());
			 row.createCell(16).setCellValue(Allcust.getCrtd_by());
			 row.createCell(17).setCellValue(Allcust.getCrtd_dttm());
			 row.createCell(18).setCellValue(Allcust.getUpdt_by());
			 row.createCell(19).setCellValue(Allcust.getUpdt_dttm());
			 
		}
		
	}






	private void writeExcelSum(List<MerSumIncome> listBook, String excelFilePath) throws IOException  {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(workbook.getSheetIndex(sheet), "SumIncome");
        createHeaderRowSum(sheet);
        int rowCount = 0;

        for (MerSumIncome aBook : listBook) {
            Row row = sheet.createRow(++rowCount);
            writeMerSumBook(aBook, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        }
		
	}






	private void createHeaderRowSum(Sheet sheet) {
     	ArrayList<String> headers = new ArrayList<String>();
		headers.add("startDate");
		headers.add("endDate");
		headers.add("name");
		headers.add("billaddress");
		headers.add("service");
		headers.add("commissionfromMerchant");
		headers.add("merchantIncome");
		headers.add("feediscountfromcode");
		headers.add("discountfromcode");
		headers.add("discountfromCoin");
		headers.add("merchantNetIncome");
		headers.add("merchant");
		headers.add("vatcase");
		headers.add("mail");
		headers.add("nameoftax");
		headers.add("bank");
		headers.add("taxNo");
		headers.add("unitTime");
		headers.add("bankaccount");
		headers.add("fee");
		headers.add("serviceValue");
		headers.add("bankbanch");
		headers.add("stringVAT");
		headers.add("totalDiscount");
		headers.add("bankaccountno");
		
	

	
		for (int i=0; i < headers.size(); i++) {
	

			    Row header = sheet.createRow(0);
			    header.createCell(0).setCellValue(headers.get(0));
			    header.createCell(1).setCellValue(headers.get(1));
			    header.createCell(2).setCellValue(headers.get(2));
			    header.createCell(3).setCellValue(headers.get(3));
			    header.createCell(4).setCellValue(headers.get(4));
			    header.createCell(5).setCellValue(headers.get(5));
			    header.createCell(6).setCellValue(headers.get(6));
			    header.createCell(7).setCellValue(headers.get(7));
			    header.createCell(8).setCellValue(headers.get(8));
			    header.createCell(9).setCellValue(headers.get(9));
			    header.createCell(10).setCellValue(headers.get(10));
			    header.createCell(11).setCellValue(headers.get(11));
			    header.createCell(12).setCellValue(headers.get(12));
			    header.createCell(13).setCellValue(headers.get(13));
			    header.createCell(14).setCellValue(headers.get(14));
			    header.createCell(15).setCellValue(headers.get(15));
			    header.createCell(16).setCellValue(headers.get(16));
			    header.createCell(17).setCellValue(headers.get(17));
			    header.createCell(18).setCellValue(headers.get(18));
			    header.createCell(19).setCellValue(headers.get(19));
			    header.createCell(20).setCellValue(headers.get(20));
			    header.createCell(21).setCellValue(headers.get(21));
			    header.createCell(22).setCellValue(headers.get(22));
			    header.createCell(23).setCellValue(headers.get(23));
			    header.createCell(24).setCellValue(headers.get(24));
		
		
			}

	}






	private void writeMerSumBook(MerSumIncome sumIncome, Row row) {
	
		
		for(int i=0;i<26;i++){
			 row.createCell(0).setCellValue(sumIncome.getStartDate());
			 row.createCell(1).setCellValue(sumIncome.getEndDate());
			 row.createCell(2).setCellValue(sumIncome.getName());
			 row.createCell(3).setCellValue(sumIncome.getBillAddress());
			 row.createCell(4).setCellValue(sumIncome.getService());
			 row.createCell(5).setCellValue(sumIncome.getDiscountFromCode());
			 row.createCell(6).setCellValue(sumIncome.getFeediscountfromcode());
			 row.createCell(7).setCellValue(sumIncome.getMerchantIncome());
			 row.createCell(8).setCellValue(sumIncome.getCommissionFromMerchant());
			 row.createCell(9).setCellValue(sumIncome.getDiscountFromCoin());
			 row.createCell(10).setCellValue(sumIncome.getvATCase());
			 row.createCell(11).setCellValue(sumIncome.getTaxNo());
			 row.createCell(12).setCellValue(sumIncome.getMerchant());
			 row.createCell(13).setCellValue(sumIncome.getNameOfTax());
			 row.createCell(14).setCellValue(sumIncome.getBankBanch());
			 row.createCell(15).setCellValue(sumIncome.getBank());
			 row.createCell(16).setCellValue(sumIncome.getUnitTime());
			 row.createCell(17).setCellValue(sumIncome.getStringVat());
			 row.createCell(18).setCellValue(sumIncome.getfEE());
			 row.createCell(19).setCellValue(sumIncome.getTotalDiscount());
			 row.createCell(20).setCellValue(sumIncome.getServiceValue());
			 row.createCell(21).setCellValue(sumIncome.getBankAccountNo());
			 row.createCell(22).setCellValue(sumIncome.getBankaccount());
			 row.createCell(23).setCellValue(sumIncome.getMail());
			 row.createCell(24).setCellValue(sumIncome.getServiceValue());
	
			 
		}
		
	}






	public void writeExcel(List<SeparateIncome> listBook, String excelFilePath) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(workbook.getSheetIndex(sheet), "SeparateIncome");
        createHeaderRow(sheet);
        int rowCount = 0;

        for (SeparateIncome aBook : listBook) {
            Row row = sheet.createRow(++rowCount);
            writeBook(aBook, row);
        }

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        }
    }

    private void writeBook(SeparateIncome separateIncome, Row row) {
    	
    	for(int i=0;i<17;i++){
    		 row.createCell(0).setCellValue(separateIncome.getTransactiondatetime());
			 row.createCell(1).setCellValue(separateIncome.getCommissionfromerchant());
			 row.createCell(2).setCellValue(separateIncome.getMerchantnetincome());
			 row.createCell(3).setCellValue(separateIncome.getFeeincludevat());
			 row.createCell(4).setCellValue(separateIncome.getDiscountfromcode());
			 row.createCell(5).setCellValue(separateIncome.getCoinused());
			 row.createCell(6).setCellValue(separateIncome.getBilladdress());
			 row.createCell(7).setCellValue(separateIncome.getService());
			 row.createCell(8).setCellValue(separateIncome.getIncomefromcust());
			 row.createCell(9).setCellValue(separateIncome.getTotaldiscount());
			 row.createCell(10).setCellValue(separateIncome.getVATCase());
			 row.createCell(11).setCellValue(separateIncome.getTaxNo());
			 row.createCell(12).setCellValue(separateIncome.getCustName());
			 row.createCell(13).setCellValue(separateIncome.getMerchant());
			 row.createCell(14).setCellValue(separateIncome.getCustLastName());
			 row.createCell(15).setCellValue(separateIncome.getNameoftax());
			 row.createCell(16).setCellValue(separateIncome.getUnit());
    		
    	}

   
        }


    private void createHeaderRow(Sheet sheet) {

    	ArrayList<String> headers = new ArrayList<String>();
		headers.add("transactiondatetime");
		headers.add("commissionfromerchant");
		headers.add("merchantnetincome");
		headers.add("feeincludevat");
		headers.add("discountfromcode");
		headers.add("coinused");
		headers.add("billaddress");
		headers.add("service");
		headers.add("unit");
		headers.add("incomefromcust");
		headers.add("merchant");
		headers.add("vatcase");
		headers.add("nameoftax");
		headers.add("custLastName");
		headers.add("taxNo");
		headers.add("totaldiscount");
		headers.add("custName");
		for (int i=0; i < headers.size(); i++) {
			

		    Row header = sheet.createRow(0);
		    header.createCell(0).setCellValue(headers.get(0));
		    header.createCell(1).setCellValue(headers.get(1));
		    header.createCell(2).setCellValue(headers.get(2));
		    header.createCell(3).setCellValue(headers.get(3));
		    header.createCell(4).setCellValue(headers.get(4));
		    header.createCell(5).setCellValue(headers.get(5));
		    header.createCell(6).setCellValue(headers.get(6));
		    header.createCell(7).setCellValue(headers.get(7));
		    header.createCell(8).setCellValue(headers.get(8));
		    header.createCell(9).setCellValue(headers.get(9));
		    header.createCell(10).setCellValue(headers.get(10));
		    header.createCell(11).setCellValue(headers.get(11));
		    header.createCell(12).setCellValue(headers.get(12));
		    header.createCell(13).setCellValue(headers.get(13));
		    header.createCell(14).setCellValue(headers.get(14));
		    header.createCell(15).setCellValue(headers.get(15));
		    header.createCell(16).setCellValue(headers.get(16));
	
		}
 
    }


    private void preparemail(String email) throws Exception {
    	System.out.println("MyEmail"+email);
 
    	   String[] attachFiles = new String[]{"C:\\test\\SeperateIncome.xls","C:\\test\\SumIncome.xls"};
         
        for (String retval: email.split(",")) {
           System.out.println(" "+retval);
           mailSenderAsync.sendEmail(mail,new  String[]{retval},new  String[]{retval}, "it@hotum.co.th", "test send email again", "<h1>Test GSUITE SSL</h1>",attachFiles);
        }
        

    }





}