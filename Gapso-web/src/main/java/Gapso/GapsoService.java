/**
 *Class Name: GapsoService
 *Project Name: Gapso-web
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 13, 2017 3:52:41 PM
 */
package Gapso;

import com.gapso.email.services.EmailService;
import com.gapso.persistence.entity.activity.DailySales;
import com.gapso.persistence.entity.activity.ProductLevel;
import com.gapso.persistence.entity.activity.StationProductMinLevel;
import com.gapso.persistence.entity.activity.StationProductPrice;
import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.PasswordChangeLog;
import com.gapso.persistence.entity.portal.PaymentOption;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.entity.portal.PortalUserSessionLog;
import com.gapso.persistence.entity.portal.Station;
import com.gapso.persistence.entity.portal.Product;
import com.gapso.persistence.entity.portal.ProductDispensingPoint;
import com.gapso.persistence.entity.portal.StationPaymentOption;
import com.gapso.persistence.entity.portal.StationProduct;
import com.gapso.persistence.services.PortalPersistence;
import com.gapso.persistence.services.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * @author Onyedika Okafor
 */
@Stateless
@Path("")
public class GapsoService {

    @EJB
    PortalPersistence portalPersistence;

    @EJB
    Util util;

    @EJB
    EmailService emailService;

    @POST
    @Path("signUp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(PortalUser ru) {
        try {
            Date today = new Date();
            PortalUser pu = new PortalUser();
            pu.setActive(true);
            pu.setCreatedDate(today);
            pu.setEmailAddress(ru.getEmailAddress());
            pu.setFirstName(ru.getFirstName());
            pu.setModifiedDate(today);
            pu.setPassword(Util.hashPassword(ru.getPassword()));
            pu.setPhoneNumber(ru.getPhoneNumber());
            pu.setSurname(ru.getSurname());
            portalPersistence.create(pu);

            Business b = new Business();
            b.setActive(true);
            b.setCreatedDate(today);
            b.setModifiedDate(today);
            b.setName(ru.getMiddleName());
            b.setOwner(pu);
            b.setApproved(false);
            portalPersistence.create(b);

            PortalUserRole pur = new PortalUserRole();
            pur.setBusiness(b);
            pur.setPortalUser(pu);
            pur.setDesignation(PortalUserRole.Designation.BUSINESSMANAGER);
            pur.setActive(true);
            portalPersistence.create(pur);

            String message = "Business Registration Request Received. Admin will contact you once your request is approved. \n"
                    + "\n"
                    + "Thank you for using Gapso.";
            String receipient = ru.getEmailAddress();
            String subject = "Business Registration Request Received";
            emailService.prepareEmail(message, subject, receipient);

            return Response.status(200).entity(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating User")).build();
        }
    }

    @POST
    @Path("createPortalUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPortalUser(PortalUser ru) {
        try {
            Date today = new Date();
            String password = ru.getFirstName() + "-" + ru.getSurname();
            PortalUser pu = new PortalUser();
            pu.setActive(ru.isActive());
            pu.setCreatedDate(today);
            pu.setEmailAddress(ru.getEmailAddress());
            pu.setFirstName(ru.getFirstName());
            pu.setMiddleName(ru.getMiddleName());
            pu.setModifiedDate(today);
            pu.setPassword(Util.hashPassword(password));
            pu.setPhoneNumber(ru.getPhoneNumber());
            pu.setSurname(ru.getSurname());
            portalPersistence.create(pu);

            PortalUserRole pur = new PortalUserRole();
            pur.setBusiness(portalPersistence.find(Business.class, ru.getBusinessId()));
            pur.setPortalUser(pu);
            pur.setStation(portalPersistence.find(Station.class, ru.getStationId()));
            pur.setActive(true);
            if (ru.getDesignation().equalsIgnoreCase("Station Manager")) {
                pur.setDesignation(PortalUserRole.Designation.STATIONMANAGER);
            }

            if (ru.getDesignation().equalsIgnoreCase("Attendant")) {
                pur.setDesignation(PortalUserRole.Designation.ATTENDANT);
            }

            portalPersistence.create(pur);

            return Response.status(200).entity(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating User")).build();
        }
    }

    @GET
    @Path("changePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@QueryParam("userid") Long userid, @QueryParam("password") String password) {
        try {
            PortalUser pu = portalPersistence.find(PortalUser.class, userid);
            pu.setPassword(Util.hashPassword(password));
            pu.setModifiedDate(new Date());
            portalPersistence.update(pu);

            PasswordChangeLog log = new PasswordChangeLog();
            log.setCompletedTime(new Date());
            log.setPortalUser(pu);
            portalPersistence.create(log);
            return Response.status(200).entity(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Changing Password")).build();
        }
    }

    @POST
    @Path("updatePortalUserRole")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePortalUserRole(PortalUser ru) {
        try {
            PortalUser pu = portalPersistence.find(PortalUser.class, ru.getId());

            PortalUserRole pur = portalPersistence.getPortalUserRole(pu);
            pur.setActive(false);
            portalPersistence.update(pur);

            PortalUserRole npur = new PortalUserRole();
            npur.setStation(portalPersistence.find(Station.class, ru.getStationId()));
            npur.setBusiness(portalPersistence.find(Business.class, ru.getBusinessId()));
            npur.setPortalUser(pu);
            npur.setActive(true);

            if (ru.getDesignation().equalsIgnoreCase("Station Manager")) {
                npur.setDesignation(PortalUserRole.Designation.STATIONMANAGER);
            }

            if (ru.getDesignation().equalsIgnoreCase("Attendant")) {
                npur.setDesignation(PortalUserRole.Designation.ATTENDANT);
            }

            portalPersistence.update(npur);
            return Response.status(200).entity(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating User Role")).build();
        }
    }

    @POST
    @Path("updatePortalUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePortalUser(PortalUser ru) {
        try {
            PortalUser pu = portalPersistence.find(PortalUser.class, ru.getId());
            pu.setEmailAddress(ru.getEmailAddress());
            pu.setFirstName(ru.getFirstName());
            pu.setMiddleName(ru.getMiddleName());
            pu.setModifiedDate(new Date());
            pu.setPhoneNumber(ru.getPhoneNumber());
            pu.setSurname(ru.getSurname());
            portalPersistence.update(pu);
            return Response.status(200).entity(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating User")).build();
        }
    }

    @POST
    @Path("updateStation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStation(Station s) {
        try {
            Station station = portalPersistence.find(Station.class, s.getId());
            station.setActive(s.isActive());
            station.setAddress(s.getAddress());
            station.setModifiedDate(new Date());
            station.setName(s.getName());
            portalPersistence.update(station);

            return Response.status(200).entity(station).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Station")).build();
        }
    }

    @GET
    @Path("validateEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateEmail(@QueryParam("emailAddress") String emailAddress) {
        if (emailAddress.isEmpty()) {
            return Response.status(404).entity(new StringBuilder("Email Address is Empty")).build();
        }

        PortalUser p = portalPersistence.getUserByEmailAddress(emailAddress);

        if (p == null) {
            return Response.status(404).entity("Email Address is not Valid").build();
        }

        String nPassword = Util.generateToken();
        p.setPassword(Util.hashPassword(nPassword.substring(0, 5)));
        p.setModifiedDate(new Date());
        portalPersistence.update(p);

        System.out.println("new password" + nPassword.substring(0, 5));

        PasswordChangeLog log = new PasswordChangeLog();
        log.setCompletedTime(new Date());
        log.setPortalUser(p);
        portalPersistence.create(log);

        String message = "Your password reset request was successful. Your new password is " + nPassword + "\n"
                + "You are adviced to change your password once you log in. \n"
                + "Thank you for using Gapso.";
        String receipient = p.getEmailAddress();
        String subject = "Password Reset";
        emailService.prepareEmail(message, subject, receipient);

        return Response.ok(p).build();
    }

    @GET
    @Path("validateLoginEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateLoginEmail(@QueryParam("emailAddress") String emailAddress) {
        if (emailAddress.isEmpty()) {
            return Response.status(404).entity(new StringBuilder("Email Address is Empty")).build();
        }

        PortalUser p = portalPersistence.getUserByEmailAddress(emailAddress);

        if (p == null) {
            return Response.status(404).entity("Email Address is not Valid").build();
        }
        return Response.ok(p).build();
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("emailAddress") String emailAddress, @QueryParam("password") String password) {

        if (emailAddress.isEmpty()) {
            return Response.status(404).entity(new StringBuilder("Email Address is Empty")).build();
        }

        if (password.isEmpty()) {
            return Response.status(404).entity(new StringBuilder("Password is Empty")).build();
        }

        PortalUser currentUser = util.login(emailAddress, password);
        if (currentUser == null) {
            return Response.status(404).entity("User Does not Exist").build();
        }
        return Response.ok(currentUser).build();
    }

    @GET
    @Path("logUserSession")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logUserSession(@QueryParam("ipaddress") String ipaddress, @QueryParam("userEmail") String userEmail,
            @QueryParam("userId") Long userId) {
        try {
            PortalUserSessionLog log = new PortalUserSessionLog();
            log.setDateCreated(new Date());
            log.setIpaddress(ipaddress);
            log.setUserEmail(userEmail);
            log.setUserId(userId);

            portalPersistence.create(log);
            return Response.ok(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Log")).build();
        }
    }

    @POST
    @Path("createPaymentOption")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPaymentOption(PaymentOption paymentOption) {
        try {
            Date today = new Date();
            paymentOption.setCreatedDate(today);
            paymentOption.setModifiedDate(today);
            portalPersistence.create(paymentOption);

            List<Station> businessStations = portalPersistence.getAllStationByBusiness(paymentOption.getBusiness());
            for (Station s : businessStations) {
                StationPaymentOption spo = new StationPaymentOption();
                spo.setActive(false);
                spo.setAddedDate(today);
                spo.setPaymentOption(paymentOption);
                spo.setStation(s);
                portalPersistence.create(spo);
            }

            return Response.ok(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Payment Option")).build();
        }
    }

    @POST
    @Path("createProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product) {
        try {
            Date today = new Date();
            product.setActive(true);
            product.setCreatedDate(today);
            product.setModifiedDate(today);
            portalPersistence.create(product);

            List<Station> businessStations = portalPersistence.getAllStationByBusiness(product.getBusiness());
            for (Station s : businessStations) {
                StationProduct sp = new StationProduct();
                sp.setActive(false);
                sp.setAddedDate(today);
                sp.setProduct(product);
                sp.setStation(s);
                portalPersistence.create(sp);

                StationProductPrice spp = new StationProductPrice();
                spp.setActive(true);
                spp.setCreatedDate(today);
                spp.setPrice(0.0);
                spp.setStationProduct(sp);
                portalPersistence.create(spp);

                StationProductMinLevel spml = new StationProductMinLevel();
                spml.setActive(true);
                spml.setCreatedDate(today);
                spml.setMininumLevel(0.0);
                spml.setStationProduct(sp);
                portalPersistence.create(spml);
            }

            return Response.ok(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Product")).build();
        }
    }

    @POST
    @Path("createStation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStation(Station station) {
        try {
            Date today = new Date();
            station.setCreatedDate(today);
            station.setModifiedDate(today);
            portalPersistence.create(station);

            List<Product> genericProducts = portalPersistence.getGenericProduct();
            for (Product p : genericProducts) {
                StationProduct sp = new StationProduct();
                sp.setActive(false);
                sp.setAddedDate(today);
                sp.setProduct(p);
                sp.setStation(station);
                portalPersistence.create(sp);

                StationProductPrice spp = new StationProductPrice();
                spp.setActive(true);
                spp.setCreatedDate(today);
                spp.setPrice(0.0);
                spp.setStationProduct(sp);
                portalPersistence.create(spp);

                StationProductMinLevel spml = new StationProductMinLevel();
                spml.setActive(true);
                spml.setCreatedDate(today);
                spml.setMininumLevel(0.0);
                spml.setStationProduct(sp);
                portalPersistence.create(spml);
            }

            List<Product> businessProducts = portalPersistence.getAllProductByBusiness(station.getBusiness());
            for (Product p : businessProducts) {
                StationProduct sp = new StationProduct();
                sp.setActive(false);
                sp.setAddedDate(today);
                sp.setProduct(p);
                sp.setStation(station);
                portalPersistence.create(sp);

                StationProductPrice spp = new StationProductPrice();
                spp.setActive(true);
                spp.setCreatedDate(today);
                spp.setPrice(0.0);
                spp.setStationProduct(sp);
                portalPersistence.create(spp);

                StationProductMinLevel spml = new StationProductMinLevel();
                spml.setActive(true);
                spml.setCreatedDate(today);
                spml.setMininumLevel(0.0);
                spml.setStationProduct(sp);
                portalPersistence.create(spml);
            }
            return Response.status(200).entity(station).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Station")).build();
        }
    }

    @POST
    @Path("updateBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBusiness(Business business) {
        try {
            Date today = new Date();
            Business b = portalPersistence.find(Business.class, business.getId());
            b.setModifiedDate(today);
            b.setName(business.getName());
            b.setMailingAddress(business.getMailingAddress());
            portalPersistence.update(b);
            return Response.status(200).entity(business).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Business")).build();
        }
    }

    @POST
    @Path("updatePaymentOption")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePaymentOption(PaymentOption paymentOption) {
        try {
            PaymentOption p = portalPersistence.find(PaymentOption.class, paymentOption.getId());
            p.setModifiedDate(new Date());
            p.setActive(paymentOption.isActive());
            p.setDescription(paymentOption.getDescription());
            p.setName(paymentOption.getName());
            portalPersistence.update(p);
            return Response.status(200).entity(paymentOption).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Payment Option")).build();
        }
    }

    @GET
    @Path("updateProductPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductPrice(@QueryParam("stationProductId") Long stationProductId, @QueryParam("newPrice") Double newPrice) {
        try {
            StationProduct sp = portalPersistence.find(StationProduct.class, stationProductId);
            List<StationProductPrice> spps = portalPersistence.getAllActiveStationProductPriceByStationProduct(sp);
            for (StationProductPrice price : spps) {
                price.setActive(false);
                portalPersistence.update(price);
            }
            StationProductPrice spp = new StationProductPrice();
            spp.setActive(true);
            spp.setCreatedDate(new Date());
            spp.setPrice(newPrice);
            spp.setStationProduct(sp);
            portalPersistence.create(spp);
            return Response.status(200).entity(spp).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Product Price")).build();
        }
    }

    @GET
    @Path("updateProductMinLevel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductMinLevel(@QueryParam("stationProductId") Long stationProductId, @QueryParam("newMinLevel") Double newMinLevel) {
        try {
            StationProduct sp = portalPersistence.find(StationProduct.class, stationProductId);
            List<StationProductMinLevel> spmls = portalPersistence.getAllActiveStationProductMinLevelByStationProduct(sp);
            for (StationProductMinLevel spml : spmls) {
                spml.setActive(false);
                portalPersistence.update(spml);
            }
            StationProductMinLevel spml = new StationProductMinLevel();
            spml.setActive(true);
            spml.setCreatedDate(new Date());
            spml.setMininumLevel(newMinLevel);
            spml.setStationProduct(sp);
            portalPersistence.create(spml);
            return Response.status(200).entity(spml).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Product Min Level")).build();
        }
    }

    @GET
    @Path("updateProductLevel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductLevel(@QueryParam("stationProductId") Long stationProductId, @QueryParam("newProductSupply") Double newProductSupply) {
        try {
            StationProduct sp = portalPersistence.find(StationProduct.class, stationProductId);
            ProductLevel pl = portalPersistence.getActiveStationProductLevelByStationProduct(sp);
            Double plNewLevel = 0.0;
            Double totalSales = 0.0;
            if (pl != null) {
                plNewLevel = pl.getNewLevel();
                totalSales = getTotalSalesFromPreviousSupplyToDate(pl);
            }
            if (totalSales == null) {
                totalSales = 0.0;
            }

            ProductLevel npl = new ProductLevel();
            npl.setActive(true);
            npl.setCreatedDate(new Date());
            npl.setModifiedDate(new Date());
            npl.setPreviousLevel(plNewLevel - totalSales);
            npl.setNewLevel(newProductSupply + (plNewLevel - totalSales));
            npl.setNewSupply(newProductSupply);
            npl.setStationProduct(sp);
            portalPersistence.create(npl);

            if (pl != null) {
                pl.setActive(false);
                pl.setModifiedDate(new Date());
                portalPersistence.update(pl);
            }
            return Response.status(200).entity(npl).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Product Level")).build();
        }
    }

    @GET
    @Path("getCurrentProductLevel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentProductLevel(@QueryParam("stationProductId") Long stationProductId) {
        try {
            StationProduct sp = portalPersistence.find(StationProduct.class, stationProductId);
            ProductLevel pl = portalPersistence.getActiveStationProductLevelByStationProduct(sp);
            Double currentProductLevel = 0.0;
            Double totalSales;
            if (pl != null) {
                currentProductLevel = pl.getNewLevel();
                totalSales = getTotalSalesFromPreviousSupplyToDate(pl);
                if (totalSales != null) {
                    currentProductLevel = pl.getNewLevel() - totalSales;
                }

            }
            return Response.status(200).entity(currentProductLevel).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Dispensing Point Attendant Reset Unsuccessful")).build();
        }
    }

//    @GET
//    @Path("changeDispensingAttendant")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response changeDispensingAttendant(@QueryParam("userid") Long userid, @QueryParam("stationProductId") Long stationProductId, @QueryParam("newPrice") Double newPrice) {
//        try {
//            StationProduct sp = portalPersistence.find(StationProduct.class, stationProductId);
//            List<StationProductPrice> spps = portalPersistence.getAllActiveStationProductPriceByStationProduct(sp);
//            for (StationProductPrice price : spps) {
//                price.setActive(false);
//                portalPersistence.update(sp);
//            }
//            StationProductPrice spp = new StationProductPrice();
//            spp.setActive(true);
//            spp.setCreatedDate(new Date());
//            spp.setPrice(newPrice);
//            spp.setStationProduct(sp);
//            portalPersistence.create(spp);
//            return Response.status(200).entity(spp).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(404).entity(new StringBuilder("Error Updating Product Price")).build();
//        }
//    }
    @POST
    @Path("uploadBusinessLogo")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadBusinessLogo(MultipartFormDataInput image) {
        String fileName = "";

        Map<String, List<InputPart>> uploadForm = image.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("image");

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);
                System.out.println("business Id " + fileName);

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);

                Business b = portalPersistence.find(Business.class, 8L);
                b.setLogo(bytes);
                portalPersistence.update(b);
                System.out.println("Done");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();
    }

    //get uploaded filename, is there a easy way in RESTEasy?
    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    @GET
    @Path("getUserRole")
    @Produces(MediaType.APPLICATION_JSON)
    public PortalUserRole getUserRole(@QueryParam("userId") Long userId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userId);
        PortalUserRole pur = null;
        if (pu != null) {
            pur = portalPersistence.getActivePortalUserRole(pu);
            if (!pur.isActive()) {
                return null;
            }
            if (pur.getDesignation().equals(PortalUserRole.Designation.ATTENDANT)) {
                DispensingPointAttendant dpa = portalPersistence.getDispensingPointAttendantByActiveAttendant(pur.getPortalUser());
                if (dpa != null) {
                    pur.setDispensingPointAttendant(dpa);
                }
            }
        }
        return pur;
    }

    @GET
    @Path("getUserBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBusiness(@QueryParam("userId") Long userId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userId);
        PortalUserRole pur = null;
        Business b = null;
        if (pu != null) {
            pur = portalPersistence.getPortalUserRole(pu);
        }
        if (pur != null) {
            b = pur.getBusiness();
        }
        return Response.ok(b).build();
    }

    @GET
    @Path("getBusinessStaff")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PortalUser> getBusinessStaff(@QueryParam("businessId") Long businessId) {
        Business b = portalPersistence.find(Business.class, businessId);
        List<PortalUser> users = portalPersistence.getAllBusinessUser(b);
        return users;
    }

    @GET
    @Path("getStationStaff")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PortalUser> getStationStaff(@QueryParam("stationId") Long stationId) {
        Station s = portalPersistence.find(Station.class, stationId);
        List<PortalUser> users = portalPersistence.getAllStationUser(s);
        return users;
    }

    @GET
    @Path("filterStationAttendants")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PortalUser> filterStationAttendants(@QueryParam("stationId") Long stationId) {
        Station s = portalPersistence.find(Station.class, stationId);
        List<PortalUser> users = portalPersistence.getAllStationUser(s);
        List<PortalUser> stationUsers = new ArrayList<>();
        for (PortalUser p : users) {
            PortalUserRole pur = portalPersistence.getPortalUserRole(p);
            if (pur.isActive() && pur.getPortalUser().isActive() && pur.getDesignation().equals(PortalUserRole.Designation.ATTENDANT)) {
                p.setDesignation(pur.getDesignation().getDesignation());
                p.setStationId(pur.getStation().getId());
                stationUsers.add(p);
            }
        }

        List<PortalUser> attendants = portalPersistence.getAllActiveDispensingPointAttendant(s);
        stationUsers.removeAll(attendants);
        return stationUsers;
    }

    @GET
    @Path("updateProductDispensingPointAttendant")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductDispensingPointAttendant(@QueryParam("attendantId") Long attendantId, @QueryParam("dispensingPointId") Long dispensingPointId) {
        try {
            ProductDispensingPoint pdp = portalPersistence.find(ProductDispensingPoint.class, dispensingPointId);
            List<DispensingPointAttendant> dpas = portalPersistence.getAllActiveDispensingPointAttendant(pdp);
            for (DispensingPointAttendant dpa : dpas) {
                dpa.setActive(false);
                portalPersistence.update(dpa);
            }
            DispensingPointAttendant dpa = new DispensingPointAttendant();
            if (attendantId == 0) {

            } else {
                dpa.setActive(true);
                dpa.setAttendant(portalPersistence.find(PortalUser.class, attendantId));
                dpa.setCreatedDate(new Date());
                dpa.setDispensingPoint(pdp);
                portalPersistence.create(dpa);
            }

            return Response.ok(dpa).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Dispensing Point Attendant")).build();
        }
    }

    @GET
    @Path("getBusinessStation")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Station> getBusinessStation(@QueryParam("businessId") Long businessId) {
        Business b = portalPersistence.find(Business.class, businessId);
        List<Station> stations = portalPersistence.getAllStationByBusiness(b);
        return stations;
    }

    @GET
    @Path("getBusinessProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getBusinessProduct(@QueryParam("businessId") Long businessId) {
        Business b = portalPersistence.find(Business.class, businessId);
        List<Product> products = portalPersistence.getGenericProduct();
        products.addAll(portalPersistence.getAllProductByBusiness(b));
        return products;
    }

    @GET
    @Path("getBusinessPaymentOption")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PaymentOption> getBusinessPaymentOption(@QueryParam("businessId") Long businessId) {
        Business b = portalPersistence.find(Business.class, businessId);
        List<PaymentOption> paymentOptions = portalPersistence.getGenericPaymentOption();
        paymentOptions.addAll(portalPersistence.getAllPaymentOptionByBusiness(b));
        return paymentOptions;
    }

    @GET
    @Path("getStationProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationProduct> getStationProduct(@QueryParam("stationId") Long stationId) {
        Station s = portalPersistence.find(Station.class, stationId);
        List<StationProduct> products = portalPersistence.getAllStationProduct(s);
        return products;
    }

    @GET
    @Path("getStationPaymentOption")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationPaymentOption> getStationPaymentOption(@QueryParam("stationId") Long stationId) {
        Station s = portalPersistence.find(Station.class, stationId);
        List<StationPaymentOption> spos = portalPersistence.getAllStationPaymentOption(s);
        return spos;
    }

    @GET
    @Path("getActiveStationProductPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public StationProductPrice getActiveStationProductPrice(@QueryParam("stationProductId") Long stationProductId) {
        StationProduct s = portalPersistence.find(StationProduct.class, stationProductId);
        StationProductPrice sp = portalPersistence.getCurrentStationProductPriceByStationProduct(s);
        return sp;
    }

    @GET
    @Path("getActiveStationProductMinLevel")
    @Produces(MediaType.APPLICATION_JSON)
    public StationProductMinLevel getActiveStationProductMinLevel(@QueryParam("stationProductId") Long stationProductId) {
        StationProduct s = portalPersistence.find(StationProduct.class, stationProductId);
        StationProductMinLevel spml = portalPersistence.getCurrentStationProductMinLevelByStationProduct(s);
        return spml;
    }

    @GET
    @Path("getActiveDispensingPointAttendant")
    @Produces(MediaType.APPLICATION_JSON)
    public PortalUser getActiveDispensingPointAttendant(@QueryParam("dispensingPointId") Long dispensingPointId) {
        ProductDispensingPoint pdp = portalPersistence.find(ProductDispensingPoint.class, dispensingPointId);
        PortalUser pu = portalPersistence.getCurrentDispensingAttendantByProductDispensingPoint(pdp);
        return pu;
    }

    @GET
    @Path("getDispensingPointAttendantByActiveAttendant")
    @Produces(MediaType.APPLICATION_JSON)
    public DispensingPointAttendant getDispensingPointAttendantByActiveAttendant(@QueryParam("userid") Long userid) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        DispensingPointAttendant dpa = portalPersistence.getDispensingPointAttendantByActiveAttendant(pu);
        return dpa;
    }

    @GET
    @Path("getStationProductPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StationProductPrice> getStationProductPrice(@QueryParam("stationId") Long stationId) {
        Station s = portalPersistence.find(Station.class, stationId);
        List<StationProductPrice> spps = portalPersistence.getAllStationProductPriceByStation(s);
        return spps;
    }

    @GET
    @Path("getActiveBusinessStation")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Station> getActiveBusinessStation(@QueryParam("businessId") Long businessId) {
        Business b = portalPersistence.find(Business.class, businessId);
        List<Station> stations = portalPersistence.getAllActiveStationByBusiness(b);
        return stations;
    }

    @GET
    @Path("getDispensingPointByStation")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDispensingPoint> getDispensingPointByStation(@QueryParam("stationId") Long stationId) {
        Station s = portalPersistence.find(Station.class, stationId);
        List<ProductDispensingPoint> pdps = portalPersistence.getAllDispensingPointByStation(s);
        return pdps;
    }

    @GET
    @Path("getAllUnapprovedBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Business> getAllUnapprovedBusiness() {
        List<Business> bizs = portalPersistence.getAllUnApprovedBusiness();
        return bizs;
    }

    @GET
    @Path("getAllBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Business> getAllBusiness() {
        List<Business> bizs = portalPersistence.getAllBusiness();
        return bizs;
    }

    @GET
    @Path("approveBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveBusiness(@QueryParam("businessId") Long businessId) {
        try {
            Business b = portalPersistence.find(Business.class, businessId);
            b.setApproved(true);
            portalPersistence.update(b);

            String message = "Congratulations. Your business has been approved. \n"
                    + "\n"
                    + "Thank you for using Gapso.";
            String receipient = b.getOwner().getEmailAddress();
            String subject = "Business Approved";
            emailService.prepareEmail(message, subject, receipient);
            return Response.ok(b).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Approving Business")).build();
        }
    }

    @GET
    @Path("declineBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response declineBusiness(@QueryParam("businessId") Long businessId) {
        try {
            Business b = portalPersistence.find(Business.class, businessId);

            String message = "Business Registration Request Received. Admin will contact you once your request is approved. \n"
                    + "\n"
                    + "Thank you for using Gapso.";
            String receipient = b.getOwner().getEmailAddress();
            String subject = "Business Registration Request Received";
            emailService.prepareEmail(message, subject, receipient);
            return Response.ok(b).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Approving Business")).build();
        }
    }

    @GET
    @Path("activateBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateBusiness(@QueryParam("businessId") Long businessId) {
        try {
            Business b = portalPersistence.find(Business.class, businessId);
            b.setActive(true);
            portalPersistence.update(b);
            return Response.ok(b).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Approving Business")).build();
        }
    }

    @GET
    @Path("deactivateBusiness")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateBusiness(@QueryParam("businessId") Long businessId) {
        try {
            Business b = portalPersistence.find(Business.class, businessId);
            b.setActive(false);
            portalPersistence.update(b);
            return Response.ok(b).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Approving Business")).build();
        }
    }

    @GET
    @Path("getBusinessLogo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBusinessLogo(@QueryParam("businessId") Long businessId) {
        try {
            Business b = portalPersistence.find(Business.class, businessId);
            byte[] logo = b.getLogo();
            String encoded = Base64.getEncoder().encodeToString(logo);
            return Response.ok(encoded).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Getting Business Logo")).build();
        }
    }

    @GET
    @Path("stationManagerCheck")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stationManagerCheck(@QueryParam("stationId") Long stationId) {
        try {
            Station s = portalPersistence.find(Station.class, stationId);
            boolean hasStationManager = portalPersistence.stationManagerCheck(s);
            return Response.ok(hasStationManager).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Doing Statino Manager Check")).build();
        }
    }

    @POST
    @Path("createStationProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStationProduct(List<Long> sps) {
        try {
            for (Long spid : sps) {
                StationProduct sp = portalPersistence.find(StationProduct.class, spid);
                sp.setActive(true);
                portalPersistence.update(sp);

                StationProductPrice spp = new StationProductPrice();
                spp.setActive(true);
                spp.setCreatedDate(new Date());
                spp.setPrice(0.0);
                spp.setStationProduct(sp);
                portalPersistence.create(spp);

                StationProductMinLevel spml = new StationProductMinLevel();
                spml.setActive(true);
                spml.setCreatedDate(new Date());
                spml.setMininumLevel(0.0);
                spml.setStationProduct(sp);
                portalPersistence.create(spml);
            }
            return Response.ok(sps).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Station Product")).build();
        }
    }

    @POST
    @Path("recordSales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recordSales(DailySales ds) {
        try {
            StationPaymentOption spo = portalPersistence.find(StationPaymentOption.class, ds.getPaymentOptionId());
            ds.setSaleDate(new Date());
            ds.setStationPaymentOption(spo);
            ds.setQuantity(Double.valueOf(String.format("%.2g%n", ds.getQuantity())));
            portalPersistence.create(ds);
            return Response.ok(ds).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Recording Sales")).build();
        }
    }

    @POST
    @Path("removeStationProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeStationProduct(List<Long> sps) {
        try {
            for (Long spid : sps) {
                StationProduct sp = portalPersistence.find(StationProduct.class, spid);
                sp.setActive(false);
                portalPersistence.update(sp);
            }
            return Response.ok(sps).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Removing Station Product")).build();
        }
    }

    @POST
    @Path("createStationPaymentOption")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStationPaymentOption(List<Long> sps) {
        try {
            for (Long spid : sps) {
                StationPaymentOption spo = portalPersistence.find(StationPaymentOption.class, spid);
                spo.setActive(true);
                portalPersistence.update(spo);
            }
            return Response.ok(sps).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Station Payment Option")).build();
        }
    }

    @POST
    @Path("removeStationPaymentOption")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeStationPaymentOption(List<Long> sps) {
        try {
            for (Long spid : sps) {
                StationPaymentOption spo = portalPersistence.find(StationPaymentOption.class, spid);
                spo.setActive(false);
                portalPersistence.update(spo);
            }
            return Response.ok(sps).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Removing Station Payment Option")).build();
        }
    }

    @POST
    @Path("createProductDispensingPoint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProductDispensingPoint(ProductDispensingPoint pdp) {
        try {
            ProductDispensingPoint dispensingPoint = new ProductDispensingPoint();
            dispensingPoint.setActive(pdp.isActive());
            dispensingPoint.setCreatedDate(new Date());
            dispensingPoint.setModifiedDate(new Date());
            dispensingPoint.setName(pdp.getName());
            dispensingPoint.setProduct(pdp.getProduct());
            dispensingPoint.setStation(pdp.getStation());
            portalPersistence.create(dispensingPoint);
            return Response.ok(dispensingPoint).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Dispensing Point")).build();
        }
    }

    @GET
    @Path("createDispensingPointAttendant")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDispensingPointAttendant(@QueryParam("attendantId") Long attendantId, @QueryParam("productDispensingPointId") Long productDispensingPointId) {
        try {
            ProductDispensingPoint dispensingPoint = portalPersistence.find(ProductDispensingPoint.class, productDispensingPointId);
            PortalUser pu = portalPersistence.find(PortalUser.class, attendantId);
            DispensingPointAttendant dpa = new DispensingPointAttendant();
            dpa.setActive(true);
            dpa.setAttendant(pu);
            dpa.setCreatedDate(new Date());
            dpa.setDispensingPoint(dispensingPoint);
            portalPersistence.create(dpa);
            return Response.ok(dispensingPoint).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Creating Dispensing Point")).build();
        }
    }

    @POST
    @Path("updateProductDispensingPoint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductDispensingPoint(ProductDispensingPoint pdp) {
        try {
            ProductDispensingPoint dispensingPoint = portalPersistence.find(ProductDispensingPoint.class, pdp.getId());
            dispensingPoint.setActive(pdp.isActive());
            dispensingPoint.setModifiedDate(new Date());
            dispensingPoint.setName(pdp.getName());
            dispensingPoint.setProduct(pdp.getProduct());
            portalPersistence.update(dispensingPoint);
            return Response.ok(dispensingPoint).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error Updating Dispensing Point")).build();
        }
    }

    @GET
    @Path("activateUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateUser(@QueryParam("portalUserId") Long portalUserId) {
        try {
            PortalUser pu = portalPersistence.find(PortalUser.class, portalUserId);
            pu.setActive(true);
            pu.setModifiedDate(new Date());
            portalPersistence.update(pu);
            return Response.ok(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("User Activation Unsuccessful")).build();
        }
    }

    @GET
    @Path("deactivateUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateUser(@QueryParam("portalUserId") Long portalUserId) {
        try {
            PortalUser pu = portalPersistence.find(PortalUser.class, portalUserId);
            pu.setActive(false);
            pu.setModifiedDate(new Date());
            portalPersistence.update(pu);
            return Response.ok(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("User De-Activation Unsuccessful")).build();
        }
    }

    @GET
    @Path("activateProductDispensingPoint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response activateProductDispensingPoint(@QueryParam("pdpId") Long pdpId) {
        try {
            ProductDispensingPoint pdp = portalPersistence.find(ProductDispensingPoint.class, pdpId);
            pdp.setActive(true);
            pdp.setModifiedDate(new Date());
            portalPersistence.update(pdp);
            return Response.ok(pdp).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Product Dispensing Point Activation Unsuccessful")).build();
        }
    }

    @GET
    @Path("deactivateProductDispensingPoint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateProductDispensingPoint(@QueryParam("pdpId") Long pdpId) {
        try {
            ProductDispensingPoint pdp = portalPersistence.find(ProductDispensingPoint.class, pdpId);
            pdp.setActive(false);
            pdp.setModifiedDate(new Date());
            portalPersistence.update(pdp);
            return Response.ok(pdp).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Product Dispensing Point De-Activation Unsuccessful")).build();
        }
    }

    @GET
    @Path("resetDispenseAttendants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetDispenseAttendants(@QueryParam("stationId") Long stationId) {
        try {
            Station s = portalPersistence.find(Station.class, stationId);
            List<ProductDispensingPoint> pdps = portalPersistence.getAllDispensingPointByStation(s);
            for (ProductDispensingPoint pdp : pdps) {
                pdp.setActive(false);
                pdp.setModifiedDate(new Date());
                portalPersistence.update(pdp);
            }
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Attendant Reset Unsuccessful")).build();
        }
    }

    @GET
    @Path("getDispensingPointByAttendant")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDispensingPointByAttendant(@QueryParam("dispointAttendantId") Long dispointAttendantId) {
        try {
            DispensingPointAttendant dpa = portalPersistence.find(DispensingPointAttendant.class, dispointAttendantId);
            ProductDispensingPoint pdp = dpa.getDispensingPoint();
            return Response.ok(pdp).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Dispensing Point Attendant Unsuccessful")).build();
        }
    }

    @GET
    @Path("getStationProductByStationAndProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStationProductByStationAndProduct(@QueryParam("stationId") Long stationId, @QueryParam("productId") Long productId) {
        try {
            StationProduct sp = portalPersistence.getStationProductByStationAndProduct(portalPersistence.find(Station.class, stationId), portalPersistence.find(Product.class, productId));
            return Response.ok(sp).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Getting Station Product Unsuccessful")).build();
        }
    }

    @GET
    @Path("resetDispensingPointAttendants")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetDispensingPointAttendants(@QueryParam("stationId") Long stationId) {
        try {
            Station s = portalPersistence.find(Station.class, stationId);
            List<DispensingPointAttendant> dpas = portalPersistence.getActiveDispensingAttendantByStation(s);
            for (DispensingPointAttendant dpa : dpas) {
                dpa.setActive(false);
                portalPersistence.update(dpa);
            }
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Dispensing Point Attendant Reset Unsuccessful")).build();
        }
    }

    @GET
    @Path("checkBusinessManagerValidity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkBusinessManagerValidity(@QueryParam("userid") Long userid, @QueryParam("businessId") Long businessId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        String response = "FALSE";
        if (pu.isActive()) {
            Business b = portalPersistence.find(Business.class, businessId);
            if (b.isActive() && b.isApproved()) {
                response = "TRUE";
            }
        }
        return Response.status(200).entity(new StringBuilder(response)).build();
    }

    @GET
    @Path("checkStationManagerValidity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkStationManagerValidity(@QueryParam("userid") Long userid, @QueryParam("stationId") Long stationId, @QueryParam("roleId") Long roleId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        String response = "FALSE";
        if (pu.isActive()) {
            PortalUserRole pur = portalPersistence.find(PortalUserRole.class, roleId);
            if (pur.isActive()) {
                Station s = portalPersistence.find(Station.class, stationId);
                if (s.isActive()) {
                    if (s.getBusiness().isActive() && s.getBusiness().isApproved()) {
                        response = "TRUE";
                    }
                }
            }
        }
        return Response.status(200).entity(new StringBuilder(response)).build();
    }

    @GET
    @Path("checkAttendantValidity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAttendantValidity(@QueryParam("userid") Long userid, @QueryParam("dispAttendantId") Long dispAttendantId, @QueryParam("roleId") Long roleId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        String response = "FALSE";
        if (pu.isActive()) {
            PortalUserRole pur = portalPersistence.find(PortalUserRole.class, roleId);
            if (pur.isActive()) {
                DispensingPointAttendant dpa = portalPersistence.find(DispensingPointAttendant.class, dispAttendantId);
                if (dpa.isActive()) {
                    if (dpa.getDispensingPoint().isActive()) {
                        if (dpa.getDispensingPoint().getStation().isActive()) {
                            if (dpa.getDispensingPoint().getStation().getBusiness().isActive() && dpa.getDispensingPoint().getStation().getBusiness().isApproved()) {
                                response = "TRUE";
                            }
                        }
                    }
                }
            }
        }
        return Response.status(200).entity(new StringBuilder(response)).build();
    }

    @GET
    @Path("getTodaySaleAmount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodaySaleAmount(@QueryParam("attendantId") Long attendantId) {

        try {
            DispensingPointAttendant dpa = portalPersistence.find(DispensingPointAttendant.class, attendantId);
            Double salesQuantity = portalPersistence.getTodaySaleAmount(dpa);
            return Response.ok(salesQuantity).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error getting Sales Amount")).build();
        }

    }

    @GET
    @Path("getTodaySaleQuantity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodaySaleQuantity(@QueryParam("attendantId") Long attendantId) {

        try {
            DispensingPointAttendant dpa = portalPersistence.find(DispensingPointAttendant.class, attendantId);
            Double salesQuantity = portalPersistence.getTodaySaleQuantity(dpa);
            return Response.ok(salesQuantity).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error getting Sales Quantity")).build();
        }

    }

    @GET
    @Path("getTodaySale")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodaySale(@QueryParam("attendantId") Long attendantId) {

        try {
            DispensingPointAttendant dpa = portalPersistence.find(DispensingPointAttendant.class, attendantId);
            List<DailySales> todaySales = portalPersistence.getTodaySale(dpa);
            return Response.ok(todaySales).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity(new StringBuilder("Error getting Today Sales")).build();
        }

    }

    private Double getTotalSalesFromPreviousSupplyToDate(ProductLevel cLevel) {
        Double sales;
        sales = portalPersistence.getTotalSalesFromPreviousSupplyToDate(cLevel.getCreatedDate());
        return sales;
    }

}
