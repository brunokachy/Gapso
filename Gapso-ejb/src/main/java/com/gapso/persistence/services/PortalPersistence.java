/**
 *Class Name: PortalPersistence
 *Project Name: Gapso-ejb
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 13, 2017 6:06:23 PM
 */
package com.gapso.persistence.services;

import com.gapso.persistence.entity.activity.DailySales;
import com.gapso.persistence.entity.activity.ProductLevel;
import com.gapso.persistence.entity.activity.StationProductMinLevel;
import com.gapso.persistence.entity.activity.StationProductPrice;
import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.PaymentOption;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.entity.portal.Product;
import com.gapso.persistence.entity.portal.ProductDispensingPoint;
import com.gapso.persistence.entity.portal.PushRegister;
import com.gapso.persistence.entity.portal.Setting;
import com.gapso.persistence.entity.portal.Station;
import com.gapso.persistence.entity.portal.StationPaymentOption;
import com.gapso.persistence.entity.portal.StationProduct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

/**
 * @author Onyedika Okafor
 */
@Stateless
public class PortalPersistence {

    @PersistenceContext(unitName = "GapsoPU")
    private EntityManager em;

    public <T> T create(T t) {
        em.persist(t);
        em.flush();
        return t;
    }

    public <T> T find(Class<T> type, Object id) {
        return em.find(type, id);
    }

    public <T> void delete(T t) {
        em.remove(em.merge(t));
    }

    public <T> T update(T t) {
        return em.merge(t);
    }

