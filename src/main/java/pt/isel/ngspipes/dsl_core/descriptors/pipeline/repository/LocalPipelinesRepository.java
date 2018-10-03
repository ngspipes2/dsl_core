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
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;


public class LocalPipelinesRepository extends WrapperPipelinesRepository {

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



    private class PipelineInfo {

        public String pipelineName;
        public String pipelineDirectory;
        public boolean existsPipeline;

        public String pipelineDescriptorName;
        public String pipelineDescriptorDirectory;
        public String pipelineDescriptorPath;
        public boolean existsPipelineDescriptor;

        public String pipelineLogoName;
        public String pipelineLogoDirectory;
        public String pipelineLogoPath;
        public boolean existsPipelineLogo;

        public PipelineSerialization.Format serializationFormat;



        public PipelineInfo( ) { }

        public PipelineInfo(    String pipelineName, String pipelineDirectory, boolean existsPipeline,
                                String pipelineDescriptorName, String pipelineDescriptorDirectory, String pipelineDescriptorPath, boolean existsPipelineDescriptor,
                                String pipelineLogoName, String pipelineLogoDirectory, String pipelineLogoPath, boolean existsPipelineLogo,
                                PipelineSerialization.Format serializationFormat) {
            this.pipelineName = pipelineName;
            this.pipelineDirectory = pipelineDirectory;
            this.existsPipeline = existsPipeline;

            this.pipelineDescriptorName = pipelineDescriptorName;
            this.pipelineDescriptorDirectory = pipelineDescriptorDirectory;
            this.pipelineDescriptorPath = pipelineDescriptorPath;
            this.existsPipelineDescriptor = existsPipelineDescriptor;

            this.pipelineLogoName = pipelineLogoName;
            this.pipelineLogoDirectory = pipelineLogoDirectory;
            this.pipelineLogoPath = pipelineLogoPath;
            this.existsPipelineLogo = existsPipelineLogo;

            this.serializationFormat = serializationFormat;
        }

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
    public byte[] getLogo() throws PipelinesRepositoryException {
        String logoPath = super.location + "/" + LOGO_FILE_NAME;

        try {
            if(IOUtils.existFile(logoPath))
                return IOUtils.readBytes(logoPath);
            else
                return null;
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error getting logo!", e);
        }
    }

    @Override
    public void setLogo(byte[] logo) throws PipelinesRepositoryException {
        String logoPath = super.location + "/" + LOGO_FILE_NAME;

        try {
            if(logo == null) {
                if(IOUtils.existFile(logoPath))
                    IOUtils.deleteFile(logoPath);
            } else {
                IOUtils.writeBytes(logo, logoPath);
            }
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error setting logo!", e);
        }
    }


    @Override
    public Collection<IPipelineDescriptor> getAllWrapped() throws PipelinesRepositoryException {
        Collection<String> names = IOUtils.getSubDirectoriesName(location);
        Collection<IPipelineDescriptor> pipelines = new LinkedList<>();

        for (String name: names)
            pipelines.add(get(name));

        return pipelines;
    }


    @Override
    public IPipelineDescriptor getWrapped(String pipelineName) throws PipelinesRepositoryException {
        PipelineInfo pipelineInfo = createPipelineInfo(pipelineName);

        if(!pipelineInfo.existsPipeline)
            return null;

        return get(pipelineInfo);
    }

    private IPipelineDescriptor get(PipelineInfo info) throws PipelinesRepositoryException {
        try {
            IPipelineDescriptor pipelineDescriptor = getPipelineDescriptor(info);
            pipelineDescriptor.setLogo(getPipelineLogo(info));

            return pipelineDescriptor;
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error loading " + info.pipelineLogoName + " pipeline descriptor!", e);
        }
    }

    private IPipelineDescriptor getPipelineDescriptor(PipelineInfo info) throws IOException, PipelinesRepositoryException {
        String content = IOUtils.read(info.pipelineDescriptorPath);

        IPipelineDescriptor pipelineDescriptor = PipelinesDescriptorUtils.createPipelineDescriptor(content, info.serializationFormat);
        pipelineDescriptor.setName(info.pipelineName);

        return pipelineDescriptor;
    }

    private byte[] getPipelineLogo(PipelineInfo info) throws IOException {
        if(!info.existsPipelineLogo)
            return null;

        return IOUtils.readBytes(info.pipelineLogoPath);
    }


    @Override
    public void updateWrapped(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        PipelineInfo pipelineInfo = createPipelineInfo(pipeline.getName());

        if(!pipelineInfo.existsPipeline)
            throw new PipelinesRepositoryException("There is no pipeline with name: " + pipeline.getName());

        update(pipelineInfo, pipeline);
    }

