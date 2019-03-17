package com.ownproj.invoice.container;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;



public class CommonDAO {
	private static final Logger logger = LogManager.getLogger(CommonDAO.class);

	public boolean submitAction(CommonBean bean, HttpSession session) {
		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result =0;
		int[] numbers =null;
		StringBuilder table=new StringBuilder();
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );
			String [] productIDArr=bean.getProductIDs().split(",");
			String inCondition= buildIN(productIDArr.length);
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String selectSQL = "SELECT  PRODUCT_ID,PRODUCT_NAME,PRODUCT_PRICE,GST_PERCENT FROM PRODUCT_DETAILS WHERE DISPLAY=? AND PRODUCT_ID IN("+inCondition+")";
				PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
				preparedStatement.setInt(1, 0);
				for (int i = 0; i < productIDArr.length; i++) {
					preparedStatement.setString(i+2, productIDArr[i]);
				}


				ResultSet rs = preparedStatement.executeQuery();
				logger.info("SQL QUERY:-"+selectSQL);
				int count=1;
				List<List<String>> products= new ArrayList<List<String>>();
				int grandTotal=0;
				while (rs.next()) {
					List<String> product=new ArrayList<String>();
					String prodId=rs.getString("PRODUCT_ID");

					String price=rs.getString("PRODUCT_PRICE");
					String gst=rs.getString("GST_PERCENT");
					int gstAmount=0;
					if(gst!=null&&gst!="") {
						gstAmount=Integer.parseInt(price)*(Integer.parseInt(gst))/100;
						gst=gst+"%(Rs."+gstAmount+")";
					}
					int priceWithGst=gstAmount+Integer.parseInt(price);
					String quantity=bean.getQuantityIDMap().get(prodId);
					String total=priceWithGst*Integer.parseInt(quantity)+"";
					product.add(count+"");
					product.add(rs.getString("PRODUCT_NAME"));
					product.add(price);
					product.add(gst);
					product.add(priceWithGst+"");
					product.add(quantity);
					product.add(total);
					products.add(product);

					grandTotal=grandTotal+Integer.parseInt(total);
					count++;
				}
				bean.setProductDetails(products);
				bean.setGrandTotal(grandTotal);

