package tn.esprit.spring.projet_pi_v2.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String id) {
        super(resourceName + " non trouvé(e) avec l'id : " + id);
    }
}
