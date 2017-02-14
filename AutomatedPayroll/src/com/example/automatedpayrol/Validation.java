package com.example.automatedpayrol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.EditText;
import java.util.regex.Pattern;
 
public class Validation {
	 static Pattern pattern;
	 static String emailRegEx,passRegEx;
	 private static final String REQUIRED_MSG = "Required";
	 private static final String ERROR_MSG = "Not_correct_email";
	 private static final String LENGTH_MSG = "AT LEAST 5 CHARACTER";
   public static boolean requirefield(EditText editText){
	  if(editText.length()==0)
	  {
		  editText.setError(REQUIRED_MSG);
		  if(editText.length()>6)
			  editText.setError(LENGTH_MSG);
		  return false;
	  }
	   
	  
	   
	   
	return true;
	   
   }
   public static boolean validate_email(EditText editText){
		 
		   
		  emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
		    
		    pattern = Pattern.compile(emailRegEx);
		    Matcher matcher = pattern.matcher(editText.getText());
		    if (!matcher.find()) {
		    	editText.setError(ERROR_MSG);
		      return false;
		    }
		   
		   
		return true;
		   
	   }
   public static boolean validate_phone(EditText editText){
		 
	   if(editText.length()<=10)
		  {
		    
		   editText.setError("phone number 11 digit");
		      return false;
		  } 
		   
	   if(editText.length()==0)
		  {passRegEx="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]){4,}$";
		    
		   editText.setError("must a digit,a uppercase,a lower case and a special symbol ");
		      return false;
		  } 
		   
		   
		return true;
		   
	   }

   public static boolean user_name_pass(EditText editText){
		 
	   if(editText.length()<=4)
		  {
		    
		   editText.setError(LENGTH_MSG);
		      return false;
		  } 
		return true;
		   
	   }
   
   public static boolean user_match_pass(EditText editText,EditText editText2){
		 
	   if((editText.getText()!=editText2.getText()))
		  {
		    
		   editText.setError("password does not match");
		      return false;
		  } 
		return true;
		   
	   }  
   
   
}