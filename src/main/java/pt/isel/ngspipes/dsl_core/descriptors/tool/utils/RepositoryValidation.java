package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import utils.ToolRepositoryException;

public class RepositoryValidation {

    public static SupportedRepository getAssociatedSupportedRepositoryInfo(String location, String label) {
        try {
            SupportedRepository supportedRepository = IOUtils.getRepositoriesSupportedData(label);
            boolean supportedRepo = evaluateLocation(supportedRepository, location);
            return supportedRepo ? supportedRepository : null;
        } catch (ToolRepositoryException e) {
            throw new ToolRepositoryException("Error loading repository", e);
        }
    }


    private static boolean evaluateLocation(SupportedRepository supportedRepo, String location) {
        boolean contains = location.contains(supportedRepo.base_location);
        boolean regexMatch = location.matches(supportedRepo.base_location);

        return contains ^ regexMatch;
    }
}
