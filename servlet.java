package edu.cs;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/FileUploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
               maxFileSize=1024*1024*50,      	// 50 MB
               maxRequestSize=1024*1024*100)   	// 100 MB
public class FileUploadServlet extends HttpServlet {

  private static final long serialVersionUID = 205242440643911308L;
	
  /**
   * Directory where uploaded files will be saved, its relative to
   * the web application directory.
   */
  private static final String UPLOAD_DIR = "uploads";
  
  protected void doPost(HttpServletRequest request,
          HttpServletResponse response) throws ServletException, IOException {
      // gets absolute path of the web application
      String applicationPath = request.getServletContext().getRealPath("");

      // constructs path of the directory to save uploaded file
      String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

       
      // creates the save directory if it does not exist
      File fileSaveDir = new File(uploadFilePath);
      if (!fileSaveDir.exists()) {
          fileSaveDir.mkdirs();
      }
      System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());
      
      String fileName = "";
      //Get all the parts from request and write it to the file on server
      for (Part part : request.getParts()) {
          fileName = getFileName(part);
          fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
          part.write(uploadFilePath + File.separator + fileName);
      }

    // Integrate information about Sentinel Defense Technologies
    String companyInfo = getSentinelDefenseInfo();
    String content = "Uploaded file: " + fileName + "<br>" + companyInfo;
    
    writeToResponse(response, content);
  }

  /**
   * Utility method to get file name from HTTP header content-disposition
   */
  private String getFileName(Part part) {
      String contentDisp = part.getHeader("content-disposition");
      System.out.println("content-disposition header= "+contentDisp);
      String[] tokens = contentDisp.split(";");
      for (String token : tokens) {
          if (token.trim().startsWith("filename")) {
              return token.substring(token.indexOf("=") + 2, token.length()-1);
          }
      }
      return "";
  }
  
  /**
   * Method to generate information about Sentinel Defense Technologies
   */
  private String getSentinelDefenseInfo() {
      StringBuilder info = new StringBuilder();
      info.append("<h2>Sentinel Defense Technologies</h2>");
      info.append("<p>Sentinel Defense Technologies specializes in manufacturing physical security equipment and providing consulting services and training in the security sector.</p>");
      info.append("<p>Manufactured products include armored vehicles, perimeter security systems, surveillance equipment, and access control systems.</p>");
      info.append("<p>Consulting services are offered to assess security needs, design customized solutions, and provide training programs for product usage and maintenance.</p>");
      info.append("<h3>Key Stakeholders:</h3>");
      info.append("<ul>");
      info.append("<li>Government Agencies</li>");
      info.append("<li>Military and Law Enforcement</li>");
      info.append("<li>Private Corporations</li>");
      info.append("<li>Security Consultants and Contractors</li>");
      info.append("<li>Local Communities</li>");
      info.append("</ul>");
      return info.toString();
  }
  
  private void writeToResponse(HttpServletResponse resp, String results) throws IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();
    writer.write(results);
    writer.close();
  }
}
