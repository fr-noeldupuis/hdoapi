package fr.noeldupuis.hdoapi.persons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchPersonRequest {
    
    @JsonProperty("operations")
    private List<Operation> operations;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Operation {
        private String op; // "add", "replace", "remove", "copy", "move", "test"
        private String path; // JSON pointer like "/firstName"
        private Object value; // new value for add/replace operations
        private String from; // source path for copy/move operations
    }
} 