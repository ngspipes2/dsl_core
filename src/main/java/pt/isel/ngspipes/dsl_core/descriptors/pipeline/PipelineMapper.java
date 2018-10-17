package pt.isel.ngspipes.dsl_core.descriptors.pipeline;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository.RepositoryDescriptorDeserializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository.RepositoryDescriptorSerializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository.value.ValueDescriptorDeserializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository.value.ValueDescriptorSerializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.exec.ExecDescriptorDeserializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.exec.ExecDescriptorSerializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.input.InputDescriptorDeserializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.input.InputDescriptorSerializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.spread.strategyDescriptor.CombineStrategyDescriptorDeserializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.spread.strategyDescriptor.StrategyDescriptorDeserializer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.spread.strategyDescriptor.StrategyDescriptorSerializer;
import pt.isel.ngspipes.dsl_core.descriptors.utils.JacksonUtils;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.IOutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.OutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.IParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.ParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.*;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.*;
import pt.isel.ngspipes.pipeline_descriptor.step.IStepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.StepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.*;
import pt.isel.ngspipes.pipeline_descriptor.step.input.*;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.ISpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.SpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.*;

public class PipelineMapper {

    public static ObjectMapper getPipelinesMapper(JsonFactory factory) {
        SimpleAbstractTypeResolver resolver = getTypeResolver();
        ObjectMapper mapper = JacksonUtils.getObjectMapper(factory, resolver);

        mapper.registerModule(getSerializerModule());
        mapper.registerModule(getDeserializerModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    public static SimpleModule getSerializerModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(IRepositoryDescriptor.class, new RepositoryDescriptorSerializer());

        module.addSerializer(IValueDescriptor.class, new ValueDescriptorSerializer());

        module.addSerializer(IExecDescriptor.class, new ExecDescriptorSerializer());

        module.addSerializer(IInputDescriptor.class, new InputDescriptorSerializer());

        module.addSerializer(IStrategyDescriptor.class, new StrategyDescriptorSerializer());

        return module;
    }

    public static SimpleModule getDeserializerModule() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(IRepositoryDescriptor.class, new RepositoryDescriptorDeserializer());

        module.addDeserializer(IValueDescriptor.class, new ValueDescriptorDeserializer());

        module.addDeserializer(IExecDescriptor.class, new ExecDescriptorDeserializer());

        module.addDeserializer(IInputDescriptor.class, new InputDescriptorDeserializer());

        module.addDeserializer(IStrategyDescriptor.class, new StrategyDescriptorDeserializer());
        module.addDeserializer(ICombineStrategyDescriptor.class, new CombineStrategyDescriptorDeserializer());

        return module;
    }

    public static SimpleAbstractTypeResolver getTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IPipelineDescriptor.class, PipelineDescriptor.class);

        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);

        resolver.addMapping(IParameterDescriptor.class, ParameterDescriptor.class);

        resolver.addMapping(IToolRepositoryDescriptor.class, ToolRepositoryDescriptor.class);
        resolver.addMapping(IPipelineRepositoryDescriptor.class, PipelineRepositoryDescriptor.class);

        resolver.addMapping(IParameterValueDescriptor.class, ParameterValueDescriptor.class);
        resolver.addMapping(ISimpleValueDescriptor.class, SimpleValueDescriptor.class);

        resolver.addMapping(IStepDescriptor.class, StepDescriptor.class);

        resolver.addMapping(ICommandExecDescriptor.class, CommandExecDescriptor.class);
        resolver.addMapping(IPipelineExecDescriptor.class, PipelineExecDescriptor.class);

        resolver.addMapping(IParameterInputDescriptor.class, ParameterInputDescriptor.class);
        resolver.addMapping(ISimpleInputDescriptor.class, SimpleInputDescriptor.class);
        resolver.addMapping(IChainInputDescriptor.class, ChainInputDescriptor.class);

        resolver.addMapping(ISpreadDescriptor.class, SpreadDescriptor.class);

        resolver.addMapping(IInputStrategyDescriptor.class, InputStrategyDescriptor.class);
        resolver.addMapping(IOneToOneStrategyDescriptor.class, OneToOneStrategyDescriptor.class);
        resolver.addMapping(IOneToManyStrategyDescriptor.class, OneToManyStrategyDescriptor.class);

        return resolver;
    }

}
