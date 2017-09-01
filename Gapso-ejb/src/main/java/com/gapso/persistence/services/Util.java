/**
 *Class Name: Util
 *Project Name: Gapso-ejb
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 19, 2017 3:18:03 PM
 */
package com.gapso.persistence.services;

import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Onyedika Okafor
 */
@Stateless
public class Util {

    @EJB
    PortalPersistence persistence;

    public static String hashPassword(String password) {
        String saltedPassword = ProjectConstant.SALT + password;
        String hashedPassword = generateHash(saltedPassword);
        return hashedPassword;
    }

    public Boolean checkHashedPassword(String enteredPassword, String storedPassword) {
        Boolean isAuthenticated;

        // remember to use the same SALT value use used while storing password
        // for the first time.
        String saltedPassword = ProjectConstant.SALT + enteredPassword;
        String hashedPassword = generateHash(saltedPassword);

        isAuthenticated = hashedPassword.equals(storedPassword);
        return isAuthenticated;
    }

    public PortalUser login(String emailAddress, String password) {
        PortalUser u = null;

        PortalUser ku = persistence.getActiveUserByEmailAddress(emailAddress);
        if (ku == null) {
            return u;
        }

//        PortalUserRole userRole = persistence.getPortalUserRole(ku);
//        if (userRole == null) {
//            return u;
//        }
        Boolean checkPassword = checkHashedPassword(password, ku.getPassword());
        if (checkPassword) {
            u = ku;
        }

        return u;
    }

    public static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

    public static String generateToken() {
        UUID token = UUID.randomUUID();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {

        }
        md.update(token.toString().getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static Date today() {
        Date dd = null;
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date d = new Date(c.getTimeInMillis());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.format(d);
            dd = dateFormat.parse(dateFormat.format(d));
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dd;

    }

}