    //Business Business Methods
    public Business getBusinessByOwner(PortalUser owner) {
        Business b = null;
        try {
            b = em.createQuery("SELECT o FROM Business o WHERE o.owner =:owner", Business.class)
                    .setParameter("owner", owner)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return b;
    }

    public List<Business> getAllBusiness() {
        List<Business> businesses = new ArrayList<>();
        try {
            businesses = em.createQuery("SELECT o FROM Business o", Business.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return businesses;
    }

    public List<Business> getAllActiveBusiness() {
        List<Business> businesses = new ArrayList<>();
        try {
            businesses = em.createQuery("SELECT o FROM Business o WHERE o.active = TRUE", Business.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return businesses;
    }

    public List<Business> getAllInActiveBusiness() {
        List<Business> businesses = new ArrayList<>();
        try {
            businesses = em.createQuery("SELECT o FROM Business o WHERE o.active = FALSE", Business.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return businesses;
    }

    public List<Business> getAllUnApprovedBusiness() {
        List<Business> businesses = new ArrayList<>();
        try {
            businesses = em.createQuery("SELECT o FROM Business o WHERE o.approved = FALSE", Business.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return businesses;
    }

    public List<Business> getAllApprovedBusiness() {
        List<Business> businesses = new ArrayList<>();
        try {
            businesses = em.createQuery("SELECT o FROM Business o WHERE o.approved = TRUE", Business.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return businesses;
    }

    public DispensingPointAttendant getDispensingPointAttendantByActiveAttendant(PortalUser pu) {
        DispensingPointAttendant dpa = null;
        try {
            dpa = em.createQuery("SELECT o FROM DispensingPointAttendant o WHERE o.attendant =:pu AND o.active = TRUE", DispensingPointAttendant.class)
                    .setParameter("pu", pu)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return dpa;
    }

    //Portal User Business Method
    public PortalUser getActiveUserByEmailAddress(String emailAddress) {
        PortalUser user = null;
        try {
            user = em.createQuery("SELECT o FROM PortalUser o WHERE o.emailAddress =:emailAddress AND o.active = TRUE", PortalUser.class)
                    .setParameter("emailAddress", emailAddress)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return user;
    }

    public PortalUser getUserByEmailAddress(String emailAddress) {
        PortalUser user = null;
        try {
            user = em.createQuery("SELECT o FROM PortalUser o WHERE o.emailAddress =:emailAddress", PortalUser.class)
                    .setParameter("emailAddress", emailAddress)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return user;
    }

    public PortalUser getUserByToken(String token) {
        PortalUser user = null;
        try {
            user = em.createQuery("SELECT o FROM PortalUser o WHERE o.token =:token", PortalUser.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return user;
    }

    public List<PortalUser> getAllInActiveUser() {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o FROM PortalUser o WHERE o.active = FALSE", PortalUser.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<PortalUser> getAllActiveUser() {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o FROM PortalUser o WHERE o.active = TRUE", PortalUser.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<PortalUser> getAllUser() {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o FROM PortalUser o", PortalUser.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public PortalUserRole getPortalUserRole(PortalUser pu, Business b) {
        PortalUserRole pur = null;
        try {
            pur = em.createQuery("SELECT o FROM PortalUserRole o WHERE o.portalUser =:portalUser AND o.business =:business", PortalUserRole.class)
                    .setParameter("portalUser", pu)
                    .setParameter("business", b)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pur;
    }

    public PortalUserRole getActivePortalUserRole(PortalUser pu) {
        PortalUserRole pur = null;
        try {
            pur = em.createQuery("SELECT o FROM PortalUserRole o WHERE o.portalUser =:portalUser AND o.active = TRUE", PortalUserRole.class)
                    .setParameter("portalUser", pu)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pur;
    }

    public PortalUserRole getPortalUserRole(PortalUser pu) {
        PortalUserRole pur = null;
        try {
            pur = em.createQuery("SELECT o FROM PortalUserRole o WHERE o.portalUser =:portalUser AND o.active = TRUE", PortalUserRole.class)
                    .setParameter("portalUser", pu)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pur;
    }

    public List<PortalUser> getAllActiveBusinessUser(Business b) {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o.portalUser FROM PortalUserRole o WHERE o.business =:b AND o.portalUser.active = TRUE", PortalUser.class)
                    .setParameter("b", b)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<PortalUser> getAllInActiveBusinessUser(Business b) {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o.portalUser FROM PortalUserRole o WHERE o.business =:b AND o.portalUser.active = FALSE", PortalUser.class)
                    .setParameter("b", b)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<PortalUser> getAllBusinessUser(Business b) {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o.portalUser FROM PortalUserRole o WHERE o.business =:b AND o.active = TRUE", PortalUser.class)
                    .setParameter("b", b)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<PortalUser> getAllStationUser(Station s) {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o.portalUser FROM PortalUserRole o WHERE o.station =:s AND o.active = TRUE", PortalUser.class)
                    .setParameter("s", s)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<PortalUser> getAllActiveDispensingPointAttendant(Station s) {
        List<PortalUser> users = new ArrayList<>();
        try {
            users = em.createQuery("SELECT o.attendant FROM DispensingPointAttendant o WHERE o.dispensingPoint.station =:s AND o.active = TRUE", PortalUser.class)
                    .setParameter("s", s)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return users;
    }

    public List<DispensingPointAttendant> getAllActiveDispensingPointAttendant(ProductDispensingPoint pdp) {
        List<DispensingPointAttendant> dpas = new ArrayList<>();
        try {
            dpas = em.createQuery("SELECT o FROM DispensingPointAttendant o WHERE o.dispensingPoint =:pdp AND o.active = TRUE", DispensingPointAttendant.class)
                    .setParameter("pdp", pdp)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return dpas;
    }

    //Push Register Business Method
    public PushRegister getPushRegisterByPortalUser(PortalUser pu) {
        PushRegister pur = null;
        try {
            pur = em.createQuery("SELECT o FROM PushRegister o WHERE o.portalUser =:portalUser", PushRegister.class)
                    .setParameter("portalUser", pu)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pur;
    }

    public PushRegister getPushRegisterByRegisterID(String registerID) {
        PushRegister pur = null;
        try {
            pur = em.createQuery("SELECT o FROM PushRegister o WHERE o.registerID =:registerID", PushRegister.class)
                    .setParameter("registerID", registerID)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pur;
    }

    //Station Business Method
    public List<Station> getAllActiveStationByBusiness(Business business) {
        List<Station> stations = new ArrayList<>();
        try {
            stations = em.createQuery("SELECT o FROM Station o WHERE o.business =:business AND o.active = TRUE", Station.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return stations;
    }

    public List<Station> getAllInActiveStationByBusiness(Business business) {
        List<Station> stations = new ArrayList<>();
        try {
            stations = em.createQuery("SELECT o FROM Station o WHERE o.business =:business AND o.active = FALSE", Station.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return stations;
    }

    public List<Station> getAllStationByBusiness(Business business) {
        List<Station> stations = new ArrayList<>();
        try {
            stations = em.createQuery("SELECT o FROM Station o WHERE o.business =:business", Station.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return stations;
    }

    public List<Station> getAllStationByManager(PortalUser manager) {
        List<Station> stations = new ArrayList<>();
        try {
            stations = em.createQuery("SELECT o FROM Station o WHERE o.manager =:manager", Station.class)
                    .setParameter("manager", manager)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return stations;
    }

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        try {
            stations = em.createQuery("SELECT o FROM Station o", Station.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return stations;
    }

    public boolean stationManagerCheck(Station station) {
        Boolean hasManager = false;
        List<PortalUserRole> purs;
        try {
            purs = em.createQuery("SELECT o FROM PortalUserRole o WHERE o.station =:station AND o.designation =:designation", PortalUserRole.class)
                    .setParameter("station", station)
                    .setParameter("designation", PortalUserRole.Designation.STATIONMANAGER)
                    .getResultList();
            for (PortalUserRole pur : purs) {
                if (pur.getPortalUser().isActive()) {
                    if (pur.isActive()) {
                        hasManager = true;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return hasManager;
    }

    //Product Business Method
    public Product getProductByCodeAndBusiness(String productCode, Business b) {
        Product product = null;
        try {
            product = em.createQuery("SELECT o FROM Product o WHERE o.productCode =:productCode AND o.business =:business", Product.class)
                    .setParameter("productCode", productCode)
                    .setParameter("business", b)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return product;
    }

    public Product getProductByCode(String productCode) {
        Product product = null;
        try {
            product = em.createQuery("SELECT o FROM Product o WHERE o.productCode =:productCode", Product.class)
                    .setParameter("productCode", productCode)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return product;
    }

    public PaymentOption getPaymentOptionByName(String name) {
        PaymentOption po = null;
        try {
            po = em.createQuery("SELECT o FROM PaymentOption o WHERE o.name =:name", PaymentOption.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return po;
    }

    public List<Product> getAllActiveProductByBusiness(Business business) {
        List<Product> prouducts = new ArrayList<>();
        try {
            prouducts = em.createQuery("SELECT o FROM Product o WHERE o.business =:business AND o.active = TRUE", Product.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<Product> getAllInActiveProductByBusiness(Business business) {
        List<Product> prouducts = new ArrayList<>();
        try {
            prouducts = em.createQuery("SELECT o FROM Product o WHERE o.business =:business AND o.active = FALSE", Product.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<Product> getAllProductByBusiness(Business business) {
        List<Product> prouducts = new ArrayList<>();
        try {
            prouducts = em.createQuery("SELECT o FROM Product o WHERE o.business =:business", Product.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<PaymentOption> getAllPaymentOptionByBusiness(Business business) {
        List<PaymentOption> pos = new ArrayList<>();
        try {
            pos = em.createQuery("SELECT o FROM PaymentOption o WHERE o.business =:business", PaymentOption.class)
                    .setParameter("business", business)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pos;
    }

    public List<Product> getGenericProduct() {
        List<Product> prouducts = new ArrayList<>();
        try {
            prouducts = em.createQuery("SELECT o FROM Product o WHERE o.business IS NULL ", Product.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<PaymentOption> getGenericPaymentOption() {
        List<PaymentOption> pos = new ArrayList<>();
        try {
            pos = em.createQuery("SELECT o FROM PaymentOption o WHERE o.business IS NULL ", PaymentOption.class)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pos;
    }

    public List<Product> getAllActiveStationProduct(Station station) {
        List<Product> prouducts = new ArrayList<>();
        try {
            prouducts = em.createQuery("SELECT o.product FROM StationProduct o WHERE o.station =:station AND o.active = TRUE", Product.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<Product> getAllInActiveStationProduct(Station station) {
        List<Product> prouducts = new ArrayList<>();
        try {
            prouducts = em.createQuery("SELECT o.product FROM StationProduct o WHERE o.station =:station AND o.active = FALSE", Product.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<StationProduct> getAllStationProduct(Station station) {
        List<StationProduct> sps = new ArrayList<>();
        try {
            sps = em.createQuery("SELECT o FROM StationProduct o WHERE o.station =:station", StationProduct.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return sps;
    }

    public List<StationPaymentOption> getAllStationPaymentOption(Station station) {
        List<StationPaymentOption> spos = new ArrayList<>();
        try {
            spos = em.createQuery("SELECT o FROM StationPaymentOption o WHERE o.station =:station", StationPaymentOption.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return spos;
    }

    public StationProduct getStationProductByStationAndProduct(Station station, Product product) {
        StationProduct prouducts = null;
        try {
            prouducts = em.createQuery("SELECT o FROM StationProduct o WHERE o.station =:station AND o.product=:product", StationProduct.class)
                    .setParameter("station", station)
                    .setParameter("product", product)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return prouducts;
    }

    public List<StationProductPrice> getAllActiveStationProductPriceByStation(Station station) {
        List<StationProductPrice> spps = new ArrayList<>();
        try {
            spps = em.createQuery("SELECT o FROM StationProductPrice o WHERE o.stationProduct.station =:station AND o.active = TRUE", StationProductPrice.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return spps;
    }

    public List<ProductDispensingPoint> getAllDispensingPointByStation(Station station) {
        List<ProductDispensingPoint> pdps = new ArrayList<>();
        try {
            pdps = em.createQuery("SELECT o FROM ProductDispensingPoint o WHERE o.station =:station", ProductDispensingPoint.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pdps;
    }

    public List<StationProductPrice> getAllActiveStationProductPriceByStationProduct(StationProduct stationProduct) {
        List<StationProductPrice> spps = new ArrayList<>();
        try {
            spps = em.createQuery("SELECT o FROM StationProductPrice o WHERE o.stationProduct =:stationProduct AND o.active = TRUE", StationProductPrice.class)
                    .setParameter("stationProduct", stationProduct)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return spps;
    }

    public List<StationProductMinLevel> getAllActiveStationProductMinLevelByStationProduct(StationProduct stationProduct) {
        List<StationProductMinLevel> spmls = new ArrayList<>();
        try {
            spmls = em.createQuery("SELECT o FROM StationProductMinLevel o WHERE o.stationProduct =:stationProduct AND o.active = TRUE", StationProductMinLevel.class)
                    .setParameter("stationProduct", stationProduct)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return spmls;
    }

    public ProductLevel getActiveStationProductLevelByStationProduct(StationProduct stationProduct) {
        ProductLevel pl = null;
        try {
            pl = em.createQuery("SELECT o FROM ProductLevel o WHERE o.stationProduct =:stationProduct AND o.active = TRUE", ProductLevel.class)
                    .setParameter("stationProduct", stationProduct)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pl;
    }

    public Double getTotalSalesFromPreviousSupplyToDate(Date startDate) {
        Double totalSales = 0.0;
        try {
            totalSales = (Double) em.createQuery("SELECT SUM(o.quantity) FROM DailySales o WHERE o.saleDate < CURRENT_TIMESTAMP AND o.saleDate >:startDate")
                    .setParameter("startDate", startDate)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return totalSales;
    }

    public Double getTodaySaleQuantity(DispensingPointAttendant dpa) {
        List<DailySales> dss = new ArrayList<>();
        Double totalSalesQuantity = 0.0;
        Date today = Util.today();

        try {
            dss = em.createQuery("SELECT o FROM DailySales o WHERE o.dispensingPointAttendant =:dpa", DailySales.class)
                    .setParameter("dpa", dpa)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        for (DailySales ds : dss) {
            if (ds.getSaleDate().getTime() >= today.getTime() && ds.getSaleDate().getTime() <= new Date().getTime()) {
                totalSalesQuantity += ds.getQuantity();
            }
        }
        return totalSalesQuantity;
    }

    public Double getTodaySaleAmount(DispensingPointAttendant dpa) {
        List<DailySales> dss = new ArrayList<>();
        Double totalSales = 0.0;
        Date today = Util.today();

        try {
            dss = em.createQuery("SELECT o FROM DailySales o WHERE o.dispensingPointAttendant =:dpa", DailySales.class)
                    .setParameter("dpa", dpa)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        for (DailySales ds : dss) {
            if (ds.getSaleDate().getTime() >= today.getTime() && ds.getSaleDate().getTime() <= new Date().getTime()) {
                totalSales += (ds.getQuantity() * ds.getStationProductPrice().getPrice());
            }
        }
        return totalSales;
    }

    public List<DailySales> getTodaySale(DispensingPointAttendant dpa) {
        List<DailySales> dss = new ArrayList<>();
        List<DailySales> todaySales = new ArrayList<>();
        Date today = Util.today();

        try {
            dss = em.createQuery("SELECT o FROM DailySales o WHERE o.dispensingPointAttendant =:dpa", DailySales.class)
                    .setParameter("dpa", dpa)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        for (DailySales ds : dss) {
            if (ds.getSaleDate().getTime() >= today.getTime() && ds.getSaleDate().getTime() <= new Date().getTime()) {
                todaySales.add(ds);
            }
        }
        return todaySales;
    }

    public StationProductPrice getCurrentStationProductPriceByStationProduct(StationProduct sp) {
        StationProductPrice price = null;
        try {
            price = em.createQuery("SELECT o FROM StationProductPrice o WHERE o.stationProduct =:sp AND o.active = TRUE", StationProductPrice.class)
                    .setParameter("sp", sp)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return price;
    }

    public StationProductMinLevel getCurrentStationProductMinLevelByStationProduct(StationProduct sp) {
        StationProductMinLevel spml = null;
        try {
            spml = em.createQuery("SELECT o FROM StationProductMinLevel o WHERE o.stationProduct =:sp AND o.active = TRUE", StationProductMinLevel.class)
                    .setParameter("sp", sp)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return spml;
    }

    public PortalUser getCurrentDispensingAttendantByProductDispensingPoint(ProductDispensingPoint pdp) {
        PortalUser pu = null;
        try {
            pu = em.createQuery("SELECT o.attendant FROM DispensingPointAttendant o WHERE o.dispensingPoint =:pdp AND o.active = TRUE", PortalUser.class)
                    .setParameter("pdp", pdp)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return pu;
    }

    public List<DispensingPointAttendant> getActiveDispensingAttendantByStation(Station s) {
        List<DispensingPointAttendant> dpas = new ArrayList<>();
        try {
            dpas = em.createQuery("SELECT o FROM DispensingPointAttendant o WHERE o.dispensingPoint.station =:s AND o.active = TRUE", DispensingPointAttendant.class)
                    .setParameter("s", s)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return dpas;
    }

    public List<StationProductPrice> getAllStationProductPriceByStation(Station station) {
        List<StationProductPrice> spps = new ArrayList<>();
        try {
            spps = em.createQuery("SELECT o FROM StationProductPrice o WHERE o.stationProduct.station =:station", StationProductPrice.class)
                    .setParameter("station", station)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return spps;
    }

    //Setting Business Method
    public Setting getSettingByName(String name) {
        Setting setting = null;
        try {
            setting = em.createQuery("SELECT o FROM Setting o WHERE o.name =:name", Setting.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return setting;
    }

    //Calendar Business Method
    public List<Calendar> getAllEventByPortalUser(PortalUser user) {
        List<Calendar> events = new ArrayList<>();
        try {
            events = em.createQuery("SELECT o FROM Calendar o WHERE o.portalUser =:user", Calendar.class)
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return events;
    }

    public List<Calendar> getAllEventByPortalUserAndDate(PortalUser user, Date date) {
        List<Calendar> events = new ArrayList<>();
        try {
            events = em.createQuery("SELECT o FROM Calendar o WHERE o.portalUser =:user AND o.eventDate =:date", Calendar.class)
                    .setParameter("user", user)
                    .setParameter("date", date, TemporalType.DATE)
                    .getResultList();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return events;
    }

}
