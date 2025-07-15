package br.com.engecore.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com ID " + id + " não foi encontrado.");
    }
}