package ch.so.agi.simi.util.properties;

import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class SimiProperty implements Comparable, Serializable {

    private static Logger log = LoggerFactory.getLogger(SimiProperty.class);

    private static SortedSet<SimiProperty> props = null;

    static {
        // Web module properties
        SimiProperty.add("cuba.web.ldap.enabled", true, CubaModule.WEB);
        SimiProperty.add("cuba.web.ldap.urls", true, CubaModule.WEB);
        SimiProperty.add("cuba.web.ldap.base", true, CubaModule.WEB);
        SimiProperty.add("cuba.web.ldap.user", true, CubaModule.WEB);
        SimiProperty.add("cuba.web.ldap.password", true, CubaModule.WEB, false);
        SimiProperty.add("cuba.web.ldap.userLoginField", true, CubaModule.WEB);

        SimiProperty.add("cuba.web.loginDialogDefaultUser", false, CubaModule.WEB);
        SimiProperty.add("cuba.web.loginDialogDefaultPassword", false, CubaModule.WEB, false);
        SimiProperty.add("cuba.web.productionMode", false, CubaModule.WEB);
        SimiProperty.add("cuba.web.requirePasswordForNewUsers", true, CubaModule.WEB);
        SimiProperty.add("cuba.web.standardAuthenticationUsers", false, CubaModule.WEB);

        SimiProperty.add("simi.publishJob.baseUrl", true, CubaModule.WEB);
        SimiProperty.add("simi.publishJob.pollTimeout", true, CubaModule.WEB);
        SimiProperty.add("simi.publishJob.secToken", true, CubaModule.WEB, false);

        SimiProperty.add("simi.schemaReader.url", true, CubaModule.WEB);

        // Core module properties
        SimiProperty.add("cuba.dataSource.dbName", true, CubaModule.CORE);
        SimiProperty.add("cuba.dataSource.host", true, CubaModule.CORE);
        SimiProperty.add("cuba.dataSource.port", false, CubaModule.CORE);

        SimiProperty.add("cuba.dataSource.username", true, CubaModule.CORE);
        SimiProperty.add("cuba.dataSource.password", true, CubaModule.CORE, false);

        //SimiProperty.add("simi.config.stopOnIncomplete", false, CubaModule.CORE);

        SimiProperty.add("simi.gitSearch.url", true, CubaModule.CORE);
        SimiProperty.add("simi.gitSearch.repos", true, CubaModule.CORE);
    }

    private String name;
    private boolean mandatory;
    private CubaModule definedIn;
    private boolean showInLog;
    private String value;

    private SimiProperty(String name, boolean mandatory, CubaModule definedIn, boolean showInLog){
        this.name = name;
        this.mandatory = mandatory;
        this.definedIn = definedIn;
        this.showInLog = showInLog;
    }

    private void readValue(){
        value = AppContext.getProperty(name);
    }

    private String envName(){
        if(name == null)
            return null;

        return name.toUpperCase().replaceAll("\\.", "_");
    }

    @Override
    public int compareTo(Object o) {
        SimiProperty other = (SimiProperty)o;

        return this.name.compareTo(other.name);
    }

    public static void setProps(SortedSet<SimiProperty> props){
        SimiProperty.props = props;
    }

    public static SortedSet<SimiProperty> getProps(){
        return SimiProperty.props;
    }

    public static void add(String name, boolean mandatory, CubaModule definedIn){
        add(name, mandatory, definedIn, true);
    }

    public static void add(String name, boolean mandatory, CubaModule definedIn, boolean showInLog){
        if(props == null)
            props = new TreeSet<>();

        props.add(new SimiProperty(name, mandatory, definedIn, showInLog));
    }

    public static void addValuesForModule(boolean webModule){
        for(SimiProperty p : props){
            if(webModule){
                if(p.definedIn == CubaModule.WEB)
                    p.readValue();
            }
            else {
                if(p.definedIn != CubaModule.WEB)
                    p.readValue();
            }
        }
    }

    public static boolean assertConfigComplete(){

        boolean missingMandatoryValues = false;

        log.info("SIMI Configration values ----------------------------------------------------");

        for(SimiProperty p : props){
            if(p.value != null && p.value.length() > 0){
                if(p.showInLog)
                    log.info("'{}': [{}]", p.envName(), p.value);
                else
                    log.info("'{}': [Secret value with {} digits]", p.envName(), p.value.length());
            }
            else {
                if(p.mandatory) {
                    log.error("'{}': [-MANDATORY BUT NOT CONFIGURED-]", p.envName());
                    missingMandatoryValues = true;
                }
                else
                    log.info("'{}': [-NOT CONFIGURED-]", p.envName());
            }
        }

        return !missingMandatoryValues;
    }
}



