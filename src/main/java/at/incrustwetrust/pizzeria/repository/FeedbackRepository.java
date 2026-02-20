package at.incrustwetrust.pizzeria.repository;

import at.incrustwetrust.pizzeria.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    // Keine SQL-Queries nötig, Mongo macht das "on the fly"
}