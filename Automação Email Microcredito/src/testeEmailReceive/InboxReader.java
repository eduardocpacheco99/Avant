package testeEmailReceive;  
  
import java.io.BufferedReader;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.sql.Date;
import java.util.List;
import java.util.Properties;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.mail.Folder;  
import javax.mail.Message;  
import javax.mail.MessagingException;  
import javax.mail.Multipart;  
import javax.mail.NoSuchProviderException;  
import javax.mail.Part;  
import javax.mail.Session;  
import javax.mail.Store;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;
  
//import software.mail.EmailAccount;  
  
public class InboxReader {  
  
    public static void main(String args[]) throws Exception {  
    	  
        Properties props = System.getProperties();  
        props.setProperty("mail.store.protocol", "imaps");  
  
  
        try {  
  
            Session session = Session.getDefaultInstance(props, null);  
            Store store = session.getStore("imaps");  
  
            //store.connect("imap.gmail.com", "eduardo.pacheco@avante.com.vc", "avante123");  
            store.connect("imap.gmail.com", "sorocred@avante.com.vc", "DadosSoroCC!");   
            //store.connect("imap.gmail.com", "microcredito@avante.com.vc", "Cred&Avante44");   
            
            System.out.println(store);  
  
            Folder inbox = store.getFolder("Inbox");

            
            		inbox.open(Folder.READ_ONLY);  
            Message messages[] = inbox.getMessages( inbox.getMessages().length -10 ,  inbox.getMessages().length);
          
            for (Message message : messages) {  
                printMessage(message);  
        
            }  
  
        } catch (NoSuchProviderException e) {  
            e.printStackTrace();  
            System.exit(1);  
        } catch (MessagingException e) {  
            e.printStackTrace();  
            System.exit(2);  
        }  
  
    }  
  
