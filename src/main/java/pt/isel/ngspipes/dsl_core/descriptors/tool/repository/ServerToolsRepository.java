package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.utils.HttpUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.tool_descriptor.implementations.*;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;
import pt.isel.ngspipes.tool_repository.implementations.ToolsRepository;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerToolsRepository extends ToolsRepository {

    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        if(!verifyLocation(location))
            return null;

        return new ServerToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws ToolsRepositoryException {
        try {
            return HttpUtils.canConnect(location + "/tools");
        } catch (IOException e) {
            if(e instanceof MalformedURLException)
                return false;

            throw new ToolsRepositoryException("Could not verify location:" + location, e);
        }
    }



    private Serialization.Format serializationFormat;
    private SimpleAbstractTypeResolver resolver;
    private JavaType klass;



    public ServerToolsRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_TOOL_SERIALIZATION_FORMAT);
    }

    public ServerToolsRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);
        this.serializationFormat = serializationFormat;
        this.resolver = getResolver();
        this.klass = getKlass();
    }

    private SimpleAbstractTypeResolver getResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IToolDescriptor.class, ToolDescriptor.class);
        resolver.addMapping(ICommandDescriptor.class, CommandDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, ParameterDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        return resolver;
    }

    private JavaType getKlass() {
        return new ObjectMapper().getTypeFactory().constructType(ToolDescriptor.class);
    }



    @Override
    public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {
        String url = getToolsUrl();

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getGETConnection(url, getFormatHeaders());
            return getToolsFromConnection(connection);
        } catch (IOException e) {
            String errorMessage = "Error getting tools from Server!" + getErrorMessage(connection);
            throw new ToolsRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private Map<String, String> getFormatHeaders() throws DSLCoreException {
        Map<String, String> headers = new HashMap<>();

        String format = Serialization.getHttpHeaderFromFormat(serializationFormat);
        headers.put("Accept", format);
        headers.put("Content-Type", format);

        return headers;
    }

    private Collection<IToolDescriptor> getToolsFromConnection(HttpURLConnection connection) throws IOException, DSLCoreException {
        String content = IOUtils.toString(connection.getInputStream());
        String httpHeader = connection.getContentType();

        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        JavaType klass = new ObjectMapper().getTypeFactory().constructCollectionType(List.class, ToolDescriptor.class);

        return Serialization.deserialize(content, format, klass, resolver);
    }


    @Override
    public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
        String url = getToolUrl(toolName);

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getGETConnection(url, getFormatHeaders());
            return getToolFromConnection(connection);
        } catch (IOException e) {
            String errorMessage = "Error getting tool " + toolName + " from Server!" + getErrorMessage(connection);
            throw new ToolsRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private IToolDescriptor getToolFromConnection(HttpURLConnection connection) throws IOException, DSLCoreException {
        String content = IOUtils.toString(connection.getInputStream());

        if(content == null || content.isEmpty())
            return null;

        String httpHeader = connection.getContentType();

        Serialization.Format format = Serialization.getFormatFromHttpHeader(httpHeader);

        return Serialization.deserialize(content, format, klass, resolver);
    }


    @Override
    public void update(IToolDescriptor tool) throws ToolsRepositoryException {
        String url = getToolUrl(tool.getName());

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getPUTConnection(url, getFormatHeaders());
            update(tool, connection);
        } catch (IOException e) {
            String errorMessage = "Error updating tool " + tool.getName() + " on Server!" + getErrorMessage(connection);
            throw new ToolsRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private void update(IToolDescriptor tool, HttpURLConnection connection) throws IOException, DSLCoreException, ToolsRepositoryException {
        String content = Serialization.serialize(tool, serializationFormat, getResolver());

        connection.setRequestProperty("Content-Length", Integer.toString(content.length()));

        IOUtils.write(content, connection.getOutputStream());

        if(connection.getResponseCode() != HttpStatus.SC_OK) {
            String message = "Tool could not be updated!";
            message += IOUtils.toString(connection.getInputStream());
            throw new ToolsRepositoryException(message);
        }
    }


    @Override
    public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
        String url = getToolsUrl();

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getPOSTConnection(url, getFormatHeaders());
            insert(tool, connection);
        } catch (IOException e) {
            String errorMessage = "Error inserting tool " + tool.getName() + " on Server!" + getErrorMessage(connection);
            throw new ToolsRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private void insert(IToolDescriptor tool, HttpURLConnection connection) throws IOException, DSLCoreException, ToolsRepositoryException {
        String content = Serialization.serialize(tool, serializationFormat, getResolver());

        connection.setRequestProperty("Content-Length", Integer.toString(content.length()));

        IOUtils.write(content, connection.getOutputStream());

        if(connection.getResponseCode() != HttpStatus.SC_CREATED && connection.getResponseCode() != HttpStatus.SC_OK) {
            String message = "Tool could not be inserted!";
            message += IOUtils.toString(connection.getInputStream());
            throw new ToolsRepositoryException(message);
        }
    }


    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
        String url = getToolUrl(toolName);

        HttpURLConnection connection = null;
        try {
            connection = HttpUtils.getDELETEConnection(url, getFormatHeaders());
            delete(toolName, connection);
        } catch (IOException e) {
            String errorMessage = "Error deleting tool " + toolName + " on Server!" + getErrorMessage(connection);
            throw new ToolsRepositoryException(errorMessage, e);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }

    private void delete(String toolName, HttpURLConnection connection) throws IOException, ToolsRepositoryException {
        if(connection.getResponseCode() != HttpStatus.SC_OK) {
            String message = "Tool could not be deleted!";
            message += IOUtils.toString(connection.getInputStream());
            throw new ToolsRepositoryException(message);
        }
    }


    private String getToolsUrl() {
        return this.location + "/tools";
    }

    private String getToolUrl(String toolName) {
        return getToolsUrl() + "/" + toolName;
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
