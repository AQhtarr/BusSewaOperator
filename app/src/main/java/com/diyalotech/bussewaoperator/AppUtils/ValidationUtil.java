package com.diyalotech.bussewaoperator.AppUtils;

/**
 * This is ValidationUtil class
 * @author smrita
 *
 */
public class ValidationUtil {



	public static boolean isNameValid(String name){
		char[] nameCharArray = name.toCharArray();
		for(char c : nameCharArray){
			if(!Character.isLetter(c)){
				return false;
			}
		}
		return true;
	}

	public static boolean isMobileNumberValid(String mobileNumber){
		
		if(mobileNumber == null ||mobileNumber.isEmpty()){
			return false;
		}
		else if(mobileNumber.matches("98[0-9]{8}")){
			return true;
		}
		
		return false;
	}

	public static boolean validateFullName(String name) {

		String[] splittedName = name.split(" ");

		if (splittedName.length < 2) {
			return false;
		}

		if (splittedName.length > 3) {
			return false;
		}

		return true;


	}










}
