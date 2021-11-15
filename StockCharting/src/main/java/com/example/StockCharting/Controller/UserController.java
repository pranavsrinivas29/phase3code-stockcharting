package com.example.StockCharting.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import org.apache.tomcat.jni.Buffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.StockCharting.Entity.User1;
import com.example.StockCharting.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class  UserController {
	@Autowired
	UserRepository userrepo;
	@CrossOrigin(origins ="http://localhost:3000")
	@RequestMapping(value = "/setuserapi",method=RequestMethod.GET)
	
	public String Stringreactuserapi(@RequestParam Map<String, String> userobj) throws AddressException, MessagingException {	
		User1 usr = new User1();

		usr.setUsername(userobj.get("name")+"from api1");
		usr.setPassword(userobj.get("password"));
		usr.setEmail(userobj.get("email"));

		userrepo.save(usr);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Responded", "UserController");
		headers.add("Access-Control-Allow-Origin", "*");
		sendemail(usr.getId()) ;
		return userobj.toString();
	}

	@CrossOrigin(origins ="http://localhost:3000")
	@RequestMapping(value = "/setuserapi2",method=RequestMethod.POST, headers = "Accept=application/json"  )

	public  ResponseEntity<Object> reactuserapi2(@RequestBody User1 user) throws AddressException, MessagingException, ClassNotFoundException, IOException {

		User1 usrsaved = userrepo.save(user);
       // make sure your entity class properties of user are in lower case and match the json,to avoid errors
		System.out.println(user +"check this " +usrsaved.getUsername());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usrsaved.getId())
				.toUri();
		sendemail(usrsaved.getId()) ;

		return ResponseEntity.created(location).build();
	}



	@RequestMapping(value = "/settestuser",method=RequestMethod.GET)
	public ResponseEntity<User1> getUser() throws AddressException, MessagingException {
		User1 usr = new User1();

		usr.setUsername("Naren");
		usr.setPassword("password");
		usr.setEmail("narenrr@yahoo.com");

		userrepo.save(usr);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Responded", "UserController");
		sendemail(usr.getId()) ;
		return ResponseEntity.accepted().headers(headers).body(usr);
	}




	public void sendemail(Long userid) throws AddressException, MessagingException {






		final String username = "sftrainerram@gmail.com";
		final String password = "pwd";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS

		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sftrainerram@gmail.com"));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse("sftrainerram@gmail.com")
					);
			message.setSubject("USer confirmation email");
			//     message.setText("Dear Mail Crawler,"
			//           + "\n\n Please do not spam my email!");
			message.setContent(
					"<h1><a href =\"http://127.0.0.1:8080/confirmuser/"+userid+"/\"> Click to confirm </a></h1>",
					"text/html");
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}


	@RequestMapping(value="/confirmuser/{userid}", method=RequestMethod.GET)
	public String welcomepage(@PathVariable Long userid) {
		Optional<User1> userlist =   userrepo.findById(userid);
		//do a null check for home work
		User1 usr = new User1();
		usr = userrepo.getById(userid);
		usr.setConfirmed(true);
		userrepo.save(usr);
		return "User confirmed" +usr.getUsername();
	}

}
