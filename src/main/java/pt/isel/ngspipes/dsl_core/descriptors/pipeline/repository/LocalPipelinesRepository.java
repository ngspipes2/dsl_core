package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.fileBased.FileBasedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.JacksonEntityService;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelinesDescriptorUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;


public class LocalPipelinesRepository extends PipelinesRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String SEPARATOR = "/";


    // IMPLEMENTATION OF IPipelinesRepositoryFactory
    public static IPipelinesRepository create(String location, Map<String, Object> config) {
        if(!verifyLocation(location))
            return null;

        return new LocalPipelinesRepository(location, config);
    }

    private static boolean verifyLocation(String location) {
        return IOUtils.existDirectory(location);
    }



    private PipelineSerialization.Format serializationFormat;



    public LocalPipelinesRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_PIPELINE_SERIALIZATION_FORMAT);
    }

    public LocalPipelinesRepository(String location, Map<String, Object> config, PipelineSerialization.Format serializationFormat) {
        super(location, config);
        this.serializationFormat = serializationFormat;
    }



    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        Collection<String> names = IOUtils.getSubDirectoriesName(location);
        Collection<IPipelineDescriptor> pipelines = new LinkedList<>();

        for (String name: names)
            pipelines.add(get(name));

        return pipelines;
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        String pipelinePath = location + SEPARATOR + pipelineName;

        if(!IOUtils.existDirectory(pipelinePath))
            return null;

        String descriptorName = getDescriptorName(pipelinePath);
        String pipelineDescriptorPath = pipelinePath + SEPARATOR + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        try {
            return getPipelineDescriptor(pipelineName, pipelinePath, pipelineDescriptorPath, type);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error loading " + pipelineName + " pipeline descriptor", e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        String pipelinePath = location + SEPARATOR + pipeline.getName();

        if(!IOUtils.existDirectory(pipelinePath))
            throw new PipelinesRepositoryException("There is no pipeline with name: " + pipeline.getName());

        String descriptorName = getDescriptorName(pipelinePath);
        String pipelineDescriptorPath = pipelinePath + SEPARATOR + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        PipelineSerialization.Format format = getFormat(type);

        new File(pipelineDescriptorPath + "/" + DESCRIPTOR_FILE_NAME).delete();
        new File(pipelineDescriptorPath + "/" + LOGO_FILE_NAME).delete();

        try {
            write(pipeline, pipelinePath, pipelineDescriptorPath, format);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error writing " +pipeline.getName() + " pipeline descriptor", e);
        }
    }

    @Override
    public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        String pipelinePath = location + SEPARATOR + pipeline.getName();

        if(IOUtils.existDirectory(pipelinePath))
           throw new PipelinesRepositoryException("There is already a pipeline with name: " + pipeline.getName());

        IOUtils.createFolder(pipelinePath);
        String pipelineDescriptorPath = pipelinePath + SEPARATOR + DESCRIPTOR_FILE_NAME + "." + getExtension();
        try {
            write(pipeline, pipelinePath, pipelineDescriptorPath, serializationFormat);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error writing " + pipeline.getName() + " pipeline descriptor", e);
        }
    }

    @Override
    public void delete(String pipelineName) {
        IOUtils.deleteFolder(location + SEPARATOR + pipelineName);
    }


    private void write(IPipelineDescriptor pipeline, String pipelinePath, String pipelineDescriptorPath, PipelineSerialization.Format format) throws IOException, PipelinesRepositoryException {
        FileBasedPipelineDescriptor fileBasedPipeline = JacksonEntityService.transformToFileBasedPipelineDescriptor(pipeline);

        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(fileBasedPipeline, format);
        IOUtils.write(content, pipelineDescriptorPath);

        if(pipeline.getLogo() != null)
            IOUtils.writeBytes(pipeline.getLogo(), pipelinePath + SEPARATOR + LOGO_FILE_NAME);
    }

    private IPipelineDescriptor getPipelineDescriptor(String pipelineName, String pipelinePath, String pipelineDescriptorPath, String type) throws IOException, PipelinesRepositoryException, DSLCoreException {
        PipelineSerialization.Format format = PipelineSerialization.getFormatFromFileExtension(type);
        String content = IOUtils.read(pipelineDescriptorPath);

        IPipelineDescriptor pipelineDescriptor = PipelinesDescriptorUtils.createPipelineDescriptor(content, format);

        pipelineDescriptor.setLogo(getLogo(pipelinePath));
        pipelineDescriptor.setName(pipelineName);

        return pipelineDescriptor;
    }

    private String getDescriptorName(String pipelinePath) throws PipelinesRepositoryException {
        String descriptorName;
        Collection<String> names = IOUtils.getDirectoryFilesName(pipelinePath);
        for (String currName: names) {
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("."));
            if(currName.equals(DESCRIPTOR_FILE_NAME)) {
                return descriptorName;
            }
        }
        throw new PipelinesRepositoryException("Couldn't find descriptor for pipeline!");
    }

    private byte[] getLogo(String pipelinePath) throws IOException {
        String logoPath = pipelinePath + SEPARATOR + LOGO_FILE_NAME;

        if(IOUtils.existFile(logoPath))
            return IOUtils.readBytes(logoPath);

        return null;
    }

    private String getExtension() throws PipelinesRepositoryException {
        try {
            return PipelineSerialization.getFileExtensionFromFormat(serializationFormat);
        } catch (DSLCoreException e ) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    private PipelineSerialization.Format getFormat(String fileExtension) throws PipelinesRepositoryException {
        try {
            return PipelineSerialization.getFormatFromFileExtension(fileExtension);
        } catch (DSLCoreException e ) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

}