    /** 
     * "printMessage()" method to print a message. 
     */  
    public static void printMessage(Message message) {  
        try {  
        	 MySQLAccess dao = new MySQLAccess();
      	   
        	emailBean escreverEmail = new emailBean();
            // Get the header information  
            String from = ((InternetAddress) message.getFrom()[0])  
                    .getPersonal();
            String from2 = ((InternetAddress) message.getFrom()[0])  
                    .getAddress();
                  
            
        //    System.out.println("FROM: " + from);  
            String subject = message.getSubject();  
            System.out.println("SUBJECT: " + subject);  
  escreverEmail.setSubject(subject);
           
           Long time = (message.getReceivedDate().getTime());
           escreverEmail.setDate(time); 
           escreverEmail.setId(0);
            // -- Get the message part (i.e. the message itself) --  
            Part messagePart = message;  
            Object content = messagePart.getContent(); 
       //     System.out.println(message.getContent().toString());
            // -- or its first body part if it is a multipart message --  
            if (content instanceof Multipart) {
                
                messagePart = ((Multipart) content).getBodyPart(0);  
               // System.out.println("CONTEÚDO: "+messagePart.getContent().toString());  
                if(messagePart.getContent().toString().contains("sorocred")){
                                System.out.println("contém sorocred no corpo");
                                escreverEmail.setParceiro("sorocred");
                              from2="sorocred";                     
                }
            if(from2.contains("@sorocred")){
            	System.out.println("contém sorocred no email");
         //   	   escreverEmail.setParceiro("sorocred");
           from2 = "sorocred";        
            	
            }
            if(messagePart.getContent().toString().contains("stone")){
                System.out.println("contém stone no corpo");
          //      escreverEmail.setParceiro(null);
                from2= "stone";
                
            }
if(from2.contains("@stone")){
System.out.println("contém stone no email");
//escreverEmail.setParceiro(null);
from2 = "stone";//escreverEmail.setParceiro("stone");
}
            
//escreverEmail.setParceiro(from2);

            }
        
        	escreverEmail.setParceiro(from2);
                       
         //   if(escreverEmail.getParceiro().isEmpty())
         
            // -- Get the content type --  
 //     if(messagePart.getContentType())
            String contentType = messagePart.getContentType();  
            String messageContent = "";
            String attachFiles = "";
            String caminhoBase = "C:\\Users\\Du\\Sorocred\\";  
                        
            if (contentType.contains("multipart")) {
                // content may contain attachments
                Multipart multiPart = (Multipart) message.getContent();
                int numberOfParts = multiPart.getCount();
                for (int partCount = 0; partCount < numberOfParts; partCount++) {	
                    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        // this part is attachment
                        String fileName = part.getFileName();
                        attachFiles += fileName + ", ";
                        
                      //tem arquivo. Se ESSE email não estiver na lista dos emails
              	      //da base, grava-o
              	      List<emailBean> lista =  dao.readDataBase();
              	      int var =0;
              		 escreverEmail.setFile(caminhoBase+escreverEmail.getDate()+"_"+partCount+".xlsx");
                 	
              	      for(emailBean eb : lista ){
              	    	//  System.out.println("id "+ eb.getId() +" subject "+eb.getSubject()+" date "+eb.getDate()+" nomedoarquivo "+eb.getFile());
              	   if(eb.getFile().equals(escreverEmail.getFile())){
              		   System.out.println("o objeto ja esta no banco " + eb.getSubject() );
              	   var = 1;
              	   }
              	      } 
              	        if(var==0 && (fileName.contains("xlsx") || fileName.contains("xls")) ){
              	             
              	    		  dao.writeDataBase(escreverEmail);
                      System.out.println(escreverEmail.getSubject()+" " +  fileName);  
                        
                        part.saveFile(caminhoBase+ escreverEmail.getDate()+"_"+partCount+".xlsx");
              	        }   
                    
                    } else {
                        // this part may be the message content
                        messageContent = part.getContent().toString();
//System.out.println(messageContent);
                    }
                }

                if (attachFiles.length() > 1) {
                    attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                }
            } else if (contentType.contains("text/plain")
                    || contentType.contains("text/html")) {
                Object content2 = message.getContent();
                if (content2 != null) {
                    messageContent = content2.toString();
                System.out.println(messageContent);
                }
            }
            
            // -- If the content is plain text, we can print it --  
           // System.out.println("CONTENT:" + contentType);  
//            if (contentType.startsWith("text/plain") || contentType.startsWith("text/html")) {  
 //              InputStream is = messagePart.getInputStream();  

      //          BufferedReader reader = new BufferedReader(  
        //                new InputStreamReader(is));  
  
          //      String thisLine = reader.readLine();  
         //       while (thisLine != null) {  
          //          thisLine = reader.readLine();  
          //          if(thisLine.contains("Sorocred")){
         //           	System.out.println("é sorocred!");
                    	
           //         }
             //   }  
  //          }  
              
    //        else  {  
            /*
            	   byte[] buf = new byte[40960];  
            	 //Aqui você define o caminho que salvará o arquivo.  
            	 //  String caminhoBase = System.getProperty("user.dir") + "\";  
            	             	  
            	//   System.out.println(caminhoBase);
            	   Multipart multi = (Multipart) content;  
            	    
            	for (int i = 0; i < multi.getCount(); i++) {  
            	     String nomeDoArquivo = multi.getBodyPart(i).getFileName();  
            	     if (nomeDoArquivo != null) {  
            	      InputStream is = multi.getBodyPart(i).getInputStream();  
            	      FileOutputStream fos = new FileOutputStream(caminhoBase + escreverEmail.getDate()+".xlsx");  
            	      escreverEmail.setFile(caminhoBase+escreverEmail.getDate()+".xlsx");
            	      
            	      //tem arquivo. Se ESSE email não estiver na lista dos emails
            	      //da base, grava-o
            	      List<emailBean> lista =  dao.readDataBase();
            	      int var =0;
              	    
            	      for(emailBean eb : lista ){
            	    	//  System.out.println("id "+ eb.getId() +" subject "+eb.getSubject()+" date "+eb.getDate()+" nomedoarquivo "+eb.getFile());
            	   if(eb.getDate().equals(escreverEmail.getDate())){
            		   System.out.println("o objeto ja esta no banco " + eb.getSubject() );
            	   var = 1;
            	   }
            	      }
            	        if(var==0 && (nomeDoArquivo.contains("xlsx") || nomeDoArquivo.contains("xls")) ){
            	        	try {
            	        	    Thread.sleep(1000);                 //1000 milliseconds is one second.
            	        	} catch(InterruptedException ex) {
            	        	    Thread.currentThread().interrupt();
            	        	}
            	      
            	    		  dao.writeDataBase(escreverEmail);
            	   //   System.out.println("Salvou o email Id" +  escreverEmail.getId()+" subject "+escreverEmail.getSubject()+" date "+escreverEmail.getDate()+ " nomedoarquivo" +escreverEmail.getFile() );
            	    	  //salva no banco e grava o arquivo
            	    	  
            	    // (emailBean iteracao : lista){
            	    //	  escrever
            	     // }
            	      int bytesRead;  
            	      while ((bytesRead = is.read(buf)) != -1) {  
            	         fos.write(buf, 0, bytesRead);  
            	      }  
            	     fos.close();  
            	      }
            	      }  
            	  }  
      //      	}  
              
              */
              
  
            System.out.println("-----------------------------");  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    
}  
