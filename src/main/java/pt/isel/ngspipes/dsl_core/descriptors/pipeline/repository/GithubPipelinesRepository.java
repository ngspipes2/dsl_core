package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelinesDescriptorUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.GithubAPI;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class GithubPipelinesRepository extends PipelinesRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME = "Descriptor";
    private static final String SEPARATOR = "/";

    // IMPLEMENTATION OF IPipelinesRepositoryFactory
    public static IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        if(!verifyLocation(location))
            return null;

        return new GithubPipelinesRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws PipelinesRepositoryException {
        try {
            if(GithubAPI.isGithubUri(location))
                return GithubAPI.existsRepository(location);
        } catch (IOException e) {
            if(e instanceof MalformedURLException)
                return false;

           throw new PipelinesRepositoryException("Could not verify location:" + location, e);
        }

        return false;
    }



    public GithubPipelinesRepository(String location, Map<String, Object> config) {
        super(location, config);
    }

    // IMPLEMENTATION OF PipelinesRepository
    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        Collection<String> names = getPipelinesName();
        Collection<IPipelineDescriptor> pipelines = new LinkedList<>();

        for (String name: names)
            pipelines.add(get(name));

        return pipelines;
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        try {
            if(!getPipelinesName().contains(pipelineName))
                return null;

            String descriptorName = getDescriptorName(pipelineName);
            String descriptorPath = pipelineName + SEPARATOR + descriptorName;
            String content = GithubAPI.getFileContent(location, descriptorPath);
            String type = IOUtils.getExtensionFromFilePath(descriptorName);

            return createPipelineDescriptor(pipelineName, type, content);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error loading " + pipelineName + " pipeline descriptor", e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        throw new PipelinesRepositoryException("Update operation not supported");
    }

    @Override
    public void insert(IPipelineDescriptor pipelineName) throws PipelinesRepositoryException {
        throw new PipelinesRepositoryException("Insert operation not supported");
    }

    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        throw new PipelinesRepositoryException("Delete operation not supported");
    }



    private Collection<String> getPipelinesName() throws PipelinesRepositoryException {
        try {
            return GithubAPI.getFoldersNames(location);
        } catch(IOException e) {
            throw new PipelinesRepositoryException("Error getting pipelines names!", e);
        }
    }

    private IPipelineDescriptor createPipelineDescriptor(String pipelineName, String type, String content) throws IOException, PipelinesRepositoryException, DSLCoreException {
        PipelineSerialization.Format format = PipelineSerialization.getFormatFromFileExtension(type);

        IPipelineDescriptor pipelineDescriptor = PipelinesDescriptorUtils.createPipelineDescriptor(content, format);

        pipelineDescriptor.setName(pipelineName);
        pipelineDescriptor.setLogo(getLogo(pipelineName));

        return pipelineDescriptor;
    }

    private String getDescriptorName(String pipelineName) throws IOException, PipelinesRepositoryException {
        String descriptorName;
        Collection<String> names = GithubAPI.getFilesNames(location, pipelineName);
        for (String currName: names) {
            String type = IOUtils.getExtensionFromFilePath(currName);
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("." + type));
            if(currName.equals(DESCRIPTOR_FILE_NAME)) {
                return descriptorName;
            }
        }

        throw new PipelinesRepositoryException("Couldn't find descriptor for pipeline " + pipelineName);
    }

    private byte[] getLogo(String pipelineName) throws IOException {
        Collection<String> files = GithubAPI.getFilesNames(location, pipelineName);

        if(files.contains(LOGO_FILE_NAME))
            return GithubAPI.getFileBytes(location, pipelineName + SEPARATOR + LOGO_FILE_NAME);

        return null;
    }

}
