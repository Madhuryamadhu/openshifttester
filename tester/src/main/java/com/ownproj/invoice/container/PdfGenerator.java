package com.ownproj.invoice.container;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
public class PdfGenerator {
	private static final Logger logger = LogManager.getLogger(PdfGenerator.class);
	public static void main(String[] args) throws FileNotFoundException, DocumentException {

		//Pdfgenerate();
		System.out.println(System.getProperty("user.dir")+"\\ImageFolder");

	}

	private static boolean Pdfgenerate() {
		try {
		String wokspacePath="F:\\ProjectWorkSpace\\JobPortal\\InvoiceBuilder";
		String pdfName=new SimpleDateFormat("ddMMyyyyhhss").format(new Date())+"table";

		Document document = new Document();
		PdfPTable table = new PdfPTable(6);
		table.setWidths(new int[]{ 3, 3, 3, 3, 3, 3});
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell("Sl no");
		table.addCell("Product");
		table.addCell("Base Price");
		table.addCell("GST");
		table.addCell("Quantity");
		table.addCell("Total");
		table.setHeaderRows(1);

		PdfPCell[] cells = table.getRow(0).getCells(); 

		for (int j=0;j<cells.length;j++){
			cells[j].setBackgroundColor(new BaseColor(69, 147, 151));
			cells[j].setPadding(10);
			//cells[j].setBorderWidth(1);;

		}	

		
		table.addCell("1");
		table.addCell("Battery");
		table.addCell("150");
		table.addCell("5%(30)");
		table.addCell("3");
		table.addCell("180*3=240");


		table.addCell("2");
		table.addCell("Bulb");
		table.addCell("10");
		table.addCell("20%(2)");
		table.addCell("2");
		table.addCell("12*2=24");


		PdfPTable table1 = new PdfPTable(6);
		table1.addCell(" ");
		PdfPCell cell = new PdfPCell(new Phrase("                                     Total"));
		cell.setColspan(4);
		//cell.setBorderWidth(1);;
		table1.addCell(cell);
		table1.addCell("       750");


			PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(wokspacePath+"/PdfFolder/"+pdfName+".pdf"));
			document.open();

			PdfContentByte canvas = writer.getDirectContent();
			Rectangle rectS = new Rectangle(595,842);

			rectS.setBorder(Rectangle.BOX); // left, right, top, bottom border
			rectS.setBorderWidth(4); // a width of 5 user units
			rectS.setBorderColor(BaseColor.BLACK); // a red border
			rectS.setUseVariableBorders(true); // the full width will be visible
			canvas.rectangle(rectS);

			Image img = Image.getInstance(wokspacePath+"/ImageFolder/ele.png");
			img.scaleToFit(200,180);  
			img.setAbsolutePosition(0,680);
			img.setAlignment(Image.MIDDLE);          

			document.add(img);


			Font myfont = FontFactory.getFont(FontFactory.HELVETICA, 12,Font.BOLD, BaseColor.BLACK);

			Paragraph p = new Paragraph();
			Chunk glue = new Chunk(new VerticalPositionMark());
			p.setFont(myfont);
			p.add("Name    :- ABc");
			p.add(new Chunk(glue));
			p.add("Date    :- 12-02-2019");
			p.add(Chunk.NEWLINE);
			p.add("Mobile  :- 9876543210");
			p.add(new Chunk(glue));
			p.add("Place   :- Bangalore");
			p.add(Chunk.NEWLINE);
			p.add("Email   :- abc@gmail.com");
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
			para2.add("Total Amount :- 750");
			para2.add(Chunk.NEWLINE);
			para2.add("Discount :- 10% (75)");
			para2.add(Chunk.NEWLINE);
			para2.add("Amount To Be Paid :- 675");
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
			System.out.println("Done");
			
		}  catch (MalformedURLException e) {
			logger.error("Exception:-"+e.getMessage());
		} catch (IOException e) {
			logger.error("Exception:-"+e.getMessage());
		} catch (DocumentException e) {
			logger.error("Exception:-"+e.getMessage());
		}


		return true;
	}

	

}  