				if(Pdfgenerate(bean)) {
					prepareTable(bean);
				}
				isSuccess=true;


			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;

	}


	private void prepareTable(CommonBean bean) {
		StringBuilder table=null;
		try {
			table= new StringBuilder();
			table.append("<div class=\"col-sm-1\"></div>");
			table.append("<div class=\"col-sm-10\" style=\"background-color: #F1FAEE;\">");
			table.append("<div class=\"container\">");
			table.append("<h2>Transaction Details</h2>");
			table.append("<p>Name      :- "+bean.getName()+"</p>");
			table.append("<p>Mobile    :- "+bean.getContact()+"</p>");
			table.append("<p>Email     :- "+bean.getEmail()+"</p>");
			table.append("<div class=\"col-sm-10\">");
			table.append("<table class=\"table table-hover\" id=\"customers\">");
			table.append("<thead class=\"thead-dark\">");
			table.append("<tr><th scope=\"col\">Sl no</th><th scope=\"col\">Product</th><th scope=\"col\">Price</th><th scope=\"col\">GST</th><th scope=\"col\">Price With GST</th><th scope=\"col\">Quantity</th><th scope=\"col\">Total</th></tr></thead><tbody>");

			for (List<String> products : bean.getProductDetails()) {
				table.append("<tr>");
				for (String product : products) {
					table.append("<td>"+product+"</td>");
				}
				table.append("</tr>");
			}
			table.append("</tbody></table></div><div class=\"col-sm-2\"></div><div class=\"form-group btn-container col-sm-8\">");
			table.append("<div class=\"col-sm-4\"><button type=\"button\" id=\"goBack\" onclick=\"goBack()\" class=\"btn btn-primary\" style=\"width: 100%;\">Enter Details Again</button></div>");
			table.append("<div class=\"col-sm-4\"><button type=\"button\" id=\"downloadPdf\" onclick=\"downloadPdf('"+bean.getPdfFileName()+"','"+bean.getPdfFullPath()+"')\" class=\"btn btn-primary\" style=\"width: 100%;\">Download Pdf</button></div>");
			table.append("<div class=\"col-sm-4\"><button type=\"button\" id=\"sendMail\" onclick=\"sendMail()\" class=\"btn btn-primary\" style=\"width: 100%;\">Send to Email</button></div>");
			table.append("</div></div></div><div class=\"col-sm-1\"></div>");
			bean.setHtmlContent(table.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static String buildIN(int length) {
		String condition="";
		for (int i = 0; i <length; i++) {
			if(condition.equals(""))
				condition="?";
			else
				condition += ",?";
		}
		return condition;
	}

	public boolean loadProducts(CommonBean bean, HttpSession session) {
		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result =0;
		int[] numbers =null;
		StringBuilder options=null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String selectSQL = "SELECT  PRODUCT_ID,PRODUCT_NAME FROM PRODUCT_DETAILS WHERE DISPLAY=?";
				PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
				preparedStatement.setInt(1, 0);
				ResultSet rs = preparedStatement.executeQuery();
				logger.info("SQL QUERY:-"+selectSQL);

				options=new StringBuilder();
				options.append("<option value=''>Select</option>");
				while (rs.next()) {
					options.append("<option value=\""+rs.getInt("PRODUCT_ID")+"\">"+rs.getString("PRODUCT_NAME")+"</option>");
				}

				bean.setHtmlContent(options.toString());
				isSuccess=true;
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;

	}

	private boolean Pdfgenerate(CommonBean bean) {
		try {
			String wokspacePath=ConnectionParam.PDF_UPLOAD_PATH;
			logger.info(wokspacePath);
			String pdfName=new SimpleDateFormat("ddMMyyyyhhss").format(new Date())+"_"+bean.getName().replace(" ", "_");

			Document document = new Document(PageSize.A4);
			PdfPTable table = new PdfPTable(7);
			table.setWidths(new int[]{ 2, 3, 3, 3, 3, 3,4});
			table.setWidthPercentage(100);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("Sl no");
			table.addCell("Product");
			table.addCell("Base Price");
			table.addCell("GST");
			table.addCell("Price with GST");
			table.addCell("Quantity");
			table.addCell("Total");
			table.setHeaderRows(1);

			PdfPCell[] cells = table.getRow(0).getCells(); 

			for (int j=0;j<cells.length;j++){
				cells[j].setBackgroundColor(new BaseColor(69, 147, 151));
				cells[j].setPadding(10);
				//cells[j].setBorderWidth(1);;

			}	

			for (List<String> products : bean.getProductDetails()) {
				for (String product : products) {
					table.addCell(product);
				}
			}

			PdfPTable table1 = new PdfPTable(7);
			table1.setWidths(new int[]{ 2, 3, 3, 3, 3, 3,4});
			table1.setWidthPercentage(100);
			table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell cell = new PdfPCell(new Phrase("Total"));
			cell.setColspan(6);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);
			table1.addCell("Rs."+bean.getGrandTotal());


			PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(wokspacePath+"/"+pdfName+".pdf"));
			document.open();

			PdfContentByte canvas = writer.getDirectContent();
			Rectangle rectS = new Rectangle(595,842);

			rectS.setBorder(Rectangle.BOX); // left, right, top, bottom border
			rectS.setBorderWidth(4); // a width of 5 user units
			rectS.setBorderColor(BaseColor.BLACK); // a red border
			rectS.setUseVariableBorders(true); // the full width will be visible
			canvas.rectangle(rectS);

			Image img = Image.getInstance(ConnectionParam.PDF_IMAGE_PATH+"/ele.png");
			img.scaleToFit(200,180);  
			img.setAbsolutePosition(0,680);
			img.setAlignment(Image.MIDDLE);          

			document.add(img);


			Font myfont = FontFactory.getFont(FontFactory.HELVETICA, 12,Font.BOLD, BaseColor.BLACK);

			Paragraph p = new Paragraph();
			Chunk glue = new Chunk(new VerticalPositionMark());
			p.setFont(myfont);
			p.add("Name    :- "+bean.getName());
			p.add(new Chunk(glue));
			p.add("Date    :- 12-02-2019");
			p.add(Chunk.NEWLINE);
			p.add("Mobile  :- "+bean.getContact());
			p.add(new Chunk(glue));
			p.add("Place   :- "+bean.getPlace());
			p.add(Chunk.NEWLINE);
			p.add("Email   :- "+bean.getEmail());
			p.add(Chunk.NEWLINE);
			p.setSpacingBefore(150);


			Paragraph para = new Paragraph("Details" , FontFactory.getFont(FontFactory.HELVETICA, 18,Font.BOLDITALIC|Font.UNDERLINE, BaseColor.BLACK));
			para.setAlignment(Element.ALIGN_CENTER);
			para.setSpacingBefore(50);
			para.setSpacingAfter(30);


			Paragraph para2 = new Paragraph();
			para2.setAlignment(Element.ALIGN_CENTER);
			para2.setSpacingBefore(50);
			para2.setSpacingAfter(30);
			para2.setFont(myfont);
			para2.add(Chunk.NEWLINE);
			para2.add("Total Amount :- ");
			para2.add("Rs."+bean.getGrandTotal());
			para2.add(Chunk.NEWLINE);
			para2.add("Discount :- 10% (Rs."+bean.getGrandTotal()*(10)/100+")");
			para2.add(Chunk.NEWLINE);
			para2.add("Amount To Be Paid :- Rs."+(bean.getGrandTotal()-(bean.getGrandTotal()*(10)/100)));

			PdfPTable tablepara = new PdfPTable(1);
			PdfPCell rightCell = new PdfPCell();
			rightCell.addElement(para2);
			rightCell.setBorderWidth(1);
			tablepara.setWidthPercentage(50);
			tablepara.addCell(rightCell);
			tablepara.setSpacingBefore(50);
			tablepara.setSpacingAfter(30);

			document.add(p);
			document.add(para);
			document.add(table);
			document.add(table1);
			//document.add(para2);
			document.add(tablepara);


			document.close();
			bean.setPdfFullPath(wokspacePath+"/"+pdfName+".pdf");
			bean.setPdfFileName(pdfName+".pdf");
		} catch (FileNotFoundException e) {
			logger.error("Exception:-"+e.getMessage(),e);
		} catch (MalformedURLException e) {
			logger.error("Exception:-"+e.getMessage(),e);
		} catch (IOException e) {
			logger.error("Exception:-"+e.getMessage(),e);
		} catch (DocumentException e) {
			logger.error("Exception:-"+e.getMessage(),e);
		}


		return true;
	}


	public static Chunk getRupeeSymbol() {
		BaseFont bf=null;
		Chunk chunkRupee=null;
		try {
			bf = BaseFont.createFont("F:\\ProjectWorkSpace\\JobPortal\\InvoiceBuilder\\ImageFolder\\arial.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font font = new Font(bf, 12);
			chunkRupee = new Chunk(" \u20B9", font);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Rupee Symbol:-"+chunkRupee);
		return chunkRupee;
	}

	public boolean viewProduct(CommonBean bean, HttpSession session) {
		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result =0;
		int[] numbers =null;
		StringBuilder productTable=null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String selectSQL = "SELECT  PRODUCT_ID,PRODUCT_NAME,PRODUCT_PRICE,GST_PERCENT,CREATE_DATE,DISPLAY FROM PRODUCT_DETAILS";
				PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
				ResultSet rs = preparedStatement.executeQuery();
				logger.info("SQL QUERY:-"+selectSQL);

				productTable=new StringBuilder();
				int count=1;
				String pattern = "dd MMM yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				while (rs.next()) {
					productTable.append("<tr>");
					productTable.append("<td>"+count+"</td>");
					productTable.append("<td>"+rs.getString("PRODUCT_NAME")+"</td>");
					productTable.append("<td>"+rs.getString("PRODUCT_PRICE")+"</td>");
					productTable.append("<td>"+rs.getInt("GST_PERCENT")+"</td>");
					productTable.append("<td>"+simpleDateFormat.format(rs.getDate("CREATE_DATE"))+"</td>");
					int status=rs.getInt("DISPLAY");
					int toCHangeStatus=0;
					if(status==0) {
						productTable.append("<td>Showing</td>");
						toCHangeStatus=1;
					}else if(status==1) {
						productTable.append("<td>Hidden</td>");
						toCHangeStatus=0;
					}
					productTable.append("<td><button align=\"center\" onclick=\"modifyProduct('"+rs.getInt("PRODUCT_ID")+"');\"><i class=\"fas fa-edit\" area-hidden=\"true\"></i></button></td>");
					productTable.append("<td><button align=\"center\" onclick=\"deleteProduct('"+rs.getInt("PRODUCT_ID")+"');\"><i class=\"fas fa-trash\" area-hidden=\"true\"></i></button></td>");
					productTable.append("<td><button align=\"center\" onclick=\"hideProduct('"+rs.getInt("PRODUCT_ID")+"','"+toCHangeStatus+"');\"><i class=\"fas fa-eye-slash\" area-hidden=\"true\"></i></button></td>");
					productTable.append("</tr>");
					count++;
				}

				bean.setHtmlContent(productTable.toString());
				isSuccess=true;
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;

	}


	public boolean deleteProduct(CommonBean bean, HttpSession session) {

		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String deleteSQL = "Delete FROM PRODUCT_DETAILS WHERE PRODUCT_ID=?";
				logger.info("SQL:: "+deleteSQL);
				stmt = conn.prepareStatement(deleteSQL);
				stmt.setInt(1, Integer.parseInt(bean.getProductId()));

				int i=stmt.executeUpdate();
				if(i>0)
					isSuccess=true;
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;


	}


	public boolean hideProduct(CommonBean bean, HttpSession session) {

		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String deleteSQL = "Update PRODUCT_DETAILS SET DISPLAY=? WHERE PRODUCT_ID=?";
				logger.info("SQL:: "+deleteSQL);
				stmt = conn.prepareStatement(deleteSQL);
				stmt.setInt(1, Integer.parseInt(bean.getDisplay()));
				stmt.setInt(2, Integer.parseInt(bean.getProductId()));

				int i=stmt.executeUpdate();
				if(i>0)
					isSuccess=true;
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;


	}


	public boolean createProduct(CommonBean bean, HttpSession session) {
		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);


			if (conn!=null) {
				//INSERT INTO group_details (GROUP_NAME,GROUP_MEMBERS,USER_EMAIL,USER_ID,COUNT) VALUES ('TEST_GROUP_2','Pradeep,mahesh,ramesh,umesh','p@p.com','9980835293',0);

				Long date=new Date().getTime();
				String sql = "INSERT INTO PRODUCT_DETAILS (PRODUCT_NAME,PRODUCT_PRICE,GST_PERCENT,CREATE_DATE,DISPLAY) VALUES (?,?,?,?,?);";
				stmt = conn.prepareStatement(sql);

				stmt.setString(1, bean.getProductName());
				stmt.setInt(2, Integer.parseInt(bean.getProductPrice()));
				stmt.setInt(3, Integer.parseInt(bean.getGst()));
				stmt.setDate(4, new java.sql.Date(date));
				stmt.setInt(5, Integer.parseInt(bean.getDisplay()));

				int rows = stmt.executeUpdate();
				logger.info("Rows impacted : " + rows );
				if(rows>=1)
				{
					isSuccess=true;
				}		
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		} catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return isSuccess;
	}


	public boolean getDetailsForModify(CommonBean bean, HttpSession session) {
		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String selectSQL = "SELECT PRODUCT_NAME,PRODUCT_PRICE,GST_PERCENT,DISPLAY FROM PRODUCT_DETAILS WHERE PRODUCT_ID=?";
				PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
				preparedStatement.setInt(1, Integer.parseInt(bean.getProductId()));
				ResultSet rs = preparedStatement.executeQuery();
				logger.info("SQL QUERY:-"+selectSQL);

				while (rs.next()) {
					bean.setProductName(rs.getString("PRODUCT_NAME"));
					bean.setProductPrice(rs.getString("PRODUCT_PRICE"));
					bean.setGst(rs.getInt("GST_PERCENT")+"");
					bean.setDisplay(rs.getInt("DISPLAY")+"");
				}
				isSuccess=true;
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;

	}


	public boolean modifyProduct(CommonBean bean, HttpSession session) {

		boolean isSuccess=false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			final String JDBC_DRIVER = ConnectionParam.DRIVER;
			final String DB_URL = ConnectionParam.URL;
			final String USER = ConnectionParam.USERNAME;
			final String PASS =ConnectionParam.PASSWORD;
			logger.info("Connection parameters::- Driver->"+JDBC_DRIVER+" | URL->"+DB_URL+" | UserName->"+USER+" | Password->"+PASS );

			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			if (conn!=null) {
				String deleteSQL = "Update PRODUCT_DETAILS SET PRODUCT_NAME=?,PRODUCT_PRICE=?,GST_PERCENT=?,DISPLAY=? WHERE PRODUCT_ID=?";
				logger.info("SQL:: "+deleteSQL);
				stmt = conn.prepareStatement(deleteSQL);
				stmt.setString(1, bean.getProductName());
				stmt.setInt(2, Integer.parseInt(bean.getProductPrice()));
				stmt.setInt(3, Integer.parseInt(bean.getGst()));
				stmt.setInt(4, Integer.parseInt(bean.getDisplay()));
				stmt.setInt(5, Integer.parseInt(bean.getProductId()));

				int i=stmt.executeUpdate();
				if(i>0)
					isSuccess=true;
			}else {
				logger.info("Connection Establishment failed!!!");
			}

		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}

		return isSuccess;


	}


}
