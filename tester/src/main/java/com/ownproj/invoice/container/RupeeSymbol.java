package com.ownproj.invoice.container;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class RupeeSymbol {
	private static final Logger logger = LogManager.getLogger(RupeeSymbol.class);
	public static final String DEST = "F:\\ProjectWorkSpace\\JobPortal\\InvoiceBuilder+\\PdfFolder\\One.pdf";
	public static final String FONT1 = "F:\\ProjectWorkSpace\\JobPortal\\InvoiceBuilder\\ImageFolder\\arial.ttf";
	public static final String FONT2 = "resources/fonts/PT_Sans-Web-Regular.ttf";
	public static final String FONT3 = "resources/fonts/FreeSans.ttf";
	public static final String RUPEE = "The Rupee character \u20B9 and the Rupee symbol \u20A8";

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new RupeeSymbol().createPdf(DEST);
	}

	public void createPdf(String dest) throws IOException, DocumentException {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(DEST));
			document.open();
			Font f1 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
			document.add(new Paragraph(RUPEE, f1));
			document.close();

		} catch (FileNotFoundException e) {
			logger.error("Exception:-"+e.getMessage());
		} catch (Exception e) {
			logger.error("Exception:-"+e.getMessage());
		}
	}
}