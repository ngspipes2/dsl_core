package pt.isel.ngspipes.dsl_core.dsl_parser.descriptors.tool.utils.support;

public class SupportedRepository {

    String base_uri;
    String access_uri;
    String api_uri;

    public String getBase_uri() { return base_uri; }
    public void setBase_uri(String base_uri) { this.base_uri = base_uri; }

    public String getAccess_uri() { return access_uri; }
    public void setAccess_location(String access_uri) { this.access_uri = access_uri; }

    public String getApi_uri() { return api_uri; }
    public void setApi_uri(String api_uri) { this.api_uri = api_uri; }
}
