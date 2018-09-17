package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.http.HttpStatus;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.JacksonEntityService;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelinesDescriptorUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerPipelinesRepository extends PipelinesRepository {

    public static final String USER_NAME_CONFIG_KEY = "username";
    public static final String PASSWORD_CONFIG_KEY = "password";
    public static final String ACCESS_TOKEN_CONFIG_KEY = "token";



    // IMPLEMENTATION OF IPipelinesRepositoryFactory
    public static IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        if(config == null)
            config = new HashMap<>();

        if(!verifyLocation(location))
            return null;

        return new ServerPipelinesRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws PipelinesRepositoryException {
        try {
            OkHttpClient client = new OkHttpClient.Builder().build();
            RequestBody body = RequestBody.create(MediaType.get("text/plain"), "");
            Request request = new Request.Builder()
                    .url(location + "/pipelines")
                    .method("OPTIONS", body)
                    .build();

            Response response = client.newCall(request).execute();

            return response.header("Allow") != null || response.header("Allow").contains("GET");
        } catch (IllegalArgumentException | IOException e) {
            if(e instanceof MalformedURLException)
                return false;

            throw new PipelinesRepositoryException("Could not verify location:" + location, e);
        }
    }



    private String userName;
    private String password;
    private String token;
    private Serialization.Format serializationFormat;



    public ServerPipelinesRepository(String location, Map<String, Object> config) {
        this(location, config, Serialization.Format.JSON);
    }

    public ServerPipelinesRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);
        this.serializationFormat = serializationFormat;

        this.userName = (String)config.get(USER_NAME_CONFIG_KEY);
        this.password = (String)config.get(PASSWORD_CONFIG_KEY);
        this.token = (String)config.get(ACCESS_TOKEN_CONFIG_KEY);
    }



    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = new Request.Builder()
                    .url(getPipelinesUrl())
                    .header("Accept", getAcceptHeader())
                    .build();

            Response response = client.newCall(request).execute();

            return handleGetAllResponse(response);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error getting pipelines from Server!", e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    private Collection<IPipelineDescriptor> handleGetAllResponse(Response response) throws PipelinesRepositoryException, IOException, DSLCoreException {
        ResponseBody body = response.body();

        if(body == null)
            throw new PipelinesRepositoryException("Server returned invalid pipelines content!");

        String content = body.string();

        if(content == null || content.isEmpty())
            throw new PipelinesRepositoryException("Server returned invalid pipelines content!");

        String httpHeader =  response.header("Content-Type");
        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        JavaType klass = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, TypedPipelineDescriptor.class);
        Collection<TypedPipelineDescriptor> typedPipelines = Serialization.deserialize(content, format, klass);

        return JacksonEntityService.transformToIPipelineDescriptor(typedPipelines);
    }


    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = new Request.Builder()
                    .url(getPipelineUrl(pipelineName))
                    .header("Accept", getAcceptHeader())
                    .build();

            Response response = client.newCall(request).execute();

            return handleGetResponse(response);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error getting pipeline " + pipelineName + " from Server!", e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    private IPipelineDescriptor handleGetResponse(Response response) throws IOException, DSLCoreException, PipelinesRepositoryException {
        ResponseBody body = response.body();

        if(body == null)
            return null;

        String content = body.string();

        if(content == null || content.isEmpty())
            return null;

        String httpHeader =  response.header("Content-Type");
        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        return PipelinesDescriptorUtils.createPipelineDescriptor(content, format);
    }


    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        try {
            String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(pipeline, serializationFormat);
            RequestBody body = RequestBody.create(MediaType.get(getContentTypeHeader()), content);

            OkHttpClient client = createClient();
            Request request = new Request.Builder()
                    .url(getPipelineUrl(pipeline.getName()))
                    .put(body)
                    .build();

            Response response = client.newCall(request).execute();

            handleUpdateResponse(response);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error updating pipeline " + pipeline.getName() + " from Server!", e);
        }
    }

    private void handleUpdateResponse(Response response) throws IOException, PipelinesRepositoryException {
        if(response.code() != HttpStatus.SC_OK) {
            String message = "Pipeline could not be updated!";

            if(response.body() != null)
                message += response.body().string();

            throw new PipelinesRepositoryException(message);
        }
    }


    @Override
    public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        try {
            String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(pipeline, serializationFormat);
            RequestBody body = RequestBody.create(MediaType.get(getContentTypeHeader()), content);

            OkHttpClient client = createClient();
            Request request = new Request.Builder()
                    .url(getPipelinesUrl())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            handleInsertResponse(response);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error inserting pipeline " + pipeline.getName() + " from Server!", e);
        }
    }

    private void handleInsertResponse(Response response) throws IOException, PipelinesRepositoryException {
        if(response.code() != HttpStatus.SC_CREATED && response.code() != HttpStatus.SC_OK) {
            String message = "Pipeline could not be inserted!";

            if(response.body() != null)
                message += response.body().string();

            throw new PipelinesRepositoryException(message);
        }
    }


    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        try {
            OkHttpClient client = createClient();
            Request request = new Request.Builder()
                    .url(getPipelineUrl(pipelineName))
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();

            handleDeleteResponse(response);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error deleting pipeline " + pipelineName + " from Server!", e);
        }
    }

    private void handleDeleteResponse(Response response) throws IOException, PipelinesRepositoryException {
        if(response.code() != HttpStatus.SC_OK) {
            String message = "Pipeline could not be deleted!";

            if(response.body() != null)
                message += response.body().string();

            throw new PipelinesRepositoryException(message);
        }
    }


    private String getPipelinesUrl() {
        return this.location + "/pipelines";
    }

    private String getPipelineUrl(String pipelineName) {
        return getPipelinesUrl() + "/" + pipelineName;
    }

    private String getAcceptHeader() throws PipelinesRepositoryException {
        try {
            return Serialization.getHttpHeaderFromFormat(serializationFormat);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    private String getContentTypeHeader() throws PipelinesRepositoryException {
        try {
            return Serialization.getHttpHeaderFromFormat(serializationFormat);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder()
            .authenticator((route, response) -> {
                    String credentials = null;

                    if(this.password != null)
                        credentials = Credentials.basic(this.userName, this.password);
                    else if(this.token != null)
                        credentials = "Bearer " + this.token;

                    if(credentials != null)
                        return response.request().newBuilder().header("Authorization", credentials).build();

                    return response.request().newBuilder().build();
            })
            .build();
    }

}
