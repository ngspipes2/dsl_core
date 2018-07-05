package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.JacksonEntityService;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelinesDescriptorUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.HttpUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerPipelinesRepository extends PipelinesRepository {

    // IMPLEMENTATION OF IPipelinesRepositoryFactory
    public static IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        if(!verifyLocation(location))
            return null;

        return new ServerPipelinesRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws PipelinesRepositoryException {
        try {
            return HttpUtils.canConnect(location + "/pipelines");
        } catch (IOException e) {
            if(e instanceof MalformedURLException)
                return false;

            throw new PipelinesRepositoryException("Could not verify location:" + location, e);
        }
    }



    private Serialization.Format serializationFormat;



    public ServerPipelinesRepository(String location, Map<String, Object> config) {
        this(location, config, Serialization.Format.JSON);
    }

    public ServerPipelinesRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);
        this.serializationFormat = serializationFormat;
    }



    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        String url = getPipelinesUrl();

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getGETConnection(url, getFormatHeaders());
            return getPipelinesFromConnection(connection);
        } catch (IOException e) {
            String errorMessage = "Error getting pipelines from Server!" + getErrorMessage(connection);
            throw new PipelinesRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private Collection<IPipelineDescriptor> getPipelinesFromConnection(HttpURLConnection connection) throws IOException, DSLCoreException, PipelinesRepositoryException {
        String content = IOUtils.toString(connection.getInputStream());
        String httpHeader = connection.getContentType();

        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        JavaType klass = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, TypedPipelineDescriptor.class);
        Collection<TypedPipelineDescriptor> typedPipelines = Serialization.deserialize(content, format, klass);

        return JacksonEntityService.transformToIPipelineDescriptor(typedPipelines);
    }

    private Map<String, String> getFormatHeaders() throws DSLCoreException, PipelinesRepositoryException {
        Map<String, String> headers = new HashMap<>();

        String format = Serialization.getHttpHeaderFromFormat(serializationFormat);
        headers.put("Accept", format);
        headers.put("Content-Type", format);

        return headers;
    }


    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        String url = getPipelineUrl(pipelineName);

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getGETConnection(url, getFormatHeaders());
            return getPipelineFromConnection(connection);
        } catch (IOException e) {
            String errorMessage = "Error getting pipeline " + pipelineName + " from Server!" + getErrorMessage(connection);
            throw new PipelinesRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private IPipelineDescriptor getPipelineFromConnection(HttpURLConnection connection) throws IOException, DSLCoreException, PipelinesRepositoryException {
        String content = IOUtils.toString(connection.getInputStream());

        if(content == null || content.isEmpty())
            return null;

        String httpHeader = connection.getContentType();

        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        return PipelinesDescriptorUtils.createPipelineDescriptor(content, format);
    }


    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        String url = getPipelineUrl(pipeline.getName());

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getPUTConnection(url, getFormatHeaders());
            update(pipeline, connection);
        } catch (IOException e) {
            String errorMessage = "Error updating Pipeline " + pipeline.getName() + " on Server!" + getErrorMessage(connection);
            throw new PipelinesRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private void update(IPipelineDescriptor pipeline, HttpURLConnection connection) throws IOException, DSLCoreException, PipelinesRepositoryException {
        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(pipeline, serializationFormat);

        connection.setRequestProperty("Content-Length", Integer.toString(content.length()));
        connection.setRequestProperty("Content-Type", Serialization.getHttpHeaderFromFormat(serializationFormat));

        IOUtils.write(content, connection.getOutputStream());

        if(connection.getResponseCode() != HttpStatus.SC_OK) {
            String message = "Pipeline could not be updated!";
            message += IOUtils.toString(connection.getInputStream());
            throw new PipelinesRepositoryException(message);
        }
    }


    @Override
    public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        String url = getPipelinesUrl();

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getPOSTConnection(url, getFormatHeaders());
            insert(pipeline, connection);
        } catch (IOException e) {
            String errorMessage = "Error inserting pipeline " + pipeline.getName() + " on Server!" + getErrorMessage(connection);
            throw new PipelinesRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private void insert(IPipelineDescriptor pipeline, HttpURLConnection connection) throws IOException, DSLCoreException, PipelinesRepositoryException {
        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(pipeline, serializationFormat);

        connection.setRequestProperty("Content-Length", Integer.toString(content.length()));
        connection.setRequestProperty("Content-Type", Serialization.getHttpHeaderFromFormat(serializationFormat));

        IOUtils.write(content, connection.getOutputStream());

        if(connection.getResponseCode() != HttpStatus.SC_CREATED && connection.getResponseCode() != HttpStatus.SC_OK) {
            String message = "Pipeline could not be inserted!";
            message += IOUtils.toString(connection.getInputStream());
            throw new PipelinesRepositoryException(message);
        }
    }


    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        String url = getPipelineUrl(pipelineName);

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getDELETEConnection(url, getFormatHeaders());
            delete(pipelineName, connection);
        } catch (IOException e) {
            String errorMessage = "Error deleting pipeline " + pipelineName + " on Server!" + getErrorMessage(connection);
            throw new PipelinesRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private void delete(String pipelineName, HttpURLConnection connection) throws IOException, PipelinesRepositoryException {
        if(connection.getResponseCode() != HttpStatus.SC_OK) {
            String message = "Pipeline could not be deleted!";
            message += IOUtils.toString(connection.getInputStream());
            throw new PipelinesRepositoryException(message);
        }
    }


    private String getPipelinesUrl() {
        return this.location + "/pipelines";
    }

    private String getPipelineUrl(String pipelineName) {
        return getPipelinesUrl() + "/" + pipelineName;
    }

    private String getErrorMessage(HttpURLConnection connection) {
        try{
            InputStream stream = connection.getErrorStream();
            if(stream != null)
                return IOUtils.toString(stream);
        } catch (IOException e) { }

        return "";
    }

}
