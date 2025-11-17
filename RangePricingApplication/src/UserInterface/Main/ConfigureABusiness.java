package UserInterface.Main;

import MarketingManagement.MarketingPersonDirectory;
import MarketingManagement.MarketingPersonProfile;
import TheBusiness.Business.Business;
import TheBusiness.CustomerManagement.CustomerDirectory;
import TheBusiness.Personnel.Person;
import TheBusiness.Personnel.PersonDirectory;
import TheBusiness.SalesManagement.SalesPersonDirectory;
import TheBusiness.SalesManagement.SalesPersonProfile;
import TheBusiness.Supplier.SupplierDirectory;
import TheBusiness.UserAccountManagement.UserAccountDirectory;

import TheBusiness.DataGenerator;

/**
 *
 * @author TEAM
 */
class ConfigureABusiness {

    static Business initialize() {

        
        Business business = new Business("Xerox");

        
        new DataGenerator().generateData(
                business.getSupplierDirectory(),
                business.getCustomerDirectory()
        );

        

        PersonDirectory personDirectory = business.getPersonDirectory();
        SalesPersonDirectory salesPersonDirectory = business.getSalesPersonDirectory();
        MarketingPersonDirectory marketingPersonDirectory = business.getMarketingPersonDirectory();
        UserAccountDirectory userAccountDirectory = business.getUserAccountDirectory();


        // Sales user
        Person pSales = personDirectory.newPerson("AutoSalesPerson");
        SalesPersonProfile spp = salesPersonDirectory.newSalesPersonProfile(pSales);
        userAccountDirectory.newUserAccount(spp, "sales", "XXXX");


        // Marketing user
        Person pMarketing = personDirectory.newPerson("AutoMarketingPerson");
        MarketingPersonProfile mpp = marketingPersonDirectory.newMarketingPersonProfile(pMarketing);
        userAccountDirectory.newUserAccount(mpp, "marketing", "XXXX");


        
       System.out.println("🎉 LIVE DATA INITIALIZED!");

System.out.println("Suppliers: "
        + business.getSupplierDirectory().getSuplierList().size());

System.out.println("Customers: "
        + business.getCustomerDirectory().getCustomerList().size());


        return business;
    }
    
}