    private void update(PipelineInfo info, IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        try {
            updatePipelineDescriptor(info, pipeline);
            updatePipelineLogo(info, pipeline);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error writing " + pipeline.getName() + " pipeline descriptor", e);
        }
    }

    private void updatePipelineDescriptor(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException, PipelinesRepositoryException {
        FileBasedPipelineDescriptor fileBasedPipeline = JacksonEntityService.transformToFileBasedPipelineDescriptor(pipeline);

        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(fileBasedPipeline, info.serializationFormat);

        IOUtils.write(content, info.pipelineDescriptorPath);
    }

    private void updatePipelineLogo(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException {
        if(pipeline.getLogo() != null)
            IOUtils.writeBytes(pipeline.getLogo(), info.pipelineLogoPath);
        else if(info.existsPipelineLogo)
            IOUtils.deleteFile(info.pipelineLogoPath);
    }


    @Override
    public void insertWrapped(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        PipelineInfo pipelineInfo = createPipelineInfo(pipeline.getName());

        if(pipelineInfo.existsPipeline)
            throw new PipelinesRepositoryException("There is already a pipeline with name: " + pipeline.getName());

        insert(pipelineInfo, pipeline);
    }

    private void insert(PipelineInfo info, IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        try {
            IOUtils.createFolder(info.pipelineDirectory);
            insertPipelineDescriptor(info, pipeline);
            insertPipelineLogo(info, pipeline);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error writing " + pipeline.getName() + " pipeline descriptor", e);
        }
    }

    private void insertPipelineDescriptor(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException, PipelinesRepositoryException {
        FileBasedPipelineDescriptor fileBasedPipeline = JacksonEntityService.transformToFileBasedPipelineDescriptor(pipeline);

        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(fileBasedPipeline, info.serializationFormat);

        IOUtils.write(content, info.pipelineDescriptorPath);
    }

    private void insertPipelineLogo(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException {
        if(pipeline.getLogo() != null)
            IOUtils.writeBytes(pipeline.getLogo(), info.pipelineLogoPath);
    }


    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        PipelineInfo pipelineInfo = createPipelineInfo(pipelineName);

        if(!pipelineInfo.existsPipeline)
            return;

        delete(pipelineInfo);
    }

    private void delete(PipelineInfo info) throws PipelinesRepositoryException {
        try {
            deletePipelineDescriptor(info);
            deletePipelineLogo(info);
            IOUtils.deleteFile(info.pipelineDirectory);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Could not delete folder " + info.pipelineDirectory+"!", e);
        }
    }

    private void deletePipelineDescriptor(PipelineInfo info) throws IOException {
        IOUtils.deleteFile(info.pipelineDescriptorPath);
    }

    private void deletePipelineLogo(PipelineInfo info) throws IOException {
        if(info.existsPipelineLogo)
            IOUtils.deleteFile(info.pipelineLogoPath);
    }


    private PipelineInfo createPipelineInfo(String pipelineName) throws PipelinesRepositoryException {
        try {
            PipelineInfo info = new PipelineInfo();

            info.pipelineName = pipelineName;
            info.pipelineDirectory = super.location + SEPARATOR + pipelineName;
            info.existsPipeline = IOUtils.existDirectory(info.pipelineDirectory);

            info.pipelineDescriptorName = getDescriptorName(info.pipelineDirectory);
            info.pipelineDescriptorDirectory = info.pipelineDirectory;
            info.pipelineDescriptorPath = info.pipelineDescriptorDirectory + SEPARATOR + info.pipelineDescriptorName;
            info.existsPipelineDescriptor = IOUtils.existFile(info.pipelineDescriptorPath);

            info.pipelineLogoName = LOGO_FILE_NAME;
            info.pipelineLogoDirectory = info.pipelineDirectory;
            info.pipelineLogoPath = info.pipelineLogoDirectory + SEPARATOR + LOGO_FILE_NAME;
            info.existsPipelineLogo = IOUtils.existFile(info.pipelineLogoPath);

            info.serializationFormat = PipelineSerialization.getFormatFromFileExtension(IOUtils.getExtensionFromFilePath(info.pipelineDescriptorPath));

            return info;
        } catch (DSLCoreException | IOException e) {
            throw new PipelinesRepositoryException("Error checking info for pipeline " + pipelineName + "!", e);
        }
    }

    private String getDescriptorName(String pipelineDirectory) throws IOException, DSLCoreException {
        for(String file : IOUtils.getDirectoryFilesName(pipelineDirectory)) {
            if(file.startsWith(DESCRIPTOR_FILE_NAME))
                return file;
        }

        String extension = "." + PipelineSerialization.getFileExtensionFromFormat(serializationFormat);
        return DESCRIPTOR_FILE_NAME + extension;
    }

}
