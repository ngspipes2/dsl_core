package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.http.HttpStatus;
import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.tool.ToolMapper;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

public class ServerToolsRepository extends WrapperToolsRepository {

    public static final String USER_NAME_CONFIG_KEY = "username";
    public static final String PASSWORD_CONFIG_KEY = "password";
    public static final String ACCESS_TOKEN_CONFIG_KEY = "token";



    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        if(config == null)
            config = new HashMap<>();

        if(!verifyLocation(location))
            return null;

        return new ServerToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws ToolsRepositoryException {
        try {
            OkHttpClient client = new OkHttpClient.Builder().build();
            RequestBody body = RequestBody.create(MediaType.get("text/plain"), "");
            Request request = new Request.Builder()
                    .url(location + "/tools")
                    .method("OPTIONS", body)
                    .build();

            Response response = client.newCall(request).execute();

            return response.header("Allow") != null && response.header("Allow").contains("GET");
        } catch (IllegalArgumentException | IOException e) {
            if(e instanceof MalformedURLException)
                return false;

            throw new ToolsRepositoryException("Could not verify location:" + location, e);
        }
    }



    private String userName;
    private String password;
    private String token;
    private Serialization.Format serializationFormat;



    public ServerToolsRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_TOOL_SERIALIZATION_FORMAT);
    }

    public ServerToolsRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);

        this.serializationFormat = serializationFormat;
        this.userName = (String)config.get(USER_NAME_CONFIG_KEY);
        this.password = (String)config.get(PASSWORD_CONFIG_KEY);
        this.token = (String)config.get(ACCESS_TOKEN_CONFIG_KEY);
    }



    @Override
    public byte[] getLogo() throws ToolsRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getLogoUrl())
                    .build();

            Response response = client.newCall(request).execute();

            return handleGetLogoResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error getting logo!", e);
        }
    }

    private byte[] handleGetLogoResponse(Response response) throws IOException {
        ResponseBody body = response.body();

        if(body == null)
            return null;

        byte[] logo = body.bytes();

        if(logo.length == 0)
            return null;

        return logo;
    }

    @Override
    public void setLogo(byte[] logo) throws ToolsRepositoryException {
        try {
            RequestBody body = RequestBody.create(null, logo == null ? new byte[0] : logo);

            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getLogoUrl())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            handleSetLogoResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error setting logo!", e);
        }
    }

    private void handleSetLogoResponse(Response response) throws IOException, ToolsRepositoryException {
        if(response.code() != HttpStatus.SC_CREATED && response.code() != HttpStatus.SC_OK) {
            String message = "Logo could not be set!";

            if(response.body() != null)
                message += response.body().string();

            throw new ToolsRepositoryException(message);
        }
    }


    @Override
    protected Collection<IToolDescriptor> getAllWrapped() throws ToolsRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getToolsUrl())
                    .header("Accept", getAcceptHeader())
                    .build();

            Response response = client.newCall(request).execute();

            return handleGetAllResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error getting tools from Server!", e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

    private Collection<IToolDescriptor> handleGetAllResponse(Response response) throws IOException, DSLCoreException, ToolsRepositoryException {
        ResponseBody body = response.body();

        if(body == null)
            throw new ToolsRepositoryException("Server returned invalid tools content!");

        String content = body.string();

        if(content == null || content.isEmpty())
            throw new ToolsRepositoryException("Server returned invalid tools content!");

        String httpHeader = response.header("Content-Type");
        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        ObjectMapper mapper = ToolMapper.getToolsMapper(format.getFactory());
        JavaType klass = mapper.getTypeFactory().constructCollectionType(List.class, IToolDescriptor.class);
        return Serialization.deserialize(content, klass, mapper);
    }


    @Override
    protected IToolDescriptor getWrapped(String toolName) throws ToolsRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getToolUrl(toolName))
                    .header("Accept", getAcceptHeader())
                    .build();

            Response response = client.newCall(request).execute();

            return handleGetResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error getting tool " + toolName + " from Server!", e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

    private IToolDescriptor handleGetResponse(Response response) throws IOException, DSLCoreException {
        ResponseBody body = response.body();

        if(body == null)
            return null;

        String content = body.string();

        if(content == null || content.isEmpty())
            return null;

        String httpHeader = response.header("Content-Type");
        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        ObjectMapper mapper = ToolMapper.getToolsMapper(format.getFactory());
        JavaType klass = mapper.constructType(IToolDescriptor.class);
        return Serialization.deserialize(content, klass, mapper);
    }


    @Override
    protected void updateWrapped(IToolDescriptor tool) throws ToolsRepositoryException {
        try {
            String content = Serialization.serialize(tool, serializationFormat);
            RequestBody body = RequestBody.create(MediaType.get(getContentTypeHeader()), content);

            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getToolUrl(tool.getName()))
                    .put(body)
                    .build();

            Response response = client.newCall(request).execute();

            handleUpdateResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error updating tool " + tool.getName() + " from Server!", e);
        } catch(DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

    private void handleUpdateResponse(Response response) throws IOException, ToolsRepositoryException {
        if(response.code() != HttpStatus.SC_OK) {
            String message = "Tool could not be updated!";

            if(response.body() != null)
                message += response.body().string();

            throw new ToolsRepositoryException(message);
        }
    }


    @Override
    protected void insertWrapped(IToolDescriptor tool) throws ToolsRepositoryException {
        try {
            String content = Serialization.serialize(tool, serializationFormat);
            RequestBody body = RequestBody.create(MediaType.get(getContentTypeHeader()), content);

            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getToolsUrl())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            handleInsertResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error inserting tool " + tool.getName() + " from Server!", e);
        } catch(DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

    private void handleInsertResponse(Response response) throws IOException, ToolsRepositoryException {
        if(response.code() != HttpStatus.SC_CREATED && response.code() != HttpStatus.SC_OK) {
            String message = "Tool could not be inserted!";

            if(response.body() != null)
                message += response.body().string();

            throw new ToolsRepositoryException(message);
        }
    }


    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = createRequestBuilder()
                    .url(getToolUrl(toolName))
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();

            handleDeleteResponse(response);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error deleting tool " + toolName + " from Server!", e);
        }
    }

    private void handleDeleteResponse(Response response) throws IOException, ToolsRepositoryException {
        if(response.code() != HttpStatus.SC_OK) {
            String message = "Tool could not be deleted!";

            if(response.body() != null)
                message += response.body().string();

            throw new ToolsRepositoryException(message);
        }
    }


    private String getLogoUrl(){
        return super.location + "/logo";
    }

    private String getToolsUrl() {
        return this.location + "/tools";
    }

    private String getToolUrl(String toolName) {
        return getToolsUrl() + "/" + toolName;
    }

    private String getAcceptHeader() throws ToolsRepositoryException {
        try {
            return Serialization.getHttpHeaderFromFormat(serializationFormat);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

    private String getContentTypeHeader() throws ToolsRepositoryException {
        try {
            return Serialization.getHttpHeaderFromFormat(serializationFormat);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

    private Request.Builder createRequestBuilder() {
        Request.Builder builder = new Request.Builder();

        String credentials = null;

        if(this.password != null)
            credentials = Base64.getEncoder().encodeToString((this.userName + ":" + this.password).getBytes());
        else if(this.token != null)
            credentials = "Bearer " + this.token;

        if(credentials != null)
            builder = builder.addHeader("Authorization", credentials);


        return builder;
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder().build();
    }

}

