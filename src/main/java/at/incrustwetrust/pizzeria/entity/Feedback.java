package at.incrustwetrust.pizzeria.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "feedbacks") // Gehört zu Mongo, nicht Postgres!
public class Feedback {

    @Id
    private String id; // Mongo nutzt standardmäßig String-IDs (ObjectIds)

    private String customerEmail;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Deine Spielwiese: Hier kann alles rein. 
     * In Bruno einfach ein Objekt "details": { ... } mitschicken.
     */
    private Map<String, Object> details;
}